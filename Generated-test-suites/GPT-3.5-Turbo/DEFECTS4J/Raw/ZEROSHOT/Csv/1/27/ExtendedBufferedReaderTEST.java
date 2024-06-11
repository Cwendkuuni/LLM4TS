import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('H', br.read());
        assertEquals('e', br.read());
        assertEquals('l', br.read());
        assertEquals('l', br.read());
        assertEquals('o', br.read());
        assertEquals(' ', br.read());
        assertEquals('W', br.read());
        assertEquals('o', br.read());
        assertEquals('r', br.read());
        assertEquals('l', br.read());
        assertEquals('d', br.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.read());
        
        br.close();
    }
    
    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        br.read();
        br.read();
        br.read();
        
        assertEquals('H', br.readAgain());
        
        br.close();
    }
    
    @Test
    public void testReadArray() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        char[] buffer = new char[5];
        br.read(buffer, 0, 5);
        assertArrayEquals(new char[] {'H', 'e', 'l', 'l', 'o'}, buffer);
        
        br.read(buffer, 0, 5);
        assertArrayEquals(new char[] {' ', 'W', 'o', 'r', 'l'}, buffer);
        
        br.read(buffer, 0, 5);
        assertArrayEquals(new char[] {'d', (char)ExtendedBufferedReader.END_OF_STREAM, 'r', 'l', 'o'}, buffer);
        
        br.close();
    }
    
    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("Line 1\nLine 2\r\nLine 3\r");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals("Line 1", br.readLine());
        assertEquals("Line 2", br.readLine());
        assertEquals("Line 3", br.readLine());
        assertNull(br.readLine());
        
        br.close();
    }
    
    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals('H', br.lookAhead());
        br.read();
        assertEquals('e', br.lookAhead());
        br.read();
        assertEquals('l', br.lookAhead());
        br.read();
        assertEquals('l', br.lookAhead());
        br.read();
        assertEquals('o', br.lookAhead());
        br.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, br.lookAhead());
        
        br.close();
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Line 1\nLine 2\r\nLine 3\r");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        assertEquals(0, br.getLineNumber());
        
        br.readLine();
        assertEquals(1, br.getLineNumber());
        
        br.readLine();
        assertEquals(2, br.getLineNumber());
        
        br.readLine();
        assertEquals(3, br.getLineNumber());
        
        br.close();
    }

}