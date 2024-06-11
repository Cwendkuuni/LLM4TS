import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceConstructor() {
        SparseInstance instance = new SparseInstance(3, 1.5, "class");
        assertNotNull(instance);
    }

    // Add more test cases for other methods as needed

    @Test
    public void testInitiate() {
        SparseInstance instance = new SparseInstance();
        instance.initiate(new double[]{1.0, 2.0, 0.0});
        assertEquals(2, instance.size());
    }

    @Test
    public void testNoAttributesForEmptyData() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testNoAttributesForNonEmptyData() {
        SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0});
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0});
        instance.removeAttribute(0);
        assertEquals(1, instance.size());
    }

    // Add more test cases for other methods as needed

}