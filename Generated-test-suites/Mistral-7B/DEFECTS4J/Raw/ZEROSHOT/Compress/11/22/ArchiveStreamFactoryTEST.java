import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ARFile;
import org.apache.commons.compress.archivers.ar.ARInputStream;
import org.apache.commons.compress.archivers.ar.AROutputStream;
import org.apache.commons.compress.archivers.cpio.CpioFile;
import org.apache.commons.compress.archivers.cpio.CpioInputStream;
import org.apache.commons.compress.archivers.cpio.CpioOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarInputStream;
import org.apache.commons.compress.archivers.jar.JarOutputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipInputStream;
import org.apache.commons.compress.archivers.zip.ZipOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(ARFile.AR_MAGIC.getBytes());
        ArchiveInputStream arArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertEquals(AR.toLowerCase(), arArchiveInputStream.getFormat().getName());
        arArchiveInputStream.close();

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(CpioFile.CPIO_MAGIC.getBytes());
        ArchiveInputStream cpioArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertEquals(ArchiveFormat.CPIO, cpioArchiveInputStream.getFormat());
        cpioArchiveInputStream.close();

        // Test Dump
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[32]); // Dump needs a bigger buffer to check the signature
        ArchiveInputStream dumpArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assert.assertEquals(ArchiveFormat.DUMP, dumpArchiveInputStream.getFormat());
        dumpArchiveInputStream.close();

        // Test JAR
        InputStream jarInputStream = new FileInputStream("path/to/jarFile.jar");
        ArchiveInputStream jarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertEquals(ArchiveFormat.JAR, jarArchiveInputStream.getFormat());
        JarArchiveEntry jarEntry = (JarArchiveEntry) jarArchiveInputStream.getNextEntry();
        Assert.assertNotNull(jarEntry);
        jarArchiveInputStream.close();

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[512]); // Tar needs an even bigger buffer to check the signature
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertEquals(ArchiveFormat.TAR, tarArchiveInputStream.getFormat());
        TarEntry tarEntry = tarArchiveInputStream.getNextEntry();
        Assert.assertNotNull(tarEntry);
        tarArchiveInputStream.close();

        // Test ZIP
        InputStream zipInputStream = new FileInputStream("path/to/zipFile.zip");
        ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertEquals(ArchiveFormat.ZIP, zipArchiveInputStream.getFormat());
        ZipEntry zipEntry = (ZipEntry) zipArchiveInputStream.getNextEntry();
        Assert.assertNotNull(zipEntry);
        zipArchiveInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        arArchiveOutputStream.putNextEntry(new ARFile.ARFileEntry("test-file.txt"));
        arArchiveOutputStream.writeTo(new ByteArrayInputStream("Test content.".getBytes()));
        arArchiveOutputStream.closeArchiveEntry();
        arArchiveOutputStream.finish();
        Assert.assertEquals(ARFile.AR_MAGIC, arOutputStream.toByteArray());

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        cpioArchiveOutputStream.putNextEntry(new CpioFile.CpioEntry("test-file.txt", (byte) 0644));
        cpioArchiveOutputStream.writeTo(new ByteArrayInputStream("Test content.".getBytes()));
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();
        Assert.assertEquals(CpioFile.CPIO_MAGIC, cpioOutputStream.toByteArray());

        // Test Dump
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        DumpArchiveEntry dumpEntry = new DumpArchiveEntry("test-file.txt", (byte) 0644);
        dumpArchiveOutputStream.putNextEntry(dumpEntry);
        dumpArchiveOutputStream.writeTo(new ByteArrayInputStream("Test content.".getBytes()));
        dumpArchiveOutputStream.closeArchiveEntry();
        dumpArchiveOutputStream.finish();
        Assert.assertArrayEquals(DumpArchiveEntry.DUMP_MAGIC, dumpOutputStream.toByteArray());

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        JarArchiveEntry jarEntry = new JarArchiveEntry("test/TestClass.class", new ByteArrayInputStream("Test class contents.".getBytes()));
        jarArchiveOutputStream.putNextEntry(jarEntry);
        jarArchiveOutputStream.finish();
        Assert.assertTrue(jarOutputStream.size() > 0);

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        TarEntry tarEntry = new TarEntry("test-file.txt");
        tarArchiveOutputStream.putNextEntry(tarEntry);
        tarArchiveOutputStream.writeTo(new ByteArrayInputStream("Test content.".getBytes()));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        Assert.assertEquals(TarEntry.TAR_HEADER_MAGIC, tarOutputStream.toByteArray());

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        ZipEntry zipEntry = new ZipEntry("test-file.txt");
        zipArchiveOutputStream.putNextEntry(zipEntry);
        zipArchiveOutputStream.writeTo(new ByteArrayInputStream("Test content.".getBytes()));
        zipArchiveOutputStream.closeArchiveEntry();
        zipArchiveOutputStream.finish();
        Assert.assertEquals(ZipEntry.ZIP_MAGIC, zipOutputStream.toByteArray());
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(ARFile.AR_MAGIC.getBytes());
        ArchiveInputStream arArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(arInputStream);
        Assert.assertEquals(AR.toLowerCase(), arArchiveInputStream.getFormat().getName());
        arArchiveInputStream.close();

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(CpioFile.CPIO_MAGIC.getBytes());
        ArchiveInputStream cpioArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(cpioInputStream);
        Assert.assertEquals(ArchiveFormat.CPIO, cpioArchiveInputStream.getFormat());
        cpioArchiveInputStream.close();

        // Test Dump
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[32]); // Dump needs a bigger buffer to check the signature
        ArchiveInputStream dumpArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(dumpInputStream);
        Assert.assertEquals(ArchiveFormat.DUMP, dumpArchiveInputStream.getFormat());
        dumpArchiveInputStream.close();

        // Test JAR
        InputStream jarInputStream = new FileInputStream("path/to/jarFile.jar");
        ArchiveInputStream jarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(jarInputStream);
        Assert.assertEquals(ArchiveFormat.JAR, jarArchiveInputStream.getFormat());
        JarArchiveEntry jarEntry = (JarArchiveEntry) jarArchiveInputStream.getNextEntry();
        Assert.assertNotNull(jarEntry);
        jarArchiveInputStream.close();

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[512]); // Tar needs an even bigger buffer to check the signature
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(tarInputStream);
        Assert.assertEquals(ArchiveFormat.TAR, tarArchiveInputStream.getFormat());
        TarEntry tarEntry = tarArchiveInputStream.getNextEntry();
        Assert.assertNotNull(tarEntry);
        tarArchiveInputStream.close();

        // Test ZIP
        InputStream zipInputStream = new FileInputStream("path/to/zipFile.zip");
        ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(zipInputStream);
        Assert.assertEquals(ArchiveFormat.ZIP, zipArchiveInputStream.getFormat());
        ZipEntry zipEntry = (ZipEntry) zipArchiveInputStream.getNextEntry();
        Assert.assertNotNull(zipEntry);
        zipArchiveInputStream.close();
    }
}