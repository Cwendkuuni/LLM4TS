import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {
  
  @Test
  public void testGetName() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    QName expected = new QName("*");
    QName actual = pointer.getName();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPropertyIndex() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    pointer.setPropertyIndex(1);
  }
  
  @Test
  public void testGetLength() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    int expected = 0;
    int actual = pointer.getLength();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetBaseValue() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    Object expected = null;
    Object actual = pointer.getBaseValue();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetImmediateNode() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    Object expected = null;
    Object actual = pointer.getImmediateNode();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsLeaf() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    boolean expected = true;
    boolean actual = pointer.isLeaf();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetValuePointer() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    NullPointer expected = new NullPointer(pointer, new QName(pointer.getPropertyName()));
    NullPointer actual = (NullPointer) pointer.getValuePointer();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsActualProperty() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    boolean expected = false;
    boolean actual = pointer.isActualProperty();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsActual() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    boolean expected = false;
    boolean actual = pointer.isActual();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsContainer() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    boolean expected = true;
    boolean actual = pointer.isContainer();
    
    assertEquals(expected, actual);
  }
  
  @Test(expected = JXPathInvalidAccessException.class)
  public void testSetValue_TargetObjectIsNull() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    
    pointer.setValue("testValue");
  }
  
  @Test(expected = JXPathInvalidAccessException.class)
  public void testSetValue_PathDoesNotMatchChangeableLocation() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    pointer.setValue("testValue");
  }
  
  @Test
  public void testCreatePath() {
    JXPathContext context = new JXPathContext();
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    NodePointer expected = parent.createPath(context);
    NodePointer actual = pointer.createPath(context);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCreatePathWithValue() {
    JXPathContext context = new JXPathContext();
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    NodePointer expected = parent.createPath(context);
    NodePointer actual = pointer.createPath(context, "testValue");
    
    assertEquals(expected, actual);
    assertEquals("testValue", actual.getValue());
  }
  
  @Test
  public void testCreateChild() {
    JXPathContext context = new JXPathContext();
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    QName name = new QName("testName");
    int index = 1;
    
    NodePointer expected = parent.createChild(context, name, index);
    NodePointer actual = pointer.createChild(context, name, index);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testCreateChildWithValue() {
    JXPathContext context = new JXPathContext();
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    QName name = new QName("testName");
    int index = 1;
    
    NodePointer expected = parent.createChild(context, name, index, "testValue");
    NodePointer actual = pointer.createChild(context, name, index, "testValue");
    
    assertEquals(expected, actual);
    assertEquals("testValue", actual.getValue());
  }
  
  @Test
  public void testGetPropertyName() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    String expected = "*";
    String actual = pointer.getPropertyName();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetPropertyName() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    pointer.setPropertyName("testName");
    
    String expected = "testName";
    String actual = pointer.getPropertyName();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testSetNameAttributeValue() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    pointer.setNameAttributeValue("testAttributeValue");
    
    String expected = "testAttributeValue";
    String actual = pointer.getPropertyName();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsCollection_IndexIsNotWholeCollection() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    pointer.setPropertyIndex(1);
    
    boolean expected = true;
    boolean actual = pointer.isCollection();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsCollection_IndexIsWholeCollection() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    boolean expected = false;
    boolean actual = pointer.isCollection();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetPropertyCount() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    int expected = 0;
    int actual = pointer.getPropertyCount();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetPropertyNames() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    
    String[] expected = new String[0];
    String[] actual = pointer.getPropertyNames();
    
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testAsPath_NotByNameAttribute() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    
    String expected = "/null";
    String actual = pointer.asPath();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testAsPath_ByNamedAttribute() {
    NullPropertyPointer pointer = new NullPropertyPointer(null);
    pointer.setNameAttributeValue("testName");
    
    String expected = "/null[@name='testName']";
    String actual = pointer.asPath();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testAsPath_WithIndex() {
    NodePointer parent = new NullPointer();
    NullPropertyPointer pointer = new NullPropertyPointer(parent);
    pointer.setPropertyIndex(1);
    
    String expected = "/null[1]";
    String actual = pointer.asPath();
    
    assertEquals(expected, actual);
  }
  
  private class NullPointer extends NodePointer {
    
    public NullPointer() {
      super(null);
    }
    
    public NullPointer(PropertyPointer parent, QName name) {
      super(parent);
    }
    
    public Object getBaseValue() {
      return null;
    }
    
    public Object getValue() {
      return null;
    }
    
    public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
      return 0;
    }
    
    public int hashCode() {
      return 0;
    }
    
    public boolean equals(Object object) {
      return false;
    }
    
    public String asPath() {
      return "/null";
    }
  }
}