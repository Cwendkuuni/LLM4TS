import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        // Test case for getFile method
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile, result.getFile());
    }

    @Test
    public void testGetString() {
        // Test case for getString method
        String testString = "Test String";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.getString());
    }

    @Test
    public void testGetDouble() {
        // Test case for getDouble method
        Double testDouble = 10.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble, result.getDouble());
    }

    @Test
    public void testGetDate() {
        // Test case for getDate method
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate, result.getDate());
    }

    @Test
    public void testGetURI() {
        // Test case for getURI method
        URI testURI = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        // Test case for getBoolean method
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean, result.getBoolean());
    }

    @Test
    public void testToString() {
        // Test case for toString method with File
        File testFile = new File("testFile.txt");
        FSPathResult resultFile = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), resultFile.toString());

        // Test case for toString method with String
        String testString = "Test String";
        FSPathResult resultString = new FSPathResult(testString);
        assertEquals(testString, resultString.toString());

        // Test case for toString method with Double
        Double testDouble = 10.5;
        FSPathResult resultDouble = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), resultDouble.toString());

        // Test case for toString method with Boolean
        Boolean testBoolean = true;
        FSPathResult resultBoolean = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), resultBoolean.toString());

        // Test case for toString method with Date
        Date testDate = new Date();
        FSPathResult resultDate = new FSPathResult(testDate);
        assertEquals(testDate.toString(), resultDate.toString());

        // Test case for toString method with URI
        URI testURI = URI.create("http://www.example.com");
        FSPathResult resultURI = new FSPathResult(testURI);
        assertEquals(testURI.toString(), resultURI.toString());

        // Test case for empty result
        FSPathResult resultEmpty = new FSPathResult(null);
        assertEquals("FSPathResult : empty", resultEmpty.toString());
    }
}