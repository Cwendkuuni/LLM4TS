import static org.junit.Assert.*;
import org.junit.Test;

public class NullPropertyPointerTest {
    
    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(new QName("*"), pointer.getName());
    }
    
    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(0);
    }
    
    @Test
    public void testGetLength() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(0, pointer.getLength());
    }
    
    @Test
    public void testGetBaseValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getBaseValue());
    }
    
    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertNull(pointer.getImmediateNode());
    }
    
    @Test
    public void testIsLeaf() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertTrue(pointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NullPointer valuePointer = new NullPointer(pointer, new QName(pointer.getPropertyName()));
        assertEquals(valuePointer, pointer.getValuePointer());
    }
    
    @Test
    public void testIsActualProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertTrue(pointer.isContainer());
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_withNullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setValue("value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_withNonContainerParent() {
        NodePointer parent = new NodePointer(null) {
            public boolean isContainer() { return false; }
        };
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        pointer.setValue("value");
    }
    
    @Test
    public void testCreatePath_withAttributeName() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer newParent = pointer.createPath(context);
        assertTrue(newParent instanceof NullPropertyPointer);
    }
    
    @Test
    public void testCreatePath_withNonAttributeName() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        NodePointer newParent = pointer.createPath(context);
        assertTrue(newParent instanceof PropertyPointer);
    }
    
    @Test
    public void testCreatePath_withValue() {
        NodePointer parent = new NodePointer(null) {
            public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
                return this;
            }
        };
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        NodePointer newParent = pointer.createPath(context, "value");
        assertNotNull(newParent);
    }
    
    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer childPointer = pointer.createChild(context, new QName("name"), 0);
        assertNotNull(childPointer);
    }
    
    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext(null);
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        NodePointer childPointer = pointer.createChild(context, new QName("name"), 0, "value");
        assertNotNull(childPointer);
    }
    
    @Test
    public void testGetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("*", pointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyName("name");
        assertEquals("name", pointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("name");
        assertEquals("name", pointer.getPropertyName());
    }
    
    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertFalse(pointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(0, pointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertArrayEquals(new String[0], pointer.getPropertyNames());
    }
    
    @Test
    public void testAsPath() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals("*", pointer.asPath());
    }
    
    private class PropertyOwnerPointer extends NodePointer {
        
        public PropertyOwnerPointer(NodePointer parent) {
            super(parent);
        }
        
        public boolean isDynamicPropertyDeclarationSupported() {
            return true;
        }
        
        public PropertyPointer getPropertyPointer() {
            return new PropertyPointer(this);
        }
    }
    
    private class NullPointer extends NodePointer {
        
        public NullPointer(NodePointer parent, QName name) {
            super(parent);
        }
    }
}