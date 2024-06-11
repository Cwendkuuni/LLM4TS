
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash hash = new MockTShortHash();
        boolean result = hash.contains((short) 5);
        assertFalse(result);
    }

    @Test
    public void testForEach() {
        TShortHash hash = new MockTShortHash();
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                System.out.println(value);
                return true;
            }
        };
        boolean result = hash.forEach(procedure);
        assertTrue(result);
    }

    @Test
    public void testRemoveAt() {
        TShortHash hash = new MockTShortHash();
        hash.removeAt(0);
        boolean result = hash.contains((short) 10);
        assertTrue(result);
    }

    @Test
    public void testIndex() {
        TShortHash hash = new MockTShortHash();
        int index = hash.index((short) 1);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash hash = new MockTShortHash();
        int index = hash.indexRehashed((short) 1, 0, 1, (byte) 1);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKey() {
        TShortHash hash = new MockTShortHash();
        int index = hash.insertKey((short) 5);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new MockTShortHash();
        int index = hash.insertKeyRehash((short) 10, 0, 1, (byte) 1);
        assertEquals(3, index);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new MockTShortHash();
        hash.insertKeyAt(0, (short) 5);
        boolean result = hash.contains((short) 5);
        assertTrue(result);
    }

    private class MockTShortHash extends TShortHash {

        public MockTShortHash() {
            super();
            this._set = new short[]{10, 20, 30};
        }

        public boolean forEach(TShortProcedure procedure) {
            return false;
        }
    }
}

