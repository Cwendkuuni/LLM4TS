
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_NullInputs() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_NullFirstInput() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o2 = new AttributeModel();
        int result = comparator.compare(null, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_NullSecondInput() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        int result = comparator.compare(o1, null);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_NameEqual() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("attribute");
        AttributeModel o2 = new AttributeModel();
        o2.setName("attribute");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_NameEqualIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("attribute");
        AttributeModel o2 = new AttributeModel();
        o2.setName("Attribute");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_NameDifferent() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("attribute1");
        AttributeModel o2 = new AttributeModel();
        o2.setName("attribute2");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_NameDifferentIgnoreCase1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("Attribute1");
        AttributeModel o2 = new AttributeModel();
        o2.setName("attribute2");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_NameDifferentIgnoreCase2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("attribute1");
        AttributeModel o2 = new AttributeModel();
        o2.setName("Attribute2");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_NameComparisons1() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("a");
        AttributeModel o2 = new AttributeModel();
        o2.setName("b");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_NameComparisons2() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("b");
        AttributeModel o2 = new AttributeModel();
        o2.setName("a");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_NameComparisons3() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("a");
        AttributeModel o2 = new AttributeModel();
        o2.setName("A");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_NameComparisons4() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("A");
        AttributeModel o2 = new AttributeModel();
        o2.setName("a");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_NameComparisons5() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("a");
        AttributeModel o2 = new AttributeModel();
        o2.setName("A");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_NameComparisons6() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("A");
        AttributeModel o2 = new AttributeModel();
        o2.setName("a");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    // Additional test cases for potential exceptions or edge cases
    
    // TODO: Write additional test cases here
    
}
