import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() {
        cpioOutputStream = new CpioArchiveOutputStream(new OutputStream());
    }

    @After
    public void tearDown() {
        cpioOutputStream.close();
    }

    @Test
    public void testPutNextEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(0644);
        entry.setUID(1001);
        entry.setGID(1001);
        entry.setNumberOfLinks(1);
        entry.setTime(System.currentTimeMillis());
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        assertEquals(entry, cpioOutputStream.getCpioEntry());
    }

    @Test
    public void testWrite() {
        byte[] data = "Hello, World!".getBytes();
        cpioOutputStream.write(data, 0, data.length);
        // Additional assertions can be added to check the data was written correctly
    }

    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(0644);
        entry.setUID(1001);
        entry.setGID(1001);
        entry.setNumberOfLinks(1);
        entry.setTime(System.currentTimeMillis());
        entry.setSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.closeArchiveEntry();
        // Additional assertions can be added to check the entry was closed correctly
    }

    // Add more test cases for other methods if needed
    
}