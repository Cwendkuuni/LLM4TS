import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setup() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }

    @Test
    public void testGetName() {
        assertEquals("*", nullPropertyPointer.getName().toString());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
        //No assertion as this method doesn't return anything
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
        assertTrue(valuePointer instanceof NullPointer);
        assertEquals("*", valuePointer.getName().toString());
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
    public void testSetValueWithNullParent() {
        nullPropertyPointer.setValue(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonContainerParent() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        parent.setValue(null);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithDynamicPropertyDeclarationNotSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer(null);
        parent.setDynamicPropertyDeclarationSupported(false);
        parent.addPropertyPointer(nullPropertyPointer);
        nullPropertyPointer.setParent(parent);
        nullPropertyPointer.setValue(null);
    }

    @Test
    public void testSetValueWithDynamicPropertyDeclarationSupported() {
        PropertyOwnerPointer parent = new PropertyOwnerPointer(null);
        parent.setDynamicPropertyDeclarationSupported(true);
        parent.addPropertyPointer(nullPropertyPointer);
        nullPropertyPointer.setParent(parent);

        //No assertion as this method doesn't return anything
        nullPropertyPointer.setValue(null);
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer path = nullPropertyPointer.createPath(context);
        assertNull(path);
    }

    @Test
    public void testCreatePathWithAttributeValue() {
        JXPathContext context = new JXPathContext();
        NodePointer path = nullPropertyPointer.createPath(context, "value");
        assertNull(path);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("name");
        int index = 0;
        NodePointer child = nullPropertyPointer.createChild(context, name, index);
        assertNull(child);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("name");
        int index = 0;
        NodePointer child = nullPropertyPointer.createChild(context, name, index, "value");
        assertNull(child);
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("*", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("name");
        assertEquals("name", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("name");
        assertEquals("name", nullPropertyPointer.getPropertyName());
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
        assertEquals(0, nullPropertyPointer.getPropertyNames().length);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer parent = new NullPropertyPointer(null);
        parent.setNameAttributeValue("parent");
        parent.addPropertyPointer(nullPropertyPointer);
        nullPropertyPointer.setParent(parent);

        assertEquals("parent[@name='*']", nullPropertyPointer.asPath());
    }

    private static class PropertyOwnerPointer extends NodePointer {
    
        private boolean dynamicPropertyDeclarationSupported;
    
        public PropertyOwnerPointer(NodePointer parent) {
            super(parent);
        }
    
        public NodePointer getPropertyPointer() {
            return new PropertyPointer(this);
        }
    
        public boolean isDynamicPropertyDeclarationSupported() {
            return dynamicPropertyDeclarationSupported;
        }
    
        public void setDynamicPropertyDeclarationSupported(boolean dynamicPropertyDeclarationSupported) {
            this.dynamicPropertyDeclarationSupported = dynamicPropertyDeclarationSupported;
        }
    
        public void addPropertyPointer(NullPropertyPointer propertyPointer) {
            //No implementation needed for testing
        }
    
        @Override
        public boolean isLeaf() {
            return false;
        }
    }
}