import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testDefaultValueInConstructor() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0.0, instance.get(0), 0.0);
    }

    @Test
    public void testValueMethod() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 2.0);
        assertEquals(2.0, instance.value(0), 0.0);
    }

    // Additional test cases for other methods can be added here

    @Test
    public void testNoAttributesIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.noAttributes() >= 0);
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.removeAttribute(0);
        assertEquals(1, instance.size());
        assertNull(instance.get(0));
    }

    // Complete the test suite with more test cases

}