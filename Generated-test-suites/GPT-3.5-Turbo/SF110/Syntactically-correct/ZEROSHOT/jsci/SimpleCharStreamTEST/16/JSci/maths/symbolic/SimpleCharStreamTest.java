package JSci.maths.symbolic;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {
    
    @Test
    public void testBeginToken() throws Exception {
        SimpleCharStream.BeginToken();
    }
    
    @Test
    public void testReadChar() throws Exception {
        SimpleCharStream.readChar();
    }
    
    @Test
    public void testGetColumn() {
        assertEquals(0, SimpleCharStream.getColumn());
    }
    
    @Test
    public void testGetLine() {
        assertEquals(1, SimpleCharStream.getLine());
    }
    
    @Test
    public void testGetEndColumn() {
        assertEquals(0, SimpleCharStream.getEndColumn());
    }
    
    @Test
    public void testGetEndLine() {
        assertEquals(1, SimpleCharStream.getEndLine());
    }
    
    @Test
    public void testGetBeginColumn() {
        assertEquals(0, SimpleCharStream.getBeginColumn());
    }
    
    @Test
    public void testGetBeginLine() {
        assertEquals(1, SimpleCharStream.getBeginLine());
    }
    
    @Test
    public void testBackup() {
        SimpleCharStream.backup(5);
    }
    
    @Test
    public void testReInit() {
        SimpleCharStream.ReInit(new java.io.StringReader(""), 1, 1, 1024);
    }
    
    @Test
    public void testGetImage() {
        assertEquals("", SimpleCharStream.GetImage());
    }
    
    @Test
    public void testGetSuffix() {
        assertArrayEquals(new char[5], SimpleCharStream.GetSuffix(5));
    }
    
    @Test
    public void testDone() {
        SimpleCharStream.Done();
    }
    
    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream.adjustBeginLineColumn(1, 1);
    }
}