import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {
  
  private DOMAttributeIterator iterator;
  
  @Before
  public void setUp() {
    // Initialize parent pointer
    NodePointer parent = new NodePointer();
    QName name = new QName("test", "attribute");
    
    // Create test DOMAttributeIterator object
    iterator = new DOMAttributeIterator(parent, name);
  }
  
  @Test
  public void testGetNodePointer() {
    // Test for a valid position
    iterator.setPosition(1);
    NodePointer nodePointer = iterator.getNodePointer();
    assertNotNull("NodePointer should not be null", nodePointer);
    
    // Test for an invalid position
    iterator.setPosition(-1);
    nodePointer = iterator.getNodePointer();
    assertNull("NodePointer should be null", nodePointer);
  }
  
  @Test
  public void testGetPosition() {
    // Set position to 1
    iterator.setPosition(1);
    int position = iterator.getPosition();
    assertEquals("Position should be 1", 1, position);
  }
  
  @Test
  public void testSetPosition() {
    // Set position to a valid value
    boolean result = iterator.setPosition(1);
    assertTrue("setPosition() should return true", result);
    
    // Set position to an invalid value
    result = iterator.setPosition(-1);
    assertFalse("setPosition() should return false", result);
  }
  
  @Test
  public void testCompleteIterator() {
    // Test a complete iteration through the attributes list
    iterator.setPosition(1);
    assertTrue("setPosition() should return true", iterator.setPosition(1));
    assertEquals("Position should be 1", 1, iterator.getPosition());
    assertNotNull("NodePointer should not be null", iterator.getNodePointer());
    
    iterator.setPosition(2);
    assertTrue("setPosition() should return true", iterator.setPosition(2));
    assertEquals("Position should be 2", 2, iterator.getPosition());
    assertNotNull("NodePointer should not be null", iterator.getNodePointer());
    
    iterator.setPosition(3);
    assertFalse("setPosition() should return false", iterator.setPosition(3));
    assertEquals("Position should be 0", 0, iterator.getPosition());
    assertNull("NodePointer should be null", iterator.getNodePointer());
  }
}