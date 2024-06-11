import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DOMAttributeIteratorTest {
  private DOMAttributeIterator attrIterator;

  @Before
  public void setup() {
    // Create a sample DOM structure
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      Element rootElement = doc.createElement("root");
      doc.appendChild(rootElement);

      Element childElement = doc.createElement("child");
      rootElement.appendChild(childElement);

      Attr attr1 = doc.createAttribute("attr1");
      childElement.setAttributeNode(attr1);

      Attr attr2 = doc.createAttribute("attr2");
      childElement.setAttributeNode(attr2);

      attrIterator = new DOMAttributeIterator(new DOMNodePointer(childElement), new QName("*"));
    } catch (Exception e) {
      fail("Failed to create test DOM structure.");
    }
  }

  @Test
  public void testGetPosition() {
    assertEquals(0, attrIterator.getPosition());
  }

  @Test
  public void testSetPositionValid() {
    assertTrue(attrIterator.setPosition(1));
    assertEquals(1, attrIterator.getPosition());
  }

  @Test
  public void testSetPositionInvalid() {
    assertFalse(attrIterator.setPosition(-1));
    assertEquals(0, attrIterator.getPosition());
    assertFalse(attrIterator.setPosition(10));
    assertEquals(0, attrIterator.getPosition());
  }

  @Test
  public void testSetPositionValidBoundary() {
    assertTrue(attrIterator.setPosition(1));
    assertEquals(1, attrIterator.getPosition());
    assertTrue(attrIterator.setPosition(2));
    assertEquals(2, attrIterator.getPosition());
  }

  @Test
  public void testGetNodePointer() {
    attrIterator.setPosition(1);
    assertNotNull(attrIterator.getNodePointer());
    assertEquals("attr1", ((Attr)attrIterator.getNodePointer().getNode()).getName());
    attrIterator.setPosition(2);
    assertNotNull(attrIterator.getNodePointer());
    assertEquals("attr2", ((Attr)attrIterator.getNodePointer().getNode()).getName());
  }

  @Test
  public void testGetNodePointerInvalid() {
    assertNull(attrIterator.getNodePointer());
  }

  @Test
  public void testSetPositionAndGetNodePointer() {
    assertTrue(attrIterator.setPosition(1));
    assertNotNull(attrIterator.getNodePointer());
    assertTrue(attrIterator.setPosition(2));
    assertNotNull(attrIterator.getNodePointer());
    assertFalse(attrIterator.setPosition(3));
    assertNull(attrIterator.getNodePointer());
  }

  @Test
  public void testInvalidAttrsExcluded() {
    /*
     * This test verifies that attributes with the "xmlns" prefix are excluded from the result.
     */
    Element element = null;
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      element = doc.createElement("test");
      element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:prefix", "http://example.com");

      attrIterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("*"));
    } catch (Exception e) {
      fail("Failed to create test DOM structure.");
    }

    assertEquals(0, attrIterator.getPosition());
    assertEquals(0, attrIterator.getNodePointer().getNode().getNodeName().compareTo("test"));
  }

  @Test
  public void testGetAttributeWithPrefix() {
    /*
     * This test verifies that the correct attribute is returned when using a prefix.
     */
    Element element = null;
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      element = doc.createElementNS("http://example.com", "example:test");
      element.setAttributeNS("http://example.com", "example:attr", "value");

      attrIterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("example:attr"));
    } catch (Exception e) {
      fail("Failed to create test DOM structure.");
    }

    assertEquals(1, attrIterator.getPosition());
    assertEquals("value", ((Attr)attrIterator.getNodePointer().getNode()).getValue());
  }

  @Test
  public void testGetAttributeWithoutPrefix() {
    /*
     * This test verifies that the correct attribute is returned when not using a prefix.
     */
    Element element = null;
    try {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.newDocument();

      element = doc.createElementNS("http://example.com", "example:test");
      element.setAttributeNS(null, "attr", "value");

      attrIterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("attr"));
    } catch (Exception e) {
      fail("Failed to create test DOM structure.");
    }

    assertEquals(1, attrIterator.getPosition());
    assertEquals("value", ((Attr)attrIterator.getNodePointer().getNode()).getValue());
  }
}