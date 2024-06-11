
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    private FSPathResult fsPathResult;

    @Before
    public void setUp() {
        File file = new File("test.txt");
        fsPathResult = new FSPathResult(file);
    }

    @Test
    public void testGetFile() {
        assertEquals("test.txt", fsPathResult.getFile().getName());
    }

    @Test
    public void testGetString() {
        assertNull(fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        assertNull(fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        assertNull(fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        assertNull(fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        assertNull(fsPathResult.getBoolean());
    }

    @Test
    public void testToString() {
        assertEquals("test.txt", fsPathResult.toString());
    }
}
