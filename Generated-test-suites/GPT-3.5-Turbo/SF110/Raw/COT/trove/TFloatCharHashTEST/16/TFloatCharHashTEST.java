import org.junit.*;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');
        hash.put(3.0f, 'c');
        StringBuilder builder = new StringBuilder();
        hash.forEach((float key, char value) -> {
            builder.append(key);
            builder.append(value);
            return true;
        });
        assertEquals("1.02.03.0abc", builder.toString());
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    // Additional test cases for edge cases and exceptions can be added here

}