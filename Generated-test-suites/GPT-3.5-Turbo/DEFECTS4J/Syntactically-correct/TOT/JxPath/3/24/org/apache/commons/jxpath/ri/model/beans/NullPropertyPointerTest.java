import org.junit.Test;

public class NullPropertyPointerTest {
    
    @Test
    public void testGetName() {
        // Test with NullPropertyPointer object
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertEquals(new QName("*"), pointer.getName());
    }

    @Test
    public void testSetValue_nullParent() {
        // Test with NullPropertyPointer object and null parent
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        assertThrows(JXPathInvalidAccessException.class, () -> {
            pointer.setValue("test value");
        });
    }

    @Test
    public void testSetValue_dynamicPropertyCreation() {
        // Test with NullPropertyPointer object and dynamic property creation
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        // Set dynamic property declaration support
        parent.setDynamicPropertyDeclarationSupported(true);
        // Set the property name
        pointer.setPropertyName("testProperty");
        // Set the value
        pointer.setValue("test value");
        
        // Check if the value is set correctly
        assertEquals("test value", parent.getPropertyPointer().getValue());
    }

    @Test
    public void testSetValue_notDynamicPropertyCreation() {
        // Test with NullPropertyPointer object and not dynamic property creation
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        // Set dynamic property declaration support
        parent.setDynamicPropertyDeclarationSupported(false);
        // Set the property name
        pointer.setPropertyName("testProperty");
        
        assertThrows(JXPathInvalidAccessException.class, () -> {
            pointer.setValue("test value");
        });
    }

    @Test
    public void testCreatePath_attribute() {
        // Test with NullPropertyPointer object and attribute creation
        JXPathContext context = new JXPathContext();
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setAttribute(true);
        
        // Create path with attribute
        NodePointer path = pointer.createPath(context);
        
        // Check if the path is created correctly
        assertTrue(path instanceof AttributePointer);
        assertEquals(pointer.getName(), path.getName());
    }

    @Test
    public void testCreatePath_propertyOwner() {
        // Test with NullPropertyPointer object and property owner parent
        JXPathContext context = new JXPathContext();
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        // Create path with property owner parent
        NodePointer path = pointer.createPath(context);
        
        // Check if the path is created correctly
        assertTrue(path instanceof PropertyPointer);
        assertEquals(pointer.getName(), path.getName());
    }

    @Test
    public void testCreateChild_attribute() {
        // Test with NullPropertyPointer object and attribute creation
        JXPathContext context = new JXPathContext();
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setAttribute(true);
        
        // Create child with attribute
        NodePointer child = pointer.createChild(context, new QName("test"), 0);
        
        // Check if the child is created correctly
        assertTrue(child instanceof AttributePointer);
        assertEquals(new QName("test"), child.getName());
        assertEquals(0, child.getIndex());
    }

    @Test
    public void testCreateChild_propertyOwner() {
        // Test with NullPropertyPointer object and property owner parent
        JXPathContext context = new JXPathContext();
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        // Create child with property owner parent
        NodePointer child = pointer.createChild(context, new QName("test"), 0);
        
        // Check if the child is created correctly
        assertTrue(child instanceof PropertyPointer);
        assertEquals(new QName("test"), child.getName());
        assertEquals(0, child.getIndex());
    }

    @Test
    public void testAsPath_noNameAttribute() {
        // Test with NullPropertyPointer object without name attribute
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        
        // Check the generated path without name attribute
        assertEquals("null:*", pointer.asPath());
    }

    @Test
    public void testAsPath_withNameAttribute_noIndex() {
        // Test with NullPropertyPointer object with name attribute and no index
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("testProperty");
        
        // Check the generated path with name attribute and no index
        assertEquals("null:[@name='testProperty']", pointer.asPath());
    }

    @Test
    public void testAsPath_withNameAttribute_withIndex() {
        // Test with NullPropertyPointer object with name attribute and index
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("testProperty");
        pointer.setIndex(0);
        
        // Check the generated path with name attribute and index
        assertEquals("null:[@name='testProperty'][1]", pointer.asPath());
    }

    @Test
    public void testEscape_noEscaping() {
        // Test with NullPropertyPointer object and no escaping required
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String value = "test value";
        
        String escaped = pointer.escape(value);
        
        // Check if the value is not modified
        assertEquals(value, escaped);
    }

    @Test
    public void testEscape_withEscaping() {
        // Test with NullPropertyPointer object and escaping required
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String value = "value with ' single and \" double quotes";
        
        String escaped = pointer.escape(value);
        
        // Check if the value is escaped correctly
        assertEquals("value with &apos; single and &quot; double quotes", escaped);
    }
}