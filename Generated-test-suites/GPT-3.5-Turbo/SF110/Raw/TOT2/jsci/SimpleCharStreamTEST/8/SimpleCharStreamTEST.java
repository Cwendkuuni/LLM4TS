package JSci.maths.symbolic;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {
    
    @Test
    public void testBeginToken() {
        // Typical case
        assertEquals('A', SimpleCharStream.BeginToken());
        // Edge case
        try {
            SimpleCharStream.ExpandBuff(true);
            SimpleCharStream.ExpandBuff(false);
            SimpleCharStream.BeginToken();
        } catch (Exception e) {
            fail("Exception not expected here: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadChar() {
        // Typical case
        assertEquals('B', SimpleCharStream.readChar());
        // Edge case
        try {
            SimpleCharStream.FillBuff();
            SimpleCharStream.ReadChar();
        } catch (Exception e) {
            fail("Exception not expected here: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetColumn() {
        assertEquals(1, SimpleCharStream.getColumn());
    }
    
    @Test
    public void testGetLine() {
        assertEquals(1, SimpleCharStream.getLine());
    }
    
    @Test
    public void testGetEndColumn() {
        assertEquals(1, SimpleCharStream.getEndColumn());
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
        assertEquals(0, SimpleCharStream.getBeginLine());
    }
    
    @Test
    public void testBackup() {
        SimpleCharStream.backup(1);
        assertEquals(1, SimpleCharStream.inBuf);
    }
    
    // Add more test cases for other public methods
    
}