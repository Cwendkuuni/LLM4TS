
import org.junit.Test;
import java.util.Comparator;

import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator attributeModelComparator = new AttributeModelComparator();

    @Test
    public void testCompare() {
        AttributeModel attributeModel1 = new AttributeModel("abc");
        AttributeModel attributeModel2 = new AttributeModel("def");

        // Test when o1 < o2
        int result1 = attributeModelComparator.compare(attributeModel1, attributeModel2);
        assertTrue(result1 < 0);

        // Test when o1 > o2
        int result2 = attributeModelComparator.compare(attributeModel2, attributeModel1);
        assertTrue(result2 > 0);

        // Test when o1 = o2
        int result3 = attributeModelComparator.compare(attributeModel1, attributeModel1);
        assertTrue(result3 == 0);
    }

    // Additional test cases for edge cases and exception handling scenarios
    @Test
    public void testCompareWithNullObjects() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;

        // Test when both objects are null
        int result1 = attributeModelComparator.compare(attributeModel1, attributeModel2);
        assertTrue(result1 == 0);

        // Test when o1 is null and o2 is not null
        attributeModel2 = new AttributeModel("abc");
        int result2 = attributeModelComparator.compare(attributeModel1, attributeModel2);
        assertTrue(result2 < 0);

        // Test when o1 is not null and o2 is null
        attributeModel1 = new AttributeModel("abc");
        int result3 = attributeModelComparator.compare(attributeModel1, attributeModel2);
        assertTrue(result3 > 0);
    }
    
    // Additional test cases for comparing names with "a"
    @Test
    public void testCompareWithNameA() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("b");

        // Test when o1 < o2 and o1.getName() is "a"
        int result1 = attributeModelComparator.compare(attributeModel1, attributeModel2);
        assertTrue(result1 > 0);

        // Test when o1 > o2 and o2.getName() is "a"
        int result2 = attributeModelComparator.compare(attributeModel2, attributeModel1);
        assertTrue(result2 < 0);

        // Test when o1 = o2 and both names are "a"
        AttributeModel attributeModel3 = new AttributeModel("a");
        int result3 = attributeModelComparator.compare(attributeModel1, attributeModel3);
        assertTrue(result3 == 0);
    }
}
