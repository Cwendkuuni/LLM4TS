import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {
    
    private PropertyPointer propertyPointer;
    
    @Before
    public void setUp() {
        propertyPointer = new PropertyPointerMock();
    }
    
    @Test
    public void testGetPropertyIndex() {
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }
    
    @Test
    public void testGetBean() {
        assertNotNull(propertyPointer.getBean());
    }
    
    @Test
    public void testGetName() {
        assertEquals("null", propertyPointer.getName().getName());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("propertyNameMock", propertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        propertyPointer.setPropertyName("newPropertyNameMock");
        assertEquals("newPropertyNameMock", propertyPointer.getPropertyName());
    }
    
    @Test
    public void testGetPropertyCount() {
        assertEquals(0, propertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = propertyPointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }
    
    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        assertTrue(propertyPointer.isActual());
    }
    
    @Test
    public void testGetImmediateNode() {
        assertNotNull(propertyPointer.getImmediateNode());
    }
    
    @Test
    public void testIsCollection() {
        assertFalse(propertyPointer.isCollection());
    }
    
    @Test
    public void testIsLeaf() {
        assertTrue(propertyPointer.isLeaf());
    }
    
    @Test
    public void testGetLength() {
        assertEquals(-1, propertyPointer.getLength());
    }
    
    @Test
    public void testGetImmediateValuePointer() {
        assertNotNull(propertyPointer.getImmediateValuePointer());
    }
    
    @Test
    public void testCreatePath() {
        assertNotNull(propertyPointer.createPath(new JXPathContext()));
    }
    
    @Test
    public void testCreatePathWithValue() {
        assertNotNull(propertyPointer.createPath(new JXPathContext(), "testValue"));
    }
    
    @Test
    public void testCreateChildWithNamedPropertyAndIndex() {
        assertNotNull(propertyPointer.createChild(new JXPathContext(), new QName("nameMock"), 1));
    }
    
    @Test
    public void testCreateChildWithNamedPropertyAndIndexWithValue() {
        assertNotNull(propertyPointer.createChild(new JXPathContext(), new QName("nameMock"), 1, "testValue"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(propertyPointer.getImmediateParentPointer().hashCode() + propertyPointer.getPropertyIndex() + propertyPointer.getIndex(), propertyPointer.hashCode());
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        assertFalse(propertyPointer.equals(new Object()));
    }
    
    @Test
    public void testEqualsWithSameObject() {
        assertTrue(propertyPointer.equals(propertyPointer));
    }
    
    @Test
    public void testEqualsWithDifferentParent() {
        PropertyPointer other = new PropertyPointerMock();
        assertFalse(propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentPropertyIndex() {
        PropertyPointer other = new PropertyPointerMock();
        other.setPropertyIndex(1);
        assertFalse(propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentPropertyName() {
        PropertyPointer other = new PropertyPointerMock();
        other.setPropertyName("newPropertyNameMock");
        assertFalse(propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWithDifferentIndex() {
        PropertyPointer other = new PropertyPointerMock();
        other.setIndex(1);
        assertFalse(propertyPointer.equals(other));
    }
    
    @Test
    public void testEqualsWithSameValues() {
        PropertyPointer other = new PropertyPointerMock();
        assertTrue(propertyPointer.equals(other));
    }
    
    @Test
    public void testCompareChildNodePointers() {
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        assertEquals(propertyPointer.getValuePointer().compareChildNodePointers(pointer1, pointer2), propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}

class PropertyPointerMock extends PropertyPointer {
    public PropertyPointerMock() {
        super(new NodePointer());
    }
    
    public String getPropertyName() {
        return "propertyNameMock";
    }
    
    public void setPropertyName(String propertyName) {
        // Do nothing
    }
    
    public int getPropertyCount() {
        return 0;
    }
    
    public String[] getPropertyNames() {
        return new String[0];
    }
    
    protected boolean isActualProperty() {
        return true;
    }
    
    public NodePointer getValuePointer() {
        return new NodePointer();
    }
}