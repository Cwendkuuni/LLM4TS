import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setUp() {
        nullPropertyPointer = new NullPropertyPointer(null);
    }

    @After
    public void tearDown() {
        nullPropertyPointer = null;
    }

    @Test
    public void testGetName() {
        assertEquals(new QName("*"), nullPropertyPointer.getName());
    }

    @Test
    public void testGetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
        // No assertion, just checking that it can run without throwing any exception
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
    public void testSetValue_ThrowsException() {
        nullPropertyPointer.setValue(null);
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer result = nullPropertyPointer.createPath(context);
        assertNotNull(result);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer result = nullPropertyPointer.createPath(context, null);
        assertNotNull(result);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("name");
        int index = 0;
        NodePointer result = nullPropertyPointer.createChild(context, name, index);
        assertNotNull(result);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("name");
        int index = 0;
        NodePointer result = nullPropertyPointer.createChild(context, name, index, null);
        assertNotNull(result);
    }

    @Test
    public void testGetPropertyName() {
        assertNull(nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "propertyName";
        nullPropertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        String attributeName = "attributeName";
        nullPropertyPointer.setNameAttributeValue(attributeName);
        assertEquals(attributeName, nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        nullPropertyPointer.setPropertyIndex(0);
        assertTrue(nullPropertyPointer.isCollection());
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
        String path = nullPropertyPointer.asPath();
        assertNotNull(path);
        assertEquals("[@name='*']", path);
    }
}