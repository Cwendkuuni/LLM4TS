
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;


public class TarArchiveOutputStreamTest {

    // Method 1: setLongFileMode

    // Basic test case for setLongFileMode
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }


    // Method 2: finish

    // Basic test case for finish
    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(byteArrayOutputStream, new CRC32());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(cos);
        tarArchiveOutputStream.finish();
        Assert.assertEquals(1024, byteArrayOutputStream.size());
    }


    // Method 3: close

    // Basic test case for close
    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(byteArrayOutputStream, new CRC32());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(cos);
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.isClosed());
    }


    // Method 4: getRecordSize

    // Basic test case for getRecordSize
    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }


    // Method 5: putArchiveEntry

    // Basic test case for putArchiveEntry with valid entry
    @Test
    public void testPutArchiveEntry_ValidEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(byteArrayOutputStream, new CRC32());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(cos);

        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.close();

        Assert.assertEquals(5120, entry.getSize());
    }

    // Test case for putArchiveEntry with long file name in LONGFILE_TRUNCATE mode
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongFileNameTruncateMode() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(byteArrayOutputStream, new CRC32());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(cos);

        TarArchiveEntry entry = new TarArchiveEntry("verylongfilenameverylongfilenameverylongfilename");
        entry.setSize(10);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.close();
    }

    // Test case for putArchiveEntry with long file name in LONGFILE_GNU mode
    @Test
    public void testPutArchiveEntry_LongFileNameGNUMode() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(byteArrayOutputStream, new CRC32());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(cos);

        TarArchiveEntry entry = new TarArchiveEntry("verylongfilenameverylongfilenameverylongfilename");
        entry.setSize(10);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        Assert.assertEquals(5120, entry.getSize());
    }


    // Method 6: closeArchiveEntry

    // Basic test case for closeArchiveEntry
    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(byteArrayOutputStream, new CRC32());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(cos);

        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.close();

        Assert.assertEquals(5120, entry.getSize());
    }


    // Method 7: write

    // Basic test case for write
    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(byteArrayOutputStream, new CRC32());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(cos);

        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.close();

        Assert.assertEquals(5120, entry.getSize());
    }


    // Method 8: writeEOFRecord

    // Test case for writeEOFRecord
    @Test
    public void testWriteEOFRecord() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(byteArrayOutputStream, new CRC32());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(cos);

        tarArchiveOutputStream.writeEOFRecord();
        tarArchiveOutputStream.close();

        Assert.assertEquals(1024, byteArrayOutputStream.size());
    }


    // Method 9: createArchiveEntry

    // Basic test case for createArchiveEntry
    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = (TarArchiveEntry) tarArchiveOutputStream.createArchiveEntry(new File("file.txt"), "file.txt");
        Assert.assertEquals("file.txt", entry.getName());
    }
}

