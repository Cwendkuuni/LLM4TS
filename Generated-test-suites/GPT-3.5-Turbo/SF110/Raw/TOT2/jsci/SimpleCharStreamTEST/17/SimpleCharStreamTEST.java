import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream.ReInit(new java.io.StringReader("test string"));
        assertEquals('t', SimpleCharStream.BeginToken());
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream.ReInit(new java.io.StringReader("test string"));
        assertEquals('e', SimpleCharStream.readChar());
    }
    
    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream.UpdateLineColumn('a');
        assertEquals(1, SimpleCharStream.getColumn());
        assertEquals(1, SimpleCharStream.getLine());
    }

    @Test
    public void testBackup() {
        SimpleCharStream.ReInit(new java.io.StringReader("test string"));
        SimpleCharStream.readChar();
        SimpleCharStream.backup(1);
        assertEquals('t', SimpleCharStream.readChar());
    }

    @Test
    public void testGetBeginColumn() {
        SimpleCharStream.ReInit(new java.io.StringReader("test string"));
        SimpleCharStream.readChar();
        assertEquals(1, SimpleCharStream.getBeginColumn());
    }

    @Test
    public void testReInit() {
        SimpleCharStream.ReInit(new java.io.StringReader("test string"));
        SimpleCharStream.ReInit(new java.io.StringReader("test string again"), 2, 2, 4096);
        assertEquals(2, SimpleCharStream.getLine());
        assertEquals(2, SimpleCharStream.getColumn());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream.ReInit(new java.io.StringReader("test string"));
        SimpleCharStream.BeginToken();
        assertEquals("test", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream.ReInit(new java.io.StringReader("test string"));
        SimpleCharStream.BeginToken();
        assertArrayEquals(new char[] {'e', 's', 't'}, SimpleCharStream.GetSuffix(3));
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream.ReInit(new java.io.StringReader("test string"));
        SimpleCharStream.adjustBeginLineColumn(2, 2);
        assertEquals(2, SimpleCharStream.getLine());
        assertEquals(2, SimpleCharStream.getColumn());
    }

}