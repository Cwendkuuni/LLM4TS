import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());
    }

    @Test
    public void testSetPropertyIndex() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        pointer.setPropertyIndex(1);
        assertEquals(1, pointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertNotNull(pointer.getBean());
    }

    @Test
    public void testGetName() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertEquals("propertyName", pointer.getName().getName());
    }

    @Test
    public void testGetPropertyName() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertEquals("propertyName", pointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        pointer.setPropertyName("newPropertyName");
        assertEquals("newPropertyName", pointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertEquals(2, pointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        String[] expected = {"property1", "property2"};
        assertArrayEquals(expected, pointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertTrue(pointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertNotNull(pointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertFalse(pointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertFalse(pointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertEquals(3, pointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertNotNull(pointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertNotNull(pointer.createPath(new JXPathContext()));
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        assertNotNull(pointer.createPath(new JXPathContext(), "value"));
    }

    @Test
    public void testCreateChildWithPathAndValue() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        QName name = new QName("testName");
        NodePointer child = pointer.createChild(new JXPathContext(), name, 0, "value");
        assertNotNull(child);
        assertEquals(name, child.getName());
        assertEquals(0, child.getIndex());
    }

    @Test
    public void testCreateChildWithPathAndNoValue() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        QName name = new QName("testName");
        NodePointer child = pointer.createChild(new JXPathContext(), name, 0);
        assertNotNull(child);
        assertEquals(name, child.getName());
        assertEquals(0, child.getIndex());
    }

    @Test
    public void testHashCode() {
        PropertyPointer pointer1 = new ConcretePropertyPointer();
        PropertyPointer pointer2 = new ConcretePropertyPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer pointer1 = new ConcretePropertyPointer();
        PropertyPointer pointer2 = new ConcretePropertyPointer();
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer pointer = new ConcretePropertyPointer();
        NodePointer nodePointer1 = new ConcreteNodePointer();
        NodePointer nodePointer2 = new ConcreteNodePointer();
        assertEquals(0, pointer.compareChildNodePointers(nodePointer1, nodePointer2));
    }
}

class ConcretePropertyPointer extends PropertyPointer {

    public ConcretePropertyPointer() {
        super(null);
    }

    @Override
    public String getPropertyName() {
        return "propertyName";
    }

    @Override
    public void setPropertyName(String propertyName) {
    }

    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public String[] getPropertyNames() {
        return new String[]{"property1", "property2"};
    }

    @Override
    protected boolean isActualProperty() {
        return true;
    }
}

class ConcreteNodePointer extends NodePointer {

    @Override
    public QName getName() {
        return new QName("testName");
    }

    @Override
    public Object getImmediateNode() {
        return null;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public NodePointer getImmediateValuePointer() {
        return null;
    }
}