import static org.junit.Assert.*;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    //Test case for read() method
    @Test
    public void testRead() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld!"));
        
        try {
            int c = reader.read();
            assertEquals('H', c);
            
            c = reader.read();
            assertEquals('e', c);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    //Test case for readAgain() method
    @Test
    public void testReadAgain() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld!"));
        
        try {
            int c = reader.read();
            assertEquals('H', c);
            
            c = reader.read();
            assertEquals('e', c);
            
            c = reader.readAgain();
            assertEquals('e', c);
            
            c = reader.read();
            assertEquals('l', c);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    //Test case for read(char[] buf, int offset, int length) method
    @Test
    public void testReadChar() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld!"));
        
        try {
            char[] buf = new char[5];
            int len = reader.read(buf, 0, 5);
            assertEquals(5, len);
            assertArrayEquals(new char[]{'H','e','l','l','o'}, buf);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    //Test case for readLine() method
    @Test
    public void testReadLine() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld!"));
        
        try {
            String line = reader.readLine();
            assertEquals("Hello", line);
            
            line = reader.readLine();
            assertEquals("World!", line);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    //Test case for lookAhead() method
    @Test
    public void testLookAhead() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld!"));
        
        try {
            int c = reader.lookAhead();
            assertEquals('H', c);
            
            c = reader.read();
            assertEquals('H', c);
            
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    //Test case for getLineNumber() method
    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld!"));
        
        try {
            int line = reader.getLineNumber();
            assertEquals(0, line);
            
            String s = reader.readLine();
            line = reader.getLineNumber();
            assertEquals(1, line);
            
            s = reader.readLine();
            line = reader.getLineNumber();
            assertEquals(2, line);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

}