import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.apache.commons.jxpath.ri.QName;

public class DOMAttributeIteratorTest {

  @Test
  public void testDOMAttributeIterator() {
    Document document = createSampleDocument();
    DOMNodePointer parent = new DOMNodePointer(document);
    
    QName name = new QName("*");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

    int count = 0;
    while (iterator.setPosition(++count)) {
      assertNotNull(iterator.getNodePointer());
    }

    assertEquals(count, document.getDocumentElement().getAttributes().getLength() + 1);
  }

  @Test
  public void testGetNodePointer() {
    Document document = createSampleDocument();
    DOMNodePointer parent = new DOMNodePointer(document);
    
    QName name = new QName("*");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

    assertFalse(iterator.setPosition(0));
    assertTrue(iterator.setPosition(1));
    DOMAttributePointer nodePointer = (DOMAttributePointer) iterator.getNodePointer();
    assertNotNull(nodePointer);

    Attr attr1 = document.createAttribute("attr1");
    attr1.setValue("value1");
    Element element = document.createElement("element");
    element.setAttributeNode(attr1);
    DOMAttributePointer expected = new DOMAttributePointer(parent, attr1);
    
    assertEquals(expected.asPath(), nodePointer.asPath());
  }

  @Test
  public void testGetPosition() {
    Document document = createSampleDocument();
    DOMNodePointer parent = new DOMNodePointer(document);
    
    QName name = new QName("*");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

    iterator.setPosition(2);
    assertEquals(2, iterator.getPosition());
  }

  @Test
  public void testSetPosition() {
    Document document = createSampleDocument();
    DOMNodePointer parent = new DOMNodePointer(document);
    
    QName name = new QName("*");
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    assertFalse(iterator.setPosition(0));
    assertFalse(iterator.setPosition(-1));
    assertFalse(iterator.setPosition(100));
  }

  private Document createSampleDocument() {
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    Element element = document.createElement("root");
    element.setAttribute("attribute1", "value1");
    element.setAttribute("attribute2", "value2");
    document.appendChild(element);
    return document;
  }
  
}