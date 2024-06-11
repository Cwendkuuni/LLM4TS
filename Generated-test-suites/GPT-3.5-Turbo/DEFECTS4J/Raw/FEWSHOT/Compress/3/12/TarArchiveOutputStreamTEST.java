
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class TarArchiveOutputStreamTest {
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutput.longFileMode);
        
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.longFileMode);
        
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.longFileMode);
        
    }
    
    @Test
    public void testFinish() throws Exception{
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(byteArrayOutputStream);
        
        tarOutput.finish();
        
        byte[] byteArray=byteArrayOutputStream.toByteArray();
        assertEquals(1024,byteArray.length);
        
        for (byte b : byteArray) {
            assertEquals(0,b);
        }
    }
    
    @Test
    public void testClose() throws Exception{
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(byteArrayOutputStream);
        
        tarOutput.close();
        
        assertTrue(tarOutput.closed);
    }
    
    @Test
    public void testGetRecordSize() throws Exception{
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(byteArrayOutputStream);
        
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutput.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() throws Exception{
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(byteArrayOutputStream);
        
        File file = new File("testfile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        tarOutput.putArchiveEntry(entry);

        assertEquals(0, tarOutput.assemLen);
        assertEquals(0, tarOutput.assemLen);
        assertEquals(0, tarOutput.currBytes);
        
        tarOutput.closeArchiveEntry();
        tarOutput.close();
    }
    
    @Test
    public void testCloseArchiveEntry() throws Exception{
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(byteArrayOutputStream);
        
        tarOutput.closeArchiveEntry();
        
        assertEquals(0, tarOutput.assemLen);
    }
    
    @Test
    public void testWriteByte() throws Exception{
        File tmpFile = File.createTempFile("unitTest", "tmp");
        tmpFile.deleteOnExit();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        TarArchiveEntry entry = new TarArchiveEntry(tmpFile);
        tarOutput.putArchiveEntry(entry);
        
        int data;
        while ((data = inputStream.read()) != -1) {
            tarOutput.write(data);
        }
        
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        
        assertTrue(tarOutput.closed);
    }
    
    @Test
    public void testWriteByteArray() throws Exception{
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(byteArrayOutputStream);
        
        byte[] buffer = new byte[1024];
        tarOutput.write(buffer);
        tarOutput.closeArchiveEntry();
        tarOutput.close();
        
        assertTrue(tarOutput.closed);
    }
    
    @Test
    public void testWriteByteArrayWithOffset() throws Exception{
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(byteArrayOutputStream);
        
        byte[] buffer = new byte[1024];
        int offset = 0;
        int length = buffer.length;
        tarOutput.write(buffer, offset, length);
        tarOutput.closeArchiveEntry();
        tarOutput.close();
        
        assertTrue(tarOutput.closed);
    }
    
    @Test
    public void testCreateArchiveEntry() throws Exception{
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(byteArrayOutputStream);
        
        File inputFile = new File("testfile.txt");
        String entryName = "testfile.txt";
        ArchiveEntry entry = tarOutput.createArchiveEntry(inputFile, entryName);
        
        assertEquals(TarArchiveEntry.class, entry.getClass());
        tarOutput.closeArchiveEntry();
        tarOutput.close();
        
        assertTrue(tarOutput.closed);
    }
   
}

