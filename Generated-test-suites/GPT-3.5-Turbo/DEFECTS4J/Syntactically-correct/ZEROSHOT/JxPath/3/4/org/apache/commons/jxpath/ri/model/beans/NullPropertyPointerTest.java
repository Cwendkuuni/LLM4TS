import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

  private NullPropertyPointer nullPropertyPointer;
  
  @Before
  public void setUp() {
    NullPropertyPointer parent = new NullPropertyPointer(null);
    nullPropertyPointer = new NullPropertyPointer(parent);
  }
  
  @Test
  public void testGetName() {
    assertNull(nullPropertyPointer.getName());
  }
  
  @Test
  public void testSetPropertyIndex() {
    nullPropertyPointer.setPropertyIndex(5);
    // assert something
  }
  
  @Test
  public void testGetLength() {
    assertEquals(0, nullPropertyPointer.getLength());
  }
  
  @Test
  public void testGetBaseValue() {
    assertNull(nullPropertyPointer.getBaseValue());
  }
  
  @Test
  public void testGetImmediateNode() {
    assertNull(nullPropertyPointer.getImmediateNode());
  }
  
  @Test
  public void testIsLeaf() {
    assertTrue(nullPropertyPointer.isLeaf());
  }
  
  @Test
  public void testGetValuePointer() {
    NodePointer valuePointer = nullPropertyPointer.getValuePointer();
    // assert something
  }
  
  @Test
  public void testIsActualProperty() {
    assertFalse(nullPropertyPointer.isActualProperty());
  }
  
  @Test
  public void testIsActual() {
    assertFalse(nullPropertyPointer.isActual());
  }
  
  @Test
  public void testIsContainer() {
    assertTrue(nullPropertyPointer.isContainer());
  }
  
  @Test(expected = JXPathInvalidAccessException.class)
  public void testSetValueWithNullParent() {
    nullPropertyPointer.setValue("value");
  }
  
  @Test(expected = JXPathInvalidAccessException.class)
  public void testSetValueWithDynamicPropertyDeclarationNotSupported() {
    PropertyOwnerPointer parent = new PropertyOwnerPointer(null);
    nullPropertyPointer.setParent(parent);
    nullPropertyPointer.setValue("value");
  }
  
  @Test
  public void testCreatePath() {
    JXPathContext context = new JXPathContext(null);
    NodePointer newPath = nullPropertyPointer.createPath(context);
    // assert something
  }
  
  @Test
  public void testCreatePathWithValue() {
    JXPathContext context = new JXPathContext(null);
    NodePointer newPath = nullPropertyPointer.createPath(context, "value");
    // assert something
  }
  
  @Test
  public void testCreateChildWithNameAndIndex() {
    JXPathContext context = new JXPathContext(null);
    QName name = new QName("name");
    int index = 5;
    NodePointer newChild = nullPropertyPointer.createChild(context, name, index);
    // assert something
  }
  
  @Test
  public void testCreateChildWithNameAndIndexValue() {
    JXPathContext context = new JXPathContext(null);
    QName name = new QName("name");
    int index = 5;
    NodePointer newChild = nullPropertyPointer.createChild(context, name, index, "value");
    // assert something
  }
  
  @Test
  public void testGetPropertyName() {
    nullPropertyPointer.setPropertyName("name");
    assertEquals("name", nullPropertyPointer.getPropertyName());
  }
  
  @Test
  public void testSetPropertyName() {
    nullPropertyPointer.setPropertyName("name");
    assertEquals("name", nullPropertyPointer.getPropertyName());
  }
  
  @Test
  public void testSetNameAttributeValue() {
    nullPropertyPointer.setNameAttributeValue("attributeValue");
    // assert something
  }
  
  @Test
  public void testIsCollection() {
    nullPropertyPointer.setPropertyIndex(5);
    assertTrue(nullPropertyPointer.isCollection());
  }
  
  @Test
  public void testGetPropertyCount() {
    assertEquals(0, nullPropertyPointer.getPropertyCount());
  }
  
  @Test
  public void testGetPropertyNames() {
    String[] propertyNames = nullPropertyPointer.getPropertyNames();
    assertEquals(0, propertyNames.length);
  }
  
  @Test
  public void testAsPath() {
    String path = nullPropertyPointer.asPath();
    // assert something
  }
  
  @Test
  public void testEscape() {
    String escapedString = nullPropertyPointer.escape("string");
    // assert something
  }

}