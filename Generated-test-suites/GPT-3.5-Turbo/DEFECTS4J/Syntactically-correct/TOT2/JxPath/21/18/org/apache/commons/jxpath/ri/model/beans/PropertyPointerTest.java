import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        propertyPointer = new PropertyPointerTestImpl(null);
    }

    @Test
    public void testGetPropertyIndex() {
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        int index = 5;
        propertyPointer.setPropertyIndex(index);
        assertEquals(index, propertyPointer.getPropertyIndex());
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
        assertNotNull(((PropertyPointerTestImpl) propertyPointer).getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "testProperty";
        ((PropertyPointerTestImpl) propertyPointer).setPropertyName(propertyName);
        assertEquals(propertyName, ((PropertyPointerTestImpl) propertyPointer).getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        assertEquals(0, ((PropertyPointerTestImpl) propertyPointer).getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        assertNotNull(((PropertyPointerTestImpl) propertyPointer).getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        assertTrue(((PropertyPointerTestImpl) propertyPointer).isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        assertNotNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testIsCollection() {
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void testGetLength() {
        assertEquals(0, propertyPointer.getLength());
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
    public void testCreateChild() {
        assertNotNull(propertyPointer.createChild(null, null, 0, null));
    }
}

class PropertyPointerTestImpl extends PropertyPointer {

    public PropertyPointerTestImpl(NodePointer parent) {
        super(parent);
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public void setPropertyName(String propertyName) {
        // Do nothing
    }

    @Override
    public int getPropertyCount() {
        return 0;
    }

    @Override
    public String[] getPropertyNames() {
        return new String[0];
    }

    @Override
    protected boolean isActualProperty() {
        return true;
    }
}