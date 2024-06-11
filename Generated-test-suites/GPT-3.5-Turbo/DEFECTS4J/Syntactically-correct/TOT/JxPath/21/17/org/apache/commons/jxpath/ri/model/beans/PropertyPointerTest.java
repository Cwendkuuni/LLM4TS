import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        // Test basic case
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        pointer.setPropertyIndex(5);
        assertEquals(5, pointer.getPropertyIndex());
        
        // Test another property index
        pointer.setPropertyIndex(3);
        assertEquals(3, pointer.getPropertyIndex());

        // Test negative property index
        pointer.setPropertyIndex(-1);
        assertEquals(-1, pointer.getPropertyIndex());
    }
    
    @Test
    public void testSetPropertyIndex() {
        // Test setting a different property index
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        pointer.setPropertyIndex(5);
        pointer.setPropertyIndex(3);
        assertEquals(3, pointer.getPropertyIndex());
        
        // Test setting the same property index
        pointer.setPropertyIndex(3);
        assertEquals(3, pointer.getPropertyIndex());
    }
    
    @Test
    public void testGetBean() {
        // Test parent pointer with a bean
        Object bean = new Object();
        NodePointer parent = new NodePointerImplementation(bean);
        PropertyPointer pointer = new PropertyPointerImplementation(parent);
        assertEquals(bean, pointer.getBean());
        
        // Test parent pointer with null
        parent = new NodePointerImplementation(null);
        pointer = new PropertyPointerImplementation(parent);
        assertNull(pointer.getBean());
    }
    
    @Test
    public void testGetName() {
        // Test property name
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        pointer.setPropertyName("name");
        assertEquals("name", pointer.getName().getLocalPart());
        assertNull(pointer.getName().getNamespaceURI());
    }
    
    @Test
    public void testGetPropertyName() {
        // Test property name
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        pointer.setPropertyName("name");
        assertEquals("name", pointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        // Test setting a different property name
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        pointer.setPropertyName("name");
        pointer.setPropertyName("newName");
        assertEquals("newName", pointer.getPropertyName());
        
        // Test setting the same property name
        pointer.setPropertyName("newName");
        assertEquals("newName", pointer.getPropertyName());
    }
    
    @Test
    public void testGetPropertyCount() {
        // Test property count
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        assertEquals(0, pointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        // Test property names
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        String[] expected = new String[]{"name", "age"};
        assertArrayEquals(expected, pointer.getPropertyNames());
    }
    
    @Test
    public void testIsActualProperty() {
        // Test actual property
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        assertTrue(pointer.isActualProperty());

        // Test non-actual property
        pointer = new PropertyPointerImplementation(null);
        assertFalse(pointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        // Test actual property
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        assertTrue(pointer.isActual());

        // Test non-actual property
        pointer = new PropertyPointerImplementation(null);
        assertFalse(pointer.isActual());
    }
    
    @Test
    public void testGetImmediateNode() {
        // Test uninitialized value
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        Object value = pointer.getImmediateNode();
        assertNull(value);
        
        // Test initialized value
        pointer.setValue("test");
        value = pointer.getImmediateNode();
        assertEquals("test", value);
    }
    
    @Test
    public void testIsCollection() {
        // Test non-collection value
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        assertFalse(pointer.isCollection());
        
        // Test collection value
        pointer.setValue(new int[]{1, 2, 3});
        assertTrue(pointer.isCollection());
    }
    
    @Test
    public void testIsLeaf() {
        // Test leaf value
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        assertTrue(pointer.isLeaf());
        
        // Test non-leaf value
        pointer.setValue("test");
        assertFalse(pointer.isLeaf());
    }
    
    @Test
    public void testGetLength() {
        // Test non-collection value
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        assertEquals(-1, pointer.getLength());
        
        // Test collection value
        pointer.setValue(new int[]{1, 2, 3});
        assertEquals(3, pointer.getLength());
    }
    
    @Test
    public void testGetImmediateValuePointer() {
        // Test immediate value pointer
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        pointer.setValue("test");
        assertEquals("test", pointer.getImmediateValuePointer().getNode());
    }
    
    @Test
    public void testCreatePath() {
        // Test create path when immediate node is null
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        pointer.createPath(new JXPathContext());
        assertNotNull(pointer.getBean());
        
        // Test create path when immediate node is not null
        pointer.setValue("test");
        PropertyPointer newPath = (PropertyPointer) pointer.createPath(new JXPathContext());
        assertEquals(pointer.getBean(), newPath.getBean());
    }
    
    @Test
    public void testCreatePathWithValue() {
        // Test create path with value when immediate node is null
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        pointer.createPath(new JXPathContext(), "test");
        assertNotNull(pointer.getBean());
        assertEquals("test", pointer.getNode());
        
        // Test create path with value when immediate node is not null
        pointer.setValue("test");
        PropertyPointer newPath = (PropertyPointer) pointer.createPath(new JXPathContext(), "newTest");
        assertEquals(pointer.getBean(), newPath.getBean());
        assertEquals("newTest", newPath.getNode());
    }
    
    @Test
    public void testCreateChildWithValue() {
        // Test create child with value
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        PropertyPointer newChild = (PropertyPointer) pointer.createChild(new JXPathContext(), new QName(null, "child"), 0, "test");
        assertNotNull(newChild);
        assertEquals("test", newChild.getNode());
    }
    
    @Test
    public void testCreateChildWithoutValue() {
        // Test create child without value
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        PropertyPointer newChild = (PropertyPointer) pointer.createChild(new JXPathContext(), new QName(null, "child"), 0);
        assertNotNull(newChild);
        assertNull(newChild.getNode());
    }
    
    @Test
    public void testHashCode() {
        // Test hash code
        PropertyPointer pointer1 = new PropertyPointerImplementation(null);
        PropertyPointer pointer2 = new PropertyPointerImplementation(null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Test equals
        PropertyPointer pointer1 = new PropertyPointerImplementation(null);
        PropertyPointer pointer2 = new PropertyPointerImplementation(null);
        assertTrue(pointer1.equals(pointer2));
        assertTrue(pointer2.equals(pointer1));
        
        // Test not equals
        pointer2.setPropertyIndex(1);
        assertFalse(pointer1.equals(pointer2));
        assertFalse(pointer2.equals(pointer1));
    }
    
    @Test
    public void testCompareChildNodePointers() {
        // Test compare child node pointers
        PropertyPointer pointer = new PropertyPointerImplementation(null);
        NodePointer pointer1 = new NodePointerImplementation("a");
        NodePointer pointer2 = new NodePointerImplementation("b");
        assertEquals(0, pointer.compareChildNodePointers(pointer1, pointer2));
    }

}