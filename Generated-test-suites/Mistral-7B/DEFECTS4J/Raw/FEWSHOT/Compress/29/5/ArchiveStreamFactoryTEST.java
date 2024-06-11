package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStream_AR() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("AR signature".getBytes());
        ArchiveInputStream inputStream = factory.createArchiveInputStream("AR", in);
        assertInstanceOf(ArArchiveInputStream.class, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullInputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream("AR", null));
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, new ByteArrayInputStream("AR signature".getBytes())));
    }

    @Test
    public void testCreateArchiveInputStream_ARJ() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("ARJ signature".getBytes());
        ArchiveInputStream inputStream = factory.createArchiveInputStream("ARJ", in);
        assertInstanceOf(ArjArchiveInputStream.class, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ARJ_NullInputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream("ARJ", null));
    }

    @Test
    public void testCreateArchiveInputStream_ARJ_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, new ByteArrayInputStream("ARJ signature".getBytes())));
    }

    @Test
    public void testCreateArchiveInputStream_Cpio() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("Cpio signature".getBytes());
        ArchiveInputStream inputStream = factory.createArchiveInputStream("Cpio", in);
        assertInstanceOf(CpioArchiveInputStream.class, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Cpio_NullInputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream("Cpio", null));
    }

    @Test
    public void testCreateArchiveInputStream_Cpio_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, new ByteArrayInputStream("Cpio signature".getBytes())));
    }

    @Test
    public void testCreateArchiveInputStream_Dump() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("Dump signature".getBytes());
        ArchiveInputStream inputStream = factory.createArchiveInputStream("Dump", in);
        assertInstanceOf(DumpArchiveInputStream.class, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Dump_NullInputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream("Dump", null));
    }

    @Test
    public void testCreateArchiveInputStream_Dump_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, new ByteArrayInputStream("Dump signature".getBytes())));
    }

    @Test
    public void testCreateArchiveInputStream_Jar() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("Jar signature".getBytes());
        ArchiveInputStream inputStream = factory.createArchiveInputStream("Jar", in);
        assertInstanceOf(JarArchiveInputStream.class, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Jar_NullInputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream("Jar", null));
    }

    @Test
    public void testCreateArchiveInputStream_Jar_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, new ByteArrayInputStream("Jar signature".getBytes())));
    }

    @Test
    public void testCreateArchiveInputStream_Tar() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("Tar signature".getBytes());
        ArchiveInputStream inputStream = factory.createArchiveInputStream("Tar", in);
        assertInstanceOf(TarArchiveInputStream.class, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Tar_NullInputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream("Tar", null));
    }

    @Test
    public void testCreateArchiveInputStream_Tar_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, new ByteArrayInputStream("Tar signature".getBytes())));
    }

    @Test
    public void testCreateArchiveInputStream_Zip() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("Zip signature".getBytes());
        ArchiveInputStream inputStream = factory.createArchiveInputStream("Zip", in);
        assertInstanceOf(ZipArchiveInputStream.class, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Zip_NullInputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream("Zip", null));
    }

    @Test
    public void testCreateArchiveInputStream_Zip_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, new ByteArrayInputStream("Zip signature".getBytes())));
    }

    @Test
    public void testCreateArchiveInputStream_SevenZ() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream("7z signature".getBytes());
        Assertions.assertThrows(StreamingNotSupportedException.class, () -> factory.createArchiveInputStream("7z", in));
    }

    @Test
    public void testCreateArchiveInputStream_SevenZ_NullInputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream("7z", null));
    }

    @Test
    public void testCreateArchiveInputStream_SevenZ_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, new ByteArrayInputStream("7z signature".getBytes())));
    }

    @Test
    public void testCreateArchiveOutputStream_AR() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("AR", out);
        assertInstanceOf(ArArchiveOutputStream.class, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_NullOutputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream("AR", null));
    }

    @Test
    public void testCreateArchiveOutputStream_AR_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_ARJ() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("ARJ", out);
        assertInstanceOf(ArjArchiveOutputStream.class, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ARJ_NullOutputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream("ARJ", null));
    }

    @Test
    public void testCreateArchiveOutputStream_ARJ_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("Cpio", out);
        assertInstanceOf(CpioArchiveOutputStream.class, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio_NullOutputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream("Cpio", null));
    }

    @Test
    public void testCreateArchiveOutputStream_Cpio_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_Dump() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("Dump", out);
        assertNull(outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Dump_NullOutputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream("Dump", null));
    }

    @Test
    public void testCreateArchiveOutputStream_Dump_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_Jar() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("Jar", out);
        assertInstanceOf(JarArchiveOutputStream.class, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Jar_NullOutputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream("Jar", null));
    }

    @Test
    public void testCreateArchiveOutputStream_Jar_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_Tar() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("Tar", out);
        assertInstanceOf(TarArchiveOutputStream.class, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Tar_NullOutputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream("Tar", null));
    }

    @Test
    public void testCreateArchiveOutputStream_Tar_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_Zip() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream outputStream = factory.createArchiveOutputStream("Zip", out);
        assertInstanceOf(ZipArchiveOutputStream.class, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_Zip_NullOutputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream("Zip", null));
    }

    @Test
    public void testCreateArchiveOutputStream_Zip_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_SevenZ() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        Assertions.assertThrows(StreamingNotSupportedException.class, () -> factory.createArchiveOutputStream("7z", out));
    }

    @Test
    public void testCreateArchiveOutputStream_SevenZ_NullOutputStream() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream("7z", null));
    }

    @Test
    public void testCreateArchiveOutputStream_SevenZ_NullArchiverName() {
        ArchiveFactory factory = new ArchiveStreamFactory();
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }
}