import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
        assertFalse(nullPropertyPointer.isLeaf());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isActual());
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        QName name = nullPropertyPointer.getName();
        assertEquals("testProperty", name.getLocalPart());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(0);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLengthWithNegativeIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(-1);
        nullPropertyPointer.getLength();
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("testValue");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer nonContainerPointer = new NodePointer(null);
        nullPropertyPointer.setValue("testValue", nonContainerPointer);
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(new JXPathContext());
        assertNull(result.getNode());
        assertTrue(result instanceof NullPropertyPointer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathWithAttribute() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        nullPropertyPointer.setPropertyName("testProperty");
        nullPropertyPointer.createPath(context, "testValue");
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(null);
        NodePointer result = null;
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);

        try {
            result = nullPropertyPointer.createChild(new JXPathContext(), new QName("testName"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildWithNullContext() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createChild(null, new QName("testName"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateChildWithNullName() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.createChild(new JXPathContext(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyNameWithNullString() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName(null);
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        assertEquals("testAttribute", nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
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
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("testProperty");
        assertEquals("testProperty", nullPropertyPointer.asPath());

        NullPropertyPointer nullPropertyPointerWithAttribute = new NullPropertyPointer(null);
        nullPropertyPointerWithAttribute.setPropertyName("testProperty");
        nullPropertyPointerWithAttribute.setNameAttributeValue("testAttribute");
        nullPropertyPointerWithAttribute.setIndex(1);
        assertEquals("[@name='testAttribute']", nullPropertyPointerWithAttribute.asPath());
    }

    @Test
    public void testEscape() {
        String string = "test'string\"";
        String escapedString = NullPropertyPointer.escape(string);
        assertEquals("test&apos;string&quot;", escapedString);
    }

}