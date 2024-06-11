import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.utils.IOUtils;
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

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamWithNameAndInputStream() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws IOException, ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        factory.createArchiveInputStream(archiverName, null);
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createArchiveInputStream("invalid", inputStream);
            Assert.fail("Should have thrown an ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithNameAndOutputStream() throws IOException, ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws IOException, ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws IOException, ArchiveException {
        String archiverName = ArchiveStreamFactory.JAR;
        factory.createArchiveOutputStream(archiverName, null);
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws IOException, ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            factory.createArchiveOutputStream("invalid", outputStream);
            Assert.fail("Should have thrown an ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        outputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInvalidInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]) {
            @Override
            public void mark(int readlimit) throws IOException {
                throw new IOException();
            }
        };
        try {
            factory.createArchiveInputStream(inputStream);
            Assert.fail("Should have thrown an ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        inputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithValidArchive() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        archiveInputStream.putNextEntry(entry);
        OutputStream outputStream = new ByteArrayOutputStream();
        archiveInputStream.copyEntryContent(entry, outputStream);
        byte[] expectedContent = new byte[0];
        Assert.assertArrayEquals(expectedContent, outputStream.toByteArray());
        archiveInputStream.closeEntry();
        archiveInputStream.close();
    }
}