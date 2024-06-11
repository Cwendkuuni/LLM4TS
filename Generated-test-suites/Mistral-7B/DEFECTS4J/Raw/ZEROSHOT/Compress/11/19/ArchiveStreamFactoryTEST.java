import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.AR;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        this.factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() {
        // Test AR
        final InputStream arInputStream = new ByteArrayInputStream(new byte[1024]);
        final ArchiveInputStream arArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        Assert.assertTrue(arArchiveInputStream instanceof ArArchiveInputStream);
        arArchiveInputStream.close();

        // Test CPIO
        final InputStream cpioInputStream = new ByteArrayInputStream(new byte[1024]);
        final ArchiveInputStream cpioArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        Assert.assertTrue(cpioArchiveInputStream instanceof CpioArchiveInputStream);
        cpioArchiveInputStream.close();

        // Test DUMP
        final InputStream dumpInputStream = new ByteArrayInputStream(new byte[1024]);
        final ArchiveInputStream dumpArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        Assert.assertTrue(dumpArchiveInputStream instanceof DumpArchiveInputStream);
        dumpArchiveInputStream.close();

        // Test JAR
        final InputStream jarInputStream = new ByteArrayInputStream(new byte[1024]);
        final ArchiveInputStream jarArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        Assert.assertTrue(jarArchiveInputStream instanceof JarArchiveInputStream);
        jarArchiveInputStream.close();

        // Test TAR
        final InputStream tarInputStream = new ByteArrayInputStream(new byte[1024]);
        final ArchiveInputStream tarArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        Assert.assertTrue(tarArchiveInputStream instanceof TarArchiveInputStream);
        tarArchiveInputStream.close();

        // Test ZIP
        final InputStream zipInputStream = new ByteArrayInputStream(new byte[1024]);
        final ArchiveInputStream zipArchiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        Assert.assertTrue(zipArchiveInputStream instanceof ZipArchiveInputStream);
        zipArchiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromUnknownName() {
        final InputStream unknownInputStream = new ByteArrayInputStream(new byte[1024]);
        factory.createArchiveInputStream("unknown", unknownInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        final InputStream arInputStream = new ByteArrayInputStream(new byte[1024]);
        factory.createArchiveInputStream(null, arInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        final String archiverName = ArchiveStreamFactory.AR;
        factory.createArchiveInputStream(archiverName, null);
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() {
        // Test AR
        final OutputStream arOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream arArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        Assert.assertTrue(arArchiveOutputStream instanceof ArArchiveOutputStream);
        arArchiveOutputStream.close();

        // Test CPIO
        final OutputStream cpioOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream cpioArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        Assert.assertTrue(cpioArchiveOutputStream instanceof CpioArchiveOutputStream);
        cpioArchiveOutputStream.close();

        // Test DUMP
        final OutputStream dumpOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream dumpArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        Assert.assertTrue(dumpArchiveOutputStream instanceof DumpArchiveOutputStream);
        dumpArchiveOutputStream.close();

        // Test JAR
        final OutputStream jarOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream jarArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        Assert.assertTrue(jarArchiveOutputStream instanceof JarArchiveOutputStream);
        jarArchiveOutputStream.close();

        // Test TAR
        final OutputStream tarOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream tarArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        Assert.assertTrue(tarArchiveOutputStream instanceof TarArchiveOutputStream);
        tarArchiveOutputStream.close();

        // Test ZIP
        final OutputStream zipOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream zipArchiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        Assert.assertTrue(zipArchiveOutputStream instanceof ZipArchiveOutputStream);
        zipArchiveOutputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromUnknownName() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        final OutputStream arOutputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, arOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        final String archiverName = ArchiveStreamFactory.AR;
        factory.createArchiveOutputStream(archiverName, null);
    }
}