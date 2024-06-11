import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

   @Test
   public void testDOMAttributeIterator() {
      // Creating a sample DOM Document
      Document document = getSampleDocument();
      
      // Creating a sample Element
      Element element = document.createElement("TestClass");
      element.setAttribute("attr1", "value1");
      element.setAttribute("attr2", "value2");
      element.setAttribute("attr3", "");
      element.setAttribute("attr4", null);

      // Creating a sample NodePointer
      NodePointer nodePointer = new DOMNodePointer(element);

      // Creating DOMAttributeIterator instance for testing
      DOMAttributeIterator attributeIterator = new DOMAttributeIterator(nodePointer, new QName("", "attr1"));

      // Testing getNodePointer() method
      NodePointer attributeNodePointer = attributeIterator.getNodePointer();
      assertNotNull(attributeNodePointer);
      assertEquals("attr1", ((Attr) attributeNodePointer.getNode()).getName());

      // Testing getPosition() method
      assertEquals(0, attributeIterator.getPosition());

      // Testing setPosition() method
      assertTrue(attributeIterator.setPosition(1));
      assertEquals(1, attributeIterator.getPosition());

      // Testing setPosition() method with invalid position
      assertFalse(attributeIterator.setPosition(0));
      assertEquals(1, attributeIterator.getPosition());

      // Testing getNodePointer() method after setPosition()
      attributeNodePointer = attributeIterator.getNodePointer();
      assertNotNull(attributeNodePointer);
      assertEquals("attr1", ((Attr) attributeNodePointer.getNode()).getName());
      assertEquals("value1", ((Attr) attributeNodePointer.getNode()).getValue());

   }

   private Document getSampleDocument() {
      // Creating a new DOM Document
      Document document = null;
      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         document = dBuilder.newDocument();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return document;
   }

}