import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

public class SimpleCharStreamTest {

    private SimpleCharStream charStream;

    @Before
    public void setup() {
        Reader reader = new StringReader("test string");
        charStream = new SimpleCharStream(reader, 1, 1, 4096);
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertEquals('t', c);
        int line = charStream.getBeginLine();
        int column = charStream.getBeginColumn();
        assertEquals(1, line);
        assertEquals(1, column);
        
        c = charStream.readChar();
        assertEquals('e', c);
    }

    @Test
    public void testReadChar() throws IOException {
        char c = charStream.readChar();
        assertEquals('t', c);
        int line = charStream.getLine();
        int column = charStream.getColumn();
        assertEquals(1, line);
        assertEquals(2, column);
    }

    @Test
    public void testGetColumn() {
        int column = charStream.getColumn();
        assertEquals(0, column);
    }

    @Test
    public void testGetLine() {
        int line = charStream.getLine();
        assertEquals(1, line);
    }

    @Test
    public void testGetEndColumn() {
        int endColumn = charStream.getEndColumn();
        assertEquals(0, endColumn);
    }

    @Test
    public void testGetEndLine() {
        int endLine = charStream.getEndLine();
        assertEquals(1, endLine);
    }

    @Test
    public void testGetBeginColumn() {
        int beginColumn = charStream.getBeginColumn();
        assertEquals(0, beginColumn);
    }

    @Test
    public void testGetBeginLine() {
        int beginLine = charStream.getBeginLine();
        assertEquals(1, beginLine);
    }

    @Test
    public void testBackup() {
        charStream.backup(2);
        int column = charStream.getColumn();
        assertEquals(-2, column);

        int line = charStream.getLine();
        assertEquals(1, line);
    }

    @Test
    public void testReInitWithReader() {
        Reader reader = new StringReader("another string");
        charStream.ReInit(reader, 2, 2, 4096);

        int line = charStream.getLine();
        int column = charStream.getColumn();
        assertEquals(2, line);
        assertEquals(1, column);
    }

    @Test
    public void testReInitWithInputStream() {
        Reader reader = new StringReader("another string");
        charStream.ReInit(reader, 2, 2, 4096);

        int line = charStream.getLine();
        int column = charStream.getColumn();
        assertEquals(2, line);
        assertEquals(1, column);
    }

    @Test
    public void testDone() {
        SimpleCharStream.Done();
        assertNull(charStream.buffer);
        assertNull(charStream.bufline);
        assertNull(charStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream.adjustBeginLineColumn(3, 4);
        int beginLine = charStream.getBeginLine();
        int beginColumn = charStream.getBeginColumn();
        assertEquals(3, beginLine);
        assertEquals(4, beginColumn);
    }
}