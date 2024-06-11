import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Tests for {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    private static final String TEST_FILE = "test.txt";
    private static final String TEST_CONTENT = "Test content.";

    /**
     * Tests creating an archive input stream from a name and an input stream.
     */
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Create a test file
        File testFile = new File(TEST_FILE);
        try (FileOutputStream fos = new FileOutputStream(testFile)) {
            fos.write(TEST_CONTENT.getBytes(StandardCharsets.UTF_8));
        }

        // Create a ZIP archive output stream and write the test file into it
        String archiveName = "zip";
        try (FileOutputStream out = new FileOutputStream("test.zip")) {
            ArchiveOutputStream archiveOut = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
            archiveOut.putArchiveEntry(new ZipArchiveEntry(TEST_FILE));
            try (FileInputStream in = new FileInputStream(TEST_FILE)) {
                IOUtils.copy(in, archiveOut.getOutputStream(new ZipArchiveEntry(TEST_FILE)));
            }
            archiveOut.closeArchiveEntry();
            archiveOut.finish();
            archiveOut.close();
        }

        // Create an archive input stream from the archive name and the archive file
        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(archiveName, new FileInputStream("test.zip"))) {
            ArchiveEntry entry = archiveIn.getNextEntry();
            Assert.assertEquals(TEST_FILE, entry.getName());
            try (InputStream input = archiveIn.getInputStream(entry)) {
                byte[] bytes = new byte[TEST_CONTENT.length()];
                int length = input.read(bytes, 0, bytes.length);
                Assert.assertEquals(TEST_CONTENT, new String(bytes, 0, length));
            }
        }

        // Clean up
        testFile.delete();
        new File("test.zip").delete();
    }

    /**
     * Tests creating an archive output stream from a name and an output stream.
     */
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Create a test file
        File testFile = new File(TEST_FILE);
        try (FileOutputStream fos = new FileOutputStream(testFile)) {
            fos.write(TEST_CONTENT.getBytes(StandardCharsets.UTF_8));
        }

        // Create an archive input stream from the test file
        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream("zip", new FileInputStream(TEST_FILE))) {
            ArchiveEntry entry = archiveIn.getNextEntry();
            Assert.assertEquals(TEST_FILE, entry.getName());
            InputStream input = archiveIn.getInputStream(entry);
            byte[] bytes = new byte[TEST_CONTENT.length()];
            int length = input.read(bytes, 0, bytes.length);
            Assert.assertEquals(TEST_CONTENT, new String(bytes, 0, length));
        }

        // Create an archive output stream and write the test file into it
        String archiveName = "zip";
        try (FileOutputStream out = new FileOutputStream("test.zip")) {
            ArchiveOutputStream archiveOut = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, out);
            archiveOut.putArchiveEntry(new ZipArchiveEntry(TEST_FILE));
            try (FileInputStream in = new FileInputStream(TEST_FILE)) {
                IOUtils.copy(in, archiveOut.getOutputStream(new ZipArchiveEntry(TEST_FILE)));
            }
            archiveOut.closeArchiveEntry();
            archiveOut.finish();
            archiveOut.close();
        }

        // Create an archive input stream from the archive name and the archive file
        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(archiveName, new FileInputStream("test.zip"))) {
            ArchiveEntry entry = archiveIn.getNextEntry();
            Assert.assertEquals(TEST_FILE, entry.getName());
            try (InputStream input = archiveIn.getInputStream(entry)) {
                byte[] bytes = new byte[TEST_CONTENT.length()];
                int length = input.read(bytes, 0, bytes.length);
                Assert.assertEquals(TEST_CONTENT, new String(bytes, 0, length));
            }
        }

        // Clean up
        testFile.delete();
        new File("test.zip").delete();
    }

    /**
     * Tests creating an archive input stream from an input stream with autodetection.
     */
    @Test
    public void testCreateArchiveInputStreamAutodetect() throws Exception {
        // Create a test file
        File testFile = new File(TEST_FILE);
        try (FileOutputStream fos = new FileOutputStream(testFile)) {
            fos.write(TEST_CONTENT.getBytes(StandardCharsets.UTF_8));
        }

        // Create a ZIP archive output stream and write the test file into it
        String archiveName = "zip";
        try (FileOutputStream out = new FileOutputStream("test.zip")) {
            ArchiveOutputStream archiveOut = new ArchiveStreamFactory()
                    .createArchiveOutputStream(archiveName, out);
            archiveOut.putArchiveEntry(new ZipArchiveEntry(TEST_FILE));
            try (FileInputStream in = new FileInputStream(TEST_FILE)) {
                IOUtils.copy(in, archiveOut.getOutputStream(new ZipArchiveEntry(TEST_FILE)));
            }
            archiveOut.closeArchiveEntry();
            archiveOut.finish();
            archiveOut.close();
        }

        // Create an archive input stream from the input stream autodetected
        try (FileInputStream in = new FileInputStream(TEST_FILE)) {
            ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                    .createArchiveInputStream(in);
            ArchiveEntry entry = archiveIn.getNextEntry();
            Assert.assertEquals(TEST_FILE, entry.getName());
            try (InputStream input = archiveIn.getInputStream(entry)) {
                byte[] bytes = new byte[TEST_CONTENT.length()];
                int length = input.read(bytes, 0, bytes.length);
                Assert.assertEquals(TEST_CONTENT, new String(bytes, 0, length));
            }
        }

        // Clean up
        testFile.delete();
        new File("test.zip").delete();
    }

    /**
     * Tests creating an archive output stream and writing an entry with a compression method.
     */
    @Test
    public void testCreateArchiveOutputStreamWithCompression() throws Exception {
        try (FileOutputStream out = new FileOutputStream("test.zip")) {
            ArchiveOutputStream archiveOut = new ArchiveStreamFactory()
                    .createArchiveOutputStream("zip", out);
            archiveOut.setLevel(CompressorLevel.ULTRA);
            archiveOut.putArchiveEntry(new ZipArchiveEntry("test.txt"));
            try (FileOutputStream compressedOut = new FileOutputStream("test.gz")) {
                GzipCompressorOutputStream gzipOut = new GzipCompressorOutputStream(compressedOut);
                ArchiveOutputStream gzipArchiveOut = new ArchiveStreamFactory()
                        .createArchiveOutputStream("gzip", gzipOut);
                gzipArchiveOut.putArchiveEntry(new ZipArchiveEntry("test.txt"));
                try (FileInputStream testFile = new FileInputStream("test.txt")) {
                    IOUtils.copy(testFile, gzipArchiveOut.getOutputStream(new ZipArchiveEntry("test.txt")));
                }
                archiveOut.putOutputStream(new ZipArchiveOutputStream(gzipOut).putNextEntry("test.gz"));
                archiveOut.closeArchiveEntry();
                gzipArchiveOut.finish();
                gzipArchiveOut.close();
                gzipOut.finish();
            }
            archiveOut.closeArchiveEntry();
            archiveOut.finish();
            archiveOut.close();
        }

        // Create an archive input stream from the archive name and the archive file
        try (ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream("zip", new FileInputStream("test.zip"))) {
            ArchiveEntry testEntry = archiveIn.getNextEntry();
            Assert.assertEquals("test.txt", testEntry.getName());
            ArchiveEntry gzipEntry = archiveIn.getNextEntry();
            Assert.assertEquals("test.gz", gzipEntry.getName());

            try (InputStream testInput = archiveIn.getInputStream(testEntry)) {
                byte[] testBytes = new byte[TEST_CONTENT.length()];
                int testLength = testInput.read(testBytes, 0, testBytes.length);
                Assert.assertEquals(TEST_CONTENT, new String(testBytes, 0, testLength));
            }

            try (InputStream gzipInput = archiveIn.getInputStream(gzipEntry)) {
                byte[] gzipBytes = new byte[TEST_CONTENT.length()];
                int gzipLength = gzipInput.read(gzipBytes, 0, gzipBytes.length);
                Assert.assertArrayEquals(testBytes, gzipBytes);
            }
        }
    }

    /**
     * Tests creating an archive input stream from an invalid archive type.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidArchiveType() throws Exception {
        try (InputStream in = new ByteArrayInputStream(new byte[0])) {
            new ArchiveStreamFactory().createArchiveInputStream("invalid", in);
        }
    }

    /**
     * Tests creating an archive output stream from an invalid archive type.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamInvalidArchiveType() throws Exception {
        try (OutputStream out = new ByteArrayOutputStream()) {
            new ArchiveStreamFactory().createArchiveOutputStream("invalid", out);
        }
    }
}