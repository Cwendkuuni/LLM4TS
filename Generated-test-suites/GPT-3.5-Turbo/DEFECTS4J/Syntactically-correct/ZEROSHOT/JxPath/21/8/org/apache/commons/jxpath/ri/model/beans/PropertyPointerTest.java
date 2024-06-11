import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PropertyPointerTest {
  
  private PropertyPointer propertyPointer;

  @Before
  public void setUp() {
    propertyPointer = new PropertyPointer(new NodePointer()) {
      
      @Override
      public String getPropertyName() {
        return "testProperty";
      }
      
      @Override
      public void setPropertyName(String propertyName) {
        // do nothing
      }
      
      @Override
      public int getPropertyCount() {
        return 1;
      }
      
      @Override
      public String[] getPropertyNames() {
        return new String[]{"testProperty"};
      }
      
      @Override
      protected boolean isActualProperty() {
        return true;
      }
    };
  }
  
  @Test
  public void testGetPropertyIndex() {
    assertEquals(Integer.MIN_VALUE, propertyPointer.getPropertyIndex());
  }
  
  @Test
  public void testSetPropertyIndex() {
    propertyPointer.setPropertyIndex(0);
    assertEquals(0, propertyPointer.getPropertyIndex());
    
    propertyPointer.setPropertyIndex(1);
    assertEquals(1, propertyPointer.getPropertyIndex());
  }
  
  @Test
  public void testGetBean() {
    assertNotNull(propertyPointer.getBean());
  }
  
  @Test
  public void testGetName() {
    assertEquals("testProperty", propertyPointer.getName().getName());
    assertNull(propertyPointer.getName().getPrefix());
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
    assertFalse(propertyPointer.isCollection());
  }
  
  @Test
  public void testIsLeaf() {
    assertTrue(propertyPointer.isLeaf());
  }
  
  @Test
  public void testGetLength() {
    assertEquals(-1, propertyPointer.getLength());
  }
  
  @Test
  public void testGetImmediateValuePointer() {
    assertNotNull(propertyPointer.getImmediateValuePointer());
  }
  
  @Test
  public void testCreatePath() {
    assertNotNull(propertyPointer.createPath(null));
  }
  
  @Test
  public void testCreatePathWithValue() {
    assertNotNull(propertyPointer.createPath(null, null));
  }
  
  @Test
  public void testCreateChildWithIndex() {
    assertNotNull(propertyPointer.createChild(null, null, 0));
  }
  
  @Test
  public void testCreateChildWithValue() {
    assertNotNull(propertyPointer.createChild(null, null, 0, null));
  }
  
  @Test
  public void testHashCode() {
    assertNotNull(propertyPointer.hashCode());
  }
  
  @Test
  public void testEquals() {
    assertTrue(propertyPointer.equals(propertyPointer));
    
    assertFalse(propertyPointer.equals(null));
    assertFalse(propertyPointer.equals(new Object()));
    
    PropertyPointer other = new PropertyPointer(new NodePointer()) {
      
      @Override
      public String getPropertyName() {
        return "testProperty";
      }
      
      @Override
      public void setPropertyName(String propertyName) {
        // do nothing
      }
      
      @Override
      public int getPropertyCount() {
        return 1;
      }
      
      @Override
      public String[] getPropertyNames() {
        return new String[]{"testProperty"};
      }
      
      @Override
      protected boolean isActualProperty() {
        return true;
      }
    };
    assertFalse(propertyPointer.equals(other));
    
    other.setPropertyIndex(0);
    assertFalse(propertyPointer.equals(other));
    
    other.setPropertyIndex(Integer.MIN_VALUE);
    assertTrue(propertyPointer.equals(other));
  }
  
  @Test
  public void testCompareChildNodePointers() {
    assertEquals(-1, propertyPointer.compareChildNodePointers(null, null));
  }
}