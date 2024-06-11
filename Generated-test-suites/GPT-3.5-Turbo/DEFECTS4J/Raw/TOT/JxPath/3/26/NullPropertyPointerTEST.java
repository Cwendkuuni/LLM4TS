import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;

import static org.junit.Assert.*;

public class NullPropertyPointerTest {
    
    private NullPropertyPointer nullPropertyPointer;
    private NodePointer parent;
    
    @Before
    public void setUp() {
        parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }
    
    @Test
    public void testGetName() {
        assertEquals("*", nullPropertyPointer.getName().getName());
    }
    
    @Test
    public void testGetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(5);
        assertEquals(0, nullPropertyPointer.getPropertyIndex());
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
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        assertNotNull(valuePointer);
        assertTrue(valuePointer instanceof NullPointer);
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
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_withNullParent() {
        nullPropertyPointer.setValue("Test Value");
    }
    
    @Test
    public void testSetValue_withDynamicPropertyDeclarationSupported() {
        NodePointer parentWithDynamic = new PropertyOwnerPointer();
        NullPropertyPointer dynamicPropertyPointer = new NullPropertyPointer(parentWithDynamic);
        parentWithDynamic.setDynamicPropertyDeclarationSupported(true);
        
        dynamicPropertyPointer.setValue("Test Value");
        
        PropertyPointer propertyPointer = ((PropertyOwnerPointer) parentWithDynamic).getPropertyPointer();
        assertNotNull(propertyPointer);
        assertEquals("Test Value", propertyPointer.getValue());
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_withNonChangeableLocation() {
        NodePointer parentWithNonChangeable = new PropertyOwnerPointer();
        NullPropertyPointer nonChangeablePropertyPointer = new NullPropertyPointer(parentWithNonChangeable);
        
        nonChangeablePropertyPointer.setValue("Test Value");
    }
    
    @Test
    public void testCreatePath_withNullParent() {
        JXPathContext context = new JXPathContext();
        NodePointer newPath = nullPropertyPointer.createPath(context);
        
        assertNotNull(newPath);
        assertTrue(newPath instanceof NullPointer);
    }
    
    @Test
    public void testCreatePath_withAttribute() {
        NodePointer parentNodeWithNull = new NullPointer();
        NullPropertyPointer attributePropertyPointer = new NullPropertyPointer(parentNodeWithNull);
        attributePropertyPointer.setNameAttributeValue("testAttribute");
        
        JXPathContext context = new JXPathContext();
        NodePointer newAttribute = attributePropertyPointer.createPath(context);
        
        assertNotNull(newAttribute);
        assertTrue(newAttribute instanceof AttributePointer);
        assertEquals("testAttribute", newAttribute.getName().getNamespaceURI());
        assertEquals("", newAttribute.getName().getPrefix());
        assertEquals("testAttribute", newAttribute.getName().getName());
    }
    
    @Test
    public void testCreatePath_withAttributeAndValue() {
        NodePointer parentNodeWithNull = new NullPointer();
        NullPropertyPointer attributePropertyPointer = new NullPropertyPointer(parentNodeWithNull);
        attributePropertyPointer.setNameAttributeValue("testAttribute");
        
        JXPathContext context = new JXPathContext();
        NodePointer newAttribute = attributePropertyPointer.createPath(context, "Test Value");
        
        assertNotNull(newAttribute);
        assertTrue(newAttribute instanceof AttributePointer);
        assertEquals("testAttribute", newAttribute.getName().getNamespaceURI());
        assertEquals("", newAttribute.getName().getPrefix());
        assertEquals("testAttribute", newAttribute.getName().getName());
        assertEquals("Test Value", newAttribute.getValue());
    }
    
    @Test
    public void testCreatePath_withPropertyOwnerPointer() {
        NodePointer parentPropertyOwner = new PropertyOwnerPointer();
        NullPropertyPointer propertyPointer = new NullPropertyPointer(parentPropertyOwner);
        
        JXPathContext context = new JXPathContext();
        ((PropertyOwnerPointer) parentPropertyOwner).setDynamicPropertyDeclarationSupported(true);
        
        NodePointer newPath = propertyPointer.createPath(context);
        
        assertNotNull(newPath);
        assertTrue(newPath instanceof PropertyPointer);
    }
    
    @Test
    public void testCreatePath_withPropertyOwnerPointerAndValue() {
        NodePointer parentPropertyOwner = new PropertyOwnerPointer();
        NullPropertyPointer propertyPointer = new NullPropertyPointer(parentPropertyOwner);
        
        JXPathContext context = new JXPathContext();
        ((PropertyOwnerPointer) parentPropertyOwner).setDynamicPropertyDeclarationSupported(true);
        
        NodePointer newPath = propertyPointer.createPath(context, "Test Value");
        
        assertNotNull(newPath);
        assertTrue(newPath instanceof PropertyPointer);
        assertEquals("Test Value", newPath.getValue());
    }
    
    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("testChild");
        int index = 1;
        
        NodePointer newChild = nullPropertyPointer.createChild(context, name, index);
        
        assertNotNull(newChild);
        assertTrue(newChild instanceof NullPointer);
    }
    
    @Test
    public void testCreateChild_withValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("testChild");
        int index = 1;
        Object value = "Test Value";
        
        NodePointer newChild = nullPropertyPointer.createChild(context, name, index, value);
        
        assertNotNull(newChild);
        assertTrue(newChild instanceof NullPointer);
        assertEquals("Test Value", newChild.getValue());
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
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        assertEquals("testAttribute", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testIsCollection() {
        assertFalse(nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        assertNotNull(propertyNames);
        assertEquals(0, propertyNames.length);
    }
    
    @Test
    public void testAsPath() {
        assertEquals("*", nullPropertyPointer.asPath());
        
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        assertEquals("[@name='testAttribute']", nullPropertyPointer.asPath());
        
        nullPropertyPointer.setPropertyIndex(2);
        assertEquals("[@name='testAttribute'][3]", nullPropertyPointer.asPath());
    }
    
    @Test
    public void testEscape() {
        String escaped = nullPropertyPointer.escape("'");
        assertEquals("&apos;", escaped);
        
        escaped = nullPropertyPointer.escape("\"");
        assertEquals("&quot;", escaped);
        
        escaped = nullPropertyPointer.escape("test'value");
        assertEquals("test&apos;value", escaped);
        
        escaped = nullPropertyPointer.escape("test\"value");
        assertEquals("test&quot;value", escaped);
    }
}