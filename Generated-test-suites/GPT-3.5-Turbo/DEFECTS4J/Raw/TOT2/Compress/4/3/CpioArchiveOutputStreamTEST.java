import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry("testFile");
        cpioOutputStream.putArchiveEntry(cpioEntry);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.close();
        
        assertNotNull(cpioOutputStream);
        assertEquals(outputStream.toByteArray().length, 76); // Based on the expected header size
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        byte[] data = {1, 2, 3, 4, 5};
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry("testFile"));
        cpioOutputStream.write(data, 0, data.length);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.close();
        
        assertNotNull(cpioOutputStream);
        assertEquals(outputStream.toByteArray().length, 81); // Based on the expected size of written data
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.finish();
        cpioOutputStream.close();
        
        assertNotNull(cpioOutputStream);
        assertEquals(outputStream.toByteArray().length, 76); // Based on the expected trailer size
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry("testFile"));
        cpioOutputStream.closeArchiveEntry();
        
        assertNull(cpioOutputStream.entry);
        assertEquals(cpioOutputStream.written, 0);
        cpioOutputStream.close();
    }

    // Additional tests for edge cases and error scenarios can be added here

}