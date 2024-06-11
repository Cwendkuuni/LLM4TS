import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyPointerTest {
    @Test
    public void testGetPropertyIndex() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getPropertyIndex()).thenReturn(1);
        int index = propertyPointer.getPropertyIndex();
        assertEquals(1, index);
    }
    
    @Test
    public void testSetPropertyIndex() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        propertyPointer.setPropertyIndex(2);
        verify(propertyPointer).setPropertyIndex(2);
    }
    
    @Test
    public void testGetBean() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getBean()).thenReturn("bean");
        Object bean = propertyPointer.getBean();
        assertEquals("bean", bean);
    }
    
    @Test
    public void testGetName() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getName()).thenReturn(new QName(null, "propertyName"));
        QName name = propertyPointer.getName();
        assertEquals(new QName(null, "propertyName"), name);
    }
    
    @Test
    public void testGetPropertyName() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getPropertyName()).thenReturn("propertyName");
        String propertyName = propertyPointer.getPropertyName();
        assertEquals("propertyName", propertyName);
    }
    
    @Test
    public void testSetPropertyName() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        propertyPointer.setPropertyName("newPropertyName");
        verify(propertyPointer).setPropertyName("newPropertyName");
    }
    
    @Test
    public void testGetPropertyCount() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getPropertyCount()).thenReturn(3);
        int count = propertyPointer.getPropertyCount();
        assertEquals(3, count);
    }
    
    @Test
    public void testGetPropertyNames() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getPropertyNames()).thenReturn(new String[]{"name1", "name2"});
        String[] names = propertyPointer.getPropertyNames();
        assertArrayEquals(new String[]{"name1", "name2"}, names);
    }
    
    @Test
    public void testIsActualProperty() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.isActualProperty()).thenReturn(true);
        boolean isActual = propertyPointer.isActualProperty();
        assertTrue(isActual);
    }
    
    @Test
    public void testIsActual() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.isActualProperty()).thenReturn(true);
        when(propertyPointer.isActual()).thenReturn(true);
        boolean isActual = propertyPointer.isActual();
        assertTrue(isActual);
    }
    
    @Test
    public void testGetImmediateNode() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        when(propertyPointer.getBaseValue()).thenReturn("value");
        Object node = propertyPointer.getImmediateNode();
        assertEquals("value", node);
    }
    
    @Test
    public void testIsCollection() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getBaseValue()).thenReturn(new int[]{1, 2, 3});
        boolean isCollection = propertyPointer.isCollection();
        assertTrue(isCollection);
    }
    
    @Test
    public void testIsLeaf() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getNode()).thenReturn("leaf");
        boolean isLeaf = propertyPointer.isLeaf();
        assertTrue(isLeaf);
    }
    
    @Test
    public void testGetLength() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class);
        when(propertyPointer.getBaseValue()).thenReturn(new int[]{1, 2, 3});
        int length = propertyPointer.getLength();
        assertEquals(3, length);
    }
    
    @Test
    public void testGetImmediateValuePointer() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        NodePointer valuePointer = mock(NodePointer.class);
        when(propertyPointer.clone()).thenReturn(propertyPointer);
        when(propertyPointer.getName()).thenReturn(new QName(null, "propertyName"));
        when(propertyPointer.getImmediateNode()).thenReturn("value");
        when(NodePointer.newChildNodePointer(propertyPointer, new QName(null, "propertyName"), "value")).thenReturn(valuePointer);
        NodePointer pointer = propertyPointer.getImmediateValuePointer();
        assertEquals(valuePointer, pointer);
    }
    
    @Test
    public void testCreatePath() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        JXPathContext context = mock(JXPathContext.class);
        AbstractFactory factory = mock(AbstractFactory.class);
        when(propertyPointer.getAbstractFactory(context)).thenReturn(factory);
        when(propertyPointer.getBaseValue()).thenReturn(null);
        when(factory.createObject(context, propertyPointer, propertyPointer.getBean(), propertyPointer.getPropertyName(), 0)).thenReturn(true);
        propertyPointer.createPath(context);
        verify(factory).createObject(context, propertyPointer, propertyPointer.getBean(), propertyPointer.getPropertyName(), 0);
    }
    
    @Test
    public void testCreatePathWithValue() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        JXPathContext context = mock(JXPathContext.class);
        when(propertyPointer.getBaseValue()).thenReturn(new int[]{1, 2, 3});
        propertyPointer.setIndex(2);
        propertyPointer.createPath(context, 4);
        verify(propertyPointer).clone();
    }
    
    @Test
    public void testCreateChildWithValue() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        JXPathContext context = mock(JXPathContext.class);
        QName name = new QName(null, "propertyName");
        propertyPointer.setPropertyName("propertyName");
        propertyPointer.setIndex(1);
        Object value = mock(Object.class);
        NodePointer childNodePointer = mock(NodePointer.class);
        when(propertyPointer.clone()).thenReturn(propertyPointer);
        when(NodePointer.newChildNodePointer(propertyPointer, name, value)).thenReturn(childNodePointer);
        NodePointer pointer = propertyPointer.createChild(context, name, 1, value);
        assertEquals(childNodePointer, pointer);
    }
    
    @Test
    public void testCreateChild() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        JXPathContext context = mock(JXPathContext.class);
        QName name = new QName(null, "propertyName");
        propertyPointer.setPropertyName("propertyName");
        propertyPointer.setIndex(1);
        NodePointer childNodePointer = mock(NodePointer.class);
        when(propertyPointer.clone()).thenReturn(propertyPointer);
        when(NodePointer.newChildNodePointer(propertyPointer, name, null)).thenReturn(childNodePointer);
        NodePointer pointer = propertyPointer.createChild(context, name, 1);
        assertEquals(childNodePointer, pointer);
    }
    
    @Test
    public void testHashCode() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        NodePointer parentPointer = mock(NodePointer.class);
        when(parentPointer.hashCode()).thenReturn(1234);
        propertyPointer.setParent(parentPointer);
        propertyPointer.setPropertyIndex(2);
        propertyPointer.setIndex(1);
        int hashCode = propertyPointer.hashCode();
        assertEquals(1237, hashCode);
    }
    
    @Test
    public void testEquals() {
        PropertyPointer propertyPointer1 = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        PropertyPointer propertyPointer2 = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        NodePointer parentPointer1 = mock(NodePointer.class);
        NodePointer parentPointer2 = mock(NodePointer.class);
        when(parentPointer1.equals(parentPointer2)).thenReturn(true);
        when(propertyPointer1.getParent()).thenReturn(parentPointer1);
        when(propertyPointer2.getParent()).thenReturn(parentPointer2);
        when(propertyPointer1.getPropertyIndex()).thenReturn(2);
        when(propertyPointer2.getPropertyIndex()).thenReturn(2);
        when(propertyPointer1.getPropertyName()).thenReturn("name");
        when(propertyPointer2.getPropertyName()).thenReturn("name");
        when(propertyPointer1.getIndex()).thenReturn(1);
        when(propertyPointer2.getIndex()).thenReturn(1);
        boolean equals = propertyPointer1.equals(propertyPointer2);
        assertTrue(equals);
    }
    
    @Test
    public void testCompareChildNodePointers() {
        PropertyPointer propertyPointer = mock(PropertyPointer.class, CALLS_REAL_METHODS);
        NodePointer pointer1 = mock(NodePointer.class);
        NodePointer pointer2 = mock(NodePointer.class);
        when(propertyPointer.getValuePointer()).thenReturn(mock(NodePointer.class));
        int compareResult = propertyPointer.compareChildNodePointers(pointer1, pointer2);
        assertEquals(0, compareResult);
    }
}