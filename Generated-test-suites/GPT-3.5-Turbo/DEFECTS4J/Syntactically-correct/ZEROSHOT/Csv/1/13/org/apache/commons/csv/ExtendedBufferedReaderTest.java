import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {
    
    @Test
    public void testRead() {
        // Test case 1: read a character from an empty input
        Reader reader1 = new StringReader("");
        ExtendedBufferedReader br1 = new ExtendedBufferedReader(reader1);
        int result1 = br1.read();
        assertEquals(-1, result1);
        
        // Test case 2: read a character from a non-empty input
        Reader reader2 = new StringReader("Hello");
        ExtendedBufferedReader br2 = new ExtendedBufferedReader(reader2);
        int result2;
        try {
            result2 = br2.read();
            assertEquals('H', result2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Test case 3: read a character from an input with a line break
        Reader reader3 = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br3 = new ExtendedBufferedReader(reader3);
        int result3;
        try {
            result3 = br3.read();
            assertEquals('H', result3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadAgain() {
        // Test case 1: readAgain with no characters read
        Reader reader1 = new StringReader("");
        ExtendedBufferedReader br1 = new ExtendedBufferedReader(reader1);
        int result1 = br1.readAgain();
        assertEquals(-2, result1);
        
        // Test case 2: readAgain with characters read
        Reader reader2 = new StringReader("Hello");
        ExtendedBufferedReader br2 = new ExtendedBufferedReader(reader2);
        int result2;
        try {
            br2.read();
            result2 = br2.readAgain();
            assertEquals('H', result2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadCharArray() {
        // Test case 1: read char from an empty input
        Reader reader1 = new StringReader("");
        ExtendedBufferedReader br1 = new ExtendedBufferedReader(reader1);
        char[] buf1 = new char[5];
        int result1;
        try {
            result1 = br1.read(buf1, 0, 5);
            assertEquals(-1, result1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Test case 2: read char from a non-empty input
        Reader reader2 = new StringReader("Hello");
        ExtendedBufferedReader br2 = new ExtendedBufferedReader(reader2);
        char[] buf2 = new char[5];
        int result2;
        try {
            result2 = br2.read(buf2, 0, 5);
            assertEquals(5, result2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Test case 3: read char from an input with a line break
        Reader reader3 = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br3 = new ExtendedBufferedReader(reader3);
        char[] buf3 = new char[5];
        int result3;
        try {
            result3 = br3.read(buf3, 0, 5);
            assertEquals(5, result3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadLine() {
        // Test case 1: read line from an empty input
        Reader reader1 = new StringReader("");
        ExtendedBufferedReader br1 = new ExtendedBufferedReader(reader1);
        String result1;
        try {
            result1 = br1.readLine();
            assertNull(result1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Test case 2: read line from a non-empty input
        Reader reader2 = new StringReader("Hello");
        ExtendedBufferedReader br2 = new ExtendedBufferedReader(reader2);
        String result2;
        try {
            result2 = br2.readLine();
            assertEquals("Hello", result2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Test case 3: read line from an input with a line break
        Reader reader3 = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br3 = new ExtendedBufferedReader(reader3);
        String result3;
        try {
            result3 = br3.readLine();
            assertEquals("Hello", result3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLookAhead() {
        // Test case 1: look ahead with no characters read
        Reader reader1 = new StringReader("");
        ExtendedBufferedReader br1 = new ExtendedBufferedReader(reader1);
        int result1;
        try {
            result1 = br1.lookAhead();
            assertEquals(-1, result1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Test case 2: look ahead with characters read
        Reader reader2 = new StringReader("Hello");
        ExtendedBufferedReader br2 = new ExtendedBufferedReader(reader2);
        int result2;
        try {
            br2.read();
            result2 = br2.lookAhead();
            assertEquals('e', result2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetLineNumber() {
        // Test case 1: get line number with no lines read
        Reader reader1 = new StringReader("");
        ExtendedBufferedReader br1 = new ExtendedBufferedReader(reader1);
        int result1 = br1.getLineNumber();
        assertEquals(0, result1);
        
        // Test case 2: get line number with lines read
        Reader reader2 = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br2 = new ExtendedBufferedReader(reader2);
        int result2;
        try {
            br2.readLine();
            result2 = br2.getLineNumber();
            assertEquals(1, result2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}