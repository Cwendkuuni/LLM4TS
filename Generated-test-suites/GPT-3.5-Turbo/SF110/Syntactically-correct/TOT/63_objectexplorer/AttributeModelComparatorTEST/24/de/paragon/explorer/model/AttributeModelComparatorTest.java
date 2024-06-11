import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullInputs_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(-1, comparator.compare(null, null));
    }
    
    @Test
    public void testCompare_NullInput1_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel2 = new AttributeModel("b");
        assertEquals(-1, comparator.compare(null, attributeModel2));
    }
    
    @Test
    public void testCompare_NullInput2_ReturnsPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("a");
        assertEquals(1, comparator.compare(attributeModel1, null));
    }

    @Test
    public void testCompare_SameName_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("abc");
        AttributeModel attributeModel2 = new AttributeModel("abc");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }
    
    @Test
    public void testCompare_DifferentName_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("xyz");
        AttributeModel attributeModel2 = new AttributeModel("abc");
        assertTrue(comparator.compare(attributeModel1, attributeModel2) < 0);
    }
    
    @Test
    public void testCompare_DifferentName_CaseInsensitive_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("XYZ");
        AttributeModel attributeModel2 = new AttributeModel("abc");
        assertTrue(comparator.compare(attributeModel1, attributeModel2) < 0);
    }
    
    @Test
    public void testCompare_NameStartsWithA_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("aaa");
        AttributeModel attributeModel2 = new AttributeModel("bbb");
        assertTrue(comparator.compare(attributeModel1, attributeModel2) < 0);
    }
    
    @Test
    public void testCompare_NameStartsWithB_ReturnsPositiveValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("bbb");
        AttributeModel attributeModel2 = new AttributeModel("aaa");
        assertTrue(comparator.compare(attributeModel1, attributeModel2) > 0);
    }
    
    @Test
    public void testCompare_NameStartsWithC_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("cde");
        AttributeModel attributeModel2 = new AttributeModel("cde");
        assertEquals(0, comparator.compare(attributeModel1, attributeModel2));
    }
    
    @Test
    public void testCompare_NameStartsWithANameStartsWithB_ReturnsPositiveValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("aaa");
        AttributeModel attributeModel2 = new AttributeModel("bbb");
        assertTrue(comparator.compare(attributeModel1, attributeModel2) > 0);
    }
    
    @Test
    public void testCompare_NameStartsWithBNameStartsWithA_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel1 = new AttributeModel("bbb");
        AttributeModel attributeModel2 = new AttributeModel("aaa");
        assertTrue(comparator.compare(attributeModel1, attributeModel2) < 0);
    }
}