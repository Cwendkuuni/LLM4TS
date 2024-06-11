import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Assert.assertNotNull(nullPropertyPointer);
        Assert.assertEquals(parent, nullPropertyPointer.getParent());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        QName qName = nullPropertyPointer.getName();
        Assert.assertEquals("testProperty", qName.getLocalPart());
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
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NullPointer nullPointer = nullPropertyPointer.getValuePointer();
        Assert.assertNotNull(nullPointer);
        Assert.assertEquals(nullPropertyPointer, nullPointer.getSource());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.isActualProperty();
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.isActual();
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testSetValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        try {
            nullPropertyPointer.setValue(new Object());
            Assert.fail();
        } catch (JXPathInvalidAccessException e) {
            Assert.assertEquals("Cannot set property " + nullPropertyPointer.asPath()
                    + ", the target object is null", e.getMessage());
        }
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer newParent = nullPropertyPointer.createPath(context);
        Assert.assertNull(newParent);
    }

    @Test
    public void testCreatePathValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        NodePointer newParent = nullPropertyPointer.createPath(context, value);
        Assert.assertNull(newParent);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testChild");
        NodePointer child = nullPropertyPointer.createChild(context, name);
        Assert.assertNull(child);
    }

    @Test
    public void testCreateChildValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testChild");
        Object value = new Object();
        NodePointer child = nullPropertyPointer.createChild(context, name, 0, value);
        Assert.assertNull(child);
    }

    @Test
    public void testCreateChildWithIndex() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("testChild");
        NodePointer child = nullPropertyPointer.createChild(context, name, 1);
        Assert.assertNull(child);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        Assert.assertEquals("testAttribute", nullPropertyPointer.getPropertyName());
        Assert.assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        Assert.assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        Assert.assertArrayEquals(new String[0], propertyNames);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String asPath = nullPropertyPointer.asPath();
        Assert.assertEquals("", asPath);
        NullPropertyPointer nullPropertyPointer1 = new NullPropertyPointer(null);
        nullPropertyPointer1.setPropertyName("testProperty");
        nullPropertyPointer1.setNameAttributeValue("testAttribute");
        nullPropertyPointer1.setIndex(1);
        String asPath1 = nullPropertyPointer1.asPath();
        Assert.assertEquals("/@testAttribute[1]", asPath1);
    }
}