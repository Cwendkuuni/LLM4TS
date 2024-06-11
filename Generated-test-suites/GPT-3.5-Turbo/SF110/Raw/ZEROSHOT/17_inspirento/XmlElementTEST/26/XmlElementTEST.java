import static org.junit.Assert.*;
import org.junit.Test;

public class XmlElementTest {
  
  @Test
  public void testAddAttribute() {
    XmlElement element = new XmlElement();
    assertNull(element.addAttribute("name", "value"));
  }
  
  @Test
  public void testGetAttribute() {
    XmlElement element = new XmlElement();
    element.addAttribute("name", "value");
    
    assertEquals("value", element.getAttribute("name"));
  }
  
  @Test
  public void testGetAttributeWithDefaultValue() {
    XmlElement element = new XmlElement();
    
    assertEquals("default", element.getAttribute("name", "default"));
  }
  
  @Test
  public void testGetAttributes() {
    XmlElement element = new XmlElement();
    element.addAttribute("name1", "value1");
    element.addAttribute("name2", "value2");
    
    assertEquals(2, element.getAttributes().size());
  }
  
  @Test
  public void testSetAttributes() {
    XmlElement element = new XmlElement();
    Hashtable<String, String> attributes = new Hashtable<>();
    attributes.put("name", "value");
    
    element.setAttributes(attributes);
    
    assertEquals(1, element.getAttributes().size());
    assertEquals("value", element.getAttribute("name"));
  }
  
  @Test
  public void testGetAttributeNames() {
    XmlElement element = new XmlElement();
    element.addAttribute("name1", "value1");
    element.addAttribute("name2", "value2");
    
    assertEquals(2, element.getAttributeNames().size());
    assertTrue(element.getAttributeNames().contains("name1"));
    assertTrue(element.getAttributeNames().contains("name2"));
  }
  
  @Test
  public void testAddElement() {
    XmlElement element = new XmlElement();
    XmlElement childElement = new XmlElement();
    
    assertTrue(element.addElement(childElement));
    assertEquals(1, element.count());
    assertEquals(childElement, element.getElement(0));
  }
  
  @Test
  public void testRemoveElement() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement = new XmlElement();
    
    assertTrue(parentElement.addElement(childElement));
    XmlElement removedElement = parentElement.removeElement(childElement);
    
    assertEquals(childElement, removedElement);
    assertEquals(0, parentElement.count());
  }
  
  @Test
  public void testRemoveElementByIndex() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement = new XmlElement();
    
    assertTrue(parentElement.addElement(childElement));
    XmlElement removedElement = parentElement.removeElement(0);
    
    assertEquals(childElement, removedElement);
    assertEquals(0, parentElement.count());
  }
  
  @Test
  public void testRemoveAllElements() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement1 = new XmlElement();
    XmlElement childElement2 = new XmlElement();
    
    assertTrue(parentElement.addElement(childElement1));
    assertTrue(parentElement.addElement(childElement2));
    parentElement.removeAllElements();
    
    assertEquals(0, parentElement.count());
  }
  
  @Test
  public void testRemoveFromParent() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement = new XmlElement();
    
    assertTrue(parentElement.addElement(childElement));
    childElement.removeFromParent();
    
    assertEquals(0, parentElement.count());
    assertNull(childElement.getParent());
  }
  
  @Test
  public void testAppend() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement = new XmlElement();
    
    childElement.appendToParent(parentElement);
    
    assertEquals(1, parentElement.count());
    assertEquals(childElement, parentElement.getElement(0));
    assertEquals(parentElement, childElement.getParent());
  }
  
  @Test
  public void testInsertElement() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement1 = new XmlElement();
    XmlElement childElement2 = new XmlElement();
    
    assertTrue(parentElement.addElement(childElement1));
    parentElement.insertElement(childElement2, 0);
    
    assertEquals(2, parentElement.count());
    assertEquals(childElement2, parentElement.getElement(0));
    assertEquals(childElement1, parentElement.getElement(1));
  }
  
  @Test
  public void testGetElements() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement1 = new XmlElement();
    XmlElement childElement2 = new XmlElement();
    
    assertTrue(parentElement.addElement(childElement1));
    assertTrue(parentElement.addElement(childElement2));
    
    assertEquals(2, parentElement.getElements().size());
    assertEquals(childElement1, parentElement.getElements().get(0));
    assertEquals(childElement2, parentElement.getElements().get(1));
  }
  
  @Test
  public void testCount() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement1 = new XmlElement();
    XmlElement childElement2 = new XmlElement();
    
    assertTrue(parentElement.addElement(childElement1));
    assertTrue(parentElement.addElement(childElement2));
    
    assertEquals(2, parentElement.count());
  }
  
  @Test
  public void testGetElement() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement1 = new XmlElement("child");
    XmlElement childElement2 = new XmlElement("child");
    
    assertTrue(parentElement.addElement(childElement1));
    assertTrue(parentElement.addElement(childElement2));
    
    assertEquals(childElement1, parentElement.getElement("child"));
    assertNull(parentElement.getElement("nonexistent"));
  }
  
  @Test
  public void testGetElementByIndex() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement1 = new XmlElement();
    XmlElement childElement2 = new XmlElement();
    
    assertTrue(parentElement.addElement(childElement1));
    assertTrue(parentElement.addElement(childElement2));
    
    assertEquals(childElement1, parentElement.getElement(0));
    assertEquals(childElement2, parentElement.getElement(1));
  }
  
  @Test
  public void testAddSubElementWithPath() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement1 = parentElement.addSubElement("child1.child2.child3");
    
    assertNotNull(childElement1);
    assertEquals("child3", childElement1.getName());
    assertEquals("child2", childElement1.getParent().getName());
    assertEquals("child1", childElement1.getParent().getParent().getName());
  }
  
  @Test
  public void testAddSubElementWithElement() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement = new XmlElement();
    
    XmlElement addedElement = parentElement.addSubElement(childElement);
    
    assertEquals(1, parentElement.count());
    assertEquals(childElement, addedElement);
    assertEquals(parentElement, childElement.getParent());
  }
  
  @Test
  public void testAddSubElementWithNameAndData() {
    XmlElement parentElement = new XmlElement();
    
    XmlElement addedElement = parentElement.addSubElement("child", "data");
    
    assertEquals(1, parentElement.count());
    assertEquals("child", addedElement.getName());
    assertEquals("data", addedElement.getData());
    assertEquals(parentElement, addedElement.getParent());
  }
  
  @Test
  public void testSetParent() {
    XmlElement parentElement = new XmlElement();
    XmlElement childElement = new XmlElement();
    
    childElement.setParent(parentElement);
    
    assertEquals(parentElement, childElement.getParent());
  }
  
  @Test
  public void testGetData() {
    XmlElement element = new XmlElement();
    element.setData("data");
    
    assertEquals("data", element.getData());
  }
  
  @Test
  public void testGetName() {
    XmlElement element = new XmlElement("name");
    
    assertEquals("name", element.getName());
  }
  
  @Test
  public void testPrintNode() {
    XmlElement element = new XmlElement("name");
    XmlElement childElement = new XmlElement("child");
    element.addElement(childElement);
    
    // Not tested since it directly prints to console
  }
  
  @Test
  public void testClone() {
    XmlElement element = new XmlElement("name");
    element.addAttribute("attr1", "value1");
    
    XmlElement clone = (XmlElement) element.clone();
    
    assertEquals(element.getName(), clone.getName());
    assertEquals(element.getAttributes(), clone.getAttributes());
  }
  
  @Test
  public void testSetName() {
    XmlElement element = new XmlElement();
    element.setName("name");
    
    assertEquals("name", element.getName());
  }
  
  @Test
  public void testNotifyObservers() {
    XmlElement element = new XmlElement();
    TestObserver observer = new TestObserver();
    element.addObserver(observer);
    
    element.notifyObservers();
    
    assertTrue(observer.notified);
  }
  
  @Test
  public void testEquals() {
    XmlElement element1 = new XmlElement("name");
    element1.addAttribute("attr1", "value1");
    
    XmlElement element2 = new XmlElement("name");
    element2.addAttribute("attr1", "value1");
    
    assertTrue(element1.equals(element2));
  }
  
  @Test
  public void testHashCode() {
    XmlElement element = new XmlElement("name");
    element.addAttribute("attr1", "value1");
    
    int expectedHashCode = 23 + element.getAttributes().hashCode() * 13 + element.getName().hashCode() * 29;
    
    assertEquals(expectedHashCode, element.hashCode());
  }
}

class TestObserver implements Observer {
  public boolean notified = false;
  
  @Override
  public void update(Observable o, Object arg) {
    this.notified = true;
  }
}