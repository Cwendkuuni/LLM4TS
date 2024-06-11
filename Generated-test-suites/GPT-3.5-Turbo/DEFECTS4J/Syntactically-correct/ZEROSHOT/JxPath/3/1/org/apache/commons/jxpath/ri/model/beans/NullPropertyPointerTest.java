import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {
  
  @Test
  public void testGetName() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setPropertyName("testName");
    assertEquals("testName", pointer.getName().getName());
  }
  
  @Test
  public void testGetPropertyIndex() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setPropertyIndex(1);
    // No assertion to check if the property index is set correctly
  }
  
  @Test
  public void testGetLength() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertEquals(0, pointer.getLength());
  }
  
  @Test
  public void testGetBaseValue() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertNull(pointer.getBaseValue());
  }
  
  @Test
  public void testGetImmediateNode() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertNull(pointer.getImmediateNode());
  }
  
  @Test
  public void testIsLeaf() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertTrue(pointer.isLeaf());
  }
  
  @Test
  public void testIsActualProperty() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertFalse(pointer.isActualProperty());
  }
  
  @Test
  public void testIsActual() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertFalse(pointer.isActual());
  }
  
  @Test
  public void testIsContainer() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertTrue(pointer.isContainer());
  }
  
  @Test
  public void testSetValue() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    try {
      pointer.setValue(null);
      fail("JXPathInvalidAccessException expected");
    } catch (JXPathInvalidAccessException e) {
      // Exception is expected
    }
  }
  
  @Test
  public void testCreatePath() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    NodePointer newParent = pointer.createPath(context);
    assertNotNull(newParent);
  }
  
  @Test
  public void testCreatePathWithValue() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    NodePointer newParent = pointer.createPath(context, "testValue");
    assertNotNull(newParent);
  }
  
  @Test
  public void testCreateChild() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    QName name = new QName("testName");
    NodePointer child = pointer.createChild(context, name, 1);
    assertNotNull(child);
  }
  
  @Test
  public void testCreateChildWithValue() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    QName name = new QName("testName");
    NodePointer child = pointer.createChild(context, name, 1, "testValue");
    assertNotNull(child);
  }
  
  @Test
  public void testGetPropertyName() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setPropertyName("testName");
    assertEquals("testName", pointer.getPropertyName());
  }
  
  @Test
  public void testSetNameAttributeValue() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setNameAttributeValue("testName");
    assertEquals("testName", pointer.getPropertyName());
  }
  
  @Test
  public void testIsCollection() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertFalse(pointer.isCollection());
  }
  
  @Test
  public void testGetPropertyCount() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertEquals(0, pointer.getPropertyCount());
  }
  
  @Test
  public void testGetPropertyNames() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    String[] propertyNames = pointer.getPropertyNames();
    assertNotNull(propertyNames);
    assertEquals(0, propertyNames.length);
  }
  
  @Test
  public void testAsPath() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setPropertyName("testName");
    pointer.setIndex(1);
    assertEquals("/testName[1]", pointer.asPath());
    pointer.setNameAttributeValue("otherName");
    assertEquals("/otherName[@name='testName'][2]", pointer.asPath());
  }
}