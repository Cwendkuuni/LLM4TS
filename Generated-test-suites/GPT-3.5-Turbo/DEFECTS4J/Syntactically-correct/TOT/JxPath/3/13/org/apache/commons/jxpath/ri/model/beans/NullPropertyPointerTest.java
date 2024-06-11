import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.PropertyOwnerPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        assertEquals(nullProperty.getName().getName(), "*");
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        nullProperty.setPropertyIndex(5);
        // No assertion as setter method only assigns a value and does not return anything
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // The null property should have length 0
        assertEquals(nullProperty.getLength(), 0);
    }

    @Test
    public void testGetBaseValue() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // The base value of null property is null
        assertNull(nullProperty.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // The immediate node of null property is null
        assertNull(nullProperty.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // Null property is a leaf node
        assertTrue(nullProperty.isLeaf());
    }

    @Test
    public void testGetValuePointer() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // The value pointer of null property is a NullPointer with the same property name
        assertEquals(nullProperty.getValuePointer().getClass(), NullPointer.class);
        assertEquals(nullProperty.getValuePointer().getName().getName(), nullProperty.getPropertyName());
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // Null property is not an actual property
        assertFalse(nullProperty.isActualProperty());
    }

    @Test
    public void testIsActual() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // Null property is not an actual node
        assertFalse(nullProperty.isActual());
    }

    @Test
    public void testIsContainer() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // Null property is a container
        assertTrue(nullProperty.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NullParent() {
        NullPropertyPointer nullProperty = new NullPropertyPointer(null);
        
        // Setting value on null property with null parent should throw JXPathInvalidAccessException
        nullProperty.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NonContainerParent() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // Setting value on null property with non-container parent should throw JXPathInvalidAccessException
        nullProperty.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_PropertyOwnerWithDynamicPropertyDeclaration() {
        NodePointer parent = new PropertyOwnerPointer(null);
        ((PropertyOwnerPointer) parent).setDynamicPropertyDeclarationSupported(true);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // Setting value on null property with property owner parent with dynamic property declaration support should throw JXPathInvalidAccessException
        nullProperty.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_PropertyOwnerWithoutDynamicPropertyDeclaration() {
        NodePointer parent = new PropertyOwnerPointer(null);
        ((PropertyOwnerPointer) parent).setDynamicPropertyDeclarationSupported(false);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // Setting value on null property with property owner parent without dynamic property declaration should throw JXPathInvalidAccessException
        nullProperty.setValue("value");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = JXPathContext.newContext();
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // Creating path using null property with empty context should return a new NullPointer with the same property name
        assertEquals(nullProperty.createPath(context).getClass(), NullPointer.class);
        assertEquals(nullProperty.createPath(context).getName().getName(), nullProperty.getPropertyName());
    }

    @Test
    public void testCreatePath_WithPropertyValue() {
        JXPathContext context = JXPathContext.newContext();
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        String value = "test";
        
        // Creating path using null property with empty context and property value should return a new NullPointer with the same property name and set the value
        NullPointer newNullPointer = (NullPointer) nullProperty.createPath(context, value);
        assertEquals(newNullPointer.getClass(), NullPointer.class);
        assertEquals(newNullPointer.getName().getName(), nullProperty.getPropertyName());
        assertEquals(newNullPointer.getValue(), value);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = JXPathContext.newContext();
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        QName name = new QName("test");
        int index = 1;
        
        // Creating child using null property with empty context, name, and index should return a new child node pointer with the same property name and index
        NodePointer newChild = nullProperty.createChild(context, name, index);
        assertEquals(newChild.getClass(), NullPropertyPointer.class);
        assertEquals(((NullPropertyPointer) newChild).getPropertyName(), nullProperty.getPropertyName());
        assertEquals(((NullPropertyPointer) newChild).getIndex(), index);
    }

    @Test
    public void testCreateChild_WithPropertyValue() {
        JXPathContext context = JXPathContext.newContext();
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        QName name = new QName("test");
        int index = 1;
        String value = "value";
        
        // Creating child using null property with empty context, name, index, and value should return a new child node pointer with the same property name, index, and value
        NodePointer newChild = nullProperty.createChild(context, name, index, value);
        assertEquals(newChild.getClass(), NullPropertyPointer.class);
        assertEquals(((NullPropertyPointer) newChild).getPropertyName(), nullProperty.getPropertyName());
        assertEquals(((NullPropertyPointer) newChild).getIndex(), index);
        assertEquals(((NullPropertyPointer) newChild).getValue(), value);
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // The property name should be "*"
        assertEquals(nullProperty.getPropertyName(), "*");
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        String propertyName = "newProperty";
        
        nullProperty.setPropertyName(propertyName);
        
        // The property name should be updated to "newProperty"
        assertEquals(nullProperty.getPropertyName(), propertyName);
    }

    @Test
    public void testSetNameAttributeValue() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        String attributeName = "name";
        String attributeValue = "newPropertyValue";
        
        nullProperty.setNameAttributeValue(attributeValue);
        
        // The property name should be updated to the attribute value and byNameAttribute should be true
        assertEquals(nullProperty.getPropertyName(), attributeValue);
        assertTrue(nullProperty.byNameAttribute);
    }

    @Test
    public void testIsCollection_True() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        nullProperty.setPropertyIndex(5);
        
        // The property should be a collection as index is set to a valid value
        assertTrue(nullProperty.isCollection());
    }

    @Test
    public void testIsCollection_False() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // The property should not be a collection as index is set to WHOLE_COLLECTION
        assertFalse(nullProperty.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // The property count should be 0
        assertEquals(nullProperty.getPropertyCount(), 0);
    }
    
    @Test
    public void testGetPropertyNames() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // The property names should be an empty array
        assertArrayEquals(nullProperty.getPropertyNames(), new String[0]);
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        
        // The path should be as expected based on the property name and index values
        String expectedPath = "/[@name='*']";
        assertEquals(nullProperty.asPath(), expectedPath);
    }

    @Test
    public void testEscape() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullProperty = new NullPropertyPointer(parent);
        String string = "te'st\"st'ring";
        String expectedEscapedString = "te&apos;st&quot;st&apos;ring";
        
        // The escaped string should be as expected
        assertEquals(nullProperty.escape(string), expectedEscapedString);
    }
}