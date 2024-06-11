import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

  @Test
  public void testGetName() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertNull(pointer.getName());
  }
  
  @Test
  public void testSetPropertyIndex() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setPropertyIndex(0);
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
  public void testGetValuePointer() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertNotNull(pointer.getValuePointer());
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
  
  @Test(expected = JXPathInvalidAccessException.class)
  public void testSetValueNoParent() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setValue("");
  }
  
  @Test
  public void testSetValueDynamicPropertyDeclarationSupported() {
    NodePointer parent = new PropertyOwnerPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    pointer.setValue("");
  }
  
  @Test(expected = JXPathInvalidAccessException.class)
  public void testSetValueNotChangeableLocation() {
    NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer(null));
    pointer.setValue("");
  }
  
  @Test
  public void testCreatePathAttribute() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    pointer.createPath(context);
  }
  
  @Test
  public void testCreatePathNotAttribute() {
    NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer(null));
    JXPathContext context = new JXPathContext();
    pointer.createPath(context);
  }
  
  @Test
  public void testCreatePathWithValueAttribute() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    pointer.createPath(context, "");
  }
  
  @Test
  public void testCreatePathWithValueNotAttribute() {
    NullPropertyPointer pointer = new NullPropertyPointer(new NullPointer(null));
    JXPathContext context = new JXPathContext();
    pointer.createPath(context, "");
  }
  
  @Test
  public void testCreateChild() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    QName name = new QName("");
    pointer.createChild(context, name, 0);
  }
  
  @Test
  public void testCreateChildWithValue() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    QName name = new QName("");
    pointer.createChild(context, name, 0, "");
  }
  
  @Test
  public void testGetPropertyName() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertEquals("*", pointer.getPropertyName());
  }
  
  @Test
  public void testSetPropertyName() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setPropertyName("");
  }
  
  @Test
  public void testSetNameAttributeValue() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setNameAttributeValue("");
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
    assertNotNull(pointer.getPropertyNames());
  }
  
  @Test
  public void testAsPath() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    assertNotNull(pointer.asPath());
  }
  
  private class PropertyOwnerPointer extends NodePointer {
    public PropertyPointer getPropertyPointer() {
      return new PropertyPointer(null);
    }
    
    public boolean isDynamicPropertyDeclarationSupported() {
      return true;
    }
    
    public NodePointer createAttribute(JXPathContext context, QName qname) {
      return new NullPropertyPointer(null);
    }
    
    public NodePointer createChild(JXPathContext context, QName qname, int index) {
      return new NullPropertyPointer(null);
    }
    
    public NodePointer createChild(JXPathContext context, QName qname, int index, Object value) {
      return new NullPropertyPointer(null);
    }
  }
  
  private class PropertyPointer extends NodePointer {
    public PropertyPointer(NodePointer parent) {
      super(parent);
    }
    
    public void setPropertyName(String propertyName) {
      
    }
    
    public void setValue(Object value) {
      
    }
  }
  
  private class NullPointer extends NodePointer {
    public NullPointer(NodePointer parent) {
      super(parent);
    }
    
    public NodePointer createAttribute(JXPathContext context, QName qname) {
      return new NullPropertyPointer(null);
    }
    
    public NodePointer createChild(JXPathContext context, QName qname, int index) {
      return new NullPropertyPointer(null);
    }
    
    public NodePointer createChild(JXPathContext context, QName qname, int index, Object value) {
      return new NullPropertyPointer(null);
    }
  }
}