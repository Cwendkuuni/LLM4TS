import org.junit.Test;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {
    
    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertEquals("*", pointer.getName().toString());
    }
    
    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        pointer.setPropertyIndex(5);
        // No assertion, just checking if it doesn't throw an exception
    }
    
    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertEquals(0, pointer.getLength());
    }
    
    @Test
    public void testGetBaseValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertNull(pointer.getBaseValue());
    }
    
    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertNull(pointer.getImmediateNode());
    }
    
    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertTrue(pointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        NodePointer valuePointer = pointer.getValuePointer();
        assertNotNull(valuePointer);
        assertTrue(valuePointer instanceof NullPointer);
    }
    
    @Test
    public void testIsActualProperty() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertFalse(pointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertFalse(pointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertTrue(pointer.isContainer());
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NodePointer parent = null;
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        pointer.setValue("value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NodePointer parent = new PropertyPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        pointer.setValue("value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithDynamicPropertyDeclarationSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        pointer.setValue("value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonChangeableLocation() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        pointer.setValue("value");
    }
    
    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        NodePointer newPath = pointer.createPath(context);
        assertNotNull(newPath);
        assertTrue(newPath instanceof NodePointer);
    }
    
    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        NodePointer newPath = pointer.createPath(context, "value");
        assertNotNull(newPath);
        assertTrue(newPath instanceof NodePointer);
    }
    
    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        QName name = new QName("test");
        int index = 3;
        
        NodePointer newChild = pointer.createChild(context, name, index);
        assertNotNull(newChild);
        assertTrue(newChild instanceof NodePointer);
    }
    
    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        QName name = new QName("test");
        int index = 3;
        Object value = "value";
        
        NodePointer newChild = pointer.createChild(context, name, index, value);
        assertNotNull(newChild);
        assertTrue(newChild instanceof NodePointer);
    }
    
    @Test
    public void testGetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertEquals("*", pointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        pointer.setPropertyName("newProperty");
        assertEquals("newProperty", pointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        pointer.setNameAttributeValue("attributeValue");
        assertEquals("attributeValue", pointer.getPropertyName());
    }
    
    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertFalse(pointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        assertEquals(0, pointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        String[] propertyNames = pointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }
    
    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        String path = pointer.asPath();
        assertNotNull(path);
        assertTrue(path instanceof String);
    }
    
    private class NodePointer {}
    
    private class PropertyPointer extends NodePointer {}
    
    private class NullPointer extends NodePointer {}
    
    private class PropertyOwnerPointer extends NodePointer {
        public PropertyOwnerPointer getPropertyPointer() {
            return new PropertyOwnerPointer();
        }
        
        public boolean isDynamicPropertyDeclarationSupported() {
            return true;
        }
    }
}