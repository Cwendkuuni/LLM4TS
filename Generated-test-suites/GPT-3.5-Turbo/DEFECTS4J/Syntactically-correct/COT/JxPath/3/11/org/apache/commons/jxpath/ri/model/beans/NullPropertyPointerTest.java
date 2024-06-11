import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {
  
  private NullPropertyPointer nullPropertyPointer;
  private JXPathContext context;
  
  @Before
  public void setUp() {
    NodePointer parent = new NodePointer();
    nullPropertyPointer = new NullPropertyPointer(parent);
    context = JXPathContext.newContext(null);
  }
  
  @Test
  public void testGetName() {
    assertEquals("*", nullPropertyPointer.getName().getName());
  }
  
  @Test
  public void testSetPropertyIndex() {
    nullPropertyPointer.setPropertyIndex(1);
    assertEquals(1, nullPropertyPointer.getIndex());
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
    assertTrue(valuePointer instanceof NullPointer);
    assertEquals("*", valuePointer.getName().getName());
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
  public void testSetValueWithNonContainerParent() {
    NodePointer parent = new NodePointer();
    parent.setContainer(false);
    nullPropertyPointer.setParent(parent);
    nullPropertyPointer.setValue("value");
  }
  
  @Test(expected = JXPathInvalidAccessException.class)
  public void testSetValueWithDynamicPropertyDeclarationNotSupported() {
    NodePointer parent = new PropertyOwnerPointer();
    nullPropertyPointer.setParent(parent);
    nullPropertyPointer.setValue("value");
  }
  
  @Test
  public void testCreatePath() {
    NodePointer newPath = nullPropertyPointer.createPath(context);
    assertTrue(newPath instanceof NullPointer);
    assertEquals("*", newPath.getName().getName());
  }
  
  @Test
  public void testCreatePathWithValue() {
    NodePointer newPath = nullPropertyPointer.createPath(context, "value");
    assertTrue(newPath instanceof NullPointer);
    assertEquals("*", newPath.getName().getName());
    assertEquals("value", newPath.getValue());
  }
  
  @Test
  public void testCreateChild() {
    NodePointer newChild = nullPropertyPointer.createChild(context, new QName("child"), 0);
    assertTrue(newChild instanceof NullPointer);
    assertEquals("child", newChild.getName().getName());
    assertEquals(0, newChild.getIndex());
  }
  
  @Test
  public void testCreateChildWithValue() {
    NodePointer newChild = nullPropertyPointer.createChild(context, new QName("child"), 0, "value");
    assertTrue(newChild instanceof NullPointer);
    assertEquals("child", newChild.getName().getName());
    assertEquals(0, newChild.getIndex());
    assertEquals("value", newChild.getValue());
  }
  
  @Test
  public void testGetPropertyName() {
    assertEquals("*", nullPropertyPointer.getPropertyName());
  }
  
  @Test
  public void testSetPropertyName() {
    nullPropertyPointer.setPropertyName("property");
    assertEquals("property", nullPropertyPointer.getPropertyName());
  }
  
  @Test
  public void testSetNameAttributeValue() {
    nullPropertyPointer.setNameAttributeValue("attributeValue");
    assertEquals("attributeValue", nullPropertyPointer.getPropertyName());
  }
  
  @Test
  public void testIsCollection() {
    nullPropertyPointer.setPropertyIndex(0);
    assertTrue(nullPropertyPointer.isCollection());
  }
  
  @Test
  public void testGetPropertyCount() {
    assertEquals(0, nullPropertyPointer.getPropertyCount());
  }
  
  @Test
  public void testGetPropertyNames() {
    assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
  }
  
  @Test
  public void testAsPath() {
    String asPath = nullPropertyPointer.asPath();
    assertEquals("/@", asPath);
  }
}