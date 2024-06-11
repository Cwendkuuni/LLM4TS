import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathTestCase;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;

public class NullPropertyPointerTest extends JXPathTestCase {

    // 
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue("test");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonPropertyOwnerPointerParent() {
        NodePointer parent = new NullPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("test");
    }
    
    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonDynamicPropertyDeclarationSupportedParent() {
        NodePointer parent = new PropertyOwnerPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("test");
    }
    
    @Test
    public void testCreatePathWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer result = nullPropertyPointer.createPath(getContext());
        assertNull(result);
    }
    
    @Test
    public void testCreatePathWithNonNullParent() {
        NodePointer parent = new NullPointer(null);
        final QName name = new QName("test");
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(getContext());
        assertNotNull(result);
        assertEquals(parent, result);
    }
    
    @Test
    public void testCreatePathWithNonNullParentAndAttribute() {
        NodePointer parent = new NullPointer(null);
        final QName name = new QName("test");
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(getContext());
        assertNotNull(result);
        assertEquals(parent, result);

        result = nullPropertyPointer.createPath(getContext(), "value");
        assertNotNull(result);
        assertTrue(result.isAttribute());
        assertEquals(name, result.getName());
    }
    
    @Test
    public void testCreatePathWithNonNullParentAndNonAttributeNonPropertyOwnerPointer() {
        NodePointer parent = new NullPointer(null);
        final QName name = new QName("test");
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(getContext());
        assertNotNull(result);
        assertEquals(parent, result);

        result = nullPropertyPointer.createPath(getContext(), "test");
        assertNotNull(result);
        assertFalse(result.isAttribute());
        assertEquals(name, result.getName());
    }
    
    @Test
    public void testCreatePathWithNonNullParentAndNonAttributePropertyOwnerPointer() {
        NodePointer parent = new PropertyOwnerPointer();
        final QName name = new QName("test");
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(getContext());
        assertNotNull(result);
        assertTrue(result instanceof PropertyPointer);

        result = nullPropertyPointer.createPath(getContext(), "test");
        assertNotNull(result);
        assertFalse(result.isAttribute());
        assertEquals(name, result.getName());
    }
    
    @Test
    public void testCreateChildWithNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        NodePointer result = nullPropertyPointer.createChild(getContext(), new QName("test"), 0);
        assertNull(result);
    }
    
    @Test
    public void testCreateChildWithNonNullParent() {
        NodePointer parent = new NullPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createChild(getContext(), new QName("test"), 0);
        assertNotNull(result);
        assertEquals(parent, result);
    }
    
    @Test
    public void testCreateChildWithNonNullParentAndAttribute() {
        NodePointer parent = new NullPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createChild(getContext(), new QName("test"), 0);
        assertNotNull(result);
        assertEquals(parent, result);

        NodePointer child = nullPropertyPointer.createChild(getContext(), new QName("name"), 0, "value");
        assertNotNull(child);
        assertTrue(child.isAttribute());
        assertEquals(new QName("name"), child.getName());
        assertEquals("value", child.getValue());
    }
    
    @Test
    public void testCreateChildWithNonNullParentAndNonAttributeNonPropertyOwnerPointer() {
        NodePointer parent = new NullPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createChild(getContext(), new QName("test"), 0);
        assertNotNull(result);
        assertEquals(parent, result);

        NodePointer child = nullPropertyPointer.createChild(getContext(), new QName("name"), 0);
        assertNotNull(child);
        assertFalse(child.isAttribute());
        assertEquals(new QName("name"), child.getName());
    }
    
    @Test
    public void testCreateChildWithNonNullParentAndNonAttributePropertyOwnerPointer() {
        NodePointer parent = new PropertyOwnerPointer();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createChild(getContext(), new QName("test"), 0);
        assertNotNull(result);
        assertTrue(result instanceof PropertyPointer);

        NodePointer child = nullPropertyPointer.createChild(getContext(), new QName("name"), 0);
        assertNotNull(child);
        assertFalse(child.isAttribute());
        assertEquals(new QName("name"), child.getName());
    }
    
    @Test
    public void testAsPathWithByNameAttributeFalse() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String path = nullPropertyPointer.asPath();
        assertEquals("*", path);
    }
    
    @Test
    public void testAsPathWithByNameAttributeTrue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("test");
        String path = nullPropertyPointer.asPath();
        assertEquals("[@name='test']", path);
    }

    // 
}