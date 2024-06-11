import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {
  
  @Test
  public void testGetPropertyIndex() {
    PropertyPointer pointer = new PropertyPointer();
    pointer.setPropertyIndex(5);
    assertEquals(5, pointer.getPropertyIndex());
  }
  
  @Test
  public void testSetPropertyIndex() {
    PropertyPointer pointer = new PropertyPointer();
    pointer.setPropertyIndex(5);
    assertEquals(5, pointer.getPropertyIndex());
    
    pointer.setPropertyIndex(10);
    assertEquals(10, pointer.getPropertyIndex());
  }
  
  @Test
  public void testGetBean() {
    PropertyPointer pointer = new PropertyPointer();
    pointer.setBean(new Object());
    assertEquals(new Object(), pointer.getBean());
  }
  
  @Test
  public void testGetName() {
    PropertyPointer pointer = new PropertyPointer();
    pointer.setPropertyName("testProperty");
    
    QName expectedName = new QName(null, "testProperty");
    assertEquals(expectedName, pointer.getName());
  }
  
  @Test
  public void testGetPropertyCount() {
    PropertyPointer pointer = new PropertyPointer();
    int count = pointer.getPropertyCount();
    assertEquals(0, count);
  }
  
  @Test
  public void testGetPropertyNames() {
    PropertyPointer pointer = new PropertyPointer();
    String[] names = pointer.getPropertyNames();
    assertEquals(0, names.length);
  }
  
  @Test
  public void testIsActualProperty() {
    PropertyPointer pointer = new PropertyPointer() {
      protected boolean isActualProperty() {
        return true;
      }
    };
    
    assertEquals(true, pointer.isActual());
  }
  
  @Test
  public void testGetImmediateNode() {
    PropertyPointer pointer = new PropertyPointer();
    Object node = new Object();
    pointer.setNode(node);
    
    assertEquals(node, pointer.getImmediateNode());
  }
  
  @Test
  public void testIsCollection() {
    PropertyPointer pointer = new PropertyPointer();
    
    assertEquals(false, pointer.isCollection());
    
    pointer.setNode(new ArrayList());
    assertEquals(true, pointer.isCollection());
  }
  
  @Test
  public void testIsLeaf() {
    PropertyPointer pointer = new PropertyPointer();
    
    assertEquals(true, pointer.isLeaf());
    
    pointer.setNode(new Object());
    assertEquals(false, pointer.isLeaf());
  }
  
  @Test
  public void testGetLength() {
    PropertyPointer pointer = new PropertyPointer();
    
    assertEquals(-1, pointer.getLength());
    
    pointer.setNode(new ArrayList());
    assertEquals(0, pointer.getLength());
  }
  
  @Test
  public void testGetImmediateValuePointer() {
    PropertyPointer pointer = new PropertyPointer();
    pointer.setPropertyName("testProperty");
    pointer.setNode(new Object());
    
    QName expectedName = new QName(null, "testProperty");
    NodePointer valuePointer = pointer.getImmediateValuePointer();
    
    assertEquals(expectedName, valuePointer.getName());
    assertEquals(pointer.getNode(), valuePointer.getNode());
  }
  
  @Test
  public void testCreatePath() {
    PropertyPointer pointer = new PropertyPointer();
    JXPathContext context = new JXPathContext();
    
    pointer.createPath(context);
    
    assertNotNull(pointer.getImmediateNode());
  }
  
  @Test
  public void testCreatePathWithValue() {
    PropertyPointer pointer = new PropertyPointer();
    JXPathContext context = new JXPathContext();
    Object value = new Object();
    
    pointer.createPath(context, value);
    
    assertNotNull(pointer.getImmediateNode());
    assertEquals(value, pointer.getNode());
  }
  
  @Test
  public void testCreateChildWithPath() {
    PropertyPointer parent = new PropertyPointer();
    parent.setPropertyName("parentProperty");
    parent.setNode(new Object());
    
    QName expectedName = new QName(null, "childProperty");
    int index = 1;
    Object value = new Object();
    
    NodePointer child = parent.createChild(context, expectedName, index, value);
    
    assertNotNull(child.getBaseValue());
    assertEquals(expectedName, child.getName());
    assertEquals(value, child.getNode());
  }
  
  @Test
  public void testCreateChildWithoutPath() {
    PropertyPointer parent = new PropertyPointer();
    parent.setPropertyName("parentProperty");
    parent.setNode(new Object());
    
    QName expectedName = new QName(null, "childProperty");
    int index = 1;
    
    NodePointer child = parent.createChild(context, expectedName, index);
    
    assertNotNull(child.getBaseValue());
    assertEquals(expectedName, child.getName());
    assertNull(child.getNode());
  }
  
  @Test
  public void testHashCode() {
    PropertyPointer pointer = new PropertyPointer();
    int hashCode = pointer.hashCode();
    
    assertEquals(pointer.getParentPointer().hashCode(), hashCode);
  }
  
  @Test
  public void testEquals() {
    PropertyPointer pointer1 = new PropertyPointer();
    PropertyPointer pointer2 = new PropertyPointer();
    
    assertEquals(true, pointer1.equals(pointer1));
    assertEquals(false, pointer1.equals(null));
    assertEquals(false, pointer1.equals(new Object()));
    assertEquals(true, pointer1.equals(pointer2));
    
    pointer1.setPropertyIndex(5);
    pointer2.setPropertyIndex(5);
    
    assertEquals(true, pointer1.equals(pointer2));
    
    pointer2.setPropertyIndex(10);
    
    assertEquals(false, pointer1.equals(pointer2));
    
    pointer2.setPropertyIndex(5);
    pointer2.setPropertyName("testProperty");
    
    assertEquals(false, pointer1.equals(pointer2));
  }
  
  @Test
  public void testCompareChildNodePointers() {
    PropertyPointer pointer = new PropertyPointer();
    NodePointer pointer1 = new NodePointer();
    NodePointer pointer2 = new NodePointer();
    
    assertEquals(0, pointer.compareChildNodePointers(pointer1, pointer2));
  }

}