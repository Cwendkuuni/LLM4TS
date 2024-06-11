import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import corina.*;

public class TucsonSimpleTest {
    private TucsonSimple tucsonSimple;
  
    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
    }
  
    
    
    @Test
    public void testToString() {
        String expected = "format.tucsonsimple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDefaultExtension() {
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(""));
        tucsonSimple.load(reader);
    }
    
    @Test
    public void testMake6digitCodeWhenIdExists() {
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWhenIdDoesNotExist() {
        Sample sample = new Sample();
        
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCodeWhenIdLengthIsGreaterThan6() {
        Sample sample = new Sample();
        sample.meta.put("id", "123456789");
        
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        
        assertEquals(expected, actual);
    }
    
    // Similarly, generate additional test cases for the remaining methods
    
    
}