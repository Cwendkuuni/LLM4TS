
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.*;
import org.apache.commons.jxpath.ri.model.*;
import java.util.*;
import org.w3c.dom.*;

public class DOMAttributeIteratorTest {

  @Test
  public void testDOMAttributeIterator() {
    // Test case 1: parent node is null
    NodePointer parent = null;
    QName name = new QName("*", null, null);
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    assertNull(iterator.getNodePointer());

    // Test case 2: parent node is not an element node
    Document document = createSampleDocument();
    parent = new DOMNodePointer(null, document);
    iterator = new DOMAttributeIterator(parent, name);
    assertNull(iterator.getNodePointer());

    // Test case 3: name is not '*', but the attribute with the name does not exist
    Element element = document.createElement("element");
    parent = new DOMNodePointer(null, element);
    name = new QName("attribute", null, null);
    iterator = new DOMAttributeIterator(parent, name);
    assertNull(iterator.getNodePointer());

    // Test case 4: name is '*', but there are no attributes
    name = new QName("*", null, null);
    iterator = new DOMAttributeIterator(parent, name);
    assertNull(iterator.getNodePointer());

    // Test case 5: name is '*', and there are attributes
    element.setAttribute("attribute1", "value1");
    element.setAttribute("attribute2", "value2");
    iterator = new DOMAttributeIterator(parent, name);
    assertNotNull(iterator.getNodePointer());
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
    assertEquals("attribute1", attributePointer.getAttribute().getName());

    assertTrue(iterator.setPosition(2));
    assertEquals(2, iterator.getPosition());
    attributePointer = (DOMAttributePointer) iterator.getNodePointer();
    assertEquals("attribute2", attributePointer.getAttribute().getName());

    // Test case 6: name is not '*', and there is an attribute with the name
    name = new QName("attribute1", null, null);
    iterator = new DOMAttributeIterator(parent, name);
    assertNotNull(iterator.getNodePointer());
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    attributePointer = (DOMAttributePointer) iterator.getNodePointer();
    assertEquals("attribute1", attributePointer.getAttribute().getName());

    // Test case 7: name is not '*', and the attribute with the name does not exist
    name = new QName("attribute3", null, null);
    iterator = new DOMAttributeIterator(parent, name);
    assertNull(iterator.getNodePointer());

    // Test case 8: name prefix is not null and name namespace is not null but does not match with attribute namespace
    name = new QName("attr", "attribute1", "http://www.example.com/namespace");
    iterator = new DOMAttributeIterator(parent, name);
    assertNull(iterator.getNodePointer());

    // Test case 9: name prefix is not null, name namespace is not null and matches with attribute namespace
    element.setAttributeNS("http://www.example.com/namespace", "attr:attribute1", "value1");
    iterator = new DOMAttributeIterator(parent, name);
    assertNotNull(iterator.getNodePointer());
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    attributePointer = (DOMAttributePointer) iterator.getNodePointer();
    assertEquals("attribute1", attributePointer.getAttribute().getName());
  }

  @Test
  public void testGetPosition() {
    Document document = createSampleDocument();
    Element element = document.createElement("element");
    DOMNodePointer parent = new DOMNodePointer(null, element);
    QName name = new QName("*", null, null);
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    assertEquals(0, iterator.getPosition());

    element.setAttribute("attribute1", "value1");
    element.setAttribute("attribute2", "value2");
    iterator = new DOMAttributeIterator(parent, name);
    assertEquals(0, iterator.getPosition());
    iterator.setPosition(3);
    assertEquals(3, iterator.getPosition());
  }

  @Test
  public void testSetPosition() {
    Document document = createSampleDocument();
    Element element = document.createElement("element");
    DOMNodePointer parent = new DOMNodePointer(null, element);
    QName name = new QName("*", null, null);
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    assertFalse(iterator.setPosition(0));
    assertFalse(iterator.setPosition(-1));
    assertFalse(iterator.setPosition(2));

    element.setAttribute("attribute1", "value1");
    element.setAttribute("attribute2", "value2");
    iterator = new DOMAttributeIterator(parent, name);
    assertFalse(iterator.setPosition(0));
    assertTrue(iterator.setPosition(1));
    assertTrue(iterator.setPosition(2));
    assertFalse(iterator.setPosition(3));
  }

  private Document createSampleDocument() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.newDocument();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
