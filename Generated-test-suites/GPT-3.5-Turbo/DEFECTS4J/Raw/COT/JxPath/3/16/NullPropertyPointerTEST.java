import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {
    
    private NullPropertyPointer nullPropertyPointer;
    
    @Before
    public void setUp() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }
    
    @Test
    public void testGetName() {
        assertNull(nullPropertyPointer.getName().getPrefix());
        assertEquals("*", nullPropertyPointer.getName().getName());
    }
    
    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
        assertEquals(0, nullPropertyPointer.getIndex());
    }
    
    @Test
    public void testGetLength() {
        assertEquals(0, nullPropertyPointer.getLength());
    }
    
    @Test
    public void testGetBaseValue() {
        assertNull(nullPropertyPointer.getBaseValue());
    }
    
    @Test
    public void testGetImmediateNode() {
        assertNull(nullPropertyPointer.getImmediateNode());
    }
    
    @Test
    public void testIsLeaf() {
        assertTrue(nullPropertyPointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        assertNotNull(nullPropertyPointer.getValuePointer());
        assertTrue(nullPropertyPointer.getValuePointer() instanceof NullPointer);
    }
    
    @Test
    public void testIsActualProperty() {
        assertFalse(nullPropertyPointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        assertFalse(nullPropertyPointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        assertTrue(nullPropertyPointer.isContainer());
    }
    
    // Exception handling scenarios
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueParentIsNull() {
        nullPropertyPointer.setValue("test");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueParentIsContainer() {
        nullPropertyPointer.setParent(new PropertyOwnerPointer(null, true));
        nullPropertyPointer.setValue("test");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueParentIsDynamicPropertyDeclarationSupported() {
        nullPropertyPointer.setParent(new PropertyOwnerPointer(null, false));
        nullPropertyPointer.setValue("test");
    }
    
    // Edge cases
    @Test
    public void testCreatePath() {
        assertNotNull(nullPropertyPointer.createPath(null));
    }
    
    @Test
    public void testCreatePathWithValue() {
        assertNotNull(nullPropertyPointer.createPath(null, "test"));
    }
    
    @Test
    public void testCreateChild() {
        QName name = new QName("test");
        assertNotNull(nullPropertyPointer.createChild(null, name, 0));
    }
    
    @Test
    public void testCreateChildWithValue() {
        QName name = new QName("test");
        assertNotNull(nullPropertyPointer.createChild(null, name, 0, "value"));
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("test");
        assertEquals("test", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        assertEquals("attributeValue", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testIsCollection() {
        nullPropertyPointer.setPropertyIndex(0);
        assertTrue(nullPropertyPointer.isCollection());
        
        nullPropertyPointer.setPropertyIndex(-1);
        assertFalse(nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        assertNotNull(nullPropertyPointer.getPropertyNames());
        assertEquals(0, nullPropertyPointer.getPropertyNames().length);
    }
    
    @Test
    public void testAsPath() {
        nullPropertyPointer.setPropertyName("test");
        assertNotNull(nullPropertyPointer.asPath());
        assertEquals("/test", nullPropertyPointer.asPath());
        
        nullPropertyPointer.setNameAttributeValue("name");
        assertNotNull(nullPropertyPointer.asPath());
        assertEquals("/name[@name='test']", nullPropertyPointer.asPath());
        
        nullPropertyPointer.setPropertyIndex(0);
        assertNotNull(nullPropertyPointer.asPath());
        assertEquals("/name[@name='test'][1]", nullPropertyPointer.asPath());
    }
}