package org.apache.commons.compress.archivers.dump;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setUp() throws ArchiveException {
        // Create a byte array for testing
        byte[] testBytes = new byte[512];
        for (int i = 0; i < testBytes.length; i++) {
            testBytes[i] = (byte) i;
        }
        InputStream inputStream = new ByteArrayInputStream(testBytes);

        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        dumpArchiveInputStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();

        Assert.assertNotNull(entry);
        Assert.assertEquals(".", entry.getName());
        Assert.assertFalse(entry.isDirectory());
        Assert.assertEquals(512, entry.getEntrySize());
    }

    @Test
    public void testRead() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();

        byte[] buf = new byte[256];
        int bytesRead = dumpArchiveInputStream.read(buf, 0, buf.length);

        Assert.assertEquals(buf.length, bytesRead);
    }

    @Test
    public void testGetBytesRead() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        byte[] buf = new byte[256];
        int bytesRead = dumpArchiveInputStream.read(buf, 0, buf.length);

        long bytesReadTotal = dumpArchiveInputStream.getBytesRead();

        Assert.assertEquals(512, bytesReadTotal);
    }
}