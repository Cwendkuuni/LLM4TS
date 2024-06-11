import static org.junit.Assert.*;
import org.junit.Test;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test
    public void testLoad() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        try {
            tucsonSimple.load(null); // should throw WrongFiletypeException
            fail("Exception not thrown");
        } catch (IOException e) {
            // Do nothing
        }
    }

    // Additional test cases can be added here for other methods in TucsonSimple class

}