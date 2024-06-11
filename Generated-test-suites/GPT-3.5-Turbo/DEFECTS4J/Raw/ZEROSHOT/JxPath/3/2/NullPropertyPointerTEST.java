import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {
    
    private NullPropertyPointer nullPropertyPointer;
    
    @Before
    public void setUp() {
        NodePointer parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }
    
    @Test
    public void testGetName() {
        assertEquals(new QName("*"), nullPropertyPointer.getName());
    }
    
    @Test
    public void testSetPropertyIndex() {
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
        assertNotNull(nullPropertyPointer.getValuePointer());
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
        nullPropertyPointer.setValue("testValue");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_withNonContainerParent() {
        nullPropertyPointer.setParent(new NodePointer());
        nullPropertyPointer.setValue("testValue");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_withNonDynamicPropertyDeclarationParent() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        nullPropertyPointer.setParent(parent);
        nullPropertyPointer.setValue("testValue");
    }
    
    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer newParent = nullPropertyPointer.createPath(context);
        assertNotNull(newParent);
    }
    
    @Test
    public void testCreatePath_withValue() {
        JXPathContext context = new JXPathContext();
        NodePointer newParent = nullPropertyPointer.createPath(context, "testValue");
        assertNotNull(newParent);
        assertEquals("testValue", newParent.getValue());
    }
    
    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        NodePointer child = nullPropertyPointer.createChild(context, name, 2);
        assertNotNull(child);
        assertEquals(name, child.getName());
        assertNull(child.getParent());
    }
    
    @Test
    public void testCreateChild_withValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        NodePointer child = nullPropertyPointer.createChild(context, name, 2, "childValue");
        assertNotNull(child);
        assertEquals(name, child.getName());
        assertEquals("childValue", child.getValue());
        assertNull(child.getParent());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("newProperty");
        assertEquals("newProperty", nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        assertEquals("attributeValue", nullPropertyPointer.getPropertyName());
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
        assertEquals("/*", nullPropertyPointer.asPath());
        
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        assertEquals("/*[@name='attributeValue']", nullPropertyPointer.asPath());
        
        nullPropertyPointer.setIndex(3);
        assertEquals("/*[@name='attributeValue'][4]", nullPropertyPointer.asPath());
    }
}