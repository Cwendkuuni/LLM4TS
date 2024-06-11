import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class SimpleCharStreamTest {

    private static SimpleCharStream scs;

    @BeforeClass
    public static void setUp() throws Exception {
        FileReader fr = new FileReader("testfile.txt");
        scs = new SimpleCharStream(fr);
    }

    @Test
    public void testBeginToken() throws IOException {
        char expected = 'H';
        char actual = scs.BeginToken();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadChar() throws IOException {
        char expected = 'e';
        scs.BeginToken();
        char actual = scs.readChar();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetColumn() throws IOException {
        int expected = 2;
        scs.BeginToken(); // Token starts at column 1
        scs.readChar(); // Move to column 2
        int actual = scs.getColumn();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLine() throws IOException {
        int expected = 1;
        scs.BeginToken(); // Token starts at line 1
        int actual = scs.getLine();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEndColumn() throws IOException {
        int expected = 1;
        scs.BeginToken(); // Token starts at column 1
        int actual = scs.getEndColumn();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetEndLine() throws IOException {
        int expected = 1;
        scs.BeginToken(); // Token starts at line 1
        int actual = scs.getEndLine();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        int expected = 1;
        scs.BeginToken(); // Token starts at column 1
        int actual = scs.getBeginColumn();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBeginLine() throws IOException {
        int expected = 1;
        scs.BeginToken(); // Token starts at line 1
        int actual = scs.getBeginLine();
        assertEquals(expected, actual);
    }

    @Test
    public void testBackup() throws IOException {
        scs.BeginToken(); // Token starts at column 1
        scs.readChar(); // Move to column 2
        int expected = 1;
        scs.backup(1); // Move back to column 1
        int actual = scs.getColumn();
        assertEquals(expected, actual);
    }

    @Test
    public void testReInit() throws IOException {
        FileReader fr = new FileReader("testfile2.txt");
        scs.ReInit(fr);
        char expected = 'A';
        char actual = scs.readChar();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImage() throws IOException {
        scs.BeginToken();
        scs.readChar();
        scs.readChar();
        String expected = "Hel";
        String actual = SimpleCharStream.GetImage();
        assertEquals(expected, actual);
    }

    @Test
    public void testDone() {
        // No assertion, just check that no errors occur
        SimpleCharStream.Done();
    }

    @AfterClass
    public static void tearDown() {
        // Clean up any resources used
    }
}