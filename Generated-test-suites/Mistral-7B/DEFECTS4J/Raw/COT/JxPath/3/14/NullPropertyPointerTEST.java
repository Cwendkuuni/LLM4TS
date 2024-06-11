import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstruction() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(null, nullPropertyPointer.getBaseValue());
        assertEquals(null, nullPropertyPointer.getImmediateNode());
        assertTrue(nullPropertyPointer.isLeaf());
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        QName expectedName = new QName("testProperty");
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName, actualName);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(null, nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(null, nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testgetValuePointer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NullPointer expectedNullPointer = new NullPointer(nullPropertyPointer, new QName("testProperty"));
        NullPointer actualNullPointer = nullPropertyPointer.getValuePointer();
        assertEquals(expectedNullPointer, actualNullPointer);
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
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context, new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer expectedNullParent = new NodePointer(context, new Object());
        assertEquals(expectedNullParent, nullPropertyPointer.createPath(context));
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context, new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        Object value = new Object();
        NodePointer expectedValueNode = new NodePointer(context, value);
        NodePointer expectedParent = new PropertyOwnerPointer(parent);
        NodePointer expectedPropertyPointer = new PropertyPointer(expectedParent);
        NodePointer actualParent = nullPropertyPointer.createPath(context, value);
        NodePointer actualPropertyPointer = actualParent.getChild(0);
        assertEquals(expectedParent, actualParent.getParent());
        assertEquals(expectedPropertyPointer.getClass(), actualPropertyPointer.getClass());
        assertEquals(expectedValueNode, actualPropertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context, new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("testChild");
        NodePointer expectedChildNode = new NodePointer(context, new Object());
        NodePointer expectedParent = new PropertyOwnerPointer(parent);
        NodePointer expectedPropertyPointer = new PropertyPointer(expectedParent);
        NodePointer actualChildNode = nullPropertyPointer.createChild(context, name);
        NodePointer actualParent = actualChildNode.getParent();
        NodePointer actualPropertyPointer = actualParent.getChild(0);
        assertEquals(expectedParent, actualParent.getParent());
        assertEquals(expectedPropertyPointer.getClass(), actualPropertyPointer.getClass());
        assertEquals(expectedChildNode, actualChildNode.getImmediateNode());
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext(new Object());
        NodePointer parent = new NodePointer(context, new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        QName name = new QName("testChild");
        Object value = new Object();
        NodePointer expectedChildNode = new NodePointer(context, value);
        NodePointer expectedParent = new PropertyOwnerPointer(parent);
        NodePointer expectedPropertyPointer = new PropertyPointer(expectedParent);
        NodePointer actualChildNode = nullPropertyPointer.createChild(context, name, value);
        NodePointer actualParent = actualChildNode.getParent();
        NodePointer actualPropertyPointer = actualParent.getChild(0);
        assertEquals(expectedParent, actualParent.getParent());
        assertEquals(expectedPropertyPointer.getClass(), actualPropertyPointer.getClass());
        assertEquals(expectedChildNode, actualChildNode.getImmediateNode());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("testProperty");
        assertTrue(nullPropertyPointer.isByNameAttribute());
        assertEquals("testProperty", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] expectedPropertyNames = new String[0];
        String[] actualPropertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expectedPropertyNames, actualPropertyNames);
    }

    @Test
    public void testAsPathWithByNameAttributeFalse() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        String expectedPath = "/testProperty";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testAsPathWithByNameAttributeTrue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        nullPropertyPointer.setNameAttributeValue("nameAttribute");
        String expectedPath = "/testProperty[@name='nameAttribute']";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }
}