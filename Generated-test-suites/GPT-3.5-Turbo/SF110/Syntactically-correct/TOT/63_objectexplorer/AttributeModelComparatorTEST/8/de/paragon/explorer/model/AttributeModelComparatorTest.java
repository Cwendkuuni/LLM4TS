import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareMethodWithBothNullAttributes() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompareMethodWithNullFirstAttribute() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel2 = new AttributeModel("b");
        int result = comparator.compare(null, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareMethodWithNullSecondAttribute() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a");
        int result = comparator.compare(attributeModel1, null);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareMethodWithEqualAttributes() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareMethodWithDifferentAttributes() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("b");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareMethodWithIgnoreCaseComparison() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("A");
        AttributeModel attributeModel2 = new AttributeModel("b");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareMethodWithEdgeCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("A");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }
}