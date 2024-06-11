import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;
  
    @Before
    public void setUp() {
        // create a new ExtendedBufferedReader with a StringReader as the underlying reader
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\nTest"));
    }

    @Test
    public void testRead() throws IOException {
        // read the first character
        int result = reader.read();
      
        // assert that the result is the ASCII value of the first character in the string
        assertEquals(72, result);

        // read the second character
        result = reader.read();
      
        // assert that the result is the ASCII value of the second character in the string
        assertEquals(101, result);

        // read the third character
        result = reader.read();
      
        // assert that the result is the ASCII value of the third character in the string
        assertEquals(108, result);
    }

    @Test
    public void testReadAgain() throws IOException {
        // read the first character
        int result = reader.read();
      
        // assert that the result is the ASCII value of the first character in the string
        assertEquals(72, result);
      
        // call the readAgain method
        int readAgainResult = reader.readAgain();
      
        // assert that the readAgainResult is equal to the ASCII value of the first character
        assertEquals(72, readAgainResult);
    }

    @Test
    public void testReadLine() throws IOException {
        // read the first line
        String line = reader.readLine();
      
        // assert that the line is equal to "Hello"
        assertEquals("Hello", line);

        // read the second line
        line = reader.readLine();
      
        // assert that the line is equal to "World"
        assertEquals("World", line);
    }

    @Test
    public void testLookAhead() throws IOException {
        // call the lookAhead method
        int result = reader.lookAhead();
      
        // assert that the result is the ASCII value of the next character in the string
        assertEquals(101, result);
    }

    @Test
    public void testGetLineNumber() {
        // call the getLineNumber method
        int lineCount = reader.getLineNumber();
      
        // assert that the lineCount is 0
        assertEquals(0, lineCount);
    }
}