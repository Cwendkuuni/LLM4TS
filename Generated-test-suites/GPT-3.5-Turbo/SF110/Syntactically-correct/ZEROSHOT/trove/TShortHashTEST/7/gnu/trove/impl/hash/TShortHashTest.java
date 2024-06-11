import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash t = new TShortHash();
        assertTrue(t.getNoEntryValue() == 0);
    }

    @Test
    public void testSetUp() {
        TShortHash t = new TShortHash();
        assertTrue(t.setUp(10) == 10);
    }

    @Test
    public void testContains() {
        TShortHash t = new TShortHash();
        assertTrue(t.contains((short) 1) == false);
    }

    @Test
    public void testForEach() {
        TShortHash t = new TShortHash();
        assertTrue(t.forEach(new TShortProcedure() {
            public boolean execute(short val) {
                return val > 0;
            }
        }) == false);
    }

    @Test
    public void testRemoveAt() {
        TShortHash t = new TShortHash();
        t.removeAt(0);
    }

    @Test
    public void testIndex() {
        TShortHash t = new TShortHash();
        assertTrue(t.index((short) 1) == -1);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash t = new TShortHash();
        assertTrue(t.indexRehashed((short) 1, 0, 0, (byte) 1) == -1);
    }

    @Test
    public void testInsertKey() {
        TShortHash t = new TShortHash();
        assertTrue(t.insertKey((short) 1) == 0);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash t = new TShortHash();
        t.insertKeyRehash((short) 1, 0, 0, (byte) 1);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash t = new TShortHash();
        t.insertKeyAt(0, (short) 1);
    }
}