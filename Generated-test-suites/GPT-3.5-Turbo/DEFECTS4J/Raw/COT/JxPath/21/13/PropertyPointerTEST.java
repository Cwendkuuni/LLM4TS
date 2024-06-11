import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.apache.commons.jxpath.JXPathContext;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        propertyPointer.setPropertyIndex(5);
        int expected = 5;
        int actual = propertyPointer.getPropertyIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        propertyPointer.setPropertyIndex(5);
        int expected = 5;
        int actual = propertyPointer.getPropertyIndex();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBean() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(parent.getNode()).thenReturn(context);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        Object expected = context;
        Object actual = propertyPointer.getBean();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        QName expected = new QName(null, "propertyName");
        QName actual = propertyPointer.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        String expected = "propertyName";
        String actual = propertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        propertyPointer.setPropertyName("newPropertyName");
        String expected = "newPropertyName";
        String actual = propertyPointer.getPropertyName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyCount() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        int expected = 3;
        int actual = propertyPointer.getPropertyCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyNames() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        String[] expected = {"propertyName1", "propertyName2", "propertyName3"};
        String[] actual = propertyPointer.getPropertyNames();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIsActualProperty() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        propertyPointer.setPropertyName("propertyName");
        boolean expected = true;
        boolean actual = propertyPointer.isActualProperty();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsActual() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        propertyPointer.setPropertyName("propertyName");
        boolean expected = true;
        boolean actual = propertyPointer.isActual();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        Object expected = 5;
        propertyPointer.setIndex(5);
        Object actual = propertyPointer.getImmediateNode();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        boolean expected = true;
        boolean actual = propertyPointer.isCollection();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        boolean expected = true;
        boolean actual = propertyPointer.isLeaf();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLength() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        int expected = 5;
        Mockito.when(propertyPointer.getBaseValue()).thenReturn(new int[5]);
        int actual = propertyPointer.getLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        NodePointer expected = Mockito.mock(NodePointer.class);
        Mockito.when(propertyPointer.clone()).thenReturn(expected);
        propertyPointer.setPropertyName("propertyName");
        propertyPointer.setIndex(5);
        Object value = Mockito.mock(Object.class);
        NodePointer actual = propertyPointer.getImmediateValuePointer();
        assertEquals(expected, actual);
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(parent.getNode()).thenReturn(context);
        AbstractFactory factory = Mockito.mock(AbstractFactory.class);
        JXPathAbstractFactoryException exception = new JXPathAbstractFactoryException("");
        Mockito.when(factory.createObject(Mockito.any(), Mockito.any(), Mockito.any(),
            Mockito.any(), Mockito.anyInt())).thenReturn(false);
        Mockito.when(propertyPointer.getAbstractFactory(context)).thenReturn(factory);
        try {
            propertyPointer.createPath(context);
            fail("Expected JXPathAbstractFactoryException");
        } catch (JXPathAbstractFactoryException e) {
            assertEquals(exception.getClass(), e.getClass());
        }
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(parent.getNode()).thenReturn(context);
        propertyPointer.setIndex(5);
        propertyPointer.createPath(context, "value");
        String expected = "value";
        String actual = (String) propertyPointer.getImmediateNode();
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(parent.getNode()).thenReturn(context);
        QName name = new QName(null, "propertyName");
        NodePointer expected = Mockito.mock(NodePointer.class);
        Mockito.when(propertyPointer.clone()).thenReturn(expected);
        NodePointer actual = propertyPointer.createChild(context, name, 0, "value");
        assertEquals(expected, actual);
        assertEquals("value", propertyPointer.getImmediateNode());
    }

    @Test
    public void testCreateChildWithoutValue() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Mockito.when(parent.getNode()).thenReturn(context);
        QName name = new QName(null, "propertyName");
        NodePointer expected = Mockito.mock(NodePointer.class);
        Mockito.when(propertyPointer.clone()).thenReturn(expected);
        NodePointer actual = propertyPointer.createChild(context, name, 0);
        assertEquals(expected, actual);
        assertNull(propertyPointer.getImmediateNode());
    }

    @Test
    public void testHashCode() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer = new PropertyPointerImpl(parent);
        propertyPointer.setIndex(5);
        int expected = 5;
        int actual = propertyPointer.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        NodePointer parent = Mockito.mock(NodePointer.class);
        PropertyPointer propertyPointer1 = new PropertyPointerImpl(parent);
        PropertyPointer propertyPointer2 = new PropertyPointerImpl(parent);
        propertyPointer1.setPropertyName("propertyName");
        propertyPointer1.setIndex(5);
        propertyPointer2.setPropertyName("propertyName");
        propertyPointer2.setIndex(5);
        assertTrue(propertyPointer1.equals(propertyPointer2));
    }

    @Test
    public void testCompareChildNodePointers() {
       NodePointer pointer1 = Mockito.mock(NodePointer.class);
       NodePointer pointer2 = Mockito.mock(NodePointer.class);
       PropertyPointer propertyPointer = new PropertyPointerImpl(null);
       int expected = 0;
       int actual = propertyPointer.compareChildNodePointers(pointer1, pointer2);
       assertEquals(expected, actual);
    }
}