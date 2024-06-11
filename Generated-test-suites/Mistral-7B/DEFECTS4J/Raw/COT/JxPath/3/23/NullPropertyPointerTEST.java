import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNull(nullPropertyPointer.getValuePointer().getNode());
        Assert.assertFalse(nullPropertyPointer.isActual());
        Assert.assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName name = nullPropertyPointer.getName();
        Assert.assertEquals("*", name.getLocalPart());
        Assert.assertNull(name.getNamespaceURI());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertFalse(nullPropertyPointer.isActual());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePathWithContainerParent() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());
        PropertyPointer propertyPointer = newParent.getPropertyPointer();
        Assert.assertEquals(nullPropertyPointer, propertyPointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithNonContainerParent() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = new NodePointer(null);
        Assert.assertNotSame(nullPropertyPointer, newParent);
        NodePointer child = nullPropertyPointer.createPath(new JXPathContext());
        Assert.assertNotSame(newParent, child);
        child.setValue("test");
        Assert.assertEquals("test", child.getValue());
    }

    @Test
    public void testCreatePathWithAttributeName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());
        Assert.assertNotSame(nullPropertyPointer, newParent);
        NodePointer attributePointer = nullPropertyPointer.createPath(new JXPathContext(), "test");
        Assert.assertEquals(newParent, attributePointer.getParentPointer());
        Assert.assertEquals("test", attributePointer.getName().getLocalPart());
    }

    @Test
    public void testCreateChildWithContainerParent() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());
        PropertyPointer propertyPointer = newParent.getPropertyPointer();
        Assert.assertEquals(nullPropertyPointer, propertyPointer);
        NodePointer child = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"));
        Assert.assertEquals(newParent, child.getParentPointer());
        Assert.assertEquals("test", child.getName().getLocalPart());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateChildWithNonContainerParent() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer child = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateChildWithNonContainerParentAndValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer child = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0, new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParentAndValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithNonContainerParent() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());
        Assert.assertNotSame(nullPropertyPointer, newParent);
        NodePointer child = nullPropertyPointer.createPath(new JXPathContext());
        Assert.assertNotSame(newParent, child);
        child.setValue("test");
        Assert.assertEquals("test", child.getValue());
    }
}