import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class XmlElementTest {
  
  private XmlElement xmlElement;
  
  @Before
  public void setUp() {
    xmlElement = new XmlElement();
    xmlElement.addAttribute("attr1", "value1");
    xmlElement.addAttribute("attr2", "value2");
    xmlElement.addElement(new XmlElement("child1"));
    xmlElement.addElement(new XmlElement("child2"));
    xmlElement.addElement(new XmlElement("child3"));
  }
  
  @Test
  public void testAddAttribute() {
    Object returnValue = xmlElement.addAttribute("attr3", "value3");
    assertNotNull(returnValue);
  }
  
  @Test
  public void testGetAttribute() {
    String attribute = xmlElement.getAttribute("attr1");
    assertEquals("value1", attribute);
  }
  
  @Test
  public void testGetAttributeWithDefaultValue() {
    String attribute = xmlElement.getAttribute("attr4", "defaultValue");
    assertEquals("defaultValue", attribute);
  }
  
  @Test
  public void testGetAttributes() {
    Hashtable<String, String> attributes = xmlElement.getAttributes();
    assertEquals(2, attributes.size());
  }
  
  @Test
  public void testSetAttributes() {
    Hashtable<String, String> attributes = new Hashtable<>();
    attributes.put("attr3", "value3");
    attributes.put("attr4", "value4");
    xmlElement.setAttributes(attributes);
    assertEquals(attributes, xmlElement.getAttributes());
  }
  
  @Test
  public void testGetAttributeNames() {
    Enumeration enumeration = xmlElement.getAttributeNames();
    assertEquals("attr1", enumeration.nextElement());
    assertEquals("attr2", enumeration.nextElement());
    assertFalse(enumeration.hasMoreElements());
  }
  
  @Test
  public void testAddElement() {
    XmlElement element = new XmlElement("child4");
    boolean result = xmlElement.addElement(element);
    assertTrue(result);
  }
  
  @Test
  public void testRemoveElementByObject() {
    XmlElement element = xmlElement.getElement(0);
    XmlElement removed = xmlElement.removeElement(element);
    assertEquals(element, removed);
  }
  
  @Test
  public void testRemoveElementByIndex() {
    int index = 1;
    XmlElement removed = xmlElement.removeElement(index);
    assertNotNull(removed);
  }
  
  @Test
  public void testRemoveAllElements() {
    xmlElement.removeAllElements();
    assertEquals(0, xmlElement.count());
  }
  
  @Test
  public void testRemoveFromParent() {
    XmlElement element = xmlElement.getElement(0);
    element.removeFromParent();
    assertNull(element.getParent());
  }
  
  @Test
  public void testAppend() {
    XmlElement element = new XmlElement("child4");
    element.append(new XmlElement("child5"));
    assertEquals(element, element.getElement(0).getParent());
  }
  
  @Test
  public void testInsertElement() {
    int index = 1;
    XmlElement element = new XmlElement("child4");
    xmlElement.insertElement(element, index);
    assertEquals(element, xmlElement.getElement(index));
    assertEquals(xmlElement, element.getParent());
  }
  
  @Test
  public void testGetElements() {
    List<XmlElement> elements = xmlElement.getElements();
    assertEquals(3, elements.size());
  }
  
  @Test
  public void testCount() {
    int count = xmlElement.count();
    assertEquals(3, count);
  }
  
  @Test
  public void testGetElementByPath() {
    XmlElement element = xmlElement.getElement("child1");
    assertNotNull(element);
  }
  
  @Test
  public void testGetElementByIndex() {
    int index = 0;
    XmlElement element = xmlElement.getElement(index);
    assertNotNull(element);
  }
  
  @Test
  public void testAddSubElementWithDotPath() {
    XmlElement element = xmlElement.addSubElement("child1.child2.child3");
    assertNotNull(element);
  }
  
  @Test
  public void testAddSubElementWithXmlElement() {
    XmlElement subElement = new XmlElement("child4");
    XmlElement element = xmlElement.addSubElement(subElement);
    assertEquals(subElement, element.getElement(0));
  }
  
  @Test
  public void testAddSubElementWithNameAndData() {
    XmlElement element = xmlElement.addSubElement("child4", "data4");
    assertEquals("data4", element.getData());
  }
  
  @Test
  public void testSetParent() {
    XmlElement parent = new XmlElement("parent");
    xmlElement.setParent(parent);
    assertEquals(parent, xmlElement.getParent());
  }
  
  @Test
  public void testGetData() {
    String data = "data";
    xmlElement.setData(data);
    assertEquals(data, xmlElement.getData());
  }
  
  @Test
  public void testGetName() {
    String name = "name";
    xmlElement.setName(name);
    assertEquals(name, xmlElement.getName());
  }
  
  @Test
  public void testPrintNode() {
    // Not a testable method
  }
  
  @Test
  public void testClone() {
    XmlElement clone = (XmlElement) xmlElement.clone();
    assertEquals(xmlElement, clone);
    assertNotSame(xmlElement, clone);
  }
  
  @Test
  public void testNotifyObservers() {
    // Not a testable method
  }
  
  @Test
  public void testEqualsByObject() {
    XmlElement element = new XmlElement();
    boolean equals = xmlElement.equals(element);
    assertFalse(equals);
  }
  
  @Test
  public void testEqualsByXmlElement() {
    XmlElement element = new XmlElement();
    element.addAttribute("attr1", "value1");
    element.addElement(new XmlElement("child1"));
    element.addElement(new XmlElement("child2"));
    element.addElement(new XmlElement("child3"));
    
    boolean equals = xmlElement.equals(element);
    assertFalse(equals);
  }
  
  @Test
  public void testHashCode() {
    int hashCode = xmlElement.hashCode();
    assertNotNull(hashCode);
  }
}