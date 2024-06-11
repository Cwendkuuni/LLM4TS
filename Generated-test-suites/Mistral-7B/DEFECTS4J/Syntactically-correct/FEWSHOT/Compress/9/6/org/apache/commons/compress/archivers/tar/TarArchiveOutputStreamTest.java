package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
        tarArchiveOutputStream.finish();
        byte[] expectedEOF = new byte[TarConstants.STD_REC_LEN];
        Arrays.fill(expectedEOF, (byte) 0);
        byte[] actualEOF = out.toByteArray();
        assertArrayEquals(expectedEOF, actualEOF);
        assertEquals(TarConstants.STD_REC_LEN, actualEOF.length);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
        tarArchiveOutputStream.close();
        byte[] expectedEOF = new byte[TarConstants.STD_REC_LEN * 2];
        Arrays.fill(expectedEOF, (byte) 0);
        byte[] actualEOF = out.toByteArray();
        assertArrayEquals(expectedEOF, actualEOF);
        assertEquals(TarConstants.STD_REC_LEN * 2, actualEOF.length);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] expectedName = "test.txt\0".getBytes();
        byte[] actualName = tarArchiveOutputStream.getBuffer().getRecord();
        assertArrayEquals(expectedName, actualName);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
        byte[] testData = "test content".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedData = testData;
        byte[] actualData = tarArchiveOutputStream.getBuffer().getRecord();
        assertArrayEquals(expectedData, actualData);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testWriteLargerThanRecordSize() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out, TarConstants.REC_SIZE * 2);
        byte[] testData = new byte[TarConstants.REC_SIZE * 3];
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry("longFileNameThatIsLongerThanNameLength.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] expectedLongFileName = "longFileNameThatIsLongerThanNameLength.txt\0".getBytes();
        byte[] actualLongFileName = tarArchiveOutputStream.getBuffer().getRecord();
        assertArrayEquals(expectedLongFileName, actualLongFileName);
    }

    @Test
    public void testPutArchiveEntryWithLongLink() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out, TarConstants.REC_SIZE * 2);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        entry.setName("longLinkNameThatIsLongerThanNameLength\\0longLinkValueThatIsLongerThanNameLength.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] expectedLongLinkName = "longLinkNameThatIsLongerThanNameLength\\0longLinkValueThatIsLongerThanNameLength.txt\0".getBytes();
        byte[] actualLongLinkName = tarArchiveOutputStream.getBuffer().getRecord();
        assertArrayEquals(expectedLongLinkName, actualLongLinkName);
        tarArchiveOutputStream.write(entry.getName().substring(entry.getName().length()).getBytes());
        tarArchiveOutputStream.write(0);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
        File testFile = new File("test.txt");
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(testFile, "test.txt");
        byte[] expectedName = "test.txt\0".getBytes();
        byte[] actualName = entry.getName().getBytes();
        assertArrayEquals(expectedName, actualName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("test content".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(out);
        File testFile = new File("longFileNameThatIsLongerThanNameLength.txt");
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(testFile, "longFileNameThatIsLongerThanNameLength.txt");
        byte[] expectedName = "longFileNameThatIsLongerThanNameLength.txt\0".getBytes();
        byte[] actualName = entry.getName().getBytes();
        assertArrayEquals(expectedName, actualName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.close();
    }
}