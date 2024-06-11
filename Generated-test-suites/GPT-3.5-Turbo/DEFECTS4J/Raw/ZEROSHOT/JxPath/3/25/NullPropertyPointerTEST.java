import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setup() {
        NodePointer parent = new NullPointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        QName expected = new QName("*");
        assertEquals(expected, nullPropertyPointer.getName());
    }

    @Test
    public void testSetPropertyIndex() {
        int index = 5;
        nullPropertyPointer.setPropertyIndex(index);
        // No assertions as this method has no effect
    }

    @Test
    public void testGetLength() {
        int expected = 0;
        assertEquals(expected, nullPropertyPointer.getLength());
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
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        assertEquals(expected, nullPropertyPointer.getValuePointer());
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
    public void testSetValue_NullParent_ThrowsException() {
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_PropertyOwnerPointer_ReturnsDynamicPropertyDeclarationSupported_ThrowsException() {
        NodePointer parent = new PropertyOwnerPointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NullPointer_ReturnsPropertyOwnerPointer_ThrowsException() {
        NodePointer parent = new NullPointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        assertEquals(expected, nullPropertyPointer.createPath(context));
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        Object value = "value";
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        expected.setValue(value);
        assertEquals(expected, nullPropertyPointer.createPath(context, value));
    }

    @Test
    public void testCreateChildWithNameAndIndex() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("name");
        int index = 5;
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        expected.createChild(context, name, index);
        assertEquals(expected, nullPropertyPointer.createChild(context, name, index));
    }

    @Test
    public void testCreateChildWithNameIndexAndValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("name");
        int index = 5;
        Object value = "value";
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        expected.createChild(context, name, index, value);
        assertEquals(expected, nullPropertyPointer.createChild(context, name, index, value));
    }

    @Test
    public void testGetPropertyName() {
        String expected = "*";
        assertEquals(expected, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "newProperty";
        nullPropertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        String attributeValue = "attributeValue";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        assertEquals(attributeValue, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection_WithIndex_ReturnsTrue() {
        nullPropertyPointer.setPropertyIndex(5);
        assertTrue(nullPropertyPointer.isCollection());
    }

    @Test
    public void testIsCollection_WithoutIndex_ReturnsFalse() {
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        int expected = 0;
        assertEquals(expected, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        String[] expected = new String[0];
        assertArrayEquals(expected, nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        String expected = "/[@name='*']";
        assertEquals(expected, nullPropertyPointer.asPath());
    }

    @Test
    public void testEscape() {
        String string = "foo'bar";
        String expected = "foo&apos;bar";
        assertEquals(expected, nullPropertyPointer.escape(string));
    }
}