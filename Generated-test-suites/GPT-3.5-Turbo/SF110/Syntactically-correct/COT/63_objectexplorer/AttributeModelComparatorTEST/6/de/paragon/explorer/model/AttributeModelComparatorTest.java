import org.junit.*;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullInput_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstInputNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel();
        int result = comparator.compare(null, attributeModel);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondInputNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel();
        int result = comparator.compare(attributeModel, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_BothInputsNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstInputNameBeforeA_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("apple");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("banana");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_SecondInputNameBeforeA_ReturnsPositiveValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("banana");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("apple");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_BothInputsNameBeforeA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("apple");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("avocado");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_BothInputsNameAfterA_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("banana");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("coconut");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstInputNullNameAfterA_SecondInputNotNullNameBeforeA_ReturnsOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel();
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("apple");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_FirstInputNotNullNameBeforeA_SecondInputNullNameAfterA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("apple");
        AttributeModel attributeModel2 = new AttributeModel();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstInputAfterA_SecondInputBeforeA_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("banana");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("apple");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstInputBeforeA_SecondInputBeforeA_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("apple");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("banana");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_FirstInputAfterA_SecondInputAfterA_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("banana");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("coconut");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

}