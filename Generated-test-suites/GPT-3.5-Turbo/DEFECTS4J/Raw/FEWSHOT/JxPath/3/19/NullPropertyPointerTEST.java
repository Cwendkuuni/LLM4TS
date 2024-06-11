import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {
    
    private NullPropertyPointer nullPropertyPointer;
    
    @Before
    public void setup() {
        NodePointer parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }
    
    @Test
    public void testGetName() {
        QName expected = new QName("*");
        assertEquals(expected, nullPropertyPointer.getName());
    }
    
    @Test
    public void testSetPropertyIndex() {
        int index = 0;
        nullPropertyPointer.setPropertyIndex(index);
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
        assertEquals(true, nullPropertyPointer.isLeaf());
    }
    
    @Test
    public void testGetValuePointer() {
        NullPointer nullPointer = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        assertEquals(nullPointer, nullPropertyPointer.getValuePointer());
    }
    
    @Test
    public void testIsActualProperty() {
        assertEquals(false, nullPropertyPointer.isActualProperty());
    }
    
    @Test
    public void testIsActual() {
        assertEquals(false, nullPropertyPointer.isActual());
    }
    
    @Test
    public void testIsContainer() {
        assertEquals(true, nullPropertyPointer.isContainer());
    }
    
    @Test
    public void testSetValue() {
        Object value = new Object();
        try {
            nullPropertyPointer.setValue(value);
        } catch (JXPathInvalidAccessException e) {
            assertEquals("Cannot set property null, the target object is null", e.getMessage());
        }
    }
    
    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer expected = null;
        assertEquals(expected, nullPropertyPointer.createPath(context));
    }
    
    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        NodePointer expected = null;
        assertEquals(expected, nullPropertyPointer.createPath(context, value));
    }
    
    @Test
    public void testCreateChildWithNameAndIndex() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("");
        int index = 0;
        NodePointer expected = null;
        assertEquals(expected, nullPropertyPointer.createChild(context, name, index));
    }
    
    @Test
    public void testCreateChildWithNameIndexAndValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("");
        int index = 0;
        Object value = new Object();
        NodePointer expected = null;
        assertEquals(expected, nullPropertyPointer.createChild(context, name, index, value));
    }
    
    @Test
    public void testGetPropertyName() {
        String expected = "*";
        assertEquals(expected, nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetPropertyName() {
        String propertyName = "foobar";
        nullPropertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testSetNameAttributeValue() {
        String attributeValue = "foobar";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        assertEquals(attributeValue, nullPropertyPointer.getPropertyName());
    }
    
    @Test
    public void testIsCollection() {
        boolean expected = false;
        assertEquals(expected, nullPropertyPointer.isCollection());
    }
    
    @Test
    public void testGetPropertyCount() {
        int expected = 0;
        assertEquals(expected, nullPropertyPointer.getPropertyCount());
    }
    
    @Test
    public void testGetPropertyNames() {
        String[] expected = new String[0];
        assertEquals(expected, nullPropertyPointer.getPropertyNames());
    }
    
    @Test
    public void testAsPath() {
        String expected = "[@name='*']";
        assertEquals(expected, nullPropertyPointer.asPath());
    }
    
    @Test
    public void testEscape() {
        String string = "foo'bar";
        String expected = "foo&apos;bar";
        assertEquals(expected, nullPropertyPointer.escape(string));
    }
}