import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class SimpleCharStreamTest {

    private SimpleCharStream simpleCharStream;
    private Reader reader;
    
    @Before
    public void setup() {
        reader = new StringReader("test string");
        simpleCharStream = new SimpleCharStream(reader);
    }
    
    @Test
    public void testBeginToken() throws IOException {
        char c = simpleCharStream.BeginToken();
        assertEquals('t', c);
        assertEquals(1, SimpleCharStream.getBeginLine());
        assertEquals(1, SimpleCharStream.getBeginColumn());
        
        c = simpleCharStream.readChar();
        assertEquals('e', c);
        assertEquals(1, SimpleCharStream.getBeginLine());
        assertEquals(2, SimpleCharStream.getBeginColumn());
    }
    
    @Test
    public void testReadChar() throws IOException {
        char c = simpleCharStream.readChar();
        assertEquals('t', c);
        assertEquals(1, SimpleCharStream.getLine());
        assertEquals(2, SimpleCharStream.getColumn());
        
        c = simpleCharStream.readChar();
        assertEquals('e', c);
        assertEquals(1, SimpleCharStream.getLine());
        assertEquals(3, SimpleCharStream.getColumn());
    }
    
    @Test
    public void testGetColumn() throws IOException {
        int column = SimpleCharStream.getColumn();
        assertEquals(1, column);
        
        simpleCharStream.readChar();
        column = SimpleCharStream.getColumn();
        assertEquals(2, column);
    }
    
    @Test
    public void testGetLine() throws IOException {
        int line = SimpleCharStream.getLine();
        assertEquals(1, line);
        
        simpleCharStream.readChar();
        line = SimpleCharStream.getLine();
        assertEquals(1, line);
    }
    
    // Add more test methods for the remaining methods of SimpleCharStream
    
}