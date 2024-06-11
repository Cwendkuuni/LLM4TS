import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, ' ');
        tCharHash.insertKey('A');
        assertTrue(tCharHash.contains('A'));
        assertFalse(tCharHash.contains('B'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, ' ');
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        StringBuffer stringBuffer = new StringBuffer();
        tCharHash.forEach(new TCharProcedure() {
            public boolean execute(char value) {
                stringBuffer.append(value);
                return true;
            }
        });
        assertEquals("AB", stringBuffer.toString());
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, ' ');
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('A'));
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, ' ');
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        assertEquals(0, tCharHash.index('A'));
        assertEquals(1, tCharHash.index('B'));
        assertEquals(-1, tCharHash.index('C'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, ' ');
        tCharHash.insertKey('A');
        tCharHash.insertKey('B');
        tCharHash.insertKey('C');
        assertEquals(0, tCharHash.indexRehashed('A', 0, 0, (byte)1));
        assertEquals(1, tCharHash.indexRehashed('B', 1, 1, (byte)1));
        assertEquals(-1, tCharHash.indexRehashed('D', 2, 2, (byte)1));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, ' ');
        assertEquals(0, tCharHash.insertKey('A'));
        assertEquals(1, tCharHash.insertKey('B'));
        assertEquals(-2, tCharHash.insertKey('A'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, ' ');
        assertEquals(0, tCharHash.insertKeyRehash('A', 0, 0, (byte)1));
        assertEquals(1, tCharHash.insertKeyRehash('B', 1, 1, (byte)1));
        assertEquals(2, tCharHash.insertKeyRehash('C', 2, 2, (byte)1));
        assertEquals(-3, tCharHash.insertKeyRehash('A', 3, 3, (byte)1));
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash(10, 0.75f, ' ');
        tCharHash.insertKeyAt(0, 'A');
        tCharHash.insertKeyAt(1, 'B');
        assertTrue(tCharHash.contains('A'));
        assertTrue(tCharHash.contains('B'));
    }
}