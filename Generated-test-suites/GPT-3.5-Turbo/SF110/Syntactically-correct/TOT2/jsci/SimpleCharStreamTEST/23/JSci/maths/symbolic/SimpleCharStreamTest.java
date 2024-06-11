package JSci.maths.symbolic;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class SimpleCharStreamTest {

    private SimpleCharStream simpleCharStream;

    @Before
    public void setUp() {
        simpleCharStream = new SimpleCharStream(new StringReader("Test"), 1, 1);
    }

    @Test
    public void testBeginToken() throws IOException {
        char result = SimpleCharStream.BeginToken();
        assertEquals('T', result);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream.UpdateLineColumn('T');
        assertEquals(1, SimpleCharStream.line);
        assertEquals(2, SimpleCharStream.column);
    }

    @Test
    public void testReadChar() throws IOException {
        char result = SimpleCharStream.readChar();
        assertEquals('e', result);
    }

    @Test
    public void testGetColumn() {
        int result = SimpleCharStream.getColumn();
        assertEquals(2, result);
    }

    @Test
    public void testGetLine() {
        int result = SimpleCharStream.getLine();
        assertEquals(1, result);
    }

    @Test
    public void testGetEndColumn() {
        int result = SimpleCharStream.getEndColumn();
        assertEquals(2, result);
    }

    @Test
    public void testGetEndLine() {
        int result = SimpleCharStream.getEndLine();
        assertEquals(1, result);
    }

    @Test
    public void testGetBeginColumn() {
        int result = SimpleCharStream.getBeginColumn();
        assertEquals(1, result);
    }

    @Test
    public void testGetBeginLine() {
        int result = SimpleCharStream.getBeginLine();
        assertEquals(1, result);
    }

    @Test
    public void testBackup() {
        SimpleCharStream.backup(1);
        int result = SimpleCharStream.inBuf;
        assertEquals(1, result);
    }

    @Test
    public void testGetImage() {
        String result = SimpleCharStream.GetImage();
        assertEquals("T", result);
    }

    @Test
    public void testGetSuffix() {
        char[] result = SimpleCharStream.GetSuffix(2);
        assertArrayEquals(new char[]{'e', 's'}, result);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream.adjustBeginLineColumn(2, 3);
        assertEquals(2, SimpleCharStream.line);
        assertEquals(3, SimpleCharStream.column);
    }
}