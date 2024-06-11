import static org.junit.Assert.*;
import org.junit.*;

public class AttributeModelComparatorTest {

  @Test
  public void testCompare() {
    AttributeModel a1 = new AttributeModel("a");
    AttributeModel a2 = new AttributeModel("a");
    AttributeModel b = new AttributeModel("b");
    
    AttributeModelComparator comparator = new AttributeModelComparator();
    
    // Test when o1 and o2 are both null
    assertEquals(0, comparator.compare(null, null));
    
    // Test when o1 is null and o2 is not null
    assertTrue(comparator.compare(null, b) < 0);
    
    // Test when o1 is not null and o2 is null
    assertTrue(comparator.compare(b, null) > 0);
    
    // Test when o1 and o2 are not null and have same name
    assertEquals(0, comparator.compare(a1, a2));
    
    // Test when o1 and o2 are not null, o1's name comes before "a", and o2's name comes after "a"
    assertTrue(comparator.compare(a1, b) < 0);
    
    // Test when o1 and o2 are not null, o1's name comes after "a", and o2's name comes before "a"
    assertTrue(comparator.compare(b, a1) > 0);
    
    // Test when o1 and o2 are not null and have different names
    assertTrue(comparator.compare(a1, b) < 0);
    assertTrue(comparator.compare(b, a1) > 0);
  }
}