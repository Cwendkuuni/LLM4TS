import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.util.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test AR format
        ByteArrayInputStream arInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream inAr = ArchiveStreamFactory.newInstance().createArchiveInputStream("ar", arInputStream);
        Assert.assertTrue(inAr instanceof ArArchiveInputStream);
        inAr.close();

        // Test AR format with a bytes array
        byte[] arBytes = new byte[1024];
        InputStream arStream = new ByteArrayInputStream(arBytes);
        ArchiveInputStream inAr2 = ArchiveStreamFactory.newInstance().createArchiveInputStream("ar", arStream);
        Assert.assertTrue(inAr2 instanceof ArArchiveInputStream);
        inAr2.close();

        // Test CPIO format
        ByteArrayInputStream cpioInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream inCpio = ArchiveStreamFactory.newInstance().createArchiveInputStream("cpio", cpioInputStream);
        Assert.assertTrue(inCpio instanceof CpioArchiveInputStream);
        inCpio.close();

        // Test Dump format
        ByteArrayInputStream dumpInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream inDump = ArchiveStreamFactory.newInstance().createArchiveInputStream("dump", dumpInputStream);
        Assert.assertTrue(inDump instanceof DumpArchiveInputStream);
        inDump.close();

        // Test JAR format
        ByteArrayInputStream jarInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream inJar = ArchiveStreamFactory.newInstance().createArchiveInputStream("jar", jarInputStream);
        Assert.assertTrue(inJar instanceof JarArchiveInputStream);
        inJar.close();

        // Test TAR format
        ByteArrayInputStream tarInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream inTar = ArchiveStreamFactory.newInstance().createArchiveInputStream("tar", tarInputStream);
        Assert.assertTrue(inTar instanceof TarArchiveInputStream);
        inTar.close();

        // Test ZIP format
        ByteArrayInputStream zipInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream inZip = ArchiveStreamFactory.newInstance().createArchiveInputStream("zip", zipInputStream);
        Assert.assertTrue(inZip instanceof ZipArchiveInputStream);
        inZip.close();

        // Test archive detection
        ByteArrayInputStream archiveInputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream inDetected = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveInputStream);
        Assert.assertFalse(inDetected instanceof ArArchiveInputStream);
        Assert.assertFalse(inDetected instanceof CpioArchiveInputStream);
        Assert.assertFalse(inDetected instanceof DumpArchiveInputStream);
        Assert.assertFalse(inDetected instanceof JarArchiveInputStream);
        Assert.assertFalse(inDetected instanceof TarArchiveInputStream);
        Assert.assertFalse(inDetected instanceof ZipArchiveInputStream);
        inDetected.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Test AR format
        ByteArrayOutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outAr = ArchiveStreamFactory.newInstance().createArchiveOutputStream("ar", arOutputStream);
        outAr.putNextEntry(new ArArchiveEntry("test.txt"));
        outAr.write(new byte[10], 0, 10);
        outAr.closeArchiveEntry();
        outAr.finish();
        outAr.close();
        Assert.assertEquals(new String(arOutputStream.toByteArray()), "!<arch>\nNAME test.txt\nSIZE 10\nMODE 0644\nMAGIC ar\nUID 0\nGID 0\n");

        // Test CPIO format
        ByteArrayOutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outCpio = ArchiveStreamFactory.newInstance().createArchiveOutputStream("cpio", cpioOutputStream);
        outCpio.putNextEntry(new CpioArchiveEntry("test.txt"));
        outCpio.write(new byte[10], 0, 10);
        outCpio.closeArchiveEntry();
        outCpio.finish();
        outCpio.close();
        Assert.assertEquals(new String(cpioOutputStream.toByteArray()), "20\032test.txt\000\020\014test\032.\n");

        // Test Dump format
        ByteArrayOutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outDump = ArchiveStreamFactory.newInstance().createArchiveOutputStream("dump", dumpOutputStream);
        outDump.putNextEntry(new DumpArchiveEntry("test.txt"));
        outDump.write(new byte[10], 0, 10);
        outDump.closeArchiveEntry();
        outDump.finish();
        outDump.close();
        Assert.assertEquals(new String(dumpOutputStream.toByteArray()), "096033201 test.txt\n");

        // Test JAR format
        ByteArrayOutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outJar = ArchiveStreamFactory.newInstance().createArchiveOutputStream("jar", jarOutputStream);
        outJar.putNextEntry(new JarArchiveEntry("test.txt"));
        outJar.write(new byte[10], 0, 10);
        outJar.closeArchiveEntry();
        outJar.finish();
        outJar.close();
        Assert.assertEquals(new String(jarOutputStream.toByteArray()), "name test.txt\nlocal-header\nversion 1.0\nmanifest-version 1.0\n");

        // Test TAR format
        ByteArrayOutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outTar = ArchiveStreamFactory.newInstance().createArchiveOutputStream("tar", tarOutputStream);
        outTar.putNextEntry(new TarArchiveEntry("test.txt"));
        outTar.write(new byte[10], 0, 10);
        outTar.closeArchiveEntry();
        outTar.finish();
        outTar.close();
        Assert.assertEquals(new String(tarOutputStream.toByteArray()), "\n0 test.txt\n");

        // Test ZIP format
        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outZip = ArchiveStreamFactory.newInstance().createArchiveOutputStream("zip", zipOutputStream);
        outZip.putNextEntry(new ZipArchiveEntry("test.txt"));
        outZip.write(new byte[10], 0, 10);
        outZip.closeArchiveEntry();
        outZip.finish();
        outZip.close();
        Assert.assertEquals(new String(zipOutputStream.toByteArray()), "\nlocal header\nname: test.txt\n");

        // Test write empty archive
        ByteArrayOutputStream emptyArchiveOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outEmpty = ArchiveStreamFactory.newInstance().createArchiveOutputStream("zip", emptyArchiveOutputStream);
        outEmpty.finish();
        outEmpty.close();
        Assert.assertEquals(new String(emptyArchiveOutputStream.toByteArray()), "");
    }

    @Test
    public void testCreateArchiveOutputStreamWithInputStream() throws IOException, ArchiveException {
        // Test AR format
        ByteArrayInputStream arInputStream = new ByteArrayInputStream(new byte[1024]);
        ByteArrayOutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outAr = ArchiveStreamFactory.newInstance().createArchiveOutputStream("ar", arOutputStream, arInputStream);
        outAr.putNextEntry(new ArArchiveEntry("test.txt"));
        outAr.write(new byte[10], 0, 10);
        outAr.closeArchiveEntry();
        outAr.finish();
        outAr.close();
        Assert.assertEquals(new String(arOutputStream.toByteArray()), "!<arch>\nNAME test.txt\nSIZE 10\nMODE 0644\nMAGIC ar\nUID 0\nGID 0\n");

        // Test CPIO format
        ByteArrayInputStream cpioInputStream = new ByteArrayInputStream(new byte[1024]);
        ByteArrayOutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outCpio = ArchiveStreamFactory.newInstance().createArchiveOutputStream("cpio", cpioOutputStream, cpioInputStream);
        outCpio.putNextEntry(new CpioArchiveEntry("test.txt"));
        outCpio.write(new byte[10], 0, 10);
        outCpio.closeArchiveEntry();
        outCpio.finish();
        outCpio.close();
        Assert.assertEquals(new String(cpioOutputStream.toByteArray()), "20\032test.txt\000\020\014test\032.\n");

        // Test Dump format
        ByteArrayInputStream dumpInputStream = new ByteArrayInputStream(new byte[1024]);
        ByteArrayOutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outDump = ArchiveStreamFactory.newInstance().createArchiveOutputStream("dump", dumpOutputStream, dumpInputStream);
        outDump.putNextEntry(new DumpArchiveEntry("test.txt"));
        outDump.write(new byte[10], 0, 10);
        outDump.closeArchiveEntry();
        outDump.finish();
        outDump.close();
        Assert.assertEquals(new String(dumpOutputStream.toByteArray()), "096033201 test.txt\n");

        // Test JAR format
        ByteArrayInputStream jarInputStream = new ByteArrayInputStream(new byte[1024]);
        ByteArrayOutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outJar = ArchiveStreamFactory.newInstance().createArchiveOutputStream("jar", jarOutputStream, jarInputStream);
        outJar.putNextEntry(new JarArchiveEntry("test.txt"));
        outJar.write(new byte[10], 0, 10);
        outJar.closeArchiveEntry();
        outJar.finish();
        outJar.close();
        Assert.assertEquals(new String(jarOutputStream.toByteArray()), "name test.txt\nlocal-header\nversion 1.0\nmanifest-version 1.0\n");

        // Test TAR format
        ByteArrayInputStream tarInputStream = new ByteArrayInputStream(new byte[1024]);
        ByteArrayOutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outTar = ArchiveStreamFactory.newInstance().createArchiveOutputStream("tar", tarOutputStream, tarInputStream);
        outTar.putNextEntry(new TarArchiveEntry("test.txt"));
        outTar.write(new byte[10], 0, 10);
        outTar.closeArchiveEntry();
        outTar.finish();
        outTar.close();
        Assert.assertEquals(new String(tarOutputStream.toByteArray()), "\n0 test.txt\n");

        // Test ZIP format
        ByteArrayInputStream zipInputStream = new ByteArrayInputStream(new byte[1024]);
        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream outZip = ArchiveStreamFactory.newInstance().createArchiveOutputStream("zip", zipOutputStream, zipInputStream);
        outZip.putNextEntry(new ZipArchiveEntry("test.txt"));
        outZip.write(new byte[10], 0, 10);
        outZip.closeArchiveEntry();
        outZip.finish();
        outZip.close();
        Assert.assertEquals(new String(zipOutputStream.toByteArray()), "\nlocal header\nname: test.txt\n");
    }

    @Test
    public void testCreateArchiveInputStreamWithBytes() throws IOException, ArchiveException {
        // Test AR format
        byte[] bytesAr = new byte[1024];
        InputStream arInputStream = new ByteArrayInputStream(bytesAr);
        ArchiveInputStream inAr = ArchiveStreamFactory.newInstance().createArchiveInputStream("ar", arInputStream);
        Assert.assertTrue(inAr instanceof ArArchiveInputStream);
        inAr.close();

        // Test CPIO format
        byte[] bytesCpio = new byte[1024];
        InputStream cpioInputStream = new ByteArrayInputStream(bytesCpio);
        ArchiveInputStream inCpio = ArchiveStreamFactory.newInstance().createArchiveInputStream("cpio", cpioInputStream);
        Assert.assertTrue(inCpio instanceof CpioArchiveInputStream);
        inCpio.close();

        // Test Dump format
        byte[] bytesDump = new byte[1024];
        InputStream dumpInputStream = new ByteArrayInputStream(bytesDump);
        ArchiveInputStream inDump = ArchiveStreamFactory.newInstance().createArchiveInputStream("dump", dumpInputStream);
        Assert.assertTrue(inDump instanceof DumpArchiveInputStream);
        inDump.close();

        // Test JAR format
        byte[] bytesJar = new byte[1024];
        InputStream jarInputStream = new ByteArrayInputStream(bytesJar);
        ArchiveInputStream inJar = ArchiveStreamFactory.newInstance().createArchiveInputStream("jar", jarInputStream);
        Assert.assertTrue(inJar instanceof JarArchiveInputStream);
        inJar.close();

        // Test TAR format
        byte[] bytesTar = new byte[1024];
        InputStream tarInputStream = new ByteArrayInputStream(bytesTar);
        ArchiveInputStream inTar = ArchiveStreamFactory.newInstance().createArchiveInputStream("tar", tarInputStream);
        Assert.assertTrue(inTar instanceof TarArchiveInputStream);
        inTar.close();

        // Test ZIP format
        byte[] bytesZip = new byte[1024];
        InputStream zipInputStream = new ByteArrayInputStream(bytesZip);
        ArchiveInputStream inZip = ArchiveStreamFactory.newInstance().createArchiveInputStream("zip", zipInputStream);
        Assert.assertTrue(inZip instanceof ZipArchiveInputStream);
        inZip.close();
    }
}