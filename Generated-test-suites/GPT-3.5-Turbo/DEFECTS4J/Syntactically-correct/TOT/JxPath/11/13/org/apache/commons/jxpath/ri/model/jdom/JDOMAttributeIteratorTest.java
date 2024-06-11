import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Attribute;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;

public class JDOMAttributeIteratorTest {

   JDOMAttributeIterator jdomAttributeIterator;
   NodePointer nodePointer;

   @Before
   public void setUp() {
       Element element = new Element("testElement");
       element.setAttribute("attr1", "value1");
       element.setAttribute("attr2", "value2");
       element.setAttribute("attr3", "value3");
       element.setAttribute("attr4", "");
       element.setAttribute("attr5", null);
       nodePointer = new JDOMAttributePointer(element, null);
       jdomAttributeIterator = new JDOMAttributeIterator(nodePointer, null);
   }

   @Test
   public void testGetNodePointer() {
       NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
       assertNotNull(nodePointer);
   }

   @Test
   public void testGetPosition() {
       int position = jdomAttributeIterator.getPosition();
       assertEquals(0, position);
   }

   @Test
   public void testSetPosition_validPosition() {
       assertTrue(jdomAttributeIterator.setPosition(1));
       assertEquals(1, jdomAttributeIterator.getPosition());
   }

   @Test
   public void testSetPosition_invalidPosition_zero() {
       assertFalse(jdomAttributeIterator.setPosition(0));
       assertEquals(0, jdomAttributeIterator.getPosition());
   }

   @Test
   public void testSetPosition_invalidPosition_negative() {
       assertFalse(jdomAttributeIterator.setPosition(-1));
       assertEquals(0, jdomAttributeIterator.getPosition());
   }

   @Test
   public void testSetPosition_invalidPosition_greaterThanSize() {
       assertFalse(jdomAttributeIterator.setPosition(10));
       assertEquals(0, jdomAttributeIterator.getPosition());
   }

   @Test
   public void testSetPosition_invalidPosition_nullAttributes() {
       jdomAttributeIterator.attributes = null;
       assertFalse(jdomAttributeIterator.setPosition(1));
       assertEquals(0, jdomAttributeIterator.getPosition());
   }

   @Test
   public void testSetPosition_validPosition_multipleTimes() {
       assertTrue(jdomAttributeIterator.setPosition(2));
       assertEquals(2, jdomAttributeIterator.getPosition());
       assertTrue(jdomAttributeIterator.setPosition(3));
       assertEquals(3, jdomAttributeIterator.getPosition());
       assertTrue(jdomAttributeIterator.setPosition(1));
       assertEquals(1, jdomAttributeIterator.getPosition());
   }

   @Test
   public void testSetPosition_validPosition_zero() {
       assertTrue(jdomAttributeIterator.setPosition(0));
       assertEquals(0, jdomAttributeIterator.getPosition());
   }

   @Test
   public void testSetPosition_validPosition_negative() {
       assertTrue(jdomAttributeIterator.setPosition(-5));
       assertEquals(0, jdomAttributeIterator.getPosition());
   }

   @Test
   public void testSetPosition_validPosition_greaterThanSize() {
       assertTrue(jdomAttributeIterator.setPosition(100));
       assertEquals(0, jdomAttributeIterator.getPosition());
   }
}