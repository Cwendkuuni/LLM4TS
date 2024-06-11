import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

  // Test for getName() method
  @Test
  public void testGetName() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertEquals(new QName("*"), nullPropertyPointer.getName());
  }

  // Test for getBaseValue() method
  @Test
  public void testGetBaseValue() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertNull(nullPropertyPointer.getBaseValue());
  }

  // Test for getImmediateNode() method
  @Test
  public void testGetImmediateNode() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertNull(nullPropertyPointer.getImmediateNode());
  }

  // Test for isLeaf() method
  @Test
  public void testIsLeaf() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertTrue(nullPropertyPointer.isLeaf());
  }

  // Test for isActualProperty() method
  @Test
  public void testIsActualProperty() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertFalse(nullPropertyPointer.isActualProperty());
  }

  // Test for isActual() method
  @Test
  public void testIsActual() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertFalse(nullPropertyPointer.isActual());
  }

  // Test for isContainer() method
  @Test
  public void testIsContainer() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertTrue(nullPropertyPointer.isContainer());
  }

  // Test for createPath() method
  @Test
  public void testCreatePath() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    NodePointer newParent = nullPropertyPointer.createPath(context);
    assertEquals(newParent, context.getPointerFactory().createNodePointer(null, null));
  }

  // Test for createPath() method when attribute is true
  @Test
  public void testCreatePathWithAttribute() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    nullPropertyPointer.setIsAttribute(true);
    NodePointer newParent = nullPropertyPointer.createPath(context);
    assertEquals(newParent, context.getPointerFactory().createAttributePointer(null, null));
  }

  // Test for createPath() method when parent is PropertyOwnerPointer
  @Test
  public void testCreatePathWithPropertyOwnerPointer() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    NodePointer newParent = nullPropertyPointer.createPath(context);
    nullPropertyPointer.setParent(new PropertyOwnerPointer());
    NodePointer result = nullPropertyPointer.createPath(context);
    assertEquals(newParent, context.getPointerFactory().createNodePointer(null, null));
    assertEquals(result, context.getPointerFactory().createNodePointer(null, null));
  }

  // Test for createPath() method with value
  @Test
  public void testCreatePathWithValue() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    NodePointer newParent = nullPropertyPointer.createPath(context);
    Object value = new Object();
    NodePointer result = nullPropertyPointer.createPath(context, value);
    assertEquals(newParent, context.getPointerFactory().createNodePointer(null, null));
    assertEquals(result, context.getPointerFactory().createNodePointer(null, null));
    assertEquals(result.getValue(), value);
  }

  // Test for createPath() method with value when attribute is true
  @Test
  public void testCreatePathWithValueAndAttribute() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    nullPropertyPointer.setIsAttribute(true);
    NodePointer newParent = nullPropertyPointer.createPath(context);
    Object value = new Object();
    NodePointer result = nullPropertyPointer.createPath(context, value);
    assertEquals(newParent, context.getPointerFactory().createAttributePointer(null, null));
    assertEquals(result.getValue(), value);
  }

  // Test for createPath() method with PropertyOwnerPointer as parent
  @Test
  public void testCreatePathWithValueAndPropertyOwnerPointer() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    NodePointer newParent = nullPropertyPointer.createPath(context);
    nullPropertyPointer.setParent(new PropertyOwnerPointer());
    Object value = new Object();
    NodePointer result = nullPropertyPointer.createPath(context, value);
    assertEquals(newParent, context.getPointerFactory().createNodePointer(null, null));
    assertEquals(result, context.getPointerFactory().createNodePointer(null, null));
    assertEquals(result.getValue(), value);
  }

  // Test for createChild() method
  @Test
  public void testCreateChild() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    QName name = new QName("*");
    NodePointer newParent = nullPropertyPointer.createPath(context);
    NodePointer result = nullPropertyPointer.createChild(context, name, 0);
    assertEquals(newParent, context.getPointerFactory().createNodePointer(null, null));
    assertEquals(result, context.getPointerFactory().createNodePointer(null, name));
  }

  // Test for createChild() method with value
  @Test
  public void testCreateChildWithValue() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    JXPathContext context = new JXPathContext();
    QName name = new QName("*");
    NodePointer newParent = nullPropertyPointer.createPath(context);
    Object value = new Object();
    NodePointer result = nullPropertyPointer.createChild(context, name, 0, value);
    assertEquals(newParent, context.getPointerFactory().createNodePointer(null, null));
    assertEquals(result, context.getPointerFactory().createNodePointer(null, name));
    assertEquals(result.getValue(), value);
  }

  // Test for getPropertyName() method
  @Test
  public void testGetPropertyName() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertEquals("*", nullPropertyPointer.getPropertyName());
  }

  // Test for setPropertyName() method
  @Test
  public void testSetPropertyName() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    nullPropertyPointer.setPropertyName("testProperty");
    assertEquals("testProperty", nullPropertyPointer.getPropertyName());
  }

  // Test for setNameAttributeValue() method
  @Test
  public void testSetNameAttributeValue() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    nullPropertyPointer.setNameAttributeValue("testAttribute");
    assertEquals("testAttribute", nullPropertyPointer.getPropertyName());
    assertTrue(nullPropertyPointer.isAttributeName());
  }

  // Test for isCollection() method
  @Test
  public void testIsCollection() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    nullPropertyPointer.setPropertyIndex(0);
    assertTrue(nullPropertyPointer.isCollection());
    nullPropertyPointer.setPropertyIndex(-1);
    assertFalse(nullPropertyPointer.isCollection());
  }

  // Test for getPropertyCount() method
  @Test
  public void testGetPropertyCount() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertEquals(0, nullPropertyPointer.getPropertyCount());
  }

  // Test for getPropertyNames() method
  @Test
  public void testGetPropertyNames() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
  }

  // Test for asPath() method
  @Test
  public void testAsPath() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    assertEquals("/*/[@name='*']", nullPropertyPointer.asPath());
    nullPropertyPointer.setNameAttributeValue("testAttribute");
    assertEquals("/*/[@name='testAttribute']", nullPropertyPointer.asPath());
  }

  // Test for escape() method
  @Test
  public void testEscape() {
    NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    String stringWithQuotes = "This is a 'string' with \"quotes\"";
    String expected = "This is a &apos;string&apos; with &quot;quotes&quot;";
    assertEquals(expected, nullPropertyPointer.escape(stringWithQuotes));
  }
}