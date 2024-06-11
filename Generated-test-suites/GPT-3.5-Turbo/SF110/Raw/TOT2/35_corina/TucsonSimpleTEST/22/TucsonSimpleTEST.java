import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

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
            tucsonSimple.load(new BufferedReader(new StringReader("test")));
            fail("Expected WrongFiletypeException");
        } catch (IOException e) {
            fail("Expected WrongFiletypeException");
        } catch (WrongFiletypeException e) {
            // Expected exception
        }
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));

        Sample sample2 = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sample2));
    }

    // Additional test cases for saveRowHeader, saveData, and computeRange methods can be added here

}