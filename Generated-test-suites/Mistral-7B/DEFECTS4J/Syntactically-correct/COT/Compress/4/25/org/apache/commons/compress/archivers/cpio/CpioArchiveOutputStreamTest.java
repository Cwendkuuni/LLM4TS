import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
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
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private File testFile;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        testFile = File.createTempFile("test", null);
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
            outputStream.close();
        }
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testConstructor() {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioArchiveOutputStream.FORMAT_NEW);
        assertNotNull(cpioArchiveOutputStream);
    }

    @Test
    public void testPutArchiveEntry_validEntry_success() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("testEntry", 1L, "test/testFile.txt"));
        assertEquals(1, cpioArchiveOutputStream.getNextEntry().getName().split("/").length);
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testPutArchiveEntry_duplicateEntry_throwsIOException() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("testEntry", 1L, "test/testFile.txt"));
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("testEntry", 1L, "test/testFile.txt")));
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry_invalidFormat_throwsIllegalArgumentException() throws IOException {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), (short) 99);
        assertThrows(IllegalArgumentException.class, cpioArchiveOutputStream::putArchiveEntry, "Invalid format: 99");
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry_nullEntry_throwsNullPointerException() throws IOException {
        assertThrows(NullPointerException.class, () -> cpioArchiveOutputStream.putArchiveEntry(null));
    }

    @Test
    public void testWrite_validData_success() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("testEntry", 1L, "test/testFile.txt"));
        byte[] testData = "Test data".getBytes();
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        byte[] testFileData = ArchiveUtils.toByteArray(testFile);
        assertEquals(testData, new String(new ArraySegment(testFileData, cpioArchiveOutputStream.getWritten())).getBytes());
        testFile.delete();
    }

    @Test
    public void testWrite_invalidOffset_throwsIndexOutOfBoundsException() throws IOException {
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(new byte[]{-1, 1}, 1, 2));
    }

    @Test
    public void testWrite_invalidLength_throwsIndexOutOfBoundsException() throws IOException {
        assertThrows(IndexOutOfBoundsException.class, () -> cpioArchiveOutputStream.write(new byte[]{1, 2}, -1, 1));
    }

    @Test
    public void testWrite_emptyData_success() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("testEntry", 1L, "test/testFile.txt"));
        cpioArchiveOutputStream.write(new byte[0], 0, 0);
        cpioArchiveOutputStream.closeArchiveEntry();
        assertTrue(cpioArchiveOutputStream.getWritten() == 0);
    }

    @Test
    public void testCloseArchiveEntry_closedEntry_success() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("testEntry", 1L, "test/testFile.txt"));
        cpioArchiveOutputStream.closeArchiveEntry();
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testCloseArchiveEntry_invalidEntry_throwsIOException() throws IOException {
        cpioArchiveOutputStream.closeArchiveEntry();
        assertThrows(IOException.class, cpioArchiveOutputStream::closeArchiveEntry);
    }

    @Test
    public void testFinish_validStream_success() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("testEntry", 1L, "test/testFile.txt"));
        cpioArchiveOutputStream.finish();
        Map<String, ArchiveEntry> expectedMap = new HashMap<>();
        expectedMap.put("test/testFile.txt", cpioArchiveOutputStream.getNextEntry());
        assertEquals(expectedMap, cpioArchiveOutputStream.getEntries());
    }

    @Test
    public void testFinish_invalidStream_throwsIOException() throws IOException {
        cpioArchiveOutputStream.finish();
        assertThrows(IOException.class, cpioArchiveOutputStream::finish);
    }

    @Test
    public void testGetNextEntry_validStream_success() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("testEntry1", 1L, "test/testFile1.txt"));
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry("testEntry2", 2L, "test/testFile2.txt"));
        cpioArchiveOutputStream.finish();
        ArchiveEntry testEntry1 = cpioArchiveOutputStream.getNextEntry();
        assertEquals("test/testFile1.txt", testEntry1.getName());
        ArchiveEntry testEntry2 = cpioArchiveOutputStream.getNextEntry();
        assertEquals("test/testFile2.txt", testEntry2.getName());
        assertFalse(cpioArchiveOutputStream.getNextEntry() == null);
    }

    @Test
    public void testGetNextEntry_emptyStream_returnsNull() throws IOException {
        cpioArchiveOutputStream.finish();
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }
}