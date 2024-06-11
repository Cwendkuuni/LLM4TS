import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setSize(5);
        
        try {
            cpioOutputStream.putArchiveEntry(entry);
            cpioOutputStream.closeArchiveEntry();
            cpioOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown while testing putArchiveEntry");
        }
        
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testWrite() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        
        byte[] data = {1, 2, 3, 4, 5};
        
        try {
            cpioOutputStream.write(data, 0, data.length);
            cpioOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown while testing write");
        }
        
        assertTrue(outputStream.size() > 0);
    }

    @Test
    public void testCloseArchiveEntry() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(5);
        
        try {
            cpioOutputStream.putArchiveEntry(entry);
            cpioOutputStream.write(new byte[5], 0, 5);
            cpioOutputStream.closeArchiveEntry();
            cpioOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception thrown while testing closeArchiveEntry");
        }
        
        assertTrue(outputStream.size() > 0);
    }
}