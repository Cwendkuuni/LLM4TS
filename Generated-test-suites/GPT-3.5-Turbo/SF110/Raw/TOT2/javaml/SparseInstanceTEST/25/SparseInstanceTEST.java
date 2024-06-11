import static org.junit.Assert.*;
import java.util.HashMap;
import org.junit.Test;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructor() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0});
        assertEquals(2.0, instance.value(1), 0.0);
    }

    // Add more test cases for other public methods here
    
    // Edge cases test
    @Test
    public void testEmptyData() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0});
        instance.removeAttribute(0);
        assertEquals(1, instance.noAttributes());
    }

    // Exception handling test
    @Test
    public void testRemoveAttributeWithInvalidIndex() {
        SparseInstance instance = new SparseInstance(new double[] {1.0, 2.0});
        try {
            instance.removeAttribute(5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Pass
        }
    }
    
}

