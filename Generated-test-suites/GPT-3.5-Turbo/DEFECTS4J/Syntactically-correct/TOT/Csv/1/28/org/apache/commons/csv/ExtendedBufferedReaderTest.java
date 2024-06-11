import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        // Test typical use case
        Reader reader = new StringReader("Hello, world!");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        int expectedChar = 'H';
        int actualChar = extendedReader.read();
        assertEquals(expectedChar, actualChar);
        
        // Test edge case for end of stream
        expectedChar = ExtendedBufferedReader.END_OF_STREAM;
        actualChar = extendedReader.read();
        assertEquals(expectedChar, actualChar);
    }
    
    @Test
    public void testReadAgain() {
        // Test typical use case
        Reader reader = new StringReader("Hello, world!");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        int expectedChar = 'H';
        extendedReader.read();
        int actualChar = extendedReader.readAgain();
        assertEquals(expectedChar, actualChar);
        
        // Test edge case for uninitialized lastChar
        expectedChar = ExtendedBufferedReader.UNDEFINED;
        actualChar = extendedReader.readAgain();
        assertEquals(expectedChar, actualChar);
    }
    
    @Test
    public void testReadCharArray() {
        // Test typical use case
        Reader reader = new StringReader("Hello\nworld");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        char[] buffer = new char[5];
        int expectedLength = 5;
        int actualLength;
        try {
            actualLength = extendedReader.read(buffer, 0, 5);
            assertEquals(expectedLength, actualLength);
            String expectedString = "Hello";
            String actualString = new String(buffer);
            assertEquals(expectedString, actualString);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        
        // Test edge case for newline character
        expectedLength = 4;
        buffer = new char[4];
        try {
            actualLength = extendedReader.read(buffer, 0, 4);
            assertEquals(expectedLength, actualLength);
            String expectedString = "worl";
            String actualString = new String(buffer);
            assertEquals(expectedString, actualString);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testReadLine() {
        // Test typical use case
        Reader reader = new StringReader("Hello\nworld");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        int expectedLineNumber = 1;
        String expectedLine = "Hello";
        try {
            String actualLine = extendedReader.readLine();
            assertEquals(expectedLineNumber, extendedReader.getLineNumber());
            assertEquals(expectedLine, actualLine);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        
        // Test edge case for end of stream
        expectedLine = null;
        try {
            String actualLine = extendedReader.readLine();
            assertEquals(expectedLineNumber, extendedReader.getLineNumber());
            assertEquals(expectedLine, actualLine);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testLookAhead() {
        // Test typical use case
        Reader reader = new StringReader("Hello");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        int expectedChar = 'e';
        int actualChar;
        try {
            actualChar = extendedReader.lookAhead();
            assertEquals(expectedChar, actualChar);
        } catch (IOException e) {
            fail("IOException occurred");
        }
        
        // Test edge case for end of stream
        reader = new StringReader("");
        extendedReader = new ExtendedBufferedReader(reader);
        expectedChar = ExtendedBufferedReader.END_OF_STREAM;
        try {
            actualChar = extendedReader.lookAhead();
            assertEquals(expectedChar, actualChar);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testGetLineNumber() {
        // Test typical use case
        Reader reader = new StringReader("Hello\nworld");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        int expectedLineNumber = 0;
        int actualLineNumber = extendedReader.getLineNumber();
        assertEquals(expectedLineNumber, actualLineNumber);
        
        // Test edge case for end of stream
        reader = new StringReader("Hello\nworld");
        extendedReader = new ExtendedBufferedReader(reader);
        try {
            extendedReader.readLine();
            expectedLineNumber = 1;
            actualLineNumber = extendedReader.getLineNumber();
            assertEquals(expectedLineNumber, actualLineNumber);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}