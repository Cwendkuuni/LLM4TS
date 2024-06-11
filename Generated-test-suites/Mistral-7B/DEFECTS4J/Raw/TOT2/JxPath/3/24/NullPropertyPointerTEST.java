import org.apache.commons.jxpath.ri.model.beans.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNotNull(nullPropertyPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParentPointerInNullPropertyPointerConstructor() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
    }

    @Test
    public void testGetName() {
        String propertyName = "*";
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        NullPropertyPointer nullPropertyPointerWithName = new NullPropertyPointer(new NodePointer(new QName("root")));
        nullPropertyPointerWithName.setPropertyName("testProperty");

        assertEquals(propertyName, nullPropertyPointer.getName().getLocalPart());
        assertEquals("testProperty", nullPropertyPointerWithName.getName().getLocalPart());
    }

    @Test
    public void testSetPropertyName() {
        String newPropertyName = "testProperty";
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        nullPropertyPointer.setPropertyName(newPropertyName);

        assertEquals(newPropertyName, nullPropertyPointer.getPropertyName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPropertyNameInSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        nullPropertyPointer.setPropertyName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPropertyNameInSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        nullPropertyPointer.setPropertyName(null);
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testSetValue() {
        String value = "testValue";
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));

        try {
            nullPropertyPointer.setValue(value);
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());

        assertNotNull(nullPropertyPointer.createPath(new JXPathContext()));
        assertNotNull(nullPropertyPointer.createPath(new JXPathContext(), null));
        assertNotNull(nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0));
        assertNotNull(nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0, null));
        assertNotNull(nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 1));
        assertNotNull(nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 1, null));
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());
        Object value = "testValue";

        NodePointer pointer = nullPropertyPointer.createPath(new JXPathContext(), value);
        assertNotNull(pointer);
        assertEquals(value, pointer.getValue());
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());

        Object value = "testValue";
        NodePointer child = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0, value);

        assertEquals("test", child.getName().getLocalPart());
        assertEquals(value, child.getValue());
    }

    @Test
    public void testCreateChildWithNameAttribute() {
        NodePointer parent = new NodePointer(new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());

        NullPropertyPointer newNullPropertyPointer = new NullPropertyPointer(newParent);
        newNullPropertyPointer.setPropertyName("testProperty");
        newNullPropertyPointer.setNameAttributeValue("testAttribute");

        NodePointer child = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0);
        NodePointer attributeChild = newNullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 0);

        assertEquals("test", child.getName().getLocalPart());
        assertEquals("test", attributeChild.getName().getLocalPart());
        assertEquals("testAttribute", attributeChild.getName().getNamespaceURI());
    }

    @Test
    public void testCreateChildWithIndex() {
        NodePointer parent = new NodePointer(new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());

        Object value = "testValue";
        NodePointer child = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), 1, value);

        assertEquals("test", child.getName().getLocalPart());
        assertEquals(value, child.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateChildWithNegativeIndex() {
        NodePointer parent = new NodePointer(new QName("root"));
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer newParent = parent.createPath(new JXPathContext());

        NodePointer child = nullPropertyPointer.createChild(new JXPathContext(), new QName("test"), -1);
    }
}