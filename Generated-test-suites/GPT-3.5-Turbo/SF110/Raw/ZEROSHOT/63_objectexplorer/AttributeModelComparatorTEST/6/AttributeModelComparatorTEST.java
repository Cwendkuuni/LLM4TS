import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class AttributeModelComparatorTest {

    @Test
    public void compare_AttributeModelsNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }
    
    @Test
    public void compare_FirstAttributeModelNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = null;
        AttributeModel attrModel2 = new AttributeModel("attr2");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(-1, result);
    }
    
    @Test
    public void compare_SecondAttributeModelNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("attr1");
        AttributeModel attrModel2 = null;
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(-1, result);
    }
    
    @Test
    public void compare_BothAttributeModelsNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = null;
        AttributeModel attrModel2 = null;
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(-1, result);
    }
    
    @Test
    public void compare_NamesEqualIgnoringCase_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("xyz");
        AttributeModel attrModel2 = new AttributeModel("XyZ");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(0, result);
    }
    
    @Test
    public void compare_FirstAttributeNameLessThanSecondAttributeName_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("abc");
        AttributeModel attrModel2 = new AttributeModel("def");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(-1, result);
    }

    @Test
    public void compare_FirstAttributeNameLessThanSecondAttributeNameIgnoresCase_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("abc");
        AttributeModel attrModel2 = new AttributeModel("DEF");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(-1, result);
    }
    
    @Test
    public void compare_FirstAttributeNameGreaterThanSecondAttributeName_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("xyz");
        AttributeModel attrModel2 = new AttributeModel("abc");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(1, result);
    }
    
    @Test
    public void compare_FirstAttributeNameGreaterThanSecondAttributeNameIgnoresCase_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("XYZ");
        AttributeModel attrModel2 = new AttributeModel("abc");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(1, result);
    }
    
    @Test
    public void compare_FirstAttributeNameEqualToAAndSecondAttributeNameGreaterThanA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("a");
        AttributeModel attrModel2 = new AttributeModel("xyz");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(-1, result);
    }
    
    @Test
    public void compare_SecondAttributeNameEqualToAAndFirstAttributeNameGreaterThanA_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("xyz");
        AttributeModel attrModel2 = new AttributeModel("a");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(1, result);
    }
    
    @Test
    public void compare_BothAttributeNamesEqualToA_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("a");
        AttributeModel attrModel2 = new AttributeModel("a");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(0, result);
    }
    
    @Test
    public void compare_BothAttributeNamesEqualToAWithDifferentCases_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attrModel1 = new AttributeModel("a");
        AttributeModel attrModel2 = new AttributeModel("A");
        int result = comparator.compare(attrModel1, attrModel2);
        assertEquals(0, result);
    }
    
}