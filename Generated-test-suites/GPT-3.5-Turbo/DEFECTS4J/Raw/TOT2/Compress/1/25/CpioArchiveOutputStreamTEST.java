import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructorWithFormat() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_OLD_ASCII);
        assertFalse(cpioOutput.isClosed());
    }

    @Test
    public void testConstructorWithoutFormat() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
        assertFalse(cpioOutput.isClosed());
    }

    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(5);
        
        cpioOutput.putNextEntry(entry);
        
        assertEquals(entry, cpioOutput.getCurrentEntry());
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        
        cpioOutput.putNextEntry(entry);
        byte[] testData = {1, 2, 3, 4, 5};
        cpioOutput.write(testData);
        
        cpioOutput.closeArchiveEntry();
        
        assertTrue(outputStream.toByteArray().length > 0);
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        
        cpioOutput.putNextEntry(entry);
        cpioOutput.finish();
        
        assertTrue(outputStream.toByteArray().length > 0);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(outputStream);
        
        cpioOutput.close();
        
        assertTrue(cpioOutput.isClosed());
    }
}