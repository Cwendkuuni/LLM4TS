package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() {
        tarOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        assertTrue(tarOutputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.finish();
        testEntry.close(); // Should throw an exception
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertTrue(tarOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        tarOutputStream.close();
        tarOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(tarOutputStream.getRecordSize(), tarOutputStream.getBuffer().getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        assertEquals(testEntry.getName(), tarOutputStream.getCurrentName());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidEntry() throws IOException {
        Object invalidEntry = new Object();
        tarOutputStream.putArchiveEntry(invalidEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNoName() throws IOException {
        ArchiveEntry testEntry = new TarArchiveEntry(new File("test.txt"));
        testEntry.setName(null);
        tarOutputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileName() throws IOException {
        byte[] longFileNameBytes = "testNameThatIsTooLongForTheArchive".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt", longFileNameBytes, new byte[0]);
        tarOutputStream.putArchiveEntry(testEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidEntrySize() throws IOException {
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        testEntry.setSize(-1);
        tarOutputStream.putArchiveEntry(testEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryAlreadyClosed() throws IOException {
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        String testString = "testString";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(testBytes, 0, testBytes.length);
        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(testString, new String(outputBytes, StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testWriteNegativeLength() throws IOException {
        byte[] testBytes = new byte[10];
        tarOutputStream.write(testBytes, -1, 5);
    }

    @Test
    public void testWriteZeroLength() throws IOException {
        byte[] testBytes = new byte[0];
        tarOutputStream.write(testBytes, 0, 0);
    }

    @Test
    public void testWriteLongFileName() throws IOException {
        String longFileName = "testNameThatIsTooLongForTheArchive";
        byte[] longFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);
        ArchiveEntry testEntry = new TarArchiveEntry("test.txt", longFileNameBytes, new byte[0]);
        tarOutputStream.putArchiveEntry(testEntry);
        String testString = "testString";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(testBytes, 0, testBytes.length);
        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(longFileName, new String(outputBytes, 0, TarConstants.NAMELEN, StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteToEOF() throws IOException {
        String testString = "testString";
        byte[] testBytes = testString.getBytes(StandardCharsets.UTF_8);

        ArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);

        int remaining = testBytes.length;
        int bufferFill = TarConstants.RECORD_SIZE - 5; // leave space for header

        while (remaining > 0) {
            int bytesToWrite = (remaining < bufferFill) ? remaining : bufferFill;
            tarOutputStream.write(testBytes, testBytes.length - remaining, bytesToWrite);
            remaining -= bytesToWrite;
        }

        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] outputBytes = outputStream.toByteArray();
        int headerLength = TarConstants.RECORD_SIZE - testBytes.length;

        for (int i = 0; i < headerLength; i++) {
            assertEquals(0, outputBytes[i]);
        }

        int index = 0;
        for (int i = 0; i < testBytes.length; i++) {
            assertEquals(testBytes[i], outputBytes[index++]);
            assertEquals(testBytes[i], outputBytes[index++]); // second occurrence due to duplicate record
        }
    }
}