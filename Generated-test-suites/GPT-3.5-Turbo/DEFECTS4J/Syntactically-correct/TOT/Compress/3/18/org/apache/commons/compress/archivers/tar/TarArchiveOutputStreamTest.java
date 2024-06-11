import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testSetLongFileMode_Error() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode_Truncate() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode_GNU() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        entry.setSize(file.length());

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        // Verify that the entry is written correctly
        byte[] outputBytes = outputStream.toByteArray();
        CheckedInputStream checkedInputStream = new CheckedInputStream(new ByteArrayInputStream(outputBytes), new CRC32());
        byte[] buffer = new byte[8192];
        while (checkedInputStream.read(buffer) >= 0) {
            // Checksum will be calculated
        }
        long checksum = checkedInputStream.getChecksum().getValue();
        Assert.assertEquals(file.length(), checksum);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        entry.setSize(file.length());

        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "test data".getBytes();
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        // Verify that the entry is closed correctly
        byte[] outputBytes = outputStream.toByteArray();
        Assert.assertEquals(0, outputBytes.length);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_InvalidEntrySize() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = Mockito.spy(new TarArchiveEntry(file));
        entry.setSize(file.length());

        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "test data".getBytes();
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        entry.setSize(file.length());

        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "test data".getBytes();
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        // Verify that the data is written correctly
        byte[] outputBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(data, outputBytes);
    }

    @Test
    public void testFinish() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        entry.setSize(file.length());

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        tarOutputStream.close();

        // Verify that the archive is finished correctly (two EOF records)
        byte[] outputBytes = outputStream.toByteArray();
        Assert.assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE * 2, outputBytes.length);
    }

    @Test
    public void testClose() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        entry.setSize(file.length());

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        // Verify that the stream is closed correctly
        Assert.assertTrue(tarOutputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarOutputStream.getRecordSize();
        Assert.assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

}