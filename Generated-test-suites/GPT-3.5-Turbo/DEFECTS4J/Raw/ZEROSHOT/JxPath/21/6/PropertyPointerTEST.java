import org.junit.*;
import static org.junit.Assert.*;

public class PropertyPointerTest {
  
  @Test
  public void getPropertyIndexTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    int index = propertyPointer.getPropertyIndex();
    assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, index);
  }
  
  @Test
  public void setPropertyIndexTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    propertyPointer.setPropertyIndex(1);   
    int index = propertyPointer.getPropertyIndex();
    assertEquals(1, index);
  }
  
  @Test
  public void getBeanTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    Object bean = propertyPointer.getBean();
    assertNull(bean);
  }
  
  @Test
  public void getNameTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    QName name = propertyPointer.getName();
    assertNull(name.getNamespace());
    assertNull(name.getName());
  }
  
  @Test
  public void getPropertyNameTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    String propertyName = propertyPointer.getPropertyName();
    assertNull(propertyName);
  }
  
  @Test
  public void setPropertyNameTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    propertyPointer.setPropertyName("test");   
    String propertyName = propertyPointer.getPropertyName();
    assertEquals("test", propertyName);
  }
  
  @Test
  public void getPropertyCountTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    int count = propertyPointer.getPropertyCount();
    assertEquals(0, count);
  }
  
  @Test
  public void getPropertyNamesTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    String[] names = propertyPointer.getPropertyNames();
    assertNotNull(names);
    assertEquals(0, names.length);
  }
  
  @Test
  public void isActualPropertyTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    boolean isActual = propertyPointer.isActualProperty();
    assertFalse(isActual);
  }
  
  @Test
  public void isActualTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    boolean isActual = propertyPointer.isActual();
    assertFalse(isActual);
  }
  
  @Test
  public void getImmediateNodeTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    Object value = propertyPointer.getImmediateNode();
    assertNull(value);
  }
  
  @Test
  public void isCollectionTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    boolean isCollection = propertyPointer.isCollection();
    assertFalse(isCollection);
  }
  
  @Test
  public void isLeafTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    boolean isLeaf = propertyPointer.isLeaf();
    assertTrue(isLeaf);
  }
  
  @Test
  public void getLengthTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    int length = propertyPointer.getLength();
    assertEquals(-1, length);
  }
  
  @Test
  public void getImmediateValuePointerTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    NodePointer valuePointer = propertyPointer.getImmediateValuePointer();
    assertNotNull(valuePointer);
  }
  
  @Test
  public void createPathTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    NodePointer newPath = propertyPointer.createPath(new JXPathContext());
    assertNotNull(newPath);
  }
  
  @Test
  public void createPathWithValueTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    NodePointer newPath = propertyPointer.createPath(new JXPathContext(), "test");
    assertNotNull(newPath);
  }
  
  @Test
  public void createChildWithValueTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    NodePointer childPointer = propertyPointer.createChild(new JXPathContext(), new QName("test"), 0, "child");
    assertNotNull(childPointer);
  }
  
  @Test
  public void createChildTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    NodePointer childPointer = propertyPointer.createChild(new JXPathContext(), new QName("test"), 0);
    assertNotNull(childPointer);
  }
  
  @Test
  public void hashCodeTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    int hashCode = propertyPointer.hashCode();
    assertTrue(hashCode >= 0);
  }
  
  @Test
  public void equalsTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer1 = new PropertyPointer(parentPointer);
    PropertyPointer propertyPointer2 = new PropertyPointer(parentPointer);
    
    boolean equals = propertyPointer1.equals(propertyPointer2);
    assertTrue(equals);
  }
  
  @Test
  public void compareChildNodePointersTest() {
    NodePointer parentPointer = new NodePointer();
    PropertyPointer propertyPointer = new PropertyPointer(parentPointer);
    
    NodePointer pointer1 = new NodePointer();
    NodePointer pointer2 = new NodePointer();
    
    int compare = propertyPointer.compareChildNodePointers(pointer1, pointer2);
    assertEquals(0, compare);
  }
  
}