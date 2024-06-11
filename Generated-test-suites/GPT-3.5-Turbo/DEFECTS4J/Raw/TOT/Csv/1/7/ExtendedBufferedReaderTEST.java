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
        Reader stringReader = new StringReader("Hello\nWorld\n");
        reader = new ExtendedBufferedReader(stringReader);
    }
    
    @Test
    public void testRead() throws IOException {
        int ch = reader.read();
        assertEquals('H', ch);
        assertEquals(1, reader.getLineNumber());
        
        ch = reader.read();
        assertEquals('e', ch);
        
        ch = reader.read();
        assertEquals('l', ch);
        
        ch = reader.read();
        assertEquals('l', ch);
        
        ch = reader.read();
        assertEquals('o', ch);
        assertEquals(1, reader.getLineNumber());
        
        // Test end of line increment and lineCounter
        reader.read();
        reader.read();
        ch = reader.read();
        assertEquals('W', ch);
        assertEquals(2, reader.getLineNumber());
    }
    
    @Test
    public void testReadAgain() throws IOException {
        int ch = reader.read();
        assertEquals('H', ch);
        
        ch = reader.readAgain();
        assertEquals('H', ch);
        
        ch = reader.read();
        assertEquals('e', ch);
        
        ch = reader.readAgain();
        assertEquals('e', ch);
        
        reader.read();
        reader.read();
        ch = reader.read();
        assertEquals('W', ch);
        
        ch = reader.readAgain();
        assertEquals('W', ch);
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        char[] buffer = new char[5];
        int bytesRead = reader.read(buffer, 0, 5);
        
        assertEquals(5, bytesRead);
        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('l', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('o', buffer[4]);
    }
    
    @Test
    public void testReadLine() throws IOException {
        String line = reader.readLine();
        assertEquals("Hello", line);
        assertEquals(1, reader.getLineNumber());
        
        line = reader.readLine();
        assertEquals("World", line);
        assertEquals(2, reader.getLineNumber());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        int ch = reader.lookAhead();
        assertEquals('H', ch);
        assertEquals('H', reader.readAgain());
        
        ch = reader.lookAhead();
        assertEquals('H', ch);
        assertEquals('H', reader.readAgain());
        
        reader.read();
        reader.read();
        ch = reader.lookAhead();
        assertEquals('W', ch);
        assertEquals('W', reader.readAgain());
        
        reader.read();
        ch = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, ch);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }
}