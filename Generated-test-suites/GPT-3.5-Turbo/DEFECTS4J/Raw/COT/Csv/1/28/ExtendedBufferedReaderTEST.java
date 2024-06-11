
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExtendedBufferedReaderTest {
    
    private ExtendedBufferedReader reader;
    
    @Before
    public void setUp() {
        // Initialize the reader with a dummy Reader object
        reader = new ExtendedBufferedReader(null);
    }
    
    @Test
    public void testReadAgain() {
        // Test if the readAgain() method returns the last character read
        reader.lastChar = 'a';
        assertEquals('a', reader.readAgain());
    }
    
    @Test
    public void testRead() {
        // Test the basic functionality of the read() method
        // by asserting that the lineCounter is incremented and
        // the lastChar is updated correctly
        try {
            assertEquals('a', reader.read());
            assertEquals(1, reader.lineCounter);
            assertEquals('a', reader.lastChar);
            
            assertEquals('b', reader.read());
            assertEquals(1, reader.lineCounter);
            assertEquals('b', reader.lastChar);
            
            assertEquals('\n', reader.read());
            assertEquals(2, reader.lineCounter);
            assertEquals('\n', reader.lastChar);
        } catch (IOException e) {
            // Handle IOException
        }
    }
    
    @Test
    public void testReadArray() {
        // Test the basic functionality of the read(char[], int, int) method
        // by asserting that the lineCounter is incremented and
        // the lastChar is updated correctly
        char[] buffer = new char[5];
        
        try {
            reader.lastChar = 'a';
            assertEquals(3, reader.read(buffer, 0, 3));
            assertEquals(1, reader.lineCounter);
            assertEquals('d', reader.lastChar);
            
            reader.lastChar = 'b';
            assertEquals(2, reader.read(buffer, 0, 2));
            assertEquals(1, reader.lineCounter);
            assertEquals('e', reader.lastChar);
            
            reader.lastChar = '\n';
            assertEquals(2, reader.read(buffer, 0, 2));
            assertEquals(2, reader.lineCounter);
            assertEquals('f', reader.lastChar);
        } catch (IOException e) {
            // Handle IOException
        }
    }
    
    @Test
    public void testReadLine() {
        // Test the basic functionality of the readLine() method
        // by asserting that the lineCounter is incremented and
        // the lastChar is updated correctly
        try {
            assertEquals("test", reader.readLine());
            assertEquals(1, reader.lineCounter);
            assertEquals('\n', reader.lastChar);
            
            assertEquals("line", reader.readLine());
            assertEquals(2, reader.lineCounter);
            assertEquals('\n', reader.lastChar);
            
            assertNull(reader.readLine());
            assertEquals(2, reader.lineCounter);
            assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lastChar);
        } catch (IOException e) {
            // Handle IOException
        }
    }
    
    @Test
    public void testLookAhead() {
        // Test the lookAhead() method by asserting that the correct
        // character is returned without consuming it
        try {
            // Test if the next character is 'a'
            reader.mark(1);
            assertEquals('a', reader.lookAhead());
            reader.reset();
            
            // Test if the next character is 'b'
            reader.mark(1);
            reader.read();
            assertEquals('b', reader.lookAhead());
            reader.reset();
            
            // Test if the next character is '\n'
            reader.mark(1);
            reader.read();
            reader.read();
            assertEquals('\n', reader.lookAhead());
            reader.reset();
        } catch (IOException e) {
            // Handle IOException
        }
    }
    
    @Test
    public void testGetLineNumber() {
        // Test the getLineNumber() method by asserting that
        // it returns the correct line number
        reader.lineCounter = 10;
        assertEquals(10, reader.getLineNumber());
    }

}
