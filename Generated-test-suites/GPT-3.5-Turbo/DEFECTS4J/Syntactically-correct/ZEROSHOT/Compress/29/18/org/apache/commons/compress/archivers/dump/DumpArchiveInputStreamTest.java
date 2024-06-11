import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class DumpArchiveInputStreamTest {
    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setUp() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("test_dump_archive.tar.gz");
        try {
            dumpArchiveInputStream = new DumpArchiveInputStream(is);
        } catch (ArchiveException e) {
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
        Assert.assertNotNull(summary);
    }

    @Test
    public void testGetNextDumpEntry() {
        try {
            DumpArchiveEntry entry = dumpArchiveInputStream.getNextDumpEntry();
            Assert.assertNotNull(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClose() {
        try {
            dumpArchiveInputStream.close();
            Assert.assertTrue(dumpArchiveInputStream.isClosed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMatches() {
        byte[] buffer = new byte[32];
        Arrays.fill(buffer, (byte) 0);
        boolean matches = DumpArchiveInputStream.matches(buffer, 32);
        Assert.assertFalse(matches);
        
        buffer = new byte[32];
        Arrays.fill(buffer, (byte) 1);
        matches = DumpArchiveInputStream.matches(buffer, 32);
        Assert.assertTrue(matches);
        
        buffer = null;
        matches = DumpArchiveInputStream.matches(buffer, 32);
        Assert.assertFalse(matches);
    }
}