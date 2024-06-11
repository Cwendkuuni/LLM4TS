import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
  
    AttributeModelComparator comparator = new AttributeModelComparator();
  
    @Test
    public void testCompare() {
        AttributeModel o1 = new AttributeModel("Apple");
        AttributeModel o2 = new AttributeModel("Banana");
        assertEquals(1, comparator.compare(o1, o2));
    }
    
    @Test
    public void testCompareWithSameName() {
        AttributeModel o1 = new AttributeModel("Apple");
        AttributeModel o2 = new AttributeModel("Apple");
        assertEquals(0, comparator.compare(o1, o2));
    }
    
    @Test
    public void testCompareWithNullAttributeModels() {
        assertEquals(-1, comparator.compare(null, null));
    }
    
    @Test
    public void testCompareWithNullAttributeModel1() {
        AttributeModel o2 = new AttributeModel("Banana");
        assertEquals(-1, comparator.compare(null, o2));
    }
    
    @Test
    public void testCompareWithNullAttributeModel2() {
        AttributeModel o1 = new AttributeModel("Apple");
        assertEquals(1, comparator.compare(o1, null));
    }
}