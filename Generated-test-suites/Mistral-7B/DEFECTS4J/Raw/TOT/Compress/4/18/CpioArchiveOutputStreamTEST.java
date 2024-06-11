import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CpioArchiveOutputStream} class.
 */
public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputFile = new File("target/test-output.cpio");
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        if (outputFile.exists()) {
            FileUtils.deleteQuietly(outputFile);
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content.";

        final File testFile = new File("test/resources/" + testFileName);
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, testFileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        final byte[] expectedContent = testFileContent.getBytes();
        final byte[] actualContent = FileUtils.readByteArray(new File("target/test-output/" + testFileName));

        assertTrue(testFile.exists());
        assertTrue(outputFile.exists());
        assertEquals(expectedContent.length, outputStream.getSize());
        assertArrayEquals(expectedContent, actualContent);

        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content.";

        final File testFile = new File("test/resources/" + testFileName);

        outputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        outputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content.";

        final File testFile = new File("test/resources/" + testFileName);

        outputStream = new CpioArchiveOutputStream(new FileOutputStream(outputFile), (short) 0x10);

        outputStream.putArchiveEntry(new CpioArchiveEntry(testFile, testFileName));
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        assertTrue(testFile.exists());
        assertTrue(outputFile.exists());
        assertFalse(outputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        outputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullFileName() throws IOException {
        final CpioArchiveEntry entry = new CpioArchiveEntry();

        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullFile() throws IOException {
        final CpioArchiveEntry entry = new CpioArchiveEntry(null, "testFile.txt");

        outputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithEmptyFile() throws IOException {
        final String testFileName = "testFile.txt";

        final File testFile = new File("test/resources/" + testFileName);
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, testFileName);

        outputStream.putArchiveEntry(entry);
        outputStream.closeArchiveEntry();

        assertTrue(testFile.exists());
        assertTrue(outputFile.exists());
        assertEquals(0, outputStream.getSize());
    }

    @Test
    public void testPutArchiveEntryWithZeroSizeFile() throws IOException {
        final String testFileName = "testFile.txt";
        final byte[] testFileContent = new byte[0];

        final File testFile = new File("test/resources/" + testFileName);
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, testFileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testFileContent);
        outputStream.closeArchiveEntry();

        assertTrue(testFile.exists());
        assertTrue(outputFile.exists());
        assertEquals(0, outputStream.getSize());
    }

    @Test
    public void testPutArchiveEntryWithLargeFile() throws IOException {
        final String testFileName = "testFile.txt";
        final int testFileSize = 1024 * 1024; // 1 MB

        final File testFile = new File("test/resources/" + testFileName);
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, testFileName);

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);

        outputStream.putArchiveEntry(entry);

        final byte[] fileContent = FileUtils.readByteArray(testFile);
        outputStream.write(fileContent);
        outputStream.closeArchiveEntry();

        assertNotNull(outputStream.getSize());
        assertTrue(outputStream.getSize() >= testFileSize);
    }

    @Test
    public void testFinish() throws IOException {
        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content.";

        final File testFile = new File("test/resources/" + testFileName);
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, testFileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        outputStream.finish();

        final File outputFileContent = new File("target/test-output.cpio");
        final Map<String, ArchiveEntry> entries = new HashMap<>();
        try (ArchiveInputStream archiveInputStream = new ArchiveInputStream(new FileInputStream(outputFileContent))) {
            ArchiveEntry readEntry;
            while ((readEntry = archiveInputStream.getNextEntry()) != null) {
                entries.put(readEntry.getName(), readEntry);
            }
        }

        final ArchiveEntry actualEntry = entries.get(testFileName);
        assertNotNull(actualEntry);
        assertEquals(testFileName, actualEntry.getName());
        assertEquals(CpioConstants.FORMAT_NEW, actualEntry.getFormat());
        assertTrue(actualEntry.isDirectory());
        assertFalse(actualEntry.isFile());
        assertEquals(testFileContent.getBytes().length, actualEntry.getSize());

        testFile.delete();
    }

    @Test
    public void testClose() throws IOException {
        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content.";

        final File testFile = new File("test/resources/" + testFileName);
        final CpioArchiveEntry entry = new CpioArchiveEntry(testFile, testFileName);

        outputStream.putArchiveEntry(entry);
        outputStream.write(testFileContent.getBytes());
        outputStream.closeArchiveEntry();

        outputStream.close();

        assertFalse(outputStream.isFinished());

        testFile.delete();
    }
}