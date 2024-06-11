import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setUp() {
        byte[] buffer = new byte[32];
        InputStream inputStream = new ByteArrayInputStream(buffer);
        try {
            dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            dumpArchiveInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCount() {
        int count = dumpArchiveInputStream.getCount();
        Assert.assertEquals(0, count);
    }

    @Test
    public void testGetBytesRead() {
        long bytesRead = dumpArchiveInputStream.getBytesRead();
        Assert.assertEquals(0, bytesRead);
    }

    @Test
    public void testGetSummary() {
        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        Assert.assertNull(summary);
    }

    @Test
    public void testGetNextDumpEntry() {
        DumpArchiveEntry entry = null;
        try {
            entry = dumpArchiveInputStream.getNextDumpEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNull(entry);
    }

    @Test
    public void testGetNextEntry() {
        DumpArchiveEntry entry = null;
        try {
            entry = dumpArchiveInputStream.getNextEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNull(entry);
    }

    @Test
    public void testRead() {
        byte[] buf = new byte[10];
        int off = 0;
        int len = 10;
        int bytesRead = 0;
        try {
            bytesRead = dumpArchiveInputStream.read(buf, off, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(0, bytesRead);
    }

    @Test
    public void testClose() {
        try {
            dumpArchiveInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(dumpArchiveInputStream.isClosed());
    }

}