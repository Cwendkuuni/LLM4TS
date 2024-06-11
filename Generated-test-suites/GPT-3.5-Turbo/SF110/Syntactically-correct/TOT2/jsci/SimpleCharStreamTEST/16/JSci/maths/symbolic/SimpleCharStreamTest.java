package JSci.maths.symbolic;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        // Test with wrapAround as true
        SimpleCharStream.ExpandBuff(true);
        // Assert that buffer size has increased by 2048
        assertEquals(2048, SimpleCharStream.bufsize);

        // Test with wrapAround as false
        SimpleCharStream.ExpandBuff(false);
        // Assert that buffer size has increased by 2048
        assertEquals(4096, SimpleCharStream.bufsize);
    }
    
    @Test(expected = IOException.class)
    public void testFillBuffIOException() throws IOException {
        SimpleCharStream.FillBuff();
    }
    
    @Test
    public void testBeginToken() throws IOException {
        assertEquals('a', SimpleCharStream.BeginToken());
    }
    
    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream.UpdateLineColumn('\n');
        assertEquals(2, SimpleCharStream.line);
    }
    
    @Test
    public void testReadChar() throws IOException {
        assertEquals('a', SimpleCharStream.readChar());
    }
    
    @Test
    public void testGetColumn() {
        assertEquals(4, SimpleCharStream.getColumn());
    }

    @Test
    public void testGetLine() {
        assertEquals(1, SimpleCharStream.getLine());
    }

    @Test
    public void testGetEndColumn() {
        assertEquals(4, SimpleCharStream.getEndColumn());
    }

    @Test
    public void testGetEndLine() {
        assertEquals(1, SimpleCharStream.getEndLine());
    }

    @Test
    public void testGetBeginColumn() {
        assertEquals(1, SimpleCharStream.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() {
        assertEquals(1, SimpleCharStream.getBeginLine());
    }

    @Test
    public void testBackup() {
        SimpleCharStream.backup(2);
        assertEquals(4094, SimpleCharStream.bufpos);
    }

    @Test
    public void testReInitReader() {
        SimpleCharStream.ReInit(new StringReader("Test"), 1, 1, 4096);
        assertEquals(4096, SimpleCharStream.bufsize);
    }

    @Test
    public void testReInitInputStream() {
        SimpleCharStream.ReInit(new ByteArrayInputStream("Test".getBytes()), 1, 1, 4096);
        assertEquals(4096, SimpleCharStream.bufsize);
    }

    @Test
    public void testGetImage() {
        String expected = "Test";
        SimpleCharStream.bufpos = 3;
        SimpleCharStream.tokenBegin = 0;
        assertEquals(expected, SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() {
        char[] expected = {'T', 'e', 's', 't'};
        SimpleCharStream.bufpos = 0;
        assertArrayEquals(expected, SimpleCharStream.GetSuffix(4));
    }
    
    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream.adjustBeginLineColumn(2, 3);
        assertEquals(2, SimpleCharStream.line);
        assertEquals(3, SimpleCharStream.column);
    }

}