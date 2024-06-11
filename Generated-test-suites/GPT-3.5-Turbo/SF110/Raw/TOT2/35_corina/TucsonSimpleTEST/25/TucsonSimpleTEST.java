
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("Should return the expected string", "Tucson Simple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("Should return the default extension", ".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test
    public void testLoad() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new StringReader("test"));
        try {
            tucsonSimple.load(r);
            fail("WrongFiletypeException not thrown");
        } catch (Exception e) {
            assertTrue("Should throw WrongFiletypeException", e instanceof WrongFiletypeException);
        }
    }

    // Add more basic test cases for other methods as per requirement
}

