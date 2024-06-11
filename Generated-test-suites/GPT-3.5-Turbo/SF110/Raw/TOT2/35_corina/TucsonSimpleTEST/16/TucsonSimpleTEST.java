import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import corina.formats.*;
import corina.ui.*;
import corina.util.*;

public class TucsonSimpleTest {
    
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("Should return the correct text", "Tucson Simple Format", tucsonSimple.toString());
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("Should return the correct default extension", ".RWL", tucsonSimple.getDefaultExtension());
    }
    
    @Test
    public void testLoad() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader("test"));
        try {
            tucsonSimple.load(reader);
            fail("Should throw WrongFiletypeException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("Should return the first 6 digits of the id", "123456", code);
    }
    
    // Additional test cases for other methods...
    
}