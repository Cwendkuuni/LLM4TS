import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        Object bean = new Object();
        when(getImmediateParentPointer().getNode()).thenReturn(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetPropertyName() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        String propertyName = "testProperty";
        when(propertyPointer.getPropertyName()).thenReturn(propertyName);
        assertEquals(propertyName, propertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        assertEquals(propertyName, propertyPointer.getPropertyName());
    }

    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getPropertyCount()).thenReturn(3);
        assertEquals(3, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        String[] propertyNames = {"testProperty1", "testProperty2", "testProperty3"};
        when(propertyPointer.getPropertyNames()).thenReturn(propertyNames);
        assertArrayEquals(propertyNames, propertyPointer.getPropertyNames());
    }

    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.isActualProperty()).thenReturn(true);
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        Object value = new Object();
        when(getBaseValue()).thenReturn(value);
        assertEquals(value, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(getBaseValue()).thenReturn(new ArrayList<>());
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(getNode()).thenReturn(null);
        assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(getBaseValue()).thenReturn(new ArrayList<>());
        assertEquals(0, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        NodePointer valuePointer = mock(NodePointer.class);
        when(clone()).thenReturn(valuePointer);
        when(getName()).thenReturn(new QName(null, "test"));
        when(getImmediateNode()).thenReturn(new Object());
        assertEquals(valuePointer, propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        JXPathContext context = mock(JXPathContext.class);
        AbstractFactory factory = mock(AbstractFactory.class);
        when(getAbstractFactory(context)).thenReturn(factory);
        propertyPointer.createPath(context);
        verify(factory, times(1)).createObject(eq(context), eq(propertyPointer), any(), eq(propertyPointer.getPropertyName()), eq(0));
    }

    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        JXPathContext context = mock(JXPathContext.class);
        when(index).thenReturn(0);
        propertyPointer.createPath(context, new Object());
        verify(propertyPointer, times(1)).setValue(any());
    }

    @Test
    public void testCreateChildWithPathAndValue() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        JXPathContext context = mock(JXPathContext.class);
        QName name = new QName(null, "test");
        int index = 1;
        Object value = new Object();
        NodePointer child = mock(NodePointer.class);
        when(clone()).thenReturn(child);
        propertyPointer.createChild(context, name, index, value);
        verify(child, times(1)).createPath(eq(context), eq(value));
    }

    @Test
    public void testCreateChildWithPath() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        JXPathContext context = mock(JXPathContext.class);
        QName name = new QName(null, "test");
        int index = 1;
        NodePointer child = mock(NodePointer.class);
        when(clone()).thenReturn(child);
        propertyPointer.createChild(context, name, index);
        verify(child, times(1)).createPath(eq(context));
    }

    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        NodePointer parentPointer = mock(NodePointer.class);
        when(getImmediateParentPointer()).thenReturn(parentPointer);
        when(parentPointer.hashCode()).thenReturn(1);
        when(getPropertyIndex()).thenReturn(2);
        when(index).thenReturn(3);
        assertEquals(6, propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        PropertyPointer other = mock(PropertyPointer.class);
        NodePointer parentPointer1 = mock(NodePointer.class);
        NodePointer parentPointer2 = mock(NodePointer.class);
        when(getImmediateParentPointer()).thenReturn(parentPointer1);
        when(other.getImmediateParentPointer()).thenReturn(parentPointer2);
        when(parentPointer1.equals(parentPointer2)).thenReturn(true);
        when(getPropertyIndex()).thenReturn(1);
        when(other.getPropertyIndex()).thenReturn(1);
        when(getPropertyName()).thenReturn("test");
        when(other.getPropertyName()).thenReturn("test");
        when(index).thenReturn(2);
        when(other.index).thenReturn(2);
        assertTrue(propertyPointer.equals(other));
    }

    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        NodePointer pointer1 = mock(NodePointer.class);
        NodePointer pointer2 = mock(NodePointer.class);
        ValuePointer valuePointer = mock(ValuePointer.class);
        when(getValuePointer()).thenReturn(valuePointer);
        propertyPointer.compareChildNodePointers(pointer1, pointer2);
        verify(valuePointer, times(1)).compareChildNodePointers(eq(pointer1), eq(pointer2));
    }
}