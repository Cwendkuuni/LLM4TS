import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(byteStream);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(644);
        entry.setSize(5);
        
        cpioStream.putNextEntry(entry);
        
        cpioStream.closeArchiveEntry();
        
        byteStream.close();
        
        byte[] result = byteStream.toByteArray();
        
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(byteStream);
        
        String testData = "12345";
        
        cpioStream.putNextEntry(new CpioArchiveEntry());
        cpioStream.write(testData.getBytes());
        
        cpioStream.closeArchiveEntry();
        
        byteStream.close();
        
        byte[] result = byteStream.toByteArray();
        
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertEquals(testData, new String(result));
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(byteStream);
        
        cpioStream.finish();
        
        byteStream.close();
        
        byte[] result = byteStream.toByteArray();
        
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(byteStream);
        
        cpioStream.close();
        
        assertTrue(byteStream.size() == 0);
    }

}