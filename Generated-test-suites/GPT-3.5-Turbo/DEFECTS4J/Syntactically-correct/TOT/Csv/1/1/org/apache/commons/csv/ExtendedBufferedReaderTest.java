import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String text = "This is a test string.";
        StringReader reader = new StringReader(text);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals('T', extendedBufferedReader.read());
        assertEquals('h', extendedBufferedReader.read());
        assertEquals('i', extendedBufferedReader.read());
        assertEquals('s', extendedBufferedReader.read());
        assertEquals(' ', extendedBufferedReader.read());
        assertEquals('i', extendedBufferedReader.read());
        assertEquals('s', extendedBufferedReader.read());
        assertEquals(' ', extendedBufferedReader.read());
        assertEquals('a', extendedBufferedReader.read());
        assertEquals(' ', extendedBufferedReader.read());
        assertEquals('t', extendedBufferedReader.read());
        assertEquals('e', extendedBufferedReader.read());
        assertEquals('s', extendedBufferedReader.read());
        assertEquals('t', extendedBufferedReader.read());
        assertEquals(' ', extendedBufferedReader.read());
        assertEquals('s', extendedBufferedReader.read());
        assertEquals('t', extendedBufferedReader.read());
        assertEquals('r', extendedBufferedReader.read());
        assertEquals('i', extendedBufferedReader.read());
        assertEquals('n', extendedBufferedReader.read());
        assertEquals('g', extendedBufferedReader.read());
        assertEquals('.', extendedBufferedReader.read());
        
        assertEquals(-1, extendedBufferedReader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }
    
    @Test
    public void testReadWithEmptyReader() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals(-1, extendedBufferedReader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }
    
    @Test
    public void testReadLine() throws IOException {
        String text = "This is a test string.\nSecond line.\nThird line.";
        StringReader reader = new StringReader(text);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals("This is a test string.", extendedBufferedReader.readLine());
        assertEquals(1, extendedBufferedReader.getLineNumber());
        assertEquals("Second line.", extendedBufferedReader.readLine());
        assertEquals(2, extendedBufferedReader.getLineNumber());
        assertEquals("Third line.", extendedBufferedReader.readLine());
        assertEquals(3, extendedBufferedReader.getLineNumber());
        assertEquals(null, extendedBufferedReader.readLine());
        assertEquals(3, extendedBufferedReader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }
    
    @Test
    public void testReadLineWithEmptyReader() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals(null, extendedBufferedReader.readLine());
        assertEquals(0, extendedBufferedReader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }
    
    @Test
    public void testReadLineWithNewlineCharacter() throws IOException {
        String text = "\n";
        StringReader reader = new StringReader(text);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals("", extendedBufferedReader.readLine());
        assertEquals(1, extendedBufferedReader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }
    
    @Test
    public void testReadLineWithEmptyLine() throws IOException {
        String text = "This is a test string.\n\nThird line.";
        StringReader reader = new StringReader(text);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals("This is a test string.", extendedBufferedReader.readLine());
        assertEquals(1, extendedBufferedReader.getLineNumber());
        assertEquals("", extendedBufferedReader.readLine());
        assertEquals(2, extendedBufferedReader.getLineNumber());
        assertEquals("Third line.", extendedBufferedReader.readLine());
        assertEquals(3, extendedBufferedReader.getLineNumber());
        assertEquals(null, extendedBufferedReader.readLine());
        assertEquals(3, extendedBufferedReader.getLineNumber());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        String text = "This is a test string.";
        StringReader reader = new StringReader(text);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals('T', extendedBufferedReader.lookAhead());
        assertEquals('T', extendedBufferedReader.read());
        assertEquals('h', extendedBufferedReader.lookAhead());
        assertEquals('h', extendedBufferedReader.read());
        assertEquals('i', extendedBufferedReader.lookAhead());
        
        extendedBufferedReader.read();
        extendedBufferedReader.read();
        extendedBufferedReader.read();
        
        assertEquals('t', extendedBufferedReader.lookAhead());
        assertEquals('t', extendedBufferedReader.read());
        assertEquals('s', extendedBufferedReader.lookAhead());
        assertEquals('s', extendedBufferedReader.read());
        assertEquals('t', extendedBufferedReader.lookAhead());
        assertEquals('t', extendedBufferedReader.read());
        assertEquals('r', extendedBufferedReader.lookAhead());
        assertEquals('r', extendedBufferedReader.read());
        assertEquals('i', extendedBufferedReader.lookAhead());
        assertEquals('i', extendedBufferedReader.read());
        assertEquals('n', extendedBufferedReader.lookAhead());
        assertEquals('n', extendedBufferedReader.read());
        assertEquals('g', extendedBufferedReader.lookAhead());
        assertEquals('g', extendedBufferedReader.read());
        assertEquals('.', extendedBufferedReader.lookAhead());
        assertEquals('.', extendedBufferedReader.read());
        
        assertEquals(-1, extendedBufferedReader.lookAhead());
        assertEquals(-1, extendedBufferedReader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        String text = "This is a test string.\n\nThird line.";
        StringReader reader = new StringReader(text);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        assertEquals(0, extendedBufferedReader.getLineNumber());
        
        extendedBufferedReader.readLine();
        assertEquals(1, extendedBufferedReader.getLineNumber());
        
        extendedBufferedReader.readLine();
        assertEquals(2, extendedBufferedReader.getLineNumber());
        
        extendedBufferedReader.readLine();
        assertEquals(3, extendedBufferedReader.getLineNumber());
        
        extendedBufferedReader.readLine();
        assertEquals(3, extendedBufferedReader.getLineNumber());
        
        extendedBufferedReader.readLine();
        assertEquals(3, extendedBufferedReader.getLineNumber());
    }
    
    @Test
    public void testReadBuffer() throws IOException {
        String text = "This is a test string.\nSecond line.";
        StringReader reader = new StringReader(text);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        char[] buf = new char[10];
        int len = extendedBufferedReader.read(buf, 0, buf.length);
        
        assertEquals(10, len);
        assertArrayEquals(new char[] {'T', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' '}, buf);
        
        buf = new char[10];
        len = extendedBufferedReader.read(buf, 0, buf.length);
        
        assertEquals(10, len);
        assertArrayEquals(new char[] {'t', 'e', 's', 't', ' ', 's', 't', 'r', 'i', 'n'}, buf);
        
        buf = new char[10];
        len = extendedBufferedReader.read(buf, 0, buf.length);
        
        assertEquals(10, len);
        assertArrayEquals(new char[] {'.', '\n', 'S', 'e', 'c', 'o', 'n', 'd', ' ', 'l'}, buf);
        
        buf = new char[10];
        len = extendedBufferedReader.read(buf, 0, buf.length);
        
        assertEquals(7, len);
        assertArrayEquals(new char[] {'i', 'n', 'e', '.', '\n', 0, 0, 0, 0, 0}, buf);
        
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }
    
    @Test
    public void testReadBufferWithEmptyReader() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        
        char[] buf = new char[10];
        int len = extendedBufferedReader.read(buf, 0, buf.length);
        
        assertEquals(-1, len);
        assertArrayEquals(new char[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, buf);
        
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }
}