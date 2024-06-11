import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    private File testFile;
    private String testString;
    private Double testDouble;
    private Date testDate;
    private URI testURI;
    private Boolean testBoolean;
    private FSPathResult result;

    @Before
    public void setUp() {
        testFile = new File("testFile.txt");
        testString = "testString";
        testDouble = 1.5;
        testDate = new Date();
        testURI = URI.create("http://www.example.com");
        testBoolean = true;
        result = new FSPathResult(testFile);
    }

    // Test getFile() method
    @Test
    public void testGetFile() {
        assertEquals(testFile, result.getFile());
    }

    // Test getString() method
    @Test
    public void testGetString() {
        result = new FSPathResult(testString);
        assertEquals(testString, result.getString());
    }

    // Test getDouble() method
    @Test
    public void testGetDouble() {
        result = new FSPathResult(testDouble);
        assertEquals(testDouble, result.getDouble());
    }

    // Test getDate() method
    @Test
    public void testGetDate() {
        result = new FSPathResult(testDate);
        assertEquals(testDate, result.getDate());
    }

    // Test getURI() method
    @Test
    public void testGetURI() {
        result = new FSPathResult(testURI);
        assertEquals(testURI, result.getURI());
    }

    // Test getBoolean() method
    @Test
    public void testGetBoolean() {
        result = new FSPathResult(testBoolean);
        assertEquals(testBoolean, result.getBoolean());
    }

    // Test toString() method
    @Test
    public void testToString() {
        String expectedResult = testFile.getAbsolutePath();
        assertEquals(expectedResult, result.toString());
    }

}