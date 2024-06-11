import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_BothAttributesNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstAttributeNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel = new AttributeModel("b");
        int result = comparator.compare(null, attributeModel);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondAttributeNull_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel = new AttributeModel("b");
        int result = comparator.compare(attributeModel, null);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothAttributeNamesNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel(null);
        AttributeModel attributeModel2 = new AttributeModel(null);
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstAttributeNameNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel(null);
        AttributeModel attributeModel2 = new AttributeModel("b");
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondAttributeNameNull_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel(null);
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothAttributeNamesEqualToA_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstAttributeNameEqualToA_SecondAttributeNameNotEqualToA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("b");
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstAttributeNameNotEqualToA_SecondAttributeNameEqualToA_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothAttributeNamesGreaterThanA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel("c");
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_BothAttributeNamesLessThanA_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel("z");
        AttributeModel attributeModel2 = new AttributeModel("y");
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_FirstAttributeNameGreaterThanA_SecondAttributeNameLessThanA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstAttributeNameLessThanA_SecondAttributeNameGreaterThanA_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("b");
        int result = comparator.compare(attributeModel1, attributeModel2);

        assertEquals(1, result);
    }
}