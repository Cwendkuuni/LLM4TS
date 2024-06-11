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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setup() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws ArchiveException, IOException {
        // Test AR
        InputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, createMockInputStream());
        Assert.assertInstanceOf(ArArchiveInputStream.class, arInputStream);

        // Test CPIO
        InputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, createMockInputStream());
        Assert.assertInstanceOf(CpioArchiveInputStream.class, cpioInputStream);

        // Test DUMP
        InputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, createMockInputStream());
        Assert.assertInstanceOf(DumpArchiveInputStream.class, dumpInputStream);

        // Test JAR
        InputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, createMockInputStream());
        Assert.assertInstanceOf(JarArchiveInputStream.class, jarInputStream);

        // Test TAR
        InputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, createMockInputStream());
        Assert.assertInstanceOf(TarArchiveInputStream.class, tarInputStream);

        // Test ZIP
        InputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, createMockInputStream());
        Assert.assertInstanceOf(ZipArchiveInputStream.class, zipInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws ArchiveException, IOException {
        // Test AR
        InputStream arInputStream = factory.createArchiveInputStream(createMockInputStream());
        Assert.assertInstanceOf(ArArchiveInputStream.class, arInputStream);

        // Test CPIO
        InputStream cpioInputStream = factory.createArchiveInputStream(createMockInputStream());
        Assert.assertInstanceOf(CpioArchiveInputStream.class, cpioInputStream);

        // Test DUMP
        InputStream dumpInputStream = factory.createArchiveInputStream(createMockInputStream());
        Assert.assertInstanceOf(DumpArchiveInputStream.class, dumpInputStream);

        // Test JAR
        InputStream jarInputStream = factory.createArchiveInputStream(createMockInputStream());
        Assert.assertInstanceOf(JarArchiveInputStream.class, jarInputStream);

        // Test TAR
        InputStream tarInputStream = factory.createArchiveInputStream(createMockInputStream());
        Assert.assertInstanceOf(TarArchiveInputStream.class, tarInputStream);

        // Test ZIP
        InputStream zipInputStream = factory.createArchiveInputStream(createMockInputStream());
        Assert.assertInstanceOf(ZipArchiveInputStream.class, zipInputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws ArchiveException {
        // Test AR
        OutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, createMockOutputStream());
        Assert.assertInstanceOf(ArArchiveOutputStream.class, arOutputStream);

        // Test CPIO
        OutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, createMockOutputStream());
        Assert.assertInstanceOf(CpioArchiveOutputStream.class, cpioOutputStream);

        // Test JAR
        OutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, createMockOutputStream());
        Assert.assertInstanceOf(JarArchiveOutputStream.class, jarOutputStream);

        // Test TAR
        OutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, createMockOutputStream());
        Assert.assertInstanceOf(TarArchiveOutputStream.class, tarOutputStream);

        // Test ZIP
        OutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, createMockOutputStream());
        Assert.assertInstanceOf(ZipArchiveOutputStream.class, zipOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws ArchiveException, IOException {
        factory.createArchiveInputStream("unknown", createMockInputStream());
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownFormat() throws ArchiveException {
        factory.createArchiveOutputStream("unknown", createMockOutputStream());
    }

    private InputStream createMockInputStream() {
        return new ByteArrayInputStream(new byte[0]);
    }

    private OutputStream createMockOutputStream() {
        return new ByteArrayOutputStream();
    }
}