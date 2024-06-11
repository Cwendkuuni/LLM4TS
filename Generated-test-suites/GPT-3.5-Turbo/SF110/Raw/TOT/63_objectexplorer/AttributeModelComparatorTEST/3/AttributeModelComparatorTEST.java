import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare_BothAttributesNull_ReturnsZero() {
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstAttributeNull_ReturnsNegativeOne() {
        AttributeModel attributeModel = new AttributeModel("b");
        
        int result = comparator.compare(null, attributeModel);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondAttributeNull_ReturnsPositiveOne() {
        AttributeModel attributeModel = new AttributeModel("b");
        
        int result = comparator.compare(attributeModel, null);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothAttributesHaveSameName_ReturnsZero() {
        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel("B");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstAttributeNameEqualToA_ReturnsNegativeOne() {
        AttributeModel attributeModel = new AttributeModel("A");
        
        int result = comparator.compare(attributeModel, new AttributeModel("b"));
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstAttributeNameLessThanAAndSecondAttributeNameGreaterThanOrEqualToA_ReturnsPositiveOne() {
        AttributeModel attributeModel = new AttributeModel("b");
        
        int result = comparator.compare(attributeModel, new AttributeModel("A"));
        assertEquals(1, result);
    }

    @Test
    public void testCompare_FirstAttributeNameGreaterThanAAndSecondAttributeNameLessThanA_ReturnsNegativeOne() {
        AttributeModel attributeModel = new AttributeModel("A");
        
        int result = comparator.compare(attributeModel, new AttributeModel("b"));
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstAttributeNameEqualToAAndSecondAttributeNameLessThanA_ReturnsNegativeOne() {
        AttributeModel attributeModel = new AttributeModel("A");
        
        int result = comparator.compare(attributeModel, new AttributeModel("a"));
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstAttributeNameLessThanAAndSecondAttributeNameEqualToA_ReturnsPositiveOne() {
        AttributeModel attributeModel = new AttributeModel("a");
        
        int result = comparator.compare(attributeModel, new AttributeModel("A"));
        assertEquals(1, result);
    }

    @Test
    public void testCompare_FirstAttributeNameLessThanAAndSecondAttributeNameEqualToA_CaseInsensitive_ReturnsZero() {
        AttributeModel attributeModel1 = new AttributeModel("a");
        AttributeModel attributeModel2 = new AttributeModel("A");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

}