import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

  @Test
  public void testGetName() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    QName expected = new QName("*");
    Assert.assertEquals(expected, nullProp.getName());
  }

  @Test
  public void testSetPropertyIndex() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    // No assertions, method has no return value
    nullProp.setPropertyIndex(0);
  }

  @Test
  public void testGetLength() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    Assert.assertEquals(0, nullProp.getLength());
  }

  @Test
  public void testGetBaseValue() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    Assert.assertNull(nullProp.getBaseValue());
  }

  @Test
  public void testGetImmediateNode() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    Assert.assertNull(nullProp.getImmediateNode());
  }

  @Test
  public void testIsLeaf() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    Assert.assertTrue(nullProp.isLeaf());
  }

  @Test
  public void testGetValuePointer() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    NullPointer expected = new NullPointer(nullProp,  new QName(nullProp.getPropertyName()));
    Assert.assertEquals(expected, nullProp.getValuePointer());
  }

  @Test
  public void testIsActualProperty() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    Assert.assertFalse(nullProp.isActualProperty());
  }

  @Test
  public void testIsActual() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    Assert.assertFalse(nullProp.isActual());
  }

  @Test
  public void testIsContainer() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    Assert.assertTrue(nullProp.isContainer());
  }

  @Test(expected = JXPathInvalidAccessException.class)
  public void testSetValue1() {
    NodePointer parent = null;
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    nullProp.setValue(new Object());
  }

  @Test(expected = JXPathInvalidAccessException.class)
  public void testSetValue2() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    nullProp.setValue(new Object());
  }

  @Test
  public void testCreatePath1() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    JXPathContext context = new JXPathContext(null);
    NodePointer expected = parent;
    Assert.assertEquals(expected, nullProp.createPath(context));
  }

  @Test
  public void testCreatePath2() {
    NodePointer parent = new NullPointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    JXPathContext context = new JXPathContext(null);
    NodePointer expected = parent.createPath(context);
    expected.createAttribute(context, nullProp.getName());
    NodePointer result = nullProp.createPath(context);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreatePath3() {
    NodePointer parent = new NullPointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    JXPathContext context = new JXPathContext(null);
    NodePointer expected = parent.createPath(context);
    expected.createChild(context, nullProp.getName(), nullProp.getIndex());
    NodePointer result = nullProp.createPath(context);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreatePath4() {
    NodePointer parent = new PropertyOwnerPointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    JXPathContext context = new JXPathContext(null);
    NodePointer expected = parent.getPropertyPointer();
    expected.createChild(context, nullProp.getName(), nullProp.getIndex());
    NodePointer result = nullProp.createPath(context);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreatePath5() {
    NodePointer parent = new PropertyOwnerPointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    JXPathContext context = new JXPathContext(null);
    NodePointer expected = parent.getPropertyPointer();
    expected.createAttribute(context, nullProp.getName());
    NodePointer result = nullProp.createPath(context);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreatePath6() {
    NodePointer parent = new PropertyOwnerPointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    JXPathContext context = new JXPathContext(null);
    NodePointer expected = parent.getPropertyPointer();
    expected.createChild(context, nullProp.getName(), nullProp.getIndex(),
        new Object());
    NodePointer result = nullProp.createPath(context, new Object());
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateChild1() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    JXPathContext context = new JXPathContext(null);
    QName name = nullProp.getName();
    int index = nullProp.getIndex();
    NodePointer expected = nullProp.createPath(context)
        .createChild(context, name, index);
    NodePointer result = nullProp.createChild(context, name, index);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testCreateChild2() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    JXPathContext context = new JXPathContext(null);
    QName name = nullProp.getName();
    int index = nullProp.getIndex();
    Object value = new Object();
    NodePointer expected = nullProp.createPath(context)
        .createChild(context, name, index, value);
    NodePointer result = nullProp.createChild(context, name, index, value);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testGetPropertyName() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    String expected = "*";
    Assert.assertEquals(expected, nullProp.getPropertyName());
  }

  @Test
  public void testSetPropertyName() {
    NodePointer parent = new NodePointer(null);
    NullPropertyPointer nullProp = new NullPropertyPointer(parent);
    String propertyName = "test";
    nullProp.setPropertyName(propertyName);
    Assert.assertEquals(propertyName, nullProp.getPropertyName());
  }
}