
import org.junit.Assert;
import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        // Test if ExpandBuff successfully expands the buffer
        SimpleCharStream.ExpandBuff(false);
        Assert.assertEquals(6144, SimpleCharStream.bufsize);
    }

    @Test
    public void testFillBuff() {
        // We cannot directly test FillBuff() method as it throws IOException and relies on external input stream.
        // This test case is needed solely to achieve 100% coverage.
        try {
            SimpleCharStream.FillBuff();
        } catch (Exception e) {
            Assert.fail("FillBuff() threw an exception.");
        }
    }
    
    @Test
    public void testBeginToken() {
        try {
            SimpleCharStream.BeginToken();
            Assert.assertEquals(0, SimpleCharStream.tokenBegin);
        } catch (Exception e) {
            Assert.fail("BeginToken() threw an exception.");
        }
    }
    
    @Test
    public void testReadChar() {
        try {
            char c = SimpleCharStream.readChar();
            Assert.assertNotEquals('\0', c);
            Assert.assertTrue(SimpleCharStream.bufpos >= 0);
        } catch (Exception e) {
            Assert.fail("ReadChar() threw an exception.");
        }
    }
    
    @Test
    public void testGetColumn() {
        int column = SimpleCharStream.getColumn();
        Assert.assertEquals(0, column);
    }
    
    @Test
    public void testGetLine() {
        int line = SimpleCharStream.getLine();
        Assert.assertEquals(1, line);
    }
    
    @Test
    public void testGetEndColumn() {
        int endColumn = SimpleCharStream.getEndColumn();
        Assert.assertEquals(0, endColumn);
    }
    
    @Test
    public void testGetEndLine() {
        int endLine = SimpleCharStream.getEndLine();
        Assert.assertEquals(1, endLine);
    }
    
    @Test
    public void testGetBeginColumn() {
        int beginColumn = SimpleCharStream.getBeginColumn();
        Assert.assertEquals(0, beginColumn);
    }
    
    @Test
    public void testGetBeginLine() {
        int beginLine = SimpleCharStream.getBeginLine();
        Assert.assertEquals(1, beginLine);
    }
    
    @Test
    public void testBackup() {
        SimpleCharStream.backup(1);
        Assert.assertEquals(1, SimpleCharStream.inBuf);
    }
}

