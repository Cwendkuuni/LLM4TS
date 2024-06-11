import static org.junit.Assert.*;
import org.junit.*;
import java.util.Arrays;
import gnu.trove.procedure.*;

public class TCharHashTest {

    @Test
    public void testContains_True() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)65);
        assertTrue(hash.contains((char)65));
    }

    @Test
    public void testContains_False() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains((char)65));
    }

    @Test
    public void testForEach_True() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)65);
        assertTrue(hash.forEach(new TCharProcedure() {
            public boolean execute(char val) {
                return true;
            }
        }));
    }

    @Test
    public void testForEach_False() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)65);
        assertFalse(hash.forEach(new TCharProcedure() {
            public boolean execute(char val) {
                return false;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)65);
        hash.removeAt(0);
        assertFalse(hash.contains((char)65));
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)65);
        assertEquals(0, hash.index((char)65));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)65);
        assertEquals(0, hash.indexRehashed((char)65, 0, hash.hash((char)65) & Integer.MAX_VALUE, hash.getState(0)));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.insertKey((char)65));
        assertTrue(hash.contains((char)65));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        assertEquals(0, hash.insertKeyRehash((char)65, 0, hash.hash((char)65) & Integer.MAX_VALUE, hash.getState(0)));
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, (char)65);
        assertTrue(hash.contains((char)65));
    }
}