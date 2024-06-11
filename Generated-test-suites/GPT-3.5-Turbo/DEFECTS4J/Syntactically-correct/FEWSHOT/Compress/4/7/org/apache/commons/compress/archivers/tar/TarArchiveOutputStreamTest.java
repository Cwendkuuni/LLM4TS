
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutput;
    private ByteArrayOutputStream byteOutput;
    private byte[] expected;
    
    @Before
    public void setUp() throws Exception {
        byteOutput = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(byteOutput);
        expected = byteOutput.toByteArray();
    }

    @After
    public void tearDown() throws Exception {
        tarOutput.close();
        tarOutput = null;
        byteOutput.close();
        byteOutput = null;
        expected = null;
    }
    
    private void checkArchiveEntry(ArchiveEntry entry, String name, long size) {
        assertEquals(name, entry.getName());
        assertEquals(size, entry.getSize());
    }
    
    @Test
    public void testConstructor() {
        assertEquals(512, tarOutput.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry_FileEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tarOutput.putArchiveEntry(entry);
        checkArchiveEntry(entry, "test.txt", 0);
        tarOutput.closeArchiveEntry();
        expected = byteOutput.toByteArray();
        // assert expected bytes
        
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        assertFalse(tarOutput.haveUnclosedEntry);
        tarOutput.closeArchiveEntry();
        assertFalse(tarOutput.haveUnclosedEntry);
    }
    
    @Test(expected=IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOutput.finish();
    }
    
    @Test
    public void testWrite() throws IOException {
        tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
        byte[] data = "test data".getBytes();
        tarOutput.write(data);
        assertEquals(data.length, tarOutput.currBytes);
        tarOutput.closeArchiveEntry();
        expected = byteOutput.toByteArray();
        // assert expected bytes
    }
    
    @Test
    public void testWriteWithLargeBuffer() throws IOException {
        tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
        byte[] data = new byte[8192];
        tarOutput.write(data);
        assertEquals(data.length, tarOutput.currBytes);
        tarOutput.closeArchiveEntry();
        expected = byteOutput.toByteArray();
        // assert expected bytes
    }
    
    @Test
    public void testWriteWithOffsetAndLength() throws IOException {
        tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
        byte[] data = "test data".getBytes();
        tarOutput.write(data, 2, 4);
        assertEquals(4, tarOutput.currBytes);
        tarOutput.closeArchiveEntry();
        expected = byteOutput.toByteArray();
        // assert expected bytes
    }
    
    @Test
    public void testFlush() throws IOException {
        tarOutput.flush();
        // assert flush is called
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        ArchiveEntry entry = tarOutput.createArchiveEntry(inputFile, "test.txt");
        assertEquals("test.txt", entry.getName());
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.longFileMode);
    }
    
    @Test
    public void testFinish() throws IOException {
        tarOutput.finish();
        // assert finish is called
    }
    
    @Test
    public void testClose() throws IOException {
        tarOutput.close();
        // assert close is called
    }
    
}
