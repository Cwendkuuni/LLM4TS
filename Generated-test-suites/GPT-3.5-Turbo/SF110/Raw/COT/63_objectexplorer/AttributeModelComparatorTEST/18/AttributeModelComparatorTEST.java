
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    private AttributeModelComparator comparator;
  
    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }
  
    @Test
    public void testCompare() {
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("def");
      
        int result = comparator.compare(model1, model2);
      
        assertTrue(result < 0);
    }
  
    // Test for null inputs
    @Test
    public void testCompareNullInputs() {
        AttributeModel model1 = null;
        AttributeModel model2 = null;
      
        int result = comparator.compare(model1, model2);
      
        assertEquals(0, result);
    }
  
    // Test for o1 being null
    @Test
    public void testCompareNullO1() {
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("abc");
      
        int result = comparator.compare(model1, model2);
      
        assertTrue(result < 0);
    }
  
    // Test for o2 being null
    @Test
    public void testCompareNullO2() {
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = null;
      
        int result = comparator.compare(model1, model2);
      
        assertTrue(result > 0);
    }
  
    // Test for both o1 and o2 being null
    @Test
    public void testCompareBothNull() {
        AttributeModel model1 = null;
        AttributeModel model2 = null;
      
        int result = comparator.compare(model1, model2);
      
        assertEquals(0, result);
    }
  
    // Test for equal names
    @Test
    public void testCompareEqualNames() {
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("abc");
      
        int result = comparator.compare(model1, model2);
      
        assertEquals(0, result);
    }
  
    // Test for both names less than "a"
    @Test
    public void testCompareBothLessThanA() {
        AttributeModel model1 = new AttributeModel("aaa");
        AttributeModel model2 = new AttributeModel("bbb");
      
        int result = comparator.compare(model1, model2);
      
        assertTrue(result < 0);
    }
  
    // Test for o1 less than "a" and o2 greater than or equal to "a"
    @Test
    public void testCompareO1LessThanAO2GreaterThanOrEqualToA() {
        AttributeModel model1 = new AttributeModel("aaa");
        AttributeModel model2 = new AttributeModel("aab");
      
        int result = comparator.compare(model1, model2);
      
        assertEquals(1, result);
    }
  
    // Test for o2 less than "a" and o1 greater than or equal to "a"
    @Test
    public void testCompareO2LessThanAO1GreaterThanOrEqualToA() {
        AttributeModel model1 = new AttributeModel("aab");
        AttributeModel model2 = new AttributeModel("aaa");
      
        int result = comparator.compare(model1, model2);
      
        assertEquals(-1, result);
    }
  
    // Test for both names greater than or equal to "a"
    @Test
    public void testCompareBothGreaterThanOrEqualToA() {
        AttributeModel model1 = new AttributeModel("bbb");
        AttributeModel model2 = new AttributeModel("aaa");
      
        int result = comparator.compare(model1, model2);
      
        assertTrue(result > 0);
    }
}
