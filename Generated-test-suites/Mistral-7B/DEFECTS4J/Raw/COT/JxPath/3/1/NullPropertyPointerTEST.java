import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals("*", nullPropertyPointer.getPropertyName());
        assertFalse(nullPropertyPointer.isActualProperty());
        assertFalse(nullPropertyPointer.isActual());
        assertTrue(nullPropertyPointer.isContainer());
        assertEquals(0, nullPropertyPointer.getLength());
        assertNull(nullPropertyPointer.getBaseValue());
        assertNull(nullPropertyPointer.getImmediateNode());
        assertTrue(nullPropertyPointer.isLeaf());
        assertNull(nullPropertyPointer.getValuePointer());
    }

    @Test
    public void testGetNameMethod() {
        NodePointer parent = new NodePointer(new Object());
        String propertyName = "testProperty";
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName(propertyName);
        QName expectedName = new QName(propertyName);
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName.getLocalPart(), actualName.getLocalPart());
        assertEquals(expectedName.getNamespaceURI(), actualName.getNamespaceURI());
    }

    @Test
    public void testSetPropertyIndexMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        int index = 0;
        nullPropertyPointer.setPropertyIndex(index);
        assertEquals(index, nullPropertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetLengthMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValueMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNodeMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeafMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testgetValuePointerMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertInstanceOf(NullPointer.class, nullPropertyPointer.getValuePointer().getNode());
    }

    @Test
    public void testIsActualPropertyMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActualMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainerMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertTrue(nullPropertyPointer.isContainer());
    }

    @Test
    public void testSetValueMethod_NULL_Parent() {
        Object value = new Object();
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(null);
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(value));
    }

    @Test
    public void testSetValueMethod_Parent_Not_PropertyOwner() {
        Object value = new Object();
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(value));
    }

    @Test
    public void testSetValueMethod_PropertyOwner_DynamicPropertyDeclarationSupported_Success() {
        Object value = new Object();
        NodePointer parent = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        PropertyPointer propertyPointer = ((PropertyOwnerPointer) parent).getPropertyPointer();
        propertyPointer.setPropertyName("testProperty");
        propertyPointer.setValue(value);
        assertEquals(value, nullPropertyPointer.getValuePointer().getNode().getValue());
    }

    @Test
    public void testSetValueMethod_PropertyOwner_DynamicPropertyDeclarationNotSupported_Failure() {
        Object value = new Object();
        NodePointer parent = new PropertyOwnerPointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertThrows(JXPathInvalidAccessException.class, () -> nullPropertyPointer.setValue(value));
    }

    @Test
    public void testCreatePathMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        NodePointer expectedPath = new NodePointer(parent);
        NodePointer actualPath = nullPropertyPointer.createPath(context);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testCreatePathMethod_WithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        Object value = new Object();
        NodePointer expectedPath = new PropertyOwnerPointer(parent).
                createChild(context, new QName("testProperty"), null, value);
        NodePointer actualPath = nullPropertyPointer.createPath(context, value);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testCreateChildMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        QName name = new QName("testProperty");
        NodePointer expectedChild = parent.createChild(context, name, null);
        NodePointer actualChild = nullPropertyPointer.createChild(context, name, 0);
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testCreateChildMethod_WithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(null);
        QName name = new QName("testProperty");
        Object value = new Object();
        NodePointer expectedChild = parent.createChild(context, name, 0, value);
        NodePointer actualChild = nullPropertyPointer.createChild(context, name, 0, value);
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testSetPropertyNameMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String propertyName = "testProperty";
        nullPropertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValueMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String attributeValue = "testAttributeValue";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        assertEquals(attributeValue, nullPropertyPointer.getPropertyName());
        assertTrue(nullPropertyPointer.isByNameAttribute());
    }

    @Test
    public void testIsCollectionMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testgetPropertyCountMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testgetPropertyNamesMethod() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String[] expectedNames = new String[0];
        String[] actualNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void testasPathMethod_WithoutByNameAttribute() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertEquals("/", nullPropertyPointer.asPath());
    }

    @Test
    public void testasPathMethod_WithByNameAttribute() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        String propertyName = "testProperty";
        nullPropertyPointer.setPropertyName(propertyName);
        nullPropertyPointer.setNameAttributeValue(propertyName);
        String index = "1";
        String expectedPath = "/" + parent.asPath() + "[@name='" + propertyName + "'][" + index + "]";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testEscapeMethod() {
        String string = "test'string";
        String expectedEscapedString = "test&apos;string";
        String actualEscapedString = new NullPropertyPointer(null).escape(string);
        assertEquals(expectedEscapedString, actualEscapedString);
    }
}