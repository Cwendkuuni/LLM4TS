import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals("*", pointer.getName().getName());
    }

    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setPropertyIndex(1);
        assertEquals(0, pointer.getPropertyIndex());
    }

    @Test
    public void testGetLength() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals(0, pointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertTrue(pointer.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertEquals("*", pointer.getValuePointer().asPath());
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertFalse(pointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertFalse(pointer.isActual());
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        assertTrue(pointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithDynamicPropertyDeclarationSupported() {
        NodePointer parent = createDynamicPropertyDeclarationSupportedParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonChangeableLocation() {
        NodePointer parent = createNonChangeableLocationParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("test");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer newPath = pointer.createPath(context);
        assertNull(newPath.getImmediateNode());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer newPath = pointer.createPath(context, "test");
        assertNull(newPath.getImmediateNode());
        assertEquals("test", newPath.getValue());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer newChild = pointer.createChild(context, new QName("child"), 0);
        assertEquals(".*[1]/child[1]", newChild.asPath());
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = JXPathContext.newContext(new Object());
        NodePointer parent = createDummyParent();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        NodePointer newChild = pointer.createChild(context, new QName("child"), 0, "test");
        assertEquals(".*[1]/child[1]", newChild.asPath());
        assertEquals("test", newChild.getValue());
    }

    private NodePointer createDummyParent() {
        return new NodePointer() {
            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object value) {

            }
        };
    }

    private NodePointer createDynamicPropertyDeclarationSupportedParent() {
        return new NodePointer() {
            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object value) {

            }
        };
    }

    private NodePointer createNonChangeableLocationParent() {
        return new NodePointer() {
            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object value) {

            }
        };
    }

}