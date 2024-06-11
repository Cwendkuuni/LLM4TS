
package org.apache.commons.compress.archivers.tar.junit;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarArchiveOutputStream;
    
    @Before
    public void setup() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void teardown() throws IOException {
        tarArchiveOutputStream.close();
    }
    
    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
        
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
        
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry());
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        assertTrue(!tarArchiveOutputStream.haveUnclosedEntry());
    }
    
    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        
        // Assert the content in the OutputStream
        byte[] expectedBytes = new byte[]{72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        byte[] actualBytes = tarArchiveOutputStream.toByteArray();
        for (int i = 0; i < expectedBytes.length; i++) {
            assertEquals(expectedBytes[i], actualBytes[i]);
        }
    }
    
    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertEquals(2 * 512, tarArchiveOutputStream.size());
    }
    
    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }
}
