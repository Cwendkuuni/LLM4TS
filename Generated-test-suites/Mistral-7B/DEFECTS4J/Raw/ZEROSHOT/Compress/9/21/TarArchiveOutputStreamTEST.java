import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(outputStream));
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        assertFalse(tarOutputStream.isFinished());
        assertFalse(tarOutputStream.isClosed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedOutput = buildExpectedOutput();
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishWithOpenEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);
        tarOutputStream.finish();
        tarOutputStream.close();

        byte[] expectedOutput = buildExpectedOutput();
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseWithOpenEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);
        tarOutputStream.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseWithUnfinishedArchive() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarOutputStream.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryWithFinishedArchive() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarOutputStream.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedOutput = buildExpectedOutput();
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        Object invalidEntry = new Object();
        tarOutputStream.putArchiveEntry(invalidEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedOutput = buildExpectedOutput();
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedOutput = buildExpectedOutput();
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);

        byte[] expectedOutput = buildExpectedOutputWithUnclosedEntry();
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteWithUnfinishedArchive() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testWriteWithClosedArchive() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);

        byte[] expectedOutput = buildExpectedOutput();
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteWithOpenEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);
        tarOutputStream.write(entry.getData(), 0, entry.getData().length);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteWithUnclosedEntryAndDataLongerThanRecordSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        byte[] longData = new byte[TarConstants.RECORD_SIZE + 1];
        tarOutputStream.write(longData, 0, longData.length);
        tarOutputStream.write(longData, 0, longData.length);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteWithUnclosedEntryAndDataLongerThanBlockSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.putArchiveEntry(entry);
        byte[] longData = new byte[TarConstants.BLOCK_SIZE * 2 + 1];
        tarOutputStream.write(longData, 0, longData.length);
        tarOutputStream.write(longData, 0, longData.length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        assertTrue(entry.getName().equals("test.txt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryWithInvalidFile() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarOutputStream.createArchiveEntry(null, "test.txt");
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithFileDoesNotExist() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarOutputStream.createArchiveEntry(new File("nonexistentfile.txt"), "test.txt");
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithFileIsDirectory() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarOutputStream.createArchiveEntry(new File("test.txt").getParentFile(), "test.txt");
    }

    private byte[] buildExpectedOutput() {
        return buildExpectedOutput("test.txt", "test data".getBytes(StandardCharsets.UTF_8));
    }

    private byte[] buildExpectedOutputWithUnclosedEntry() {
        return buildExpectedOutput("test.txt", "test data".getBytes(StandardCharsets.UTF_8), true);
    }

    private byte[] buildExpectedOutput(String fileName, byte[] fileData) {
        byte[] header = new TarArchiveEntry(fileName, fileData.length).getHeaderBytes();
        byte[] record = new byte[TarConstants.RECORD_SIZE];
        System.arraycopy(header, 0, record, 0, header.length);
        System.arraycopy(fileData, 0, record, TarConstants.HEADER_LENGTH, fileData.length);
        byte[] footer = new byte[TarConstants.RECORD_SIZE];
        for (int i = 0; i < TarConstants.RECORD_SIZE; ++i) {
            footer[i] = 0;
        }

        byte[] expectedOutput = new byte[TarConstants.HEADER_LENGTH + fileData.length + TarConstants.RECORD_SIZE];
        System.arraycopy(header, 0, expectedOutput, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(fileData, 0, expectedOutput, TarConstants.HEADER_LENGTH, fileData.length);
        System.arraycopy(record, TarConstants.HEADER_LENGTH, expectedOutput, TarConstants.HEADER_LENGTH + fileData.length, TarConstants.RECORD_SIZE - TarConstants.HEADER_LENGTH);
        System.arraycopy(footer, 0, expectedOutput, TarConstants.HEADER_LENGTH + fileData.length + TarConstants.RECORD_SIZE - TarConstants.HEADER_LENGTH, TarConstants.RECORD_SIZE - TarConstants.HEADER_LENGTH);

        return expectedOutput;
    }

    private byte[] buildExpectedOutput(String fileName, byte[] fileData, boolean unclosed) {
        byte[] header = new TarArchiveEntry(fileName, fileData.length).getHeaderBytes();
        byte[] record = new byte[TarConstants.RECORD_SIZE];
        System.arraycopy(header, 0, record, 0, header.length);
        System.arraycopy(fileData, 0, record, TarConstants.HEADER_LENGTH, fileData.length);

        byte[] footer = new byte[TarConstants.RECORD_SIZE];
        for (int i = 0; i < TarConstants.RECORD_SIZE; ++i) {
            footer[i] = 0;
        }

        byte[] expectedOutput = new byte[TarConstants.HEADER_LENGTH + fileData.length + TarConstants.RECORD_SIZE];
        System.arraycopy(header, 0, expectedOutput, 0, TarConstants.HEADER_LENGTH);
        System.arraycopy(fileData, 0, expectedOutput, TarConstants.HEADER_LENGTH, fileData.length);
        System.arraycopy(record, TarConstants.HEADER_LENGTH, expectedOutput, TarConstants.HEADER_LENGTH + fileData.length, TarConstants.RECORD_SIZE - TarConstants.HEADER_LENGTH);

        if (unclosed) {
            byte[] assemBuf = new byte[TarConstants.RECORD_SIZE];
            int assemLen = 0;
            byte[] assemRecord = new byte[TarConstants.RECORD_SIZE];

            for (int i = TarConstants.HEADER_LENGTH + fileData.length; i < TarConstants.RECORD_SIZE; ++i) {
                assemBuf[assemLen++] = 0;
                assemRecord[i - (TarConstants.HEADER_LENGTH + fileData.length)] = assemBuf[assemLen - 1];
            }

            for (int i = 0; i < fileData.length; ++i) {
                assemBuf[assemLen++] = fileData[i];
            }

            expectedOutput = new byte[expectedOutput.length + TarConstants.RECORD_SIZE];
            System.arraycopy(expectedOutput, 0, expectedOutput, 0, expectedOutput.length);
            System.arraycopy(assemRecord, 0, expectedOutput, expectedOutput.length - TarConstants.RECORD_SIZE, TarConstants.RECORD_SIZE);
        }

        return expectedOutput;
    }
}