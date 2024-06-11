import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = folder.newFile("test.tar");
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(testFile);
        IOUtils.deleteQuietly(testFile);
    }

    /**
     * Test constructor with default block size and record size.
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            assertNotNull(out);
        }
    }

    /**
     * Test constructor with custom block size and record size.
     */
    @Test
    public void testCustomConstructors() throws IOException {
        int blockSize = 1024;
        int recordSize = 512;

        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile), blockSize, recordSize)) {
            assertEquals(blockSize, out.getBlockSize());
            assertEquals(recordSize, out.getRecordSize());
        }
    }

    /**
     * Test putArchiveEntry method with empty name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutEmptyNameEntry() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            out.putArchiveEntry(new TarArchiveEntry(""));
        }
    }

    /**
     * Test putArchiveEntry method with too long name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutLongNameEntry() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            String longName = "a".repeat(TarConstants.NAMELEN + 1);
            out.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
            TarArchiveEntry entry = new TarArchiveEntry(longName);
            out.putArchiveEntry(entry);
        }
    }

    /**
     * Test putArchiveEntry method with long file name using GNU extension.
     */
    @Test
    public void testPutLongNameEntryWithGNUExtension() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile), TarArchiveOutputStream.LONGFILE_GNU)) {
            String longName = "a".repeat(TarConstants.NAMELEN + 1);
            TarArchiveEntry entry = new TarArchiveEntry(longName);
            out.putArchiveEntry(entry);
            TarArchiveEntry longLinkEntry = out.getNextEntry();
            assertNotNull(longLinkEntry);
            assertNotNull(longLinkEntry.getName());
            assertEquals(longName, longLinkEntry.getName().substring(1));
            out.closeArchiveEntry();
        }
    }

    /**
     * Test putArchiveEntry method with valid name.
     */
    @Test
    public void testPutValidNameEntry() throws IOException {
        String validName = "test.txt";

        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry(validName);
            out.putArchiveEntry(entry);
            TarArchiveEntry resultEntry = out.getNextEntry();
            assertNotNull(resultEntry);
            assertEquals(validName, resultEntry.getName());
            out.closeArchiveEntry();
        }
    }

    /**
     * Test putArchiveEntry method with directory entry.
     */
    @Test
    public void testPutDirectoryEntry() throws IOException {
        String dirName = "testDir/";

        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry(dirName);
            entry.setDirectory(true);
            out.putArchiveEntry(entry);
            TarArchiveEntry resultEntry = out.getNextEntry();
            assertNotNull(resultEntry);
            assertEquals(dirName, resultEntry.getName());
            out.closeArchiveEntry();
        }
    }

    /**
     * Test write method with small input.
     */
    @Test
    public void testWriteSmallInput() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            String testData = "hello world";
            byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            out.putArchiveEntry(entry);
            out.write(testDataBytes, 0, testDataBytes.length);
            out.closeArchiveEntry();

            InputStream testInputStream = new FileInputStream(testFile);
            byte[] buffer = new byte[testDataBytes.length];
            int bytesRead = testInputStream.read(buffer);
            testInputStream.close();
            assertEquals(testDataBytes.length, bytesRead);
            assertEquals(testData, new String(buffer, StandardCharsets.UTF_8));

            testFile.delete();
        }
    }

    /**
     * Test write method with large input.
     */
    @Test
    public void testWriteLargeInput() throws IOException {
        int largeDataLength = 1024 * 1024;
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            byte[] largeData = new byte[largeDataLength];
            Arrays.fill(largeData, (byte) 0x01);
            TarArchiveEntry entry = new TarArchiveEntry("test.bin");
            out.putArchiveEntry(entry);
            out.write(largeData, 0, largeDataLength);
            out.closeArchiveEntry();

            InputStream testInputStream = new FileInputStream(testFile);
            byte[] buffer = new byte[largeDataLength];
            int bytesRead = testInputStream.read(buffer);
            testInputStream.close();
            assertEquals(largeDataLength, bytesRead);
            assertNotNull(Arrays.equals(largeData, buffer));

            testFile.delete();
        }
    }

    /**
     * Test closeArchiveEntry method with no entry.
     */
    @Test(expected = IllegalStateException.class)
    public void testCloseNoEntry() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            out.closeArchiveEntry();
        }
    }

    /**
     * Test closeArchiveEntry method with unclosed entry.
     */
    @Test(expected = IllegalStateException.class)
    public void testCloseUnclosedEntry() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            out.putArchiveEntry(entry);
            out.closeArchiveEntry();
            out.closeArchiveEntry();
        }
    }

    /**
     * Test closeArchiveEntry method with closed entry.
     */
    @Test
    public void testCloseClosedEntry() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            out.putArchiveEntry(entry);
            out.closeArchiveEntry();

            TarArchiveEntry closedEntry = out.getNextEntry();
            assertNull(closedEntry);
        }
    }

    /**
     * Test finish method with closed entry.
     */
    @Test
    public void testFinishClosedEntry() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            out.putArchiveEntry(entry);
            out.finish();

            TarArchiveEntry closedEntry = out.getNextEntry();
            assertNull(closedEntry);

            InputStream testInputStream = new FileInputStream(testFile);
            byte[] buffer = new byte[1024];
            int bytesRead = testInputStream.read(buffer);
            testInputStream.close();
            assertEquals(0, bytesRead);
        }
    }

    /**
     * Test finish method with unclosed entry.
     */
    @Test(expected = IllegalStateException.class)
    public void testFinishUnclosedEntry() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            out.putArchiveEntry(entry);
            out.finish();
        }
    }

    /**
     * Test close method with closed entry and no exceptions.
     */
    @Test
    public void testCloseClosedEntryNoExceptions() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            out.putArchiveEntry(entry);
            out.close();

            TarArchiveEntry closedEntry = out.getNextEntry();
            assertNull(closedEntry);

            InputStream testInputStream = new FileInputStream(testFile);
            byte[] buffer = new byte[1024];
            int bytesRead = testInputStream.read(buffer);
            testInputStream.close();
            assertEquals(0, bytesRead);
        }
    }

    /**
     * Test close method with open entry and IOException.
     */
    @Test(expected = IOException.class)
    public void testCloseOpenEntryIOException() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            out.putArchiveEntry(entry);
            out.setErrorMode(TarStreamErrorHandler.FAIL_FAST);

            try {
                out.close();
            } finally {
                TarStreamErrorHandler errorHandler = out.getErrorHandler();
                assertNotNull(errorHandler);
                assertEquals(TarStreamErrorHandler.FAIL_FAST, errorHandler);
            }
        }
    }

    /**
     * Test close method with open entry and EOFException.
     */
    @Test(expected = EOFException.class)
    public void testCloseOpenEntryEOFException() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            out.putArchiveEntry(entry);
            out.setErrorMode(TarStreamErrorHandler.TERMINATE_STREAM);

            try {
                out.close();
            } finally {
                TarStreamErrorHandler errorHandler = out.getErrorHandler();
                assertNotNull(errorHandler);
                assertFalse(errorHandler instanceof TarStreamErrorHandler.FailFast);
                assertFalse(errorHandler instanceof TarStreamErrorHandler.TerminateStream);
            }
        }
    }

    /**
     * Test close method with open entry and RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void testCloseOpenEntryRuntimeException() throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            out.putArchiveEntry(entry);
            out.setErrorMode(TarStreamErrorHandler.THROW_EXCEPTION);

            try {
                out.close();
            } finally {
                TarStreamErrorHandler errorHandler = out.getErrorHandler();
                assertNotNull(errorHandler);
                assertFalse(errorHandler instanceof TarStreamErrorHandler.FailFast);
                assertFalse(errorHandler instanceof TarStreamErrorHandler.TerminateStream);
                assertTrue(errorHandler instanceof TarStreamErrorHandler.ThrowException);
            }
        }
    }
}