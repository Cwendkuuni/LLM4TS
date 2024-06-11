import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;

public class DOMAttributeIteratorTest {

  @Test
  public void testGetNodePointer_emptyNode() {
    Element element = createEmptyElement();
    DOMAttributeIterator iterator = new DOMAttributeIterator(createNodePointer(element), new QName("*"));
    
    assertNull(iterator.getNodePointer());
  }
  
  @Test
  public void testGetNodePointer_singleAttribute() {
    Element element = createElementWithAttribute("attr1", "value1");
    DOMAttributeIterator iterator = new DOMAttributeIterator(createNodePointer(element), new QName("*"));
    
    NodePointer nodePointer = iterator.getNodePointer();
    
    assertTrue(nodePointer instanceof DOMAttributePointer);
    assertEquals("attr1", ((DOMAttributePointer) nodePointer).getAttr().getName());
    assertEquals("value1", ((DOMAttributePointer) nodePointer).getAttr().getValue());
    assertNull(iterator.getNodePointer());
  }
  
  @Test
  public void testGetNodePointer_multipleAttributes() {
    Element element = createElementWithAttributes("attr1", "value1", "attr2", "value2", "attr3", "value3");
    DOMAttributeIterator iterator = new DOMAttributeIterator(createNodePointer(element), new QName("*"));
    
    NodePointer nodePointer1 = iterator.getNodePointer();
    assertTrue(nodePointer1 instanceof DOMAttributePointer);
    assertEquals("attr1", ((DOMAttributePointer) nodePointer1).getAttr().getName());
    assertEquals("value1", ((DOMAttributePointer) nodePointer1).getAttr().getValue());
    
    NodePointer nodePointer2 = iterator.getNodePointer();
    assertTrue(nodePointer2 instanceof DOMAttributePointer);
    assertEquals("attr2", ((DOMAttributePointer) nodePointer2).getAttr().getName());
    assertEquals("value2", ((DOMAttributePointer) nodePointer2).getAttr().getValue());
    
    NodePointer nodePointer3 = iterator.getNodePointer();
    assertTrue(nodePointer3 instanceof DOMAttributePointer);
    assertEquals("attr3", ((DOMAttributePointer) nodePointer3).getAttr().getName());
    assertEquals("value3", ((DOMAttributePointer) nodePointer3).getAttr().getValue());
    
    assertNull(iterator.getNodePointer());
  }
  
  @Test
  public void testSetPosition_validPosition() {
    Element element = createElementWithAttributes("attr1", "value1", "attr2", "value2", "attr3", "value3");
    DOMAttributeIterator iterator = new DOMAttributeIterator(createNodePointer(element), new QName("*"));
    
    assertTrue(iterator.setPosition(1));
    assertEquals(1, iterator.getPosition());
    
    assertTrue(iterator.setPosition(3));
    assertEquals(3, iterator.getPosition());
  }
  
  @Test
  public void testSetPosition_invalidPosition() {
    Element element = createElementWithAttributes("attr1", "value1", "attr2", "value2", "attr3", "value3");
    DOMAttributeIterator iterator = new DOMAttributeIterator(createNodePointer(element), new QName("*"));
    
    assertFalse(iterator.setPosition(0));
    assertEquals(0, iterator.getPosition());
    
    assertFalse(iterator.setPosition(-1));
    assertEquals(0, iterator.getPosition());
    
    assertFalse(iterator.setPosition(4));
    assertEquals(0, iterator.getPosition());
  }
  
  private Element createEmptyElement() {
    return DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .newDocument()
        .createElement("element");
  }
  
  private Element createElementWithAttribute(String attributeName, String attributeValue) {
    Element element = createEmptyElement();
    element.setAttribute(attributeName, attributeValue);
    return element;
  }
  
  private Element createElementWithAttributes(String... attributes) {
    Element element = createEmptyElement();
    
    for (int i = 0; i < attributes.length; i += 2) {
      element.setAttribute(attributes[i], attributes[i + 1]);
    }
    
    return element;
  }
  
  private NodePointer createNodePointer(Element element) {
    return new NodePointer() {
      @Override
      public Object getBaseValue() {
        return element;
      }
      
      // mocked methods
      
      @Override
      public String getNamespaceURI(String prefix) {
        return null;
      }
      
      @Override
      public Object getNode() {
        return element;
      }
    };
  }

}