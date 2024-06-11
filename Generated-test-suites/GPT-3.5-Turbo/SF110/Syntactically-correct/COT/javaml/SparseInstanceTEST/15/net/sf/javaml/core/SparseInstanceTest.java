
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes);
    }

    @Test
    public void testDefaultSparseInstance() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.data.isEmpty());
        assertEquals(-1, instance.noAttributes);
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertTrue(instance.data.isEmpty());
        assertEquals(5, instance.noAttributes);
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.5);
        assertTrue(instance.data.isEmpty());
        assertEquals(5, instance.noAttributes);
        assertEquals(0.5, instance.defaultValue, 0.0001);
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        SparseInstance instance = new SparseInstance(5, "class1");
        assertTrue(instance.data.isEmpty());
        assertEquals(5, instance.noAttributes);
        assertEquals("class1", instance.classValue());
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(5, instance.noAttributes);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(2.0, instance.get(1), 0.0001);
        assertEquals(3.0, instance.get(2), 0.0001);
        assertEquals(4.0, instance.get(3), 0.0001);
        assertEquals(5.0, instance.get(4), 0.0001);
    }

    @Test
    public void testSparseInstanceWithDataVectorAndDefaultValue() {
        double[] datavector = {1.0, 0.0, 0.0, 4.0, 5.0};
        SparseInstance instance = new SparseInstance(datavector, 0.5);
        assertEquals(5, instance.noAttributes);
        assertEquals(1.0, instance.get(0), 0.0001);
        assertEquals(0.5, instance.get(1), 0.0001);
        assertEquals(0.5, instance.get(2), 0.0001);
        assertEquals(4.0, instance.get(3), 0.0001);
        assertEquals(5.0, instance.get(4), 0.0001);
    }

    // ... All other methods testing

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
        instance.initiate(datavector);
        instance.removeAttributes(new HashSet<>(Arrays.asList(0, 2, 4)));
        assertEquals(2, instance.data.size());
        assertTrue(instance.data.containsKey(1));
        assertTrue(instance.data.containsKey(3));
    }

}

