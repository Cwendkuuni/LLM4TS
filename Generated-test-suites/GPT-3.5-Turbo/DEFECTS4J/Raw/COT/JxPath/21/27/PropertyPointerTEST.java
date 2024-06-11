import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;

    @Before
    public void setUp() {
        propertyPointer = new PropertyPointerMock();
    }

    @Test
    public void getPropertyIndex() {
        int expected = PropertyPointer.UNSPECIFIED_PROPERTY;
        int actual = propertyPointer.getPropertyIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void setPropertyIndex() {
        int index = 5;
        propertyPointer.setPropertyIndex(index);
        assertEquals(index, propertyPointer.getPropertyIndex());
    }

    @Test
    public void getBean() {
        Object bean = new Object();
        propertyPointer.setBaseValue(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void getName() {
        QName expected = new QName(null, propertyPointer.getPropertyName());
        QName actual = propertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getPropertyName() {
        String expected = "propertyName";
        propertyPointer.setPropertyName(expected);
        String actual = propertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void setPropertyName() {
        String propertyName = "newPropertyName";
        propertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, propertyPointer.getPropertyName());
    }

    @Test
    public void getPropertyCount() {
        int expected = 0;
        int actual = propertyPointer.getPropertyCount();
        assertEquals(expected, actual);
    }

    @Test
    public void getPropertyNames() {
        String[] expected = new String[0];
        String[] actual = propertyPointer.getPropertyNames();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void isActualProperty() {
        assertFalse(propertyPointer.isActualProperty());
    }

    @Test
    public void isActual() {
        assertFalse(propertyPointer.isActual());
    }

    @Test
    public void getImmediateNode() {
        Object expected = propertyPointer.getBaseValue();
        Object actual = propertyPointer.getImmediateNode();
        assertEquals(expected, actual);
    }

    @Test
    public void isCollection() {
        assertFalse(propertyPointer.isCollection());
    }

    @Test
    public void isLeaf() {
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void getLength() {
        int expected = 0;
        int actual = propertyPointer.getLength();
        assertEquals(expected, actual);
    }

    @Test
    public void getImmediateValuePointer() {
        NodePointer expected = NodePointer.newChildNodePointer(
                (NodePointer) propertyPointer.clone(),
                propertyPointer.getName(),
                propertyPointer.getImmediateNode()
        );
        NodePointer actual = propertyPointer.getImmediateValuePointer();
        assertEquals(expected, actual);
    }

    @Test
    public void createPathWithNullBaseValue() {
        propertyPointer.setBaseValue(null);
        JXPathContext context = new JXPathContext();
        boolean success = propertyPointer.createPath(context);
        assertTrue(success);
        assertNotNull(propertyPointer.getBaseValue());
    }

    @Test(expected = JXPathAbstractFactoryException.class)
    public void createPathWithNonSuccessfulFactory() {
        JXPathContext context = new JXPathContext();
        propertyPointer.createPath(context);
    }

    @Test
    public void createPathWithValue() {
        propertyPointer.setBaseValue(new Object());
        JXPathContext context = new JXPathContext();
        boolean success = propertyPointer.createPath(context, "testValue");
        assertTrue(success);
        assertEquals("testValue", propertyPointer.getImmediateNode());
    }

    @Test
    public void createChildWithValue() {
        QName name = new QName(null, "childName");
        int index = 0;
        Object value = "childValue";
        NodePointer expected = propertyPointer.createChild(
                null,
                name,
                index,
                value
        );
        NodePointer actual = propertyPointer.createChild(
                null,
                name,
                index,
                value
        );
        assertEquals(expected, actual);
    }

    @Test
    public void hashCodeTest() {
        int expected = propertyPointer.getImmediateParentPointer().hashCode()
                + propertyPointer.getPropertyIndex()
                + propertyPointer.getIndex();
        int actual = propertyPointer.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void equalsTest() {
        assertTrue(propertyPointer.equals(propertyPointer));

        PropertyPointer other = new PropertyPointerMock(propertyPointer.getParent());
        assertTrue(propertyPointer.equals(other));
        assertTrue(other.equals(propertyPointer));

        other.setPropertyIndex(5);
        assertFalse(propertyPointer.equals(other));
        assertFalse(other.equals(propertyPointer));
    }
}

class PropertyPointerMock extends PropertyPointer {

    public PropertyPointerMock() {
        super(null);
    }

    public PropertyPointerMock(NodePointer parent) {
        super(parent);
    }

    @Override
    public String getPropertyName() {
        return null;
    }

    @Override
    public void setPropertyName(String propertyName) {

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
        return false;
    }

    @Override
    public Object getBaseValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }

    @Override
    public PropertyPointer clone() {
        return this;
    }

    @Override
    protected NodePointer createNodePointer() {
        return null;
    }

    @Override
    public NodePointer createPath(JXPathContext context) {
        return this;
    }

    @Override
    public NodePointer createPath(JXPathContext context, Object value) {
        return this;
    }

    @Override
    public NodePointer createChild(JXPathContext context, QName name, int index, Object value) {
        return this;
    }

    @Override
    public NodePointer createChild(JXPathContext context, QName name, int index) {
        return this;
    }

    @Override
    protected ValuePointer getValuePointer() {
        return null;
    }

    @Override
    public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
        return 0;
    }
}