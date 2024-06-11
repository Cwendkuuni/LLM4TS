import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DumpArchiveInputStreamTest {

    @Test
    public void testConstructor() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1024]);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(is);
        Assert.assertNotNull(archiveInputStream);
    }

    @Test
    public void testGetBytesRead() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1024]);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(is);
        long bytesRead = archiveInputStream.getBytesRead();
        Assert.assertEquals(0, bytesRead);
    }

    @Test
    public void testGetSummary() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1024]);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(is);
        DumpArchiveSummary summary = archiveInputStream.getSummary();
        Assert.assertNotNull(summary);
    }

    @Test
    public void testGetNextEntry() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1024]);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(is);
        DumpArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testRead() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1024]);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(is);
        byte[] buffer = new byte[10];
        int bytesRead = archiveInputStream.read(buffer, 0, 10);
        Assert.assertEquals(10, bytesRead);
    }

    @Test
    public void testClose() throws Exception {
        InputStream is = new ByteArrayInputStream(new byte[1024]);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(is);
        archiveInputStream.close();
        Assert.assertTrue(archiveInputStream.isClosed);
    }
}