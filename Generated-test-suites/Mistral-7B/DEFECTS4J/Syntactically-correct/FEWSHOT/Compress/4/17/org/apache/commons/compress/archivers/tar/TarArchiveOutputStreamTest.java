package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntryWithSmallFileName() throws IOException {
        byte[] recordBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] assemBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] nameBytes = "smallFileName".getBytes();

        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, TarConstants.TAR_BLOCK_SIZE, TarConstants.TAR_RECORD_SIZE);

        ArchiveEntry archiveEntry = new TarArchiveEntry("smallFileName");

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        archiveEntry.writeEntryHeader(recordBuf);
        tarArchiveOutputStream.buffer.writeRecord(recordBuf);

        assertEquals(archiveEntry.getSize(), tarArchiveOutputStream.currSize);
        assertEquals(archiveEntry.getName(), tarArchiveOutputStream.currName);

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        byte[] recordBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] assemBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] nameBytes = "longFileNameThatIsLongerThanNameLength".getBytes();

        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, TarConstants.TAR_BLOCK_SIZE, TarConstants.TAR_RECORD_SIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        ArchiveEntry archiveEntry = new TarArchiveEntry("longFileNameThatIsLongerThanNameLength");

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        archiveEntry.writeEntryHeader(recordBuf);
        tarArchiveOutputStream.buffer.writeRecord(recordBuf);

        tarArchiveOutputStream.write(nameBytes);
        tarArchiveOutputStream.write(0); // NUL terminator
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarInputStream(inputStream);

        ArchiveEntry longFileNameEntry = archiveInputStream.getNextEntry();
        assertEquals("longFileNameThatIsLongerThanNameLength", longFileNameEntry.getName());
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameButLongFileModeIsTruncated() throws IOException {
        byte[] recordBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] assemBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] nameBytes = "longFileNameThatIsLongerThanNameLength".getBytes();

        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, TarConstants.TAR_BLOCK_SIZE, TarConstants.TAR_RECORD_SIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        ArchiveEntry archiveEntry = new TarArchiveEntry("longFileNameThatIsLongerThanNameLength");

        assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.putArchiveEntry(archiveEntry));
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        byte[] recordBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] assemBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] nameBytes = "directoryName".getBytes();

        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, TarConstants.TAR_BLOCK_SIZE, TarConstants.TAR_RECORD_SIZE);

        ArchiveEntry archiveEntry = new TarArchiveEntry("directoryName", true);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        archiveEntry.writeEntryHeader(recordBuf);
        tarArchiveOutputStream.buffer.writeRecord(recordBuf);

        assertEquals(0, tarArchiveOutputStream.currSize);
        assertEquals(archiveEntry.getName(), tarArchiveOutputStream.currName);

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] wBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] assemBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] data = "dataToWrite".getBytes();

        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, TarConstants.TAR_BLOCK_SIZE, TarConstants.TAR_RECORD_SIZE);

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("dataFile"));
        tarArchiveOutputStream.write(wBuf, 0, TarConstants.TAR_RECORD_LENGTH);

        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarInputStream(inputStream);

        ArchiveEntry dataEntry = archiveInputStream.getNextEntry();
        assertEquals("dataFile", dataEntry.getName());

        byte[] readData = new byte[data.length];
        archiveInputStream.read(readData, 0, data.length);
        assertArrayEquals(data, readData);
    }

    @Test
    public void testClose() throws IOException {
        byte[] recordBuf = new byte[TarConstants.TAR_RECORD_LENGTH];
        byte[] assemBuf = new byte[TarConstants.TAR_RECORD_LENGTH];

        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os, TarConstants.TAR_BLOCK_SIZE, TarConstants.TAR_RECORD_SIZE);

        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarArchiveOutputStream.write(recordBuf, 0, TarConstants.TAR_RECORD_LENGTH);

        tarArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarInputStream(inputStream);

        ArchiveEntry testEntry = archiveInputStream.getNextEntry();
        assertEquals("testFile", testEntry.getName());
        assertNull(testEntry.getSize());
        assertNull(testEntry.getMethod());
    }
}