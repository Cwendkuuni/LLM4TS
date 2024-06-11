
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test when both o1 and o2 are null
        int result = comparator.compare(null, null);
        assertEquals(0, result);

        // Test when o1 is null and o2 is not null
        result = comparator.compare(null, new AttributeModel());
        assertTrue(result < 0);

        // Test when o1 is not null and o2 is null
        result = comparator.compare(new AttributeModel(), null);
        assertTrue(result > 0);

        // Test when o1 and o2 have the same names
        AttributeModel o1 = new AttributeModel();
        o1.setName("Test");
        AttributeModel o2 = new AttributeModel();
        o2.setName("Test");
        result = comparator.compare(o1, o2);
        assertEquals(0, result);

        // Test when o1 name is "a" and o2 name is not "a"
        o1.setName("a");
        o2.setName("b");
        result = comparator.compare(o1, o2);
        assertEquals(1, result);

        // Test when o2 name is "a" and o1 name is not "a"
        o1.setName("b");
        o2.setName("a");
        result = comparator.compare(o1, o2);
        assertEquals(-1, result);

        // Test when o1 name is "a" and o2 name is "a" (ignoring case)
        o1.setName("A");
        o2.setName("a");
        result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
}