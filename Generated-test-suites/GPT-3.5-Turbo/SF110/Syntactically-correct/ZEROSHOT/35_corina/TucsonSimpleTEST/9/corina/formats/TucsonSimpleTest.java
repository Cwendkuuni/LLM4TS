import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

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
        BufferedReader r = null; // Replace with a valid BufferedReader object
        tucsonSimple.load(r);
    }
    
    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample(); // Replace with valid Sample object
        String expected = ""; // Replace with the expected result
        String actual = tucsonSimple.make6digitCode(s);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = null; // Replace with a valid BufferedWriter object
        Sample s = new Sample(); // Replace with valid Sample object
        String code = ""; // Replace with a valid code
        int yearWidth = 0; // Replace with a valid year width
        Year y = new Year(); // Replace with a valid Year object
        try {
            tucsonSimple.saveRowHeader(w, s, code, yearWidth, y);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter w = null; // Replace with a valid BufferedWriter object
        Sample s = new Sample(); // Replace with valid Sample object
        try {
            tucsonSimple.saveData(s, w);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample(); // Replace with valid Sample object
        Range expected = new Range(); // Replace with the expected result
        Range actual = tucsonSimple.computeRange(s);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample s = new Sample(); // Replace with valid Sample object
        BufferedWriter w = null; // Replace with a valid BufferedWriter object
        try {
            tucsonSimple.save(s, w);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
}