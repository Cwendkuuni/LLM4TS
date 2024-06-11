
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class TarArchiveOutputStreamTest {
    
    private ByteArrayOutputStream bos;
    private TarArchiveOutputStream tar;

    @Before
    public void setUp() throws Exception {
        bos = new ByteArrayOutputStream();
        tar = new TarArchiveOutputStream(bos);
    }

    @Test
    public void testConstructor() throws IOException {
        assertNotNull(tar.buffer);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tar.buffer.blockSize);

        assertNotNull(tar.out);
        assertEquals(bos, tar.out);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("example.txt");
        entry.setSize(10);

        tar.putArchiveEntry(entry);
        
        tar.write("example".getBytes());
        assertEquals(7, tar.currBytes);
        
        tar.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryExceedSize() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("example.txt");
        entry.setSize(5);

        tar.putArchiveEntry(entry);

        tar.write("example".getBytes());

        tar.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("example.txt");
        entry.setSize(10);

        tar.putArchiveEntry(entry);
        
        tar.write("example".getBytes());
        assertEquals(7, tar.currBytes);
        
        tar.closeArchiveEntry();
        
        assertEquals(10, tar.currBytes);
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("example.txt");
        entry.setSize(10);

        tar.putArchiveEntry(entry);
        
        tar.write("example".getBytes());
        
        tar.closeArchiveEntry();
        tar.close();
    }
}
