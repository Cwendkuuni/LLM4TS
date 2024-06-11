package org.apache.commons.jxpath.ri.model.beans;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    @Test
    public void testCreateNullPropertyPointer() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        assertNull(np.getValuePointer());
        assertFalse(np.isActualProperty());
        assertFalse(np.isActual());
        assertTrue(np.isContainer());
        assertEquals(0, np.getLength());
        assertNull(np.getBaseValue());
        assertNull(np.getImmediateNode());
        assertFalse(np.isLeaf());
    }

    @Test
    public void testCreateNullPropertyPointerWithName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent, new QName("test"));
        assertNull(np.getValuePointer());
        assertTrue(np.getPropertyName().equals("test"));
        assertFalse(np.isActualProperty());
        assertFalse(np.isActual());
        assertTrue(np.isContainer());
        assertEquals(0, np.getLength());
        assertNull(np.getBaseValue());
        assertNull(np.getImmediateNode());
        assertFalse(np.isLeaf());
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        np.setPropertyName("test");
        QName name = np.getName();
        assertEquals("test", name.getLocalPart());
        assertEquals("", name.getNamespaceURI());
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        np.setPropertyName("test");
        assertEquals("test", np.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        np.setNameAttributeValue("test");
        assertEquals("test", np.getPropertyName());
        assertTrue(np.isByNameAttribute());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        assertFalse(np.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        assertEquals(0, np.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        String[] propertyNames = np.getPropertyNames();
        assertEquals(propertyNames.length, 0);
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        String path = np.asPath();
        assertTrue(path.isEmpty());

        np = new NullPropertyPointer(parent, new QName("test"));
        path = np.asPath();
        assertTrue(path.startsWith("//"));
        assertTrue(path.endsWith(""));

        np.setNameAttributeValue("test");
        path = np.asPath();
        assertTrue(path.startsWith("//[@name='test']"));
        assertTrue(path.endsWith(""));

        np.setIndex(1);
        path = np.asPath();
        assertTrue(path.startsWith("//[@name='test']"));
        assertTrue(path.endsWith("[1]"));
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        NodePointer result = np.createPath(null);
        assertSame(parent, result);

        np.setPropertyName("test");
        result = np.createPath(null);
        assertNull(result);

        NodePointer context = new NodePointer(null);
        result = np.createPath(context);
        assertNull(result);

        np.setNameAttributeValue("test");
        result = np.createPath(context);
        assertSame(context, result);
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        NodePointer result = np.createPath(null, null);
        assertSame(parent, result);

        np.setPropertyName("test");
        result = np.createPath(null, null);
        assertNull(result);

        NodePointer context = new NodePointer(null);
        result = np.createPath(context, null, null);
        assertSame(context, result);

        np.setNameAttributeValue("test");
        result = np.createPath(context, null, null);
        assertSame(context, result);

        result = np.createPath(context, "test", -1, null);
        assertNull(result);

        result = np.createPath(context, "test", 0, "value");
        assertNull(result);

        result = np.createPath(context, "test", 1, "value");
        assertNull(result);

        result = np.createPath(context, "test", -1, new Object() {});
        assertNull(result);

        result = np.createPath(context, "test", 0, new Object() {});
        assertNull(result);

        result = np.createPath(context, "test", 1, new Object() {});
        assertNull(result);
    }

    @Test
    public void testValuePointer() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        NullPointer valuePointer = np.getValuePointer();
        assertSame(np, valuePointer.getParent());
        assertEquals(np.getQName(), valuePointer.getQName());
        assertNull(valuePointer.getValue());
        assertFalse(valuePointer.isLeaf());
    }

    @Test
    public void testSetValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);
        NodePointer context = new NodePointer(null);
        JXPathContext jxpathContext = new JXPathContext(context);
        Object value = new Object();

        assertThrows(JXPathInvalidAccessException.class, () -> np.setValue(value));

        parent = new PropertyOwnerPointer(context, new QName("test"), new Object() {});
        np = new NullPropertyPointer(parent);
        np.setPropertyName("test");
        np.setValue(value);
        assertEquals(value, np.getBaseValue());
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);

        assertThrows(IllegalArgumentException.class, () -> np.createChild(null, null, 0));
        assertThrows(IllegalArgumentException.class, () -> np.createChild(null, null, 0, null));
        assertThrows(IllegalArgumentException.class, () -> np.createChild(null, null, 0, new Object() {}));

        NodePointer child = np.createChild(null, new QName("test"), 0);
        assertNull(child);

        QName qname = new QName("test");
        child = np.createChild(null, qname, 0, null);
        assertNull(child);

        child = np.createChild(null, qname, 0, new Object() {});
        assertNull(child);

        parent = new NodePointer(null);
        np = new NullPropertyPointer(parent);
        child = np.createChild(null, new QName("test"), 0, null);
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertNull(child.getValue());
        assertFalse(child.isLeaf());
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);

        assertThrows(IllegalArgumentException.class, () -> np.createChild(null, null, 0, null));
        assertThrows(IllegalArgumentException.class, () -> np.createChild(null, null, 0, new Object() {}));

        NodePointer context = new NodePointer(null);
        JXPathContext jxpathContext = new JXPathContext(context);
        Object value = new Object();

        NodePointer child = np.createChild(jxpathContext, new QName("test"), 0, null);
        assertSame(context, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertNull(child.getValue());
        assertFalse(child.isLeaf());

        child = np.createChild(jxpathContext, new QName("test"), 0, value);
        assertSame(context, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertEquals(value, child.getValue());
        assertFalse(child.isLeaf());
    }

    @Test
    public void testCreateChildWithValueAndIndex() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer np = new NullPropertyPointer(parent);

        assertThrows(IllegalArgumentException.class, () -> np.createChild(null, null, -1, null));
        assertThrows(IllegalArgumentException.class, () -> np.createChild(null, null, 0, null));
        assertThrows(IllegalArgumentException.class, () -> np.createChild(null, null, 0, new Object() {}));

        NodePointer context = new NodePointer(null);
        JXPathContext jxpathContext = new JXPathContext(context);
        Object value = new Object();

        NodePointer child = np.createChild(jxpathContext, new QName("test"), 0, null);
        assertNull(child);

        child = np.createChild(jxpathContext, new QName("test"), 1, null);
        assertNull(child);

        child = np.createChild(jxpathContext, new QName("test"), -1, null);
        assertNull(child);

        child = np.createChild(jxpathContext, new QName("test"), 0, value);
        assertNull(child);

        child = np.createChild(jxpathContext, new QName("test"), 1, value);
        assertNull(child);

        child = np.createChild(jxpathContext, new QName("test"), -1, value);
        assertNull(child);

        child = np.createChild(jxpathContext, new QName("test"), 0, new Object() {});
        assertNull(child);

        child = np.createChild(jxpathContext, new QName("test"), 1, new Object() {});
        assertNull(child);

        child = np.createChild(jxpathContext, new QName("test"), -1, new Object() {});
        assertNull(child);

        parent = new NodePointer(null);
        np = new NullPropertyPointer(parent);

        child = np.createChild(jxpathContext, new QName("test"), 0, null);
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertNull(child.getValue());
        assertFalse(child.isLeaf());

        child = np.createChild(jxpathContext, new QName("test"), 1, null);
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertNull(child.getValue());
        assertFalse(child.isLeaf());

        child = np.createChild(jxpathContext, new QName("test"), -1, null);
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertNull(child.getValue());
        assertFalse(child.isLeaf());

        child = np.createChild(jxpathContext, new QName("test"), 0, value);
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertEquals(value, child.getValue());
        assertFalse(child.isLeaf());

        child = np.createChild(jxpathContext, new QName("test"), 1, value);
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertEquals(value, child.getValue());
        assertFalse(child.isLeaf());

        child = np.createChild(jxpathContext, new QName("test"), -1, value);
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertEquals(value, child.getValue());
        assertFalse(child.isLeaf());

        child = np.createChild(jxpathContext, new QName("test"), 0, new Object() {});
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertNull(child.getValue());
        assertFalse(child.isLeaf());

        child = np.createChild(jxpathContext, new QName("test"), 1, new Object() {});
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertNull(child.getValue());
        assertFalse(child.isLeaf());

        child = np.createChild(jxpathContext, new QName("test"), -1, new Object() {});
        assertSame(parent, child.getParent());
        assertEquals(np.getQName(), child.getParent().getQName());
        assertNull(child.getValue());
        assertFalse(child.isLeaf());
    }
}