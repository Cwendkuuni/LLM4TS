import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testGetName() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        QName expected = new QName("*");
        QName actual = pointer.getName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPropertyIndex() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setPropertyIndex(0);
        int expected = 0;
        int actual = pointer.getIndex();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLength() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        int expected = 0;
        int actual = pointer.getLength();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetBaseValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Object expected = null;
        Object actual = pointer.getBaseValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        Object expected = null;
        Object actual = pointer.getImmediateNode();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsLeaf() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean expected = true;
        boolean actual = pointer.isLeaf();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsActualProperty() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean expected = false;
        boolean actual = pointer.isActualProperty();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsActual() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean expected = false;
        boolean actual = pointer.isActual();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsContainer() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean expected = true;
        boolean actual = pointer.isContainer();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetValue_NullParent() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        try {
            pointer.setValue("value");
            fail("Expected JXPathInvalidAccessException to be thrown");
        } catch (JXPathInvalidAccessException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testSetValue_ContainerParentAndDynamicPropertyDeclarationSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        parent.setDynamicPropertyDeclarationSupported(true);
        
        try {
            pointer.setValue("value");
            assertNotNull(parent.getPropertyPointer());
            assertEquals("value", parent.getPropertyPointer().getValue());
        } catch (JXPathInvalidAccessException e) {
            fail("Expected no exception to be thrown");
        }
    }
    
    @Test
    public void testSetValue_ContainerParentAndDynamicPropertyDeclarationNotSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        
        try {
            pointer.setValue("value");
            fail("Expected JXPathInvalidAccessException to be thrown");
        } catch (JXPathInvalidAccessException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testCreatePath_Attribute() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();
        NodePointer expected = context.createAttribute(null, new QName("*"));
        NodePointer actual = pointer.createPath(context);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreatePath_PropertyOwnerPointerParent() {
        NodePointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expected = parent.getPropertyPointer().createPath(context);
        NodePointer actual = pointer.createPath(context);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreatePath_PropertyOwnerPointerParentAndValue() {
        NodePointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expected = parent.getPropertyPointer().createPath(context);
        expected.setValue("value");
        NodePointer actual = pointer.createPath(context, "value");
        assertEquals(expected, actual);
        assertEquals("value", expected.getValue());
    }
    
    @Test
    public void testCreatePath_PropertyOwnerPointerParentAndNoValue() {
        NodePointer parent = new PropertyOwnerPointer();
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expected = parent.getPropertyPointer().createPath(context);
        NodePointer actual = pointer.createPath(context, null);
        assertEquals(expected, actual);
        assertEquals(null, expected.getValue());
    }
    
    @Test
    public void testCreatePath_NullPointerParent() {
        NodePointer parent = new NullPointer(null, null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expected = parent.createChild(context, new QName("*"), WHOLE_COLLECTION);
        NodePointer actual = pointer.createPath(context);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreatePath_NullPointerParentAndValue() {
        NodePointer parent = new NullPointer(null, null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expected = parent.createChild(context, new QName("*"), WHOLE_COLLECTION);
        expected.setValue("value");
        NodePointer actual = pointer.createPath(context, "value");
        assertEquals(expected, actual);
        assertEquals("value", expected.getValue());
    }
    
    @Test
    public void testCreatePath_NullPointerParentAndNoValue() {
        NodePointer parent = new NullPointer(null, null);
        NullPropertyPointer pointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expected = parent.createChild(context, new QName("*"), WHOLE_COLLECTION);
        NodePointer actual = pointer.createPath(context, null);
        assertEquals(expected, actual);
        assertEquals(null, expected.getValue());
    }
    
    @Test
    public void testCreateChild() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();
        QName name = new QName("*");
        int index = WHOLE_COLLECTION;
        NodePointer expected = pointer.createPath(context).createChild(context, name, index);
        NodePointer actual = pointer.createChild(context, name, index);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateChildWithValue() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        JXPathContext context = new JXPathContext();
        QName name = new QName("*");
        int index = WHOLE_COLLECTION;
        Object value = "value";
        NodePointer expected = pointer.createPath(context).createChild(context, name, index, value);
        NodePointer actual = pointer.createChild(context, name, index, value);
        assertEquals(expected, actual);
        assertEquals(value, expected.getValue());
    }
    
    @Test
    public void testIsCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        boolean expected = false;
        boolean actual = pointer.isCollection();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        int expected = 0;
        int actual = pointer.getPropertyCount();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String[] expected = new String[0];
        String[] actual = pointer.getPropertyNames();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testAsPath_NotByNameAttribute() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String expected = super.asPath();
        String actual = pointer.asPath();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAsPath_ByNameAttribute_IndexNotWholeCollection() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        pointer.setNameAttributeValue("name");
        pointer.setIndex(0);
        String expected = super.asPath() + "[@name='name'][1]";
        String actual = pointer.asPath();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEscape() {
        NullPropertyPointer pointer = new NullPropertyPointer(null);
        String string = "string";
        String expected = "string";
        String actual = pointer.escape(string);
        assertEquals(expected, actual);
        
        string = "st'ri'ng";
        expected = "st&apos;ri&apos;ng";
        actual = pointer.escape(string);
        assertEquals(expected, actual);
        
        string = "st\"ring";
        expected = "st&quot;ring";
        actual = pointer.escape(string);
        assertEquals(expected, actual);
    }

}