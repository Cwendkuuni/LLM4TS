
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    private AttributeModel attributeModel1;
    private AttributeModel attributeModel2;

    @Before
    public void setup() {
        comparator = new AttributeModelComparator();
        attributeModel1 = new AttributeModel("attribute1");
        attributeModel2 = new AttributeModel("attribute2");
    }

    @Test
    public void compare_WhenBothAttributeModelsAreNull_ShouldReturnZero() {
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void compare_WhenFirstAttributeModelIsNull_ShouldReturnNegativeOne() {
        int result = comparator.compare(null, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void compare_WhenSecondAttributeModelIsNull_ShouldReturnPositiveOne() {
        int result = comparator.compare(attributeModel1, null);
        assertEquals(1, result);
    }

    @Test
    public void compare_WhenBothAttributeModelsAreNotNullAndEqual_ShouldReturnZero() {
        int result = comparator.compare(attributeModel1, attributeModel1);
        assertEquals(0, result);
    }

    @Test
    public void compare_WhenBothAttributeModelsAreNotNullAndFirstIsLessThanSecond_ShouldReturnNegativeOne() {
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void compare_WhenBothAttributeModelsAreNotNullAndFirstIsGreaterThanSecond_ShouldReturnPositiveOne() {
        int result = comparator.compare(attributeModel2, attributeModel1);
        assertEquals(1, result);
    }

    @Test
    public void compare_WhenBothAttributeModelsAreNotNullAndFirstIsLessThanSecondAndFirstIsEqualToA_ShouldReturnNegativeOne() {
        attributeModel1.setName("a");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void compare_WhenBothAttributeModelsAreNotNullAndFirstIsGreaterThanSecondAndSecondIsEqualToA_ShouldReturnPositiveOne() {
        attributeModel2.setName("a");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }
}
