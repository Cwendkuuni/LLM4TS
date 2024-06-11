import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        Reader reader = new StringReader("test string");
        SimpleCharStream.ReInit(reader);
        char c = SimpleCharStream.BeginToken();
        assertEquals('t', c);
    }
    
    @Test
    public void testReadChar() throws IOException {
        Reader reader = new StringReader("test string");
        SimpleCharStream.ReInit(reader);
        char c = SimpleCharStream.readChar();
        assertEquals('t', c);
    }
    
    @Test
    public void testGetColumn() throws IOException {
        Reader reader = new StringReader("test string");
        SimpleCharStream.ReInit(reader);
        SimpleCharStream.readChar();
        int column = SimpleCharStream.getColumn();
        assertEquals(1, column);
    }
    
    @Test
    public void testGetLine() throws IOException {
        Reader reader = new StringReader("test\nstring");
        SimpleCharStream.ReInit(reader);
        SimpleCharStream.readChar();
        int line = SimpleCharStream.getLine();
        assertEquals(1, line);
    }
    
    @Test
    public void testGetEndColumn() throws IOException {
        Reader reader = new StringReader("test string");
        SimpleCharStream.ReInit(reader);
        SimpleCharStream.readChar();
        int endColumn = SimpleCharStream.getEndColumn();
        assertEquals(1, endColumn);
    }
    
    @Test
    public void testGetEndLine() throws IOException {
        Reader reader = new StringReader("test\nstring");
        SimpleCharStream.ReInit(reader);
        SimpleCharStream.readChar();
        int endLine = SimpleCharStream.getEndLine();
        assertEquals(1, endLine);
    }
    
    @Test
    public void testGetBeginColumn() throws IOException {
        Reader reader = new StringReader("test string");
        SimpleCharStream.ReInit(reader);
        SimpleCharStream.readChar();
        int beginColumn = SimpleCharStream.getBeginColumn();
        assertEquals(1, beginColumn);
    }
    
    @Test
    public void testGetBeginLine() throws IOException {
        Reader reader = new StringReader("test\nstring");
        SimpleCharStream.ReInit(reader);
        SimpleCharStream.readChar();
        int beginLine = SimpleCharStream.getBeginLine();
        assertEquals(1, beginLine);
    }
    
    @Test
    public void testBackup() throws IOException {
        Reader reader = new StringReader("test string");
        SimpleCharStream.ReInit(reader);
        SimpleCharStream.readChar();
        SimpleCharStream.backup(1);
        int column = SimpleCharStream.getColumn();
        assertEquals(0, column);
    }
}