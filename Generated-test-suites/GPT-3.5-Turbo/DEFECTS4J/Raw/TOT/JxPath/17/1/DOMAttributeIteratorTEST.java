import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

  @Test
  public void testGetNodePointer() {
    Document document = createDummyDocument();
    DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMAttributePointer(new DOMNodePointer(document.getDocumentElement())), new QName("test"));
    NodePointer pointer = iterator.getNodePointer();
    assertTrue(pointer instanceof DOMAttributePointer);
    assertEquals("attr1", ((DOMAttributePointer) pointer).getNode().getNodeName());
  }

  @Test
  public void testGetNodePointerNull() {
    Document document = createDummyDocument();
    DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMAttributePointer(new DOMNodePointer(document.getDocumentElement())), new QName("nonexistent"));
    NodePointer pointer = iterator.getNodePointer();
    assertNull(pointer);
  }

  @Test
  public void testGetPosition() {
    Document document = createDummyDocument();
    DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMAttributePointer(new DOMNodePointer(document.getDocumentElement())), new QName("test"));
    assertEquals(0, iterator.getPosition());
    iterator.setPosition(2);
    assertEquals(2, iterator.getPosition());
  }

  @Test
  public void testSetPositionValid() {
    Document document = createDummyDocument();
    DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMAttributePointer(new DOMNodePointer(document.getDocumentElement())), new QName("test"));
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    assertTrue(iterator.setPosition(2));
    assertEquals(2, iterator.getPosition());
  }

  @Test
  public void testSetPositionInvalid() {
    Document document = createDummyDocument();
    DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMAttributePointer(new DOMNodePointer(document.getDocumentElement())), new QName("test"));
    assertFalse(iterator.setPosition(0));
    assertEquals(0, iterator.getPosition());
    assertFalse(iterator.setPosition(3));
    assertEquals(0, iterator.getPosition());
  }

  private Document createDummyDocument() {
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element root = document.createElement("root");
    Attr attr1 = document.createAttribute("attr1");
    attr1.setValue("value1");
    root.setAttributeNode(attr1);
    Attr attr2 = document.createAttribute("attr2");
    attr2.setValue("value2");
    root.setAttributeNode(attr2);
    document.appendChild(root);
    return document;
  }

}