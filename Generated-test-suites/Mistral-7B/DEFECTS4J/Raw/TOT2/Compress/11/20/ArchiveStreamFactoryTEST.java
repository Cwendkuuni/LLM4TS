import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers. Archives;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test with valid archiver name and InputStream
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(Archives.getTestZipBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);

        // Test with unknown archiver name
        archiverName = "unknown";
        inputStream = new ByteArrayInputStream(Archives.getTestZipBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: unknown not found.", e.getMessage());
        }

        // Test with null InputStream
        inputStream = null;
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("InputStream must not be null.", e.getMessage());
        }

        // Test with invalid InputStream (does not support mark)
        inputStream = new ByteArrayInputStream(Archives.getTestZipBytes()) {
            @Override
            public synchronized void mark(int readlimit) {
                throw new UnsupportedOperationException();
            }

            @Override
            public synchronized void reset() throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Mark is not supported.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Test with valid archiver name and OutputStream
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        Assert.assertNotNull(archiveOutputStream);

        // Test with unknown archiver name
        archiverName = "unknown";
        outputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            Assert.assertEquals("Archiver: unknown not found.", e.getMessage());
        }

        // Test with null OutputStream
        outputStream = null;
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws IOException, ArchiveException {
        // Test with valid InputStream
        InputStream inputStream = new ByteArrayInputStream(Archives.getTestZipBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);

        // Test with null InputStream
        inputStream = null;
        try {
            new ArchiveStreamFactory().createArchiveInputStream(inputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Stream must not be null.", e.getMessage());
        }
    }

}