import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        // Test case for getting File object
        File testFile = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertEquals(testFile, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        // Test case for getting String object
        String testString = "Test String";
        FSPathResult fsPathResult = new FSPathResult(testString);
        assertEquals(testString, fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        // Test case for getting Double object
        Double testDouble = 10.5;
        FSPathResult fsPathResult = new FSPathResult(testDouble);
        assertEquals(testDouble, fsPathResult.getDouble(), 0);
    }

    @Test
    public void testGetDate() {
        // Test case for getting Date object
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        assertEquals(testDate, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        // Test case for getting URI object
        URI testURI = URI.create("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        assertEquals(testURI, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        // Test case for getting Boolean object
        Boolean testBoolean = true;
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean, fsPathResult.getBoolean());
    }

    @Test
    public void testToString() {
        // Test case for toString method
        File testFile = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testToStringEmpty() {
        // Test case for toString method when underlying object is null
        FSPathResult fsPathResult = new FSPathResult(null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }
}