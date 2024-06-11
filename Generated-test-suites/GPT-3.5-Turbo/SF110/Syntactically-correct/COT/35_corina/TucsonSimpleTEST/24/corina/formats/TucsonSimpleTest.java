import org.junit.*;
import static org.junit.Assert.*;

public class TucsonSimpleTest {
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }
    
    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader r = new BufferedReader(new StringReader(""));
        tucsonSimple.load(r);
    }
    
    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = new BufferedWriter(new StringWriter());
        Sample s = new Sample();
        tucsonSimple.save(s, w);
        // Add assertions for save method's functionality
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testMake6digitCode_idNotPresent() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(s);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testMake6digitCode_idLongerThan6Characters() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample();
        s.meta.put("id", "12345678");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(s);
        assertEquals(expected, actual);
    }
    
    // Add more test cases for other identified edge cases and exception handling scenarios
    
}