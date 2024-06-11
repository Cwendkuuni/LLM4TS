import org.junit.Test;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {
  
  // Test getAttribute method when Name is not *
  @Test
  public void testGetAttribute_NotStarName() {
    // Create a sample Element
    Element element = createSampleElement();
  
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer(element);
    
    // Create a sample QName
    QName name = new QName("prefix", "localName");
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    // Get the attribute using getAttribute method
    Attr attribute = iterator.getAttribute(element, name);
    
    // Test the result
    assertNotNull(attribute);
    assertEquals("attrValue", attribute.getValue());
  }
  
  // Test getAttribute method when Name is *
  @Test
  public void testGetAttribute_StarName() {
    // Create a sample Element
    Element element = createSampleElement();
  
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer(element);
    
    // Create a sample QName
    QName name = new QName("*");
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    // Get the attribute using getAttribute method
    Attr attribute = iterator.getAttribute(element, name);
    
    // Test the result
    assertNotNull(attribute);
    assertEquals("attrValue", attribute.getValue());
  }
  
  // Test testAttr method when attr is not null and testName is not *
  @Test
  public void testTestAttr_NotNullAttr_NotStarTestName() {
    // Create a sample Attr
    Attr attr = createSampleAttr("prefix:localName", "attrValue");
  
    // Create a sample QName
    QName testName = new QName("prefix", "localName");
    
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer(attr);
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, testName);
    
    // Call testAttr method
    boolean result = iterator.testAttr(attr, testName);
    
    // Test the result
    assertTrue(result);
  }
  
  // Test testAttr method when attr is not null and testName is *
  @Test
  public void testTestAttr_NotNullAttr_StarTestName() {
    // Create a sample Attr
    Attr attr = createSampleAttr("prefix:localName", "attrValue");
  
    // Create a sample QName
    QName testName = new QName("*");
    
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer(attr);
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, testName);
    
    // Call testAttr method
    boolean result = iterator.testAttr(attr, testName);
    
    // Test the result
    assertTrue(result);
  }
  
  // Test testAttr method when attr is null and testName is not *
  @Test
  public void testTestAttr_NullAttr_NotStarTestName() {
    // Create a sample QName
    QName testName = new QName("prefix", "localName");
    
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer();
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, testName);
    
    // Call testAttr method
    boolean result = iterator.testAttr(null, testName);
    
    // Test the result
    assertFalse(result);
  }
  
  // Test testAttr method when attr is null and testName is *
  @Test
  public void testTestAttr_NullAttr_StarTestName() {
    // Create a sample QName
    QName testName = new QName("*");
    
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer();
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, testName);
    
    // Call testAttr method
    boolean result = iterator.testAttr(null, testName);
    
    // Test the result
    assertFalse(result);
  }
  
  // Test equalStrings method when both strings are equal
  @Test
  public void testEqualStrings_EqualStrings() {
    // Create sample strings
    String s1 = "test";
    String s2 = "test";
    
    // Call equalStrings method
    boolean result = DOMAttributeIterator.equalStrings(s1, s2);
    
    // Test the result
    assertTrue(result);
  }
  
  // Test equalStrings method when both strings are not equal
  @Test
  public void testEqualStrings_NotEqualStrings() {
    // Create sample strings
    String s1 = "test1";
    String s2 = "test2";
    
    // Call equalStrings method
    boolean result = DOMAttributeIterator.equalStrings(s1, s2);
    
    // Test the result
    assertFalse(result);
  }
  
  // Test equalStrings method when both strings are null
  @Test
  public void testEqualStrings_NullStrings() {
    // Create sample strings
    String s1 = null;
    String s2 = null;
    
    // Call equalStrings method
    boolean result = DOMAttributeIterator.equalStrings(s1, s2);
    
    // Test the result
    assertTrue(result);
  }
  
  // Test getNodePointer method
  @Test
  public void testGetNodePointer() {
    // Create a sample Element
    Element element = createSampleElement();
  
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer(element);
    
    // Create a sample QName
    QName name = new QName("prefix", "localName");
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    // Call getNodePointer method
    NodePointer result = iterator.getNodePointer();
    
    // Test the result
    assertNotNull(result);
    assertTrue(result instanceof DOMAttributePointer);

    DOMAttributePointer attributePointer = (DOMAttributePointer) result;
    assertEquals(element, attributePointer.getNode());
  }
  
  // Test getPosition method
  @Test
  public void testGetPosition() {
    // Create a sample Element
    Element element = createSampleElement();
  
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer(element);
    
    // Create a sample QName
    QName name = new QName("prefix", "localName");
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    // Call getPosition method
    int result = iterator.getPosition();
    
    // Test the result
    assertEquals(0, result);
  }
  
  // Test setPosition method when position is valid
  @Test
  public void testSetPosition_ValidPosition() {
    // Create a sample Element
    Element element = createSampleElement();
  
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer(element);
    
    // Create a sample QName
    QName name = new QName("prefix", "localName");
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    // Call setPosition method
    boolean result = iterator.setPosition(1);
    
    // Test the result
    assertTrue(result);
    assertEquals(1, iterator.getPosition());
  }
  
  // Test setPosition method when position is invalid
  @Test
  public void testSetPosition_InvalidPosition() {
    // Create a sample Element
    Element element = createSampleElement();
  
    // Create a sample NodePointer
    NodePointer parent = createSampleNodePointer(element);
    
    // Create a sample QName
    QName name = new QName("prefix", "localName");
    
    // Create a DOMAttributeIterator object
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
    
    // Call setPosition method
    boolean result = iterator.setPosition(10);
    
    // Test the result
    assertFalse(result);
    assertEquals(10, iterator.getPosition());
  }
  
  // Helper method to create a sample Element
  private Element createSampleElement() {
    // Create a sample Element
    Element element = new Element();
    element.setAttribute("prefix:localName", "attrValue");
    
    return element;
  }
  
  // Helper method to create a sample NodePointer
  private NodePointer createSampleNodePointer(Node node) {
    // Create a sample NodePointer
    NodePointer parent = new NodePointer(node);
    
    return parent;
  }
  
  // Helper method to create a sample Attr
  private Attr createSampleAttr(String name, String value) {
    // Create a sample Attr
    Attr attr = new Attr();
    attr.setName(name);
    attr.setValue(value);
    
    return attr;
  }
  
  // Helper method to create a sample NodePointer
  private NodePointer createSampleNodePointer() {
    // Create a sample NodePointer
    NodePointer parent = new NodePointer();
    
    return parent;
  }
}