import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyPointerTest {
  
  private PropertyPointer propertyPointer;
  private NodePointer parentPointer;
  
  @Before
  public void setUp() {
    parentPointer = new NodePointer();
    propertyPointer = new PropertyPointerImpl(parentPointer);
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
    // TODO: Test when parent pointer is set to an object
  }
  
  @Test
  public void testGetName() {
    // TODO: Test when parent pointer is set to an object
  }
  
  @Test
  public void testGetPropertyName() {
    // TODO: Test when property name is set
  }
  
  @Test
  public void testSetPropertyName() {
    String propertyName = "test";
    propertyPointer.setPropertyName(propertyName);
    assertEquals(propertyName, propertyPointer.getPropertyName());
  }
  
  @Test
  public void testGetPropertyCount() {
    // TODO: Test when properties are set
  }
  
  @Test
  public void testGetPropertyNames() {
    // TODO: Test when properties are set
  }
  
  @Test
  public void testIsActualProperty() {
    // TODO: Test when property is set
  }
  
  @Test
  public void testIsActual() {
    // TODO: Test when property is set and pointer is actual
  }
  
  @Test
  public void testGetImmediateNode() {
    // TODO: Test when property is set
  }
  
  @Test
  public void testIsCollection() {
    // TODO: Test when property is a collection
  }
  
  @Test
  public void testIsLeaf() {
    // TODO: Test when property is a leaf node
  }
  
  @Test
  public void testGetLength() {
    // TODO: Test when property is a collection
  }
  
  @Test
  public void testGetImmediateValuePointer() {
    // TODO: Implement this test case
  }
  
  @Test
  public void testCreatePath() {
    // TODO: Implement this test case
  }
  
  @Test
  public void testCreatePathWithValue() {
    // TODO: Implement this test case
  }
  
  @Test
  public void testCreateChildWithIndex() {
    // TODO: Implement this test case
  }
  
  @Test
  public void testCreateChildWithoutIndex() {
    // TODO: Implement this test case
  }
  
  @Test
  public void testHashCode() {
    // TODO: Implement this test case
  }
  
  @Test
  public void testEquals() {
    // TODO: Implement this test case
  }
  
  @Test
  public void testCompareChildNodePointers() {
    // TODO: Implement this test case
  }
}