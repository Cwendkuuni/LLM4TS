import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(10);
        assertEquals(10, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructor1() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructor2() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
    }

    // Additional test methods for other constructors will go here

    @Test
    public void testValue() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(2.0, instance.value(1), 0.001);
    }

    // Additional test methods for other public methods will go here

    @Test
    public void testNoAttributesEdgeCases() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());

        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance2 = new SparseInstance(dataVector);
        assertEquals(3, instance2.noAttributes());
    }

    // Additional test methods for edge cases and exception handling will go here

}