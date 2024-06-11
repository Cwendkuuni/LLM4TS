import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello world");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        int result = br.read();
        
        assertEquals('H', result);
    }
    
    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello world");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        br.read();
        int result = br.readAgain();
        
        assertEquals('H', result);
    }
    
    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        String result = br.readLine();
        
        assertEquals("Hello", result);
    }
    
    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        int result = br.lookAhead();
        
        assertEquals('H', result);
    }
    
    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        
        br.readLine();
        int result = br.getLineNumber();
        
        assertEquals(1, result);
    }

}