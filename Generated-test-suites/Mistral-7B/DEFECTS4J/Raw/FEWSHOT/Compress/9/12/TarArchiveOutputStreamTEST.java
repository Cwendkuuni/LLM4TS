package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.ArchiveUtils;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isFinished());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isFinished());
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(tarArchiveOutputStream.getRecordSize(), TarBuffer.DEFAULT_RCDSIZE);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(data.length);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveEntry archiveEntry = tarArchiveOutputStream.getInputArchive().getEntry("test.txt");
        assertSame(archiveEntry, entry);
        assertEquals(archiveEntry.getSize(), data.length);
        byte[] readData = new byte[data.length];
        inputStream.read(readData);
        assertSame(data, readData);
    }

    @Test
    public void testCloseArchiveEntryWithoutWriting() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveEntry archiveEntry = tarArchiveOutputStream.getInputArchive().getEntry("test.txt");
        assertNull(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntryWithData() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(data.length);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveEntry archiveEntry = tarArchiveOutputStream.getInputArchive().getEntry("test.txt");
        assertSame(archiveEntry, entry);
        assertEquals(archiveEntry.getSize(), data.length);
        byte[] readData = new byte[data.length];
        inputStream.read(readData);
        assertSame(data, readData);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameWithoutGNU() throws IOException {
        String longFileName = "a" + ArchiveUtils.LONG_NAME_LENGTH + "b";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameWithGNU() throws IOException {
        String longFileName = "a" + ArchiveUtils.LONG_NAME_LENGTH + "b";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName, TarConstants.GNU_LONGLINK);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(),
                TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameWithTruncation() throws IOException {
        String longFileName = "a" + ArchiveUtils.LONG_NAME_LENGTH + "b";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName, TarConstants.GNU_LONGLINK);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(),
                TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(entry);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.getOutputArchive().toByteArray());
        ArchiveEntry archiveEntry = tarArchiveOutputStream.getInputArchive().getEntry(longFileName.substring(0, ArchiveUtils.NAMELEN));
        assertSame(archiveEntry, entry);
        assertEquals(archiveEntry.getSize(), 0);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] wBuf = "Hello, World!".getBytes();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.write(wBuf, 0, wBuf.length);
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.getOutputArchive().toByteArray());
        byte[] readData = new byte[wBuf.length];
        inputStream.read(readData);
        assertSame(wBuf, readData);
    }

    @Test
    public void testWriteWithAssembly() throws IOException {
        byte[] wBuf = "Hello, World!".getBytes();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.write(wBuf, 0, 32);
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.getOutputArchive().toByteArray());
        byte[] readData = new byte[wBuf.length];
        inputStream.read(readData);
        assertSame(wBuf, readData);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        byte[] wBuf = "Hello, World!".getBytes();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write(wBuf, 0, wBuf.length + 1);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        WriteToFile writeToFile = new WriteToFile("test.txt", "Hello, World!");
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(inputFile.length(), entry.getSize());
        writeToFile.writeFile();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(tarArchiveOutputStream.getOutputArchive().toByteArray());
        ArchiveEntry archiveEntry = tarArchiveOutputStream.getInputArchive().getEntry("test.txt");
        assertSame(archiveEntry, entry);
        assertEquals(inputFile.length(), archiveEntry.getSize());
        byte[] readData = new byte[inputFile.length()];
        inputStream.read(readData);
        assertSame(inputFile.getName().getBytes(), readData);
    }
}

class WriteToFile {
    private final File file;
    private final String content;

    WriteToFile(String fileName, String content) {
        this.file = new File(fileName);
        this.content = content;
    }

    void writeFile() throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        }
    }
}