import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {
  
  @Test
  public void testGetName() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    QName expectedName = new QName("*");
    assertEquals(expectedName, nullPropertyPointer.getName());
  }
  
  @Test
  public void testSetPropertyIndex() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if property index is set correctly
    int expectedIndex = 5;
    nullPropertyPointer.setPropertyIndex(expectedIndex);
    assertEquals(expectedIndex, nullPropertyPointer.getIndex());
  }
  
  @Test
  public void testGetLength() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if length is 0
    int expectedLength = 0;
    assertEquals(expectedLength, nullPropertyPointer.getLength());
  }
  
  @Test
  public void testGetBaseValue() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if base value is null
    assertNull(nullPropertyPointer.getBaseValue());
  }
  
  @Test
  public void testGetImmediateNode() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if immediate node is null
    assertNull(nullPropertyPointer.getImmediateNode());
  }
  
  @Test
  public void testIsLeaf() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if isLeaf returns true
    assertTrue(nullPropertyPointer.isLeaf());
  }
  
  @Test
  public void testGetValuePointer() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if value pointer is NullPointer with the same property name
    NullPointer expectedValuePointer = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
    assertEquals(expectedValuePointer, nullPropertyPointer.getValuePointer());
  }
  
  @Test
  public void testIsActualProperty() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if isActualProperty returns false
    assertFalse(nullPropertyPointer.isActualProperty());
  }
  
  @Test
  public void testIsActual() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if isActual returns false
    assertFalse(nullPropertyPointer.isActual());
  }
  
  @Test
  public void testIsContainer() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if isContainer returns true
    assertTrue(nullPropertyPointer.isContainer());
  }
  
  @Test
  public void testSetValue() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if setting value throws JXPathInvalidAccessException for null parent
    try {
      nullPropertyPointer.setValue("test");
      fail("expected JXPathInvalidAccessException");
    } catch (JXPathInvalidAccessException e) {
      // Exception thrown as expected
    }
  }
  
  @Test
  public void testCreatePath() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    JXPathContext context = new JXPathContext(null);
    
    // Test if createPath returns NullPointer with the same property name
    NullPointer expectedPath = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
    assertEquals(expectedPath, nullPropertyPointer.createPath(context));
  }
  
  @Test
  public void testCreatePathWithValue() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    JXPathContext context = new JXPathContext(null);
    String value = "test";
    
    // Test if createPath returns NullPointer with the same property name and set value
    NullPointer expectedPath = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
    expectedPath.setValue(value);
    assertEquals(expectedPath, nullPropertyPointer.createPath(context, value));
  }
  
  @Test
  public void testCreateChildWithoutValue() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    JXPathContext context = new JXPathContext(null);
    QName name = new QName("name");
    int index = 1;
    
    // Test if createChild returns NullPointer with the same property name, name, and index
    NullPointer expectedChild = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
    expectedChild.setName(name);
    expectedChild.setIndex(index);
    assertEquals(expectedChild, nullPropertyPointer.createChild(context, name, index));
  }
  
  @Test
  public void testCreateChildWithValue() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    JXPathContext context = new JXPathContext(null);
    QName name = new QName("name");
    int index = 1;
    String value = "test";
    
    // Test if createChild returns NullPointer with the same property name, name, index, and set value
    NullPointer expectedChild = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
    expectedChild.setName(name);
    expectedChild.setIndex(index);
    expectedChild.setValue(value);
    assertEquals(expectedChild, nullPropertyPointer.createChild(context, name, index, value));
  }
  
  @Test
  public void testGetPropertyName() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if getPropertyName returns "*"
    assertEquals("*", nullPropertyPointer.getPropertyName());
  }
  
  @Test
  public void testSetPropertyName() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    String propertyName = "test";
    nullPropertyPointer.setPropertyName(propertyName);
    
    // Test if setPropertyName sets property name correctly
    assertEquals(propertyName, nullPropertyPointer.getPropertyName());
  }
  
  @Test
  public void testSetNameAttributeValue() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    String attributeValue = "test";
    nullPropertyPointer.setNameAttributeValue(attributeValue);
    
    // Test if setNameAttributeValue sets property name correctly and byNameAttribute is true
    assertEquals(attributeValue, nullPropertyPointer.getPropertyName());
    assertTrue(nullPropertyPointer.byNameAttribute);
  }
  
  @Test
  public void testIsCollection() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if isCollection is false for WHOLE_COLLECTION
    assertFalse(nullPropertyPointer.isCollection());
    
    // Test if isCollection is true for index other than WHOLE_COLLECTION
    nullPropertyPointer.setPropertyIndex(5);
    assertTrue(nullPropertyPointer.isCollection());
  }
  
  @Test
  public void testGetPropertyCount() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if getPropertyCount returns 0
    assertEquals(0, nullPropertyPointer.getPropertyCount());
  }
  
  @Test
  public void testGetPropertyNames() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    
    // Test if getPropertyNames returns empty array
    assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
  }
  
  @Test
  public void testAsPath() {
    NullPointer nullPointer = new NullPointer(null, new QName("*"));
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
    nullPropertyPointer.setPropertyName("test");
    
    StringBuilder expectedPath = new StringBuilder();
    expectedPath.append(nullPropertyPointer.getImmediateParentPointer().asPath());
    expectedPath.append("[@name='");
    expectedPath.append(nullPropertyPointer.getEscapedPropertyName());
    expectedPath.append("']");
    
    // Test if asPath returns expected path
    assertEquals(expectedPath.toString(), nullPropertyPointer.asPath());
  }
  
  private String escape(String string) {
    int index = string.indexOf('\'');
    while (index != -1) {
      string = string.substring(0, index) + "&apos;" + string.substring(index + 1);
      index = string.indexOf('\'');
    }
    index = string.indexOf('\"');
    while (index != -1) {
      string = string.substring(0, index) + "&quot;" + string.substring(index + 1);
      index = string.indexOf('\"');
    }
    return string;
  }
  
}