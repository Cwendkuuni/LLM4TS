import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Assert;
import org.junit.Test;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointer() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        Assert.assertEquals("*", nullPropertyPointer.getPropertyName());
        Assert.assertFalse(nullPropertyPointer.isActual());
        Assert.assertFalse(nullPropertyPointer.isContainer());
        Assert.assertFalse(nullPropertyPointer.isLeaf());
        Assert.assertNull(nullPropertyPointer.getBaseValue());
        Assert.assertNull(nullPropertyPointer.getImmediateNode());
        Assert.assertEquals(0, nullPropertyPointer.getLength());
        Assert.assertNull(nullPropertyPointer.getValuePointer());
        Assert.assertFalse(nullPropertyPointer.isActualProperty());

        try {
            nullPropertyPointer.setPropertyIndex(1);
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        Assert.assertEquals(0, nullPropertyPointer.getPropertyCount());
        Assert.assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());

        try {
            nullPropertyPointer.createPath(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        nullPropertyPointer.setPropertyName("testProperty");

        Assert.assertEquals("testProperty", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        nullPropertyPointer.setNameAttributeValue("testProperty");

        Assert.assertEquals("testProperty", nullPropertyPointer.getPropertyName());
        Assert.assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        Assert.assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        try {
            nullPropertyPointer.createPath(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        NodePointer context = new NodePointer(new Object());

        try {
            nullPropertyPointer.createPath(context);
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        NullPropertyPointer newNullPropertyPointer =
                new NullPropertyPointer(new NodePointer(new Object()));
        NodePointer newParent = new NodePointer(new Object());

        NodePointer newPath = nullPropertyPointer.createPath(context);
        newPath = newParent.createPath(context);
        newPath = newParent.createPath(context, newNullPropertyPointer.getName());

        Assert.assertNotNull(newPath);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        try {
            nullPropertyPointer.createPath(null, null);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        NodePointer context = new NodePointer(new Object());
        Object value = new Object();

        try {
            nullPropertyPointer.createPath(context, value);
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        NodePointer newParent = new NodePointer(new Object());

        NodePointer newPath = nullPropertyPointer.createPath(context);
        newPath = newParent.createPath(context, nullPropertyPointer.getName(), null, value);

        Assert.assertNotNull(newPath);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        try {
            nullPropertyPointer.createChild(null, null);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        NodePointer context = new NodePointer(new Object());
        Object value = new Object();
        QName name = new QName("testName");

        try {
            nullPropertyPointer.createChild(context, name, -1, value);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            nullPropertyPointer.createChild(context, null, 0, value);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        NodePointer newParent = new NodePointer(new Object());
        NodePointer newChild = new NullPropertyPointer(new NodePointer(new Object()));

        NodePointer newChildPath = nullPropertyPointer.createPath(context);
        newChildPath = newParent.createPath(context, newChild.getName());

        NodePointer newChildCreated = newChildPath.createChild(context, name, 0, value);

        Assert.assertNotNull(newChildCreated);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        try {
            nullPropertyPointer.createChild(null, null, 0, null);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }

        NodePointer context = new NodePointer(new Object());
        Object value = new Object();
        QName name = new QName("testName");

        try {
            nullPropertyPointer.createChild(context, name, -1, value);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        NodePointer newParent = new NodePointer(new Object());
        NodePointer newChild = new NullPropertyPointer(new NodePointer(new Object()));

        NodePointer newChildPath = nullPropertyPointer.createPath(context);
        newChildPath = newParent.createPath(context, newChild.getName());

        NodePointer newChildCreated = newChildPath.createChild(context, name, 0, value);

        Assert.assertNotNull(newChildCreated);
    }
}