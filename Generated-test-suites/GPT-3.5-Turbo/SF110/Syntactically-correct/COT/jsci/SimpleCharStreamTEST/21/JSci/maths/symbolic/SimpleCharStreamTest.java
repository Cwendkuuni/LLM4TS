
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        StringReader reader = new StringReader("test");
        SimpleCharStream.ReInit(reader);
        char c = SimpleCharStream.BeginToken();
        assertEquals(c, 't');
    }

    @Test
    public void testReadChar() throws IOException {
        StringReader reader = new StringReader("test");
        SimpleCharStream.ReInit(reader);
        char c = SimpleCharStream.readChar();
        assertEquals(c, 't');
    }

    @Test
    public void testGetColumn() throws IOException {
        StringReader reader = new StringReader("test");
        SimpleCharStream.ReInit(reader);
        int column = SimpleCharStream.getColumn();
        assertEquals(column, 1);
    }

    @Test
    public void testGetLine() throws IOException {
        StringReader reader = new StringReader("test");
        SimpleCharStream.ReInit(reader);
        int line = SimpleCharStream.getLine();
        assertEquals(line, 1);
    }

    @Test
    public void testGetEndColumn() throws IOException {
        StringReader reader = new StringReader("test");
        SimpleCharStream.ReInit(reader);
        int endColumn = SimpleCharStream.getEndColumn();
        assertEquals(endColumn, 1);
    }

    @Test
    public void testGetEndLine() throws IOException {
        StringReader reader = new StringReader("test");
        SimpleCharStream.ReInit(reader);
        int endLine = SimpleCharStream.getEndLine();
        assertEquals(endLine, 1);
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        StringReader reader = new StringReader("test");
        SimpleCharStream.ReInit(reader);
        int beginColumn = SimpleCharStream.getBeginColumn();
        assertEquals(beginColumn, 0);
    }

    @Test
    public void testGetBeginLine() throws IOException {
        StringReader reader = new StringReader("test");
        SimpleCharStream.ReInit(reader);
        int beginLine = SimpleCharStream.getBeginLine();
        assertEquals(beginLine, 1);
    }

    @Test
    public void testBackup() throws IOException {
        StringReader reader = new StringReader("test");
        SimpleCharStream.ReInit(reader);
        SimpleCharStream.backup(1);
        char c = SimpleCharStream.readChar();
        assertEquals(c, 't');
    }

}
