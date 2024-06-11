import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(0.0f));
        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertEquals(1, hash.get(1.0f));
    }

    // Add more test cases for other methods as needed

}