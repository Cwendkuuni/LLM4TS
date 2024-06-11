import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {

   private DOMAttributeIterator attributeIterator;

   // Test setup
   @Before
   public void setup() {
      NodePointer parent = new NodePointer();
      QName name = new QName("attribute");
      attributeIterator = new DOMAttributeIterator(parent, name);
   }

   // Test getNodePointer() method
   @Test
   public void testGetNodePointer() {
      // Test when position is 0
      attributeIterator.position = 0;
      assertNull(attributeIterator.getNodePointer());

      // Test when position is within the attributes list range
      attributeIterator.position = 1;
      assertNotNull(attributeIterator.getNodePointer());

      attributeIterator.position = 2;
      assertNotNull(attributeIterator.getNodePointer());

      // Test when position is out of bounds range
      attributeIterator.position = 3;
      assertNull(attributeIterator.getNodePointer());
   }

   // Test getPosition() method
   @Test
   public void testGetPosition() {
      // Test when position is 0
      attributeIterator.position = 0;
      assertEquals(0, attributeIterator.getPosition());

      // Test when position is within the attributes list range
      attributeIterator.position = 1;
      assertEquals(1, attributeIterator.getPosition());

      attributeIterator.position = 2;
      assertEquals(2, attributeIterator.getPosition());

      // Test when position is out of bounds range
      attributeIterator.position = 3;
      assertEquals(3, attributeIterator.getPosition());
   }

   // Test setPosition() method
   @Test
   public void testSetPosition() {
      // Test when position is 0
      assertTrue(attributeIterator.setPosition(0));

      // Test when position is within the attributes list range
      assertTrue(attributeIterator.setPosition(1));
      assertTrue(attributeIterator.setPosition(2));

      // Test when position is out of bounds range
      assertFalse(attributeIterator.setPosition(3));
      assertFalse(attributeIterator.setPosition(4));
   }
}