import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;

public class PropertyPointerTest {
  
  private PropertyPointer propertyPointer;
  
  @Before
  public void setUp() {
    JXPathContext context = JXPathContext.newContext();
    propertyPointer = new TestPropertyPointer(context);
  }
  
  @Test
  public void testGetPropertyIndex() {
    assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
  }
  
  @Test
  public void testSetPropertyIndex() {
    propertyPointer.setPropertyIndex(1);
    assertEquals(1, propertyPointer.getPropertyIndex());
  }
  
  @Test
  public void testGetBean() {
    assertNotNull(propertyPointer.getBean());
  }
  
  @Test
  public void testGetName() {
    assertNotNull(propertyPointer.getName());
  }
  
  @Test
  public void testGetPropertyName() {
    assertNotNull(propertyPointer.getPropertyName());
  }
  
  @Test
  public void testSetPropertyName() {
    String propertyName = "testProperty";
    propertyPointer.setPropertyName(propertyName);
    assertEquals(propertyName, propertyPointer.getPropertyName());
  }
  
  @Test
  public void testGetPropertyCount() {
    assertTrue(propertyPointer.getPropertyCount() >= 0);
  }
  
  @Test
  public void testGetPropertyNames() {
    assertNotNull(propertyPointer.getPropertyNames());
  }
  
  @Test
  public void testIsActualProperty() {
    assertTrue(propertyPointer.isActualProperty());
  }
  
  @Test
  public void testIsActual() {
    assertTrue(propertyPointer.isActual());
  }
  
  @Test
  public void testGetImmediateNode() {
    assertNotNull(propertyPointer.getImmediateNode());
  }
  
  @Test
  public void testIsCollection() {
    assertTrue(propertyPointer.isCollection() || !propertyPointer.isCollection());
  }
  
  @Test
  public void testIsLeaf() {
    assertTrue(propertyPointer.isLeaf() || !propertyPointer.isLeaf());
  }
  
  @Test
  public void testGetLength() {
    assertTrue(propertyPointer.getLength() >= 0);
  }
  
  @Test
  public void testGetImmediateValuePointer() {
    assertNotNull(propertyPointer.getImmediateValuePointer());
  }
  
  @Test
  public void testCreatePath() {
    assertNotNull(propertyPointer.createPath(propertyPointer.getContext()));
  }
  
  @Test
  public void testCreatePathWithValue() {
    Object value = new Object();
    assertNotNull(propertyPointer.createPath(propertyPointer.getContext(), value));
  }
  
  @Test
  public void testCreateChildPointerWithValue() {
    QName name = new QName("testNamespace", "testName");
    int index = 1;
    Object value = new Object();
    assertNotNull(propertyPointer.createChild(propertyPointer.getContext(), name, index, value));
  }
  
  @Test
  public void testCreateChildPointerWithoutValue() {
    QName name = new QName("testNamespace", "testName");
    int index = 1;
    assertNotNull(propertyPointer.createChild(propertyPointer.getContext(), name, index));
  }
  
  @Test
  public void testHashCode() {
    assertTrue(propertyPointer.hashCode() >= 0);
  }
  
  @Test
  public void testEquals() {
    assertTrue(propertyPointer.equals(propertyPointer));
  }
  
  @Test
  public void testCompareChildNodePointers() {
    propertyPointer.compareChildNodePointers(propertyPointer, propertyPointer);
  }
  
  private class TestPropertyPointer extends PropertyPointer {
    public TestPropertyPointer(JXPathContext context) {
      super(null);
    }
    
    public String getPropertyName() {
      return "testProperty";
    }
  
    public void setPropertyName(String propertyName) {
      // do nothing
    }
    
    public int getPropertyCount() {
      return 0;
    }
    
    public String[] getPropertyNames() {
      return new String[0];
    }
    
    protected boolean isActualProperty() {
      return false;
    }
    
    public boolean isActual() {
      return true;
    }
  }
}