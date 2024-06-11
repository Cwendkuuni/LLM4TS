import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    private static final String TEST_STRING = "test";
    private static final Double TEST_DOUBLE = 10.0;
    private static final Date TEST_DATE = new Date();
    private static final URI TEST_URI = URI.create("http://www.example.com");
    private static final Boolean TEST_BOOLEAN = true;

    @Test
    public void testFileConstructorAndGetFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testStringConstructorAndGetString() {
        FSPathResult result = new FSPathResult(TEST_STRING);
        assertEquals(TEST_STRING, result.getString());
    }

    @Test
    public void testDoubleConstructorAndGetDouble() {
        FSPathResult result = new FSPathResult(TEST_DOUBLE);
        assertEquals(TEST_DOUBLE, result.getDouble());
    }

    @Test
    public void testDateConstructorAndGetDate() {
        FSPathResult result = new FSPathResult(TEST_DATE);
        assertEquals(TEST_DATE, result.getDate());
    }

    @Test
    public void testURIConstructorAndGetURI() {
        FSPathResult result = new FSPathResult(TEST_URI);
        assertEquals(TEST_URI, result.getURI());
    }

    @Test
    public void testBooleanConstructorAndGetBoolean() {
        FSPathResult result = new FSPathResult(TEST_BOOLEAN);
        assertEquals(TEST_BOOLEAN, result.getBoolean());
    }

    @Test
    public void testToString() {
        File file = new File("test.txt");
        FSPathResult fileResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fileResult.toString());

        FSPathResult stringResult = new FSPathResult(TEST_STRING);
        assertEquals(TEST_STRING, stringResult.toString());

        FSPathResult doubleResult = new FSPathResult(TEST_DOUBLE);
        assertEquals(TEST_DOUBLE.toString(), doubleResult.toString());

        FSPathResult booleanResult = new FSPathResult(TEST_BOOLEAN);
        assertEquals(TEST_BOOLEAN.toString(), booleanResult.toString());

        FSPathResult dateResult = new FSPathResult(TEST_DATE);
        assertEquals(TEST_DATE.toString(), dateResult.toString());

        FSPathResult uriResult = new FSPathResult(TEST_URI);
        assertEquals(TEST_URI.toString(), uriResult.toString());
    }
}