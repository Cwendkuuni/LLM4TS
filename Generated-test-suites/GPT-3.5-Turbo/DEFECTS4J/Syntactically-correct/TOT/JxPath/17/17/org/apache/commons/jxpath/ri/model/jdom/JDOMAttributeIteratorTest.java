import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
   private Element element;
   private NodePointer nodePointer;

   @Before
   public void setUp() {
      element = new Element("testElement");
      element.setAttribute("attr1", "value1");
      element.setAttribute("attr2", "value2");
      element.setAttribute("attr3", "value3");
      nodePointer = new JDOMAttributeIterator(new NodePointer(), new QName("attr")).getNodePointer();
   }

   @Test
   public void testGetNodePointer() {
      assertNotNull(nodePointer);
   }

   @Test
   public void testGetPosition() {
      assertEquals(0, nodePointer.getPosition());
   }

   @Test
   public void testSetPosition() {
      assertFalse(nodePointer.setPosition(0));
      assertTrue(nodePointer.setPosition(1));
      assertEquals(1, nodePointer.getPosition());
      assertTrue(nodePointer.setPosition(2));
      assertEquals(2, nodePointer.getPosition());
      assertTrue(nodePointer.setPosition(3));
      assertEquals(3, nodePointer.getPosition());
      assertFalse(nodePointer.setPosition(4));
   }

   @Test
   public void testGetPositionWithEmptyParent() {
      nodePointer = new JDOMAttributeIterator(new NodePointer(), new QName("attr")).getNodePointer();
      assertNull(nodePointer);
   }

   @Test
   public void testGetNodePointerWithInvalidName() {
      nodePointer = new JDOMAttributeIterator(new NodePointer(), new QName("invalidAttr")).getNodePointer();
      assertNull(nodePointer);
   }

   @Test
   public void testGetNodePointerWithPrefix() {
      nodePointer = new JDOMAttributeIterator(new NodePointer(), new QName("prefix:attr1")).getNodePointer();
      Attribute attribute = (Attribute) nodePointer.getNode();
      assertNotNull(attribute);
      assertEquals("prefix:attr1", attribute.getQualifiedName());
      assertEquals("value1", attribute.getValue());
   }

   @Test
   public void testGetNodePointerWithXmlPrefix() {
      element.addNamespaceDeclaration(org.jdom.Namespace.getNamespace("xml", "http://www.w3.org/XML/1998/namespace"));
      nodePointer = new JDOMAttributeIterator(new NodePointer(), new QName("xml:attr1")).getNodePointer();
      Attribute attribute = (Attribute) nodePointer.getNode();
      assertNotNull(attribute);
      assertEquals("xml:attr1", attribute.getQualifiedName());
      assertEquals("value1", attribute.getValue());
   }
}