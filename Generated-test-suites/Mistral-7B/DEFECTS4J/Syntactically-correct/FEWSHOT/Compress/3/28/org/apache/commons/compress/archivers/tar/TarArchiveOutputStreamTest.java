package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.TarConstants;
import org.apache.commons.compress.archivers.TarEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @Test
    public void testConstructor() {
        assertNull(tarArchiveOutputStream.getStream());
        assertEquals(512, tarArchiveOutputStream.getBlockSize());
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertEquals(1024, outputStream.size());

        byte[] expectedEOFRecord = new byte[TarConstants.STD_MAGIC * 2];
        System.arraycopy(TarConstants.STD_MAGIC, 0, expectedEOFRecord, 0, TarConstants.STD_MAGIC.length);
        assertArrayEquals(expectedEOFRecord, outputStream.toString().subArray(0, expectedEOFRecord.length));

        tarArchiveOutputStream.finish();
        assertEquals(1024, outputStream.size());
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertEquals(1024, outputStream.size());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File inputFile = new File("inputFile.txt");
        String entryName = "testEntry.txt";
        TarArchiveEntry tarArchiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        TarEntry tarEntry = tarArchiveOutputStream.getNextEntry();
        assertEquals("testEntry.txt", tarEntry.getName());

        byte[] expectedHeaderRecord = new byte[512];
        System.arraycopy(tarArchiveEntry.getHeaderRecord(), 0, expectedHeaderRecord, 0, tarArchiveEntry.getHeaderRecord().length);
        assertArrayEquals(expectedHeaderRecord, byteArrayOutputStream.toByteArray());

        tarArchiveOutputStream.closeArchiveEntry();
        tarEntry = tarArchiveOutputStream.getNextEntry();
        assertNull(tarEntry);
    }

    @Test
    public void testCloseArchiveEntryWithoutPuttingItFirst() {
        assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.closeArchiveEntry());
    }

    @Test
    public void testWrite() throws IOException {
        String textToWrite = "This is a test.";
        byte[] textToWriteBytes = textToWrite.getBytes();

        TarArchiveEntry tarArchiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("inputFile.txt"), "testEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write("This is a test.".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedRecord = new byte[512];
        byte[] textToWriteRecord = new byte[textToWrite.length() + 1];
        System.arraycopy(tarArchiveEntry.getHeaderRecord(), 0, expectedRecord, 0, tarArchiveEntry.getHeaderRecord().length);
        System.arraycopy(textToWriteBytes, 0, textToWriteRecord, 0, textToWriteBytes.length);
        textToWriteRecord[textToWriteBytes.length] = 0;

        System.arraycopy(textToWriteRecord, 0, expectedRecord, tarArchiveEntry.getHeaderRecord().length, textToWriteRecord.length);

        assertArrayEquals(expectedRecord, byteArrayOutputStream.toByteArray());
    }

    @Test
    public void testWriteSmallerChunks() throws IOException {
        String textToWrite = "This is a test.";
        byte[] textToWriteBytes = textToWrite.getBytes();

        TarArchiveEntry tarArchiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("inputFile.txt"), "testEntry.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        tarArchiveOutputStream.write(new byte[]{"T", "h", "i", "s", " ", "i", "s", " ", "a", " ", "t", "e", "s", "t"});
        tarArchiveOutputStream.write(new byte[]{'.'});
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedRecord = new byte[512];
        byte[] textToWriteRecord = new byte[textToWrite.length() + 1];
        String textToWriteChunk1 = "This is a test.";
        String textToWriteChunk2 = ".";

        System.arraycopy(tarArchiveEntry.getHeaderRecord(), 0, expectedRecord, 0, tarArchiveEntry.getHeaderRecord().length);

        System.arraycopy(textToWriteChunk1.getBytes(), 0, expectedRecord, tarArchiveEntry.getHeaderRecord().length, textToWriteChunk1.getBytes().length);
        int index = tarArchiveEntry.getHeaderRecord().length + textToWriteChunk1.getBytes().length;
        System.arraycopy(textToWriteChunk2.getBytes(), 0, expectedRecord, index, textToWriteChunk2.getBytes().length);

        assertArrayEquals(expectedRecord, byteArrayOutputStream.toByteArray());
    }
}