
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioStream;

    @Before
    public void setUp() {
        cpioStream = new CpioArchiveOutputStream(null, CpioArchiveOutputStream.FORMAT_NEW);
    }
  
    @Test
    public void testConstructor() {
        assertNotNull(cpioStream);
    }

    @Test
    public void testPutArchiveEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        try {
            cpioStream.putArchiveEntry(entry);
        } catch (Exception e) {
            fail("putArchiveEntry should not throw an exception");
        }
    }
    
    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        try {
            cpioStream.putArchiveEntry(entry);
            cpioStream.closeArchiveEntry();
        } catch (Exception e) {
            fail("closeArchiveEntry should not throw an exception");
        }
    }
    
    @Test
    public void testWrite() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        byte[] data = {1, 2, 3};
        try {
            cpioStream.putArchiveEntry(entry);
            cpioStream.write(data);
            cpioStream.closeArchiveEntry();
        } catch (Exception e) {
            fail("write should not throw an exception");
        }
    }
    
    @Test
    public void testFinish() {
        try {
            cpioStream.finish();
        } catch (Exception e) {
            fail("finish should not throw an exception");
        }
    }
    
    @Test
    public void testClose() {
        try {
            cpioStream.close();
        } catch (Exception e) {
            fail("close should not throw an exception");
        }
    }
    
    @Test
    public void testCreateArchiveEntry() {
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        try {
            ArchiveEntry entry = cpioStream.createArchiveEntry(inputFile, entryName);
            assertNotNull(entry);
        } catch (Exception e) {
            fail("createArchiveEntry should not throw an exception");
        }
    }
    
    @Test
    public void testEnsureOpen() {
        try {
            cpioStream.ensureOpen();
        } catch (Exception e) {
            fail("ensureOpen should not throw an exception");
        }
    }
}
