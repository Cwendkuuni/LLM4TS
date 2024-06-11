package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.finish();
        byte[] eofBytes = new byte[512 * 2];
        for (int i = 0; i < eofBytes.length; i++) {
            eofBytes[i] = 0;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(eofBytes);
        ArchiveInputStream ais = new TarArchiveInputStream(inputStream);
        ArchiveEntry entry = ais.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.close();
        assertFalse(tarStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "test.txt";
        File file = new File(fileName);
        byte[] fileContent = "Test file content.".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarStream.putArchiveEntry(archiveEntry);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] expectedHeader = new byte[TarConstants.RECORD_LENGTH];
        archiveEntry.writeHeader(expectedHeader);
        outputStream.write(expectedHeader);
        outputStream.write(fileContent, 0, fileContent.length);
        assertFalse(tarStream.isClosed());
        tarStream.close();
        assertTrue(tarStream.isClosed());

        ArchiveInputStream ais = new TarArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArchiveEntry entry = ais.getNextEntry();
        assertEquals(fileName, entry.getName());
        assertEquals(fileContent.length, entry.getSize());
        byte[] readContent = new byte[fileContent.length];
        ais.read(readContent, 0, fileContent.length);
        assertArrayEquals(fileContent, readContent);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "very/long/filename.txt";
        File file = new File(fileName);
        byte[] fileContent = "Test file content.".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarStream.putArchiveEntry(archiveEntry);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] expectedHeader = new byte[TarConstants.RECORD_LENGTH + fileName.getBytes().length + 1];
        archiveEntry.writeHeader(expectedHeader, 0, TarConstants.RECORD_LENGTH);
        System.arraycopy(fileName.getBytes(), 0, expectedHeader, TarConstants.RECORD_LENGTH, fileName.getBytes().length);
        outputStream.write(expectedHeader);
        outputStream.write(fileContent, 0, fileContent.length);
        assertFalse(tarStream.isClosed());
        tarStream.close();
        assertTrue(tarStream.isClosed());

        ArchiveInputStream ais = new TarArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArchiveEntry entry = ais.getNextEntry();
        assertEquals(fileName, entry.getName());
        assertEquals(fileContent.length, entry.getSize());
        byte[] readContent = new byte[fileContent.length];
        ais.read(readContent, 0, fileContent.length);
        assertArrayEquals(fileContent, readContent);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTooLongFileName() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "very/long/filename.txt";
        File file = new File(fileName);
        byte[] fileContent = "Test file content.".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarStream.putArchiveEntry(archiveEntry);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] expectedHeader = new byte[TarConstants.RECORD_LENGTH];
        archiveEntry.writeHeader(expectedHeader);
        outputStream.write(expectedHeader);
        outputStream.write(fileContent, 0, fileContent.length);
        assertFalse(tarStream.isClosed());
        tarStream.close();
        assertTrue(tarStream.isClosed());

        ArchiveInputStream ais = new TarArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArchiveEntry entry = ais.getNextEntry();
        assertEquals("", entry.getName());
        assertNull(entry.getSize());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "test.txt";
        File file = new File(fileName);
        byte[] fileContent = "Test file content.".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarStream.putArchiveEntry(archiveEntry);
        tarStream.closeArchiveEntry();
        assertFalse(tarStream.haveUnclosedEntry());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "test.txt";
        File file = new File(fileName);
        byte[] fileContent = "Test file content.".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarStream.putArchiveEntry(archiveEntry);
        byte[] wBuf = new byte[5];
        int wOffset = 0;
        int numToWrite = fileContent.length;
        tarStream.write(wBuf, wOffset, numToWrite);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] expectedOutput = new byte[fileContent.length + numToWrite];
        System.arraycopy(fileContent, 0, expectedOutput, 0, fileContent.length);
        outputStream.write(expectedOutput);
        ArchiveInputStream ais = new TarArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        byte[] readContent = new byte[fileContent.length];
        ais.read(readContent, 0, fileContent.length);
        assertArrayEquals(fileContent, readContent);
    }

    @Test
    public void testWriteWithRemainingBytes() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "test.txt";
        File file = new File(fileName);
        byte[] fileContent = "Test file content.".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarStream.putArchiveEntry(archiveEntry);
        byte[] wBuf = new byte[5];
        int wOffset = 0;
        int numToWrite = fileContent.length;
        tarStream.write(wBuf, wOffset, numToWrite);
        numToWrite = 5;
        tarStream.write(wBuf, wOffset, numToWrite);
        ArchiveInputStream ais = new TarArchiveInputStream(new ByteArrayInputStream(tarStream.toByteArray()));
        byte[] readContent = new byte[fileContent.length + numToWrite];
        ais.read(readContent, 0, fileContent.length);
        assertArrayEquals(fileContent, Arrays.copyOfRange(readContent, 0, fileContent.length));
        assertEquals(numToWrite, readContent.length - fileContent.length);
    }

    @Test
    public void testWriteWithAssemblingRecords() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "test.txt";
        File file = new File(fileName);
        byte[] fileContent = "Test file content.".getBytes();
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarStream.putArchiveEntry(archiveEntry);
        byte[] wBuf = new byte[5];
        int wOffset = 0;
        int numToWrite = fileContent.length;
        tarStream.write(wBuf, wOffset, numToWrite);
        wOffset += numToWrite;
        numToWrite = 4;
        tarStream.write(wBuf, wOffset, numToWrite);
        wOffset += numToWrite;
        numToWrite = 1;
        tarStream.write(wBuf, wOffset, numToWrite);
        wOffset += numToWrite;
        ArchiveInputStream ais = new TarArchiveInputStream(new ByteArrayInputStream(tarStream.toByteArray()));
        byte[] readContent = new byte[fileContent.length + numToWrite];
        ais.read(readContent, 0, fileContent.length);
        assertArrayEquals(fileContent, Arrays.copyOfRange(readContent, 0, fileContent.length));
        assertEquals(numToWrite, readContent.length - fileContent.length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tarStream.createArchiveEntry(inputFile, entryName);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarStream.getOutputStream();
        byte[] expectedHeader = new byte[TarConstants.RECORD_LENGTH];
        archiveEntry.writeHeader(expectedHeader);
        outputStream.write(expectedHeader);
        archiveEntry.writeTo(outputStream);
        outputStream.flush();
        ArchiveInputStream ais = new TarArchiveInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        ArchiveEntry entry = ais.getNextEntry();
        assertEquals(entryName, entry.getName());
        assertEquals(inputFile.length(), entry.getSize());
        byte[] readContent = new byte[inputFile.length()];
        ais.read(readContent, 0, inputFile.length());
        assertArrayEquals(Files.readAllBytes(inputFile.toPath()), readContent);
    }

}