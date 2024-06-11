import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNotNull(nullPropertyPointer);
    }

    @Test
    public void testGetNameMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        QName qName = nullPropertyPointer.getName();
        assertEquals("*", qName.getLocalPart());
        assertEquals("", qName.getNamespaceURI());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyIndexMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyIndex(0);
    }

    @Test
    public void testGetLengthMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValueMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNodeMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeafMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testGetValuePointerMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertInstanceOf(NullPointer.class, nullPropertyPointer.getValuePointer().getNode());
    }

    @Test
    public void testIsActualPropertyMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActualMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainerMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueMethodNullParent() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setValue(new Integer(1));
    }

    @Test
    public void testSetValueMethodPropertyOwnerParent() {
        NodePointer parent = new PropertyOwnerPointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Integer(1));
        PropertyPointer propertyPointer = parent.getPropertyPointer();
        assertEquals("*", propertyPointer.getPropertyName().getLocalPart());
        assertEquals(new Integer(1), propertyPointer.getValue());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueMethodNonPropertyParent() {
        NodePointer parent = new DOMNodePointer(null, null, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(new Integer(1));
    }

    @Test
    public void testCreatePathMethod() {
        JXPathContext context = new StaticContext();
        NodePointer parent = new PropertyOwnerPointer(context, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(context);
        assertInstanceOf(PropertyOwnerPointer.class, result.getParent());
    }

    @Test
    public void testCreatePathMethodAttribute() {
        JXPathContext context = new StaticContext();
        NodePointer parent = new PropertyOwnerPointer(context, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(context, "attrName");
        assertInstanceOf(AttributePointer.class, result.getNode());
    }

    @Test
    public void testCreatePathMethodCollection() {
        JXPathContext context = new StaticContext();
        NodePointer parent = new PropertyOwnerPointer(context, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(context, "colName", 0);
        assertInstanceOf(ArrayPointer.class, result.getNode());
    }

    @Test
    public void testCreatePathMethodWithValue() {
        JXPathContext context = new StaticContext();
        NodePointer parent = new PropertyOwnerPointer(context, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createPath(context, "colName", 0, new Integer(1));
        assertInstanceOf(ArrayItemPointer.class, result.getNode());
        assertEquals(new Integer(1), result.getValue());
    }

    @Test
    public void testCreateChildMethod() {
        JXPathContext context = new StaticContext();
        NodePointer parent = new PropertyOwnerPointer(context, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createChild(context, new QName("name"), 0);
        assertInstanceOf(PropertyPointer.class, result.getNode());
    }

    @Test
    public void testCreateChildMethodWithValue() {
        JXPathContext context = new StaticContext();
        NodePointer parent = new PropertyOwnerPointer(context, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NodePointer result = nullPropertyPointer.createChild(context, new QName("name"), 0, new Integer(1));
        assertInstanceOf(ArrayItemPointer.class, result.getNode());
        assertEquals(new Integer(1), result.getValue());
    }

    @Test
    public void testSetPropertyNameMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setPropertyName("newName");
        assertEquals("newName", nullPropertyPointer.getPropertyName().getLocalPart());
    }

    @Test
    public void testSetNameAttributeValueMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        nullPropertyPointer.setNameAttributeValue("newName");
        assertEquals("newName", nullPropertyPointer.getPropertyName().getLocalPart());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollectionMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testgetPropertyCountMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testgetPropertyNamesMethod() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        String[] result = nullPropertyPointer.getPropertyNames();
        assertEquals(0, result.length);
    }

    @Test
    public void testAsPathMethod() {
        JXPathContext context = new StaticContext();
        NodePointer parent = new PropertyOwnerPointer(context, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String result = nullPropertyPointer.asPath();
        assertEquals("", result);
    }

    @Test
    public void testAsPathMethodByNameAttribute() {
        JXPathContext context = new StaticContext();
        NodePointer parent = new PropertyOwnerPointer(context, null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("attrName");
        nullPropertyPointer.setIndex(0);
        String result = nullPropertyPointer.asPath();
        assertEquals("[@name='attrName']", result);
    }
}