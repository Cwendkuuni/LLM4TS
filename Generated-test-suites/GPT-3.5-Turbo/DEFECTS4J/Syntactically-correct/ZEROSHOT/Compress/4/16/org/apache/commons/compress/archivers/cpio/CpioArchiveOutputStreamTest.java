import org.junit.*;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
    private CpioArchiveOutputStream cpioOut;

    @Before
    public void setUp() {
        cpioOut = new CpioArchiveOutputStream(null);
    }

    @After
    public void tearDown() {
        cpioOut = null;
    }

    @Test
    public void testPutArchiveEntry() {
        try {
            cpioOut.putArchiveEntry(new CpioArchiveEntry());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        try {
            cpioOut.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        try {
            cpioOut.write("test".getBytes(), 0, 4);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        try {
            cpioOut.finish();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            cpioOut.close();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}