import org.junit.Test;

public class PropertyPointerTest {
    
    @Test
    public void testGetPropertyIndex() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());
    }
    
    @Test
    public void testSetPropertyIndex() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());
        pointer.setPropertyIndex(2);
        assertEquals(2, pointer.getPropertyIndex());
    }
    
    @Test
    public void testGetBean() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        Object bean = new Object();
        pointer.setBean(bean);
        assertEquals(bean, pointer.getBean());
    }
    
    @Test
    public void testGetName() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        pointer.setPropertyName("property");
        assertEquals(new QName(null, "property"), pointer.getName());
    }
    
    @Test
    public void testIsActual() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertFalse(pointer.isActual());
    }
    
    @Test
    public void testGetImmediateNode() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        Object value = new Object();
        pointer.setValue(value);
        assertEquals(value, pointer.getImmediateNode());
    }
    
    @Test
    public void testIsCollection() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertFalse(pointer.isCollection());
        pointer.setValue(new Object[0]);
        assertTrue(pointer.isCollection());
    }
    
    @Test
    public void testIsLeaf() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertTrue(pointer.isLeaf());
        pointer.setValue(new Object());
        assertTrue(pointer.isLeaf());
    }
    
    @Test
    public void testGetLength() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertEquals(-1, pointer.getLength());
        pointer.setValue(new Object[3]);
        assertEquals(3, pointer.getLength());
    }
    
    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        Object value = new Object();
        pointer.setValue(value);
        NodePointer valuePointer = pointer.getImmediateValuePointer();
        assertEquals(value, valuePointer.getNode());
    }
    
    @Test
    public void testCreatePath() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertNull(pointer.getImmediateNode());
        pointer.createPath(null);
        assertNotNull(pointer.getImmediateNode());
    }
    
    @Test
    public void testCreatePathWithValue() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertNull(pointer.getImmediateNode());
        Object value = new Object();
        pointer.createPath(null, value);
        assertEquals(value, pointer.getImmediateNode());
    }
    
    @Test
    public void testCreateChildWithPath() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertNull(pointer.getImmediateNode());
        QName name = new QName(null, "child");
        NodePointer childPointer = pointer.createChild(null, name, 0);
        assertEquals(name, childPointer.getName());
        assertNotNull(childPointer.getImmediateNode());
    }
    
    @Test
    public void testCreateChildWithPathWithValue() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertNull(pointer.getImmediateNode());
        QName name = new QName(null, "child");
        Object value = new Object();
        NodePointer childPointer = pointer.createChild(null, name, 0, value);
        assertEquals(name, childPointer.getName());
        assertEquals(value, childPointer.getImmediateNode());
    }
    
    @Test
    public void testHashCode() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        PropertyPointer other = new PropertyPointerImpl(null);
        assertEquals(pointer.hashCode(), other.hashCode());
        pointer.setPropertyIndex(1);
        assertNotEquals(pointer.hashCode(), other.hashCode());
        other.setPropertyIndex(1);
        assertEquals(pointer.hashCode(), other.hashCode());
    }
    
    @Test
    public void testEquals() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        assertFalse(pointer.equals(null));
        assertTrue(pointer.equals(pointer));
        assertFalse(pointer.equals(new Object()));
        PropertyPointer other = new PropertyPointerImpl(null);
        assertTrue(pointer.equals(other));
        pointer.setPropertyIndex(1);
        assertFalse(pointer.equals(other));
        other.setPropertyIndex(1);
        assertTrue(pointer.equals(other));
        QName name = new QName(null, "property");
        pointer.setPropertyName(name.toString());
        assertFalse(pointer.equals(other));
        other.setPropertyName(name.toString());
        assertTrue(pointer.equals(other));
        pointer.setIndex(1);
        assertFalse(pointer.equals(other));
        other.setIndex(1);
        assertTrue(pointer.equals(other));
    }
    
    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer pointer = new PropertyPointerImpl(null);
        QName name = new QName(null, "child");
        NodePointer pointer1 = new PropertyPointerImpl(null);
        pointer1.setName(name);
        NodePointer pointer2 = new PropertyPointerImpl(null);
        pointer2.setName(name);
        assertEquals(0, pointer.compareChildNodePointers(pointer1, pointer2));
    }
}