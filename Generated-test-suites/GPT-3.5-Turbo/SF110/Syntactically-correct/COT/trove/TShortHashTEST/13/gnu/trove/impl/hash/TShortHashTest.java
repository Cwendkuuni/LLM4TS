import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.contains((short) 5));
        assertFalse(tShortHash.contains((short) 10));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 5);
        tShortHash.insertKey((short) 10);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value > 0; // any condition
            }
        };
        assertTrue(tShortHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 5);
        tShortHash.removeAt(0);
        assertFalse(tShortHash.contains((short) 5));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        int index = tShortHash.insertKey((short) 5);
        assertTrue(index >= 0);
        index = tShortHash.insertKey((short) 5);
        assertTrue(index < 0);
    }
}