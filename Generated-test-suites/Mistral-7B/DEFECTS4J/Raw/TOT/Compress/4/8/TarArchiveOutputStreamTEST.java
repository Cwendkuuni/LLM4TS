import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        archiveOutputStream.close();
    }

    @Test
    public void testCreateOutputStream() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new File("test.tar"));
        assertNull(tarArchiveOutputStream.getOutputStream());
        tarArchiveOutputStream.close();
        archiveOutputStream = new TarArchiveOutputStream(new File("test.tar"), 1024, 512);
        assertSame(outputStream, archiveOutputStream.getOutputStream());
    }

    @Test
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        archiveOutputStream.closeArchiveEntry();
        try {
            archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
        archiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        int headerLength = TarConstants.HEADER_SIZE + TarConstants.NAME_LENGTH;
        int nameLength = "test.txt".getBytes().length;
        int expectedLength = headerLength + nameLength;
        assertEquals(expectedLength, bytes.length);
        TarArchiveEntry readEntry = new TarArchiveEntry(new ByteArrayInputStream(bytes));
        assertEquals(entry.getName(), readEntry.getName());
        assertEquals(entry.getSize(), readEntry.getSize());
        archiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        int headerLength = TarConstants.HEADER_SIZE + TarConstants.NAME_LENGTH;
        int nameLength = longFileName.getBytes().length;
        int expectedLength = headerLength + nameLength;
        assertEquals(expectedLength, bytes.length);
        TarArchiveEntry readEntry = new TarArchiveEntry(new ByteArrayInputStream(bytes));
        assertEquals(entry.getName(), readEntry.getName());
        archiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError() throws IOException {
        String longFileName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        try {
            archiveOutputStream.putArchiveEntry(entry);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
        archiveOutputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        archiveOutputStream.finish();
        byte[] bytes = outputStream.toByteArray();
        int expectedLength = TarConstants.EOF_RECORD_SIZE * 2;
        assertEquals(expectedLength, bytes.length);
        archiveOutputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        String testString = "test";
        byte[] testBytes = testString.getBytes();
        archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStream.write(testBytes, 0, testBytes.length);
        archiveOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        int headerLength = TarConstants.HEADER_SIZE + TarConstants.NAME_LENGTH;
        int nameLength = "test.txt".getBytes().length;
        int expectedLength = headerLength + testBytes.length;
        assertEquals(expectedLength, bytes.length);
        archiveOutputStream.close();
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        String testString = "test";
        byte[] testBytes = testString.getBytes();
        archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStream.write(testBytes, 0, testBytes.length);
        try {
            archiveOutputStream.write(testBytes, 0, testBytes.length);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
        archiveOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        int headerLength = TarConstants.HEADER_SIZE + TarConstants.NAME_LENGTH;
        int nameLength = "test.txt".getBytes().length;
        int expectedLength = headerLength + testBytes.length;
        assertEquals(expectedLength, bytes.length);
        archiveOutputStream.close();
    }

    @Test
    public void testWriteWithRemainingBytes() throws IOException {
        String testString = "test";
        byte[] testBytes = testString.getBytes();
        archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStream.write(testBytes, 0, testBytes.length);
        int remainingBytes = 5;
        archiveOutputStream.write(testBytes, 0, remainingBytes);
        archiveOutputStream.closeArchiveEntry();
        byte[] bytes = outputStream.toByteArray();
        int headerLength = TarConstants.HEADER_SIZE + TarConstants.NAME_LENGTH;
        int nameLength = "test.txt".getBytes().length;
        int expectedLength = headerLength + testBytes.length + remainingBytes;
        assertEquals(expectedLength, bytes.length);
        archiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longFileName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        try {
            archiveOutputStream.putArchiveEntry(entry);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
        archiveOutputStream.close();
    }
}