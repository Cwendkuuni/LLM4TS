
import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        assertFalse(tShortHash.contains((short) 0));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        assertTrue(tShortHash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return false;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash._set = new short[] {1, 2, 3};
        tShortHash._states = new byte[] {1, 1, 1};
        tShortHash.removeAt(1);
        assertEquals(0, tShortHash._set[1]);
        assertEquals(0, tShortHash._states[1]);
    }

    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        tShortHash._set = new short[] {1, 2, 3};
        tShortHash._states = new byte[] {1, 1, 1};
        assertEquals(1, tShortHash.index((short) 2));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        tShortHash._set = new short[] {1, 2, 3};
        tShortHash._states = new byte[] {1, 1, 1};
        tShortHash.insertKey((short) 4);
        assertEquals(4, tShortHash._set[3]);
        assertEquals(1, tShortHash._states[3]);
    }
}
