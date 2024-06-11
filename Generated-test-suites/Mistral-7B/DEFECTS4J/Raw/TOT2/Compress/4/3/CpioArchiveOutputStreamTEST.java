import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link CpioArchiveOutputStream} class.
 */
public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private static final String TEST_ENTRY_NAME = "testfile";
    private static final String TEST_CONTENTS = "12345";

    private CpioArchiveOutputStream outputStream;
    private File testFile;

    @Before
    public void setUp() throws IOException {
        testFile = new File(TEST_FILE_NAME);
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    /**
     * Test for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)} method
     * with valid arguments.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        outputStream.putArchiveEntry(entry);
        assertNotNull(entry.getName());
        assertNotNull(outputStream.getNextEntry());
    }

    /**
     * Test for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)} method
     * with invalid argument.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPutArchiveEntryInvalidArgument() throws IOException {
        assertThrows(ClassCastException.class, () -> outputStream.putArchiveEntry(new Object() {}));
    }

    /**
     * Test for {@link CpioArchiveOutputStream#write(byte[], int, int)} method
     * with valid arguments.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testWriteValidArguments() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        outputStream.putArchiveEntry(entry);
        byte[] testData = TEST_CONTENTS.getBytes();
        outputStream.write(testData, 0, testData.length);
        byte[] resultData = new ByteArrayOutputStream(testData.length).toByteArray();
        ArchiveUtils.InputStreamWithLength archiveInputStream = outputStream.getInputStream();
        int readBytes = 0;
        byte[] buffer = new byte[1024];
        while ((readBytes = archiveInputStream.read(buffer)) != -1) {
            System.arraycopy(buffer, 0, resultData, 0, readBytes);
        }
        assertArrayEquals(testData, resultData);
    }

    /**
     * Test for {@link CpioArchiveOutputStream#write(byte[], int, int)} method
     * with invalid arguments.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testWriteInvalidArguments() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        outputStream.putArchiveEntry(entry);
        byte[] testData = TEST_CONTENTS.getBytes();
        outputStream.write(testData, -1, testData.length);
        outputStream.write(testData, 0, -1);
    }

    /**
     * Test for {@link CpioArchiveOutputStream#finish()} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(TEST_ENTRY_NAME);
        outputStream.putArchiveEntry(entry1);
        byte[] testData1 = TEST_CONTENTS.getBytes();
        outputStream.write(testData1, 0, testData1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2");
        outputStream.putArchiveEntry(entry2);
        byte[] testData2 = TEST_CONTENTS.getBytes();
        outputStream.write(testData2, 0, testData2.length);

        outputStream.finish();

        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        ArchiveUtils.InputStreamArchive inputStream = new ArchiveInputStream(testFile);
        ArchiveEntry archiveEntry;
        while ((archiveEntry = inputStream.getNextEntry()) != null) {
            entries.put(archiveEntry.getName(), (CpioArchiveEntry) archiveEntry);
        }
        assertEquals(2, entries.size());
        assertEquals(TEST_ENTRY_NAME, entries.get(TEST_ENTRY_NAME).getName());
        assertEquals("testfile2", entries.get("testfile2").getName());
        assertFalse(outputStream.hasNextEntry());
    }

    /**
     * Test for {@link CpioArchiveOutputStream#close()} method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        outputStream.putArchiveEntry(entry);
        byte[] testData = TEST_CONTENTS.getBytes();
        outputStream.write(testData, 0, testData.length);

        outputStream.close();
        assertNull(outputStream.getNextEntry());
        assertFalse(outputStream.hasNextEntry());
    }
}