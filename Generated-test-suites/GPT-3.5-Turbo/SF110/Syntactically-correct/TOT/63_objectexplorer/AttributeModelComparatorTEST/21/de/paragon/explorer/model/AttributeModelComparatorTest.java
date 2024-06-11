import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullAttributes() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_NullAttribute() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel();
        int result = comparator.compare(attributeModel, null);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_AttributeNamesEqual() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("a");
        
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("a");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_AttributeName1BeforeName2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("a");
        
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("b");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_AttributeName2BeforeName1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("b");
        
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("a");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_AttributeName1BeforeName2_CaseInsensitive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("A");
        
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("b");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_AttributeName2BeforeName1_CaseInsensitive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("b");
        
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("A");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_AttributeName1BeforeAAndName2AfterA_CaseInsensitive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("A");
        
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("c");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_AttributeName1AfterAAndName2BeforeA_CaseInsensitive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("c");
        
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("A");
        
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(1, result);
    }
}