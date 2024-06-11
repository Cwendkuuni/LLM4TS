import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/A/B/C/D/E/F/G/H/I/J/K/L/M/N/O/P/Q/R/S/T/U/V/W/X/Y/Z/1/2/3/4/5/6/7/8/9/0";
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        entry.setName(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        Assert.assertEquals(512, os.size());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        byte[] testData = new byte[10];
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertEquals(testData.length, os.size());
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondEntrySize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        byte[] testData = new byte[10];
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(testData, 0, testData.length + 1);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry entry1 = new TarArchiveEntry(new File("testFile1.txt"));
        TarArchiveEntry entry2 = new TarArchiveEntry(new File("testFile2.txt"));
        byte[] testData1 = new byte[5];
        byte[] testData2 = new byte[5];
        tarArchiveOutputStream.putArchiveEntry(entry1);
        tarArchiveOutputStream.write(testData1, 0, testData1.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(entry2);
        tarArchiveOutputStream.write(testData2, 0, testData2.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byte[] expectedOutput = new byte[2 * (512 + 5)];
        System.arraycopy(testData1, 0, expectedOutput, 0, testData1.length);
        System.arraycopy(testData2, 0, expectedOutput, 512 + testData1.length, testData2.length);
        Assert.assertArrayEquals(expectedOutput, os.toByteArray());
    }

    @Test
    public void testWriteRecord() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        byte[] testData = new byte[512];
        tarArchiveOutputStream.writeRecord(testData);
        Assert.assertEquals(512, os.size());
    }

    @Test(expected = IOException.class)
    public void testWriteRecordWithLessData() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        byte[] testData = new byte[500];
        tarArchiveOutputStream.writeRecord(testData);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        File testFile = new File("testFile.txt");
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(testFile, "testFile.txt");
        Assert.assertNotNull(entry);
        Assert.assertEquals("testFile.txt", entry.getName());
    }

    @Test
    public void testFlush() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        byte[] testData = new byte[10];
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.flush();
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertEquals(testData.length, os.size());
    }

    @Test
    public void testLongFilePathWithLongFileNameExtension() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/A/B/C/D/E/F/G/H/I/J/K/L/M/N/O/P/Q/R/S/T/U/V/W/X/Y/Z/1/2/3/4/5/6/7/8/9/0/longFileExtension.txt";
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        entry.setName(longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        Assert.assertTrue(os.toString().contains(longFileName));
    }
}