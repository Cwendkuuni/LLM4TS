import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        // Create a new ExtendedBufferedReader instance for each test case
        reader = new ExtendedBufferedReader();
    }

    @Test
    public void testRead() {
        // Test read() method functionality
        
        // Test reading a single character
        int result = reader.read();
        assertEquals("Expected character read is different", 'a', result);
        
        // Test reading multiple characters
        char[] buffer = new char[5];
        int length = reader.read(buffer, 0, 5);
        assertEquals("Expected length is different", 5, length);
        assertEquals("Expected character read is different", 'b', buffer[0]);
        assertEquals("Expected character read is different", 'c', buffer[1]);
        assertEquals("Expected character read is different", 'd', buffer[2]);
        assertEquals("Expected character read is different", 'e', buffer[3]);
        assertEquals("Expected character read is different", 'f', buffer[4]);
    }

    @Test
    public void testReadAgain() {
        // Test readAgain() method functionality
        
        // Test reading a character and checking readAgain()
        assertEquals("Expected character read is different", 'a', reader.read());
        assertEquals("Expected character readAgain is different", 'a', reader.readAgain());
        
        // Test reading multiple characters and checking readAgain()
        char[] buffer = new char[5];
        reader.read(buffer, 0, 5);
        assertEquals("Expected character read is different", 'b', buffer[0]);
        assertEquals("Expected character readAgain is different", 'b', reader.readAgain());
    }

    @Test
    public void testReadLine() {
        // Test readLine() method functionality
        
        // Test reading a single line
        String line = reader.readLine();
        assertEquals("Expected line is different", "This is a test line", line);
        
        // Test reading multiple lines
        String line1 = reader.readLine();
        String line2 = reader.readLine();
        String line3 = reader.readLine();
        assertEquals("Expected line is different", "Line 1", line1);
        assertEquals("Expected line is different", "Line 2", line2);
        assertEquals("Expected line is different", "Line 3", line3);
    }

    @Test
    public void testLookAhead() {
        // Test lookAhead() method functionality
        
        // Test looking ahead at the next character
        int result = reader.lookAhead();
        assertEquals("Expected character is different", 'a', result);
        
        // Test looking ahead at multiple characters
        int result1 = reader.lookAhead();
        int result2 = reader.lookAhead();
        int result3 = reader.lookAhead();
        assertEquals("Expected character is different", 'a', result1);
        assertEquals("Expected character is different", 'a', result2);
        assertEquals("Expected character is different", 'a', result3);
    }

    @Test
    public void testGetLineNumber() {
        // Test getLineNumber() method functionality
        
        // Test getting the line number after reading a line
        reader.readLine();
        int lineNumber = reader.getLineNumber();
        assertEquals("Expected line number is different", 1, lineNumber);
        
        // Test getting the line number after reading multiple lines
        reader.readLine();
        reader.readLine();
        lineNumber = reader.getLineNumber();
        assertEquals("Expected line number is different", 3, lineNumber);
    }

}