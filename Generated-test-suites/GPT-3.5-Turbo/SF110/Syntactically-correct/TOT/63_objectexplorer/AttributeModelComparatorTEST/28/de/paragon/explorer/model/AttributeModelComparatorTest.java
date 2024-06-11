import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_BothAttributesNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(-1, comparator.compare(null, null));
    }

    @Test
    public void testCompare_FirstAttributeNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute2 = new AttributeModel("Test Attribute");
        assertEquals(-1, comparator.compare(null, attribute2));
    }

    @Test
    public void testCompare_SecondAttributeNull_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("Test Attribute");
        assertEquals(1, comparator.compare(attribute1, null));
    }

    @Test
    public void testCompare_BothAttributesNotNullAndEqual_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("Test Attribute");
        AttributeModel attribute2 = new AttributeModel("Test Attribute");
        assertEquals(0, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompare_BothAttributesNotNullAndFirstAttributeLowerThanA_ReturnsOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("a_test");
        AttributeModel attribute2 = new AttributeModel("Test Attribute");
        assertEquals(1, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompare_BothAttributesNotNullAndFirstAttributeGreaterThanA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("x_test");
        AttributeModel attribute2 = new AttributeModel("Test Attribute");
        assertEquals(-1, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompare_BothAttributesNotNullAndFirstAttributeEqualToA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("a_test");
        AttributeModel attribute2 = new AttributeModel("Test Attribute");
        assertEquals(-1, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompare_BothAttributesNotNullAndSecondAttributeLowerThanA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("Test Attribute");
        AttributeModel attribute2 = new AttributeModel("b_test");
        assertEquals(-1, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompare_BothAttributesNotNullAndSecondAttributeGreaterThanA_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("Test Attribute");
        AttributeModel attribute2 = new AttributeModel("x_test");
        assertEquals(1, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompare_BothAttributesNotNullAndSecondAttributeEqualToA_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("Test Attribute");
        AttributeModel attribute2 = new AttributeModel("a_test");
        assertEquals(1, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompare_FirstAttributeHasNullName_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel(null);
        AttributeModel attribute2 = new AttributeModel("Test Attribute");
        assertEquals(-1, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompare_SecondAttributeHasNullName_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel("Test Attribute");
        AttributeModel attribute2 = new AttributeModel(null);
        assertEquals(1, comparator.compare(attribute1, attribute2));
    }

    @Test
    public void testCompare_BothAttributesHaveNullNames_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attribute1 = new AttributeModel(null);
        AttributeModel attribute2 = new AttributeModel(null);
        assertEquals(-1, comparator.compare(attribute1, attribute2));
    }

}