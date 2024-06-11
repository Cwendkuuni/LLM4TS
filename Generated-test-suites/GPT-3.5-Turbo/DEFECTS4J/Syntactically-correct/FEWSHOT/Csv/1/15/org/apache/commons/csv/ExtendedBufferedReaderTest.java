
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {
    private ExtendedBufferedReader reader;
    
    @Before
    public void setUp() {
        StringReader stringReader = new StringReader("Hello\nWorld");
        reader = new ExtendedBufferedReader(stringReader);
    }
    
    @Test
    public void testRead() throws IOException {
        assertEquals((int) 'H', reader.read());
        assertEquals(1, reader.getLineNumber());
        
        assertEquals((int) 'e', reader.read());
        assertEquals(1, reader.getLineNumber());
    }
    
    @Test
    public void testReadAgain() throws IOException {
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
        
        reader.read();
        assertEquals((int) 'H', reader.readAgain());
    }
    
    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[5];
        assertEquals(5, reader.read(buf, 0, 5));
        assertArrayEquals(new char[] {'H', 'e', 'l', 'l', 'o'}, buf);
        assertEquals(1, reader.getLineNumber());
    }
    
    @Test
    public void testReadLine() throws IOException {
        assertEquals("Hello", reader.readLine());
        assertEquals(2, reader.getLineNumber());
        
        assertNull(reader.readLine());
        assertEquals(-1, reader.readAgain());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        assertEquals((int) 'H', reader.lookAhead());
        assertEquals(0, reader.getLineNumber());
    }
    
    @Test
    public void testGetLineNumber() {
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
    }
}
