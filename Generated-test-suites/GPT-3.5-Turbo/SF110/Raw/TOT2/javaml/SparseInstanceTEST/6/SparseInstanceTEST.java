import static org.junit.Assert.*;
import org.junit.Test;

public class SparseInstanceTest {
    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes);
    }

    // Add more test cases for each public method in SparseInstance class...

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.put(3, 3.0);

        instance.removeAttribute(2);

        assertFalse(instance.containsKey(2));
        assertEquals(2, instance.size());
        assertEquals("{1=1.0;null}", instance.toString());
    }

    // Add test cases for edge cases and error scenarios...

}