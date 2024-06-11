import org.junit.Test;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName expectedName = new QName("*");
        
        assertEquals(expectedName, nullPropertyPointer.getName());
    }
    
    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);

        assertTrue(nullPropertyPointer.isLeaf());
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_TargetObjectIsNull() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        
        nullPropertyPointer.setValue("new value");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_PathDoesNotMatchChangeableLocation() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer());
        
        nullPropertyPointer.setValue("new value");
    }
    
    @Test(expected = NullPointerException.class)
    public void testCreatePath_WithNullContext() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        
        nullPropertyPointer.createPath(null);
    }
    
    @Test
    public void testCreatePath_ReturnsNewParentWhenAttribute() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new AttributePointer());
        JXPathContext context = new JXPathContext(null);
        
        NodePointer result = nullPropertyPointer.createPath(context);
        
        assertNotNull(result);
        assertTrue(result instanceof AttributePointer);
    }
    
    @Test
    public void testCreatePath_ReturnsNewParentWhenNotAttribute() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer());
        JXPathContext context = new JXPathContext(null);
        
        NodePointer result = nullPropertyPointer.createPath(context);
        
        assertNotNull(result);
        assertTrue(result instanceof PropertyPointer);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCreatePath_WithNullContextAndValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        
        nullPropertyPointer.createPath(null, "new value");
    }
    
    @Test
    public void testCreatePath_ReturnsNewParentWhenAttributeWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new AttributePointer());
        JXPathContext context = new JXPathContext(null);
        
        NodePointer result = nullPropertyPointer.createPath(context, "new value");
        
        assertNotNull(result);
        assertTrue(result instanceof AttributePointer);
        assertEquals("new value", result.getValue());
    }
    
    @Test
    public void testCreatePath_ReturnsNewParentWhenNotAttributeWithValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new PropertyOwnerPointer());
        JXPathContext context = new JXPathContext(null);
        
        NodePointer result = nullPropertyPointer.createPath(context, "new value");
        
        assertNotNull(result);
        assertTrue(result instanceof PropertyPointer);
        assertEquals("new value", result.getValue());
    }
    
    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("property name");
        
        assertEquals("property name", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("property name");
        
        assertEquals("property name", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testIsCollection_WholeCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(NullPropertyPointer.WHOLE_COLLECTION);
        
        assertTrue(nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testIsCollection_NotWholeCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(0);
        
        assertTrue(nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        
        assertNotNull(nullPropertyPointer.getPropertyNames());
        assertEquals(0, nullPropertyPointer.getPropertyNames().length);
    }
    
    @Test
    public void testAsPath_NotByNameAttribute() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        
        assertEquals("./*", nullPropertyPointer.asPath());
    }
    
    @Test
    public void testAsPath_ByNameAttribute() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("attribute name");
        
        assertEquals("./*[@name='attribute name']", nullPropertyPointer.asPath());
    }
}