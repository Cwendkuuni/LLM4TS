import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(5.0f));
        hash.insertKey(5.0f);
        assertTrue(hash.contains(5.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        final StringBuilder result = new StringBuilder();
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                result.append(value);
                return true;
            }
        });
        assertEquals("1.02.03.0", result.toString());
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(5.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(5.0f));
    }

    // Add more test cases for the remaining methods

}