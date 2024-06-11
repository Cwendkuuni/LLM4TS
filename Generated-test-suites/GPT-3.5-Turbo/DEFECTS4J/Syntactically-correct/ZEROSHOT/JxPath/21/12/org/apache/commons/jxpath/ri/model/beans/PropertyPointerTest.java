import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;

    @Before
    public void setup() {
        propertyPointer = new PropertyPointer(null){};

    }

    @Test
    public void testGetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(2);
        assertEquals(2, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        assertNotNull(propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        assertNotNull(propertyPointer.getName());
    }

    @Test
    public void testGetPropertyName() {
        assertNotNull(propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        assertNotNull(propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        assertNotNull(propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        assertTrue(propertyPointer.isActual());
    }

    @Test
    public void testGetImmediateNode() {
        assertNotNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        assertEquals(-1, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        assertNotNull(propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        assertNotNull(propertyPointer.createPath(null));
    }

    @Test
    public void testCreatePathWithValue() {
        assertNotNull(propertyPointer.createPath(null, null));
    }

    @Test
    public void testCreateChildWithPath() {
        assertNotNull(propertyPointer.createChild(null, null, 0, null));
    }

    @Test
    public void testCreateChildWithoutPath() {
        assertNotNull(propertyPointer.createChild(null, null, 0));
    }

    @Test
    public void testHashCode() {
        assertEquals(propertyPointer.getImmediateParentPointer().hashCode() + propertyPointer.getPropertyIndex() + propertyPointer.getIndex(), propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(propertyPointer.equals(propertyPointer));
        assertFalse(propertyPointer.equals(null));
        assertFalse(propertyPointer.equals(new Object()));
        assertFalse(propertyPointer.equals(new PropertyPointer(null) {}));
        assertFalse(propertyPointer.equals(new PropertyPointer(propertyPointer) {}));
        assertTrue(propertyPointer.equals(propertyPointer));
    }

    @Test
    public void testCompareChildNodePointers() {
        assertNotNull(propertyPointer.compareChildNodePointers(propertyPointer, propertyPointer));
    }
}