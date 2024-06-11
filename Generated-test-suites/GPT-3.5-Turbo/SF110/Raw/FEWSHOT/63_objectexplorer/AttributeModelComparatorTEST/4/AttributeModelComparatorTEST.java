import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributeModelComparatorTest {
    AttributeModelComparator comparator = new AttributeModelComparator();

    @Test
    public void testCompare() {
        // Test case 1: both o1 and o2 are null
        int result1 = comparator.compare(null, null);
        assertEquals(0, result1);

        // Test case 2: o1 is null and o2 is not null
        int result2 = comparator.compare(null, new AttributeModel("b"));
        assertEquals(-1, result2);

        // Test case 3: o1 is not null and o2 is null
        int result3 = comparator.compare(new AttributeModel("b"), null);
        assertEquals(1, result3);

        // Test case 4: o1 and o2 are not null and have the same name
        int result4 = comparator.compare(new AttributeModel("b"), new AttributeModel("b"));
        assertEquals(0, result4);

        // Test case 5: o1 and o2 are not null and o1 < o2
        int result5 = comparator.compare(new AttributeModel("a"), new AttributeModel("b"));
        assertEquals(-1, result5);

        // Test case 6: o1 and o2 are not null and o1 > o2
        int result6 = comparator.compare(new AttributeModel("b"), new AttributeModel("a"));
        assertEquals(1, result6);
    }
}