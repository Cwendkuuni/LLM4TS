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

    @Before
    public void setUp() {
        testFile = new File("testFile.txt");
        testString = "testString";
        testDouble = 2.5;
        testDate = new Date();
        testURI = URI.create("http://example.com");
        testBoolean = true;
    }

    @Test
    public void testGetFile() {
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertEquals(testFile, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        FSPathResult fsPathResult = new FSPathResult(testString);
        assertEquals(testString, fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        FSPathResult fsPathResult = new FSPathResult(testDouble);
        assertEquals(testDouble, fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        FSPathResult fsPathResult = new FSPathResult(testDate);
        assertEquals(testDate, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        FSPathResult fsPathResult = new FSPathResult(testURI);
        assertEquals(testURI, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean, fsPathResult.getBoolean());
    }

    @Test
    public void testToString_File() {
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testToString_String() {
        FSPathResult fsPathResult = new FSPathResult(testString);
        assertEquals(testString, fsPathResult.toString());
    }

    @Test
    public void testToString_Double() {
        FSPathResult fsPathResult = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_Boolean() {
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_Date() {
        FSPathResult fsPathResult = new FSPathResult(testDate);
        assertEquals(testDate.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_URI() {
        FSPathResult fsPathResult = new FSPathResult(testURI);
        assertEquals(testURI.toString(), fsPathResult.toString());
    }
}