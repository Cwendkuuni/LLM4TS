import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SparseInstanceTest {

    SparseInstance sparseInstance;

    @Before
    public void setUp() {
        sparseInstance = new SparseInstance();
    }

    @Test
    public void testSetNoAttributes() {
        sparseInstance.setNoAttributes(5);
        assertEquals(5, sparseInstance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructor1() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructor2() {
        SparseInstance instance = new SparseInstance(3);
        assertEquals(3, instance.noAttributes());
    }

    // Add more test cases for different constructors and methods

    // Testing edge cases and exception handling scenarios

    @Test
    public void testNoAttributesWithEmptyData() {
        assertEquals(0, sparseInstance.noAttributes());
    }

    @Test
    public void testNoAttributesWithNonDefaultValues() {
        double[] datavector = {1.0, 2.0, 0.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, null);
        assertEquals(3, instance.noAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAttributeOutOfBounds() {
        sparseInstance.removeAttribute(-1);
    }

    // Merge all the individual test cases into a complete test file

}