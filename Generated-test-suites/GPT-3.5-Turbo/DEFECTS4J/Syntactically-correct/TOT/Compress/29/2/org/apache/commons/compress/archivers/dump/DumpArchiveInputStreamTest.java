import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        String headerString = "////ARCREA000931////";
        byte[] headerBytes = headerString.getBytes();
        ByteArrayInputStream is = new ByteArrayInputStream(headerBytes);
        inputStream = new DumpArchiveInputStream(is);
    }

    @Test
    public void testGetCount() {
        int count = inputStream.getCount();
        Assert.assertEquals(0, count);
    }

    @Test
    public void testGetBytesRead() {
        long bytesRead = inputStream.getBytesRead();
        Assert.assertEquals(0L, bytesRead);
    }

    @Test
    public void testGetSummary() {
        DumpArchiveSummary summary = inputStream.getSummary();
        Assert.assertNotNull(summary);
    }

    @Test
    public void testGetNextDumpEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextDumpEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buf = new byte[10];
        int read = inputStream.read(buf, 0, 10);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        // Check that the input stream is closed
        Assert.assertTrue(inputStream.isClosed);
    }
}