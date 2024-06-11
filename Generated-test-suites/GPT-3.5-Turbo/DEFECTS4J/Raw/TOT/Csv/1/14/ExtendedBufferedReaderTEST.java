
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;
    
    @Before
    public void setUp() {
        Reader r = new StringReader("Line1\nLine2\rLine3\r\nLine4");
        reader = new ExtendedBufferedReader(r);
    }
    
    @Test
    public void testRead() throws IOException {
        int ch = reader.read();
        assertEquals('L', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('i', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('n', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('e', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('1', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('\n', ch);
        assertEquals(2, reader.getLineNumber());
    }
    
    @Test
    public void testReadAgain() throws IOException {
        int ch = reader.read();
        assertEquals('L', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('i', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('n', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('e', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('1', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.readAgain();
        assertEquals('1', ch);
        assertEquals(1, reader.getLineNumber());
    }
    
    @Test
    public void testReadLine() throws IOException {
        String line = reader.readLine();
        assertEquals("Line1", line);
        assertEquals(1, reader.getLineNumber());
        
        line = reader.readLine();
        assertEquals("Line2", line);
        assertEquals(2, reader.getLineNumber());
        
        line = reader.readLine();
        assertEquals("Line3", line);
        assertEquals(3, reader.getLineNumber());
        
        line = reader.readLine();
        assertEquals("Line4", line);
        assertEquals(4, reader.getLineNumber());
        
        line = reader.readLine();
        assertNull(line);
        assertEquals(-1, reader.readAgain());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        int ch = reader.lookAhead();
        assertEquals('L', ch);
        assertEquals(1, reader.getLineNumber());
        assertEquals('L', reader.readAgain());
        
        ch = reader.lookAhead();
        assertEquals('L', ch);
        assertEquals(1, reader.getLineNumber());
        assertEquals('L', reader.readAgain());
        
        reader.read();
        
        ch = reader.lookAhead();
        assertEquals('i', ch);
        assertEquals(1, reader.getLineNumber());
        assertEquals('i', reader.readAgain());
    }
    
    @Test
    public void testGetLineNumber() {
        int lineNumber = reader.getLineNumber();
        assertEquals(0, lineNumber);
        
        try {
            reader.read();
            lineNumber = reader.getLineNumber();
            assertEquals(1, lineNumber);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
}
