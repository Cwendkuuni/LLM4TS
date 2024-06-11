import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class PropertyPointerTest {

    private PropertyPointer propertyPointer;
    private NodePointer parentPointer;
    private JXPathContext context;

    @Before
    public void setUp() {
        parentPointer = Mockito.mock(NodePointer.class);
        context = Mockito.mock(JXPathContext.class);
        propertyPointer = new DummyPropertyPointer(parentPointer);
    }

    @Test
    public void testGetPropertyIndex() {
        // Test when propertyIndex is set
        propertyPointer.setPropertyIndex(1);
        assertEquals(1, propertyPointer.getPropertyIndex());

        // Test when propertyIndex is not set
        propertyPointer.setPropertyIndex(PropertyPointer.UNSPECIFIED_PROPERTY);
        assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        Object bean = new Object();
        Mockito.when(parentPointer.getNode()).thenReturn(bean);
        assertEquals(bean, propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        String propertyName = "testProperty";
        propertyPointer.setPropertyName(propertyName);
        QName expectedName = new QName(null, propertyName);
        QName actualName = propertyPointer.getName();
        assertEquals(expectedName.getLocalPart(), actualName.getLocalPart());
        assertEquals(expectedName.getNamespaceURI(), actualName.getNamespaceURI());
    }

    @Test
    public void testGetPropertyCount() throws Exception {
        int expectedPropertyCount = 3;
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        DummyPropertyPointer propertyPointer = new DummyPropertyPointer(parentPointer);
        assertEquals(expectedPropertyCount, propertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() throws Exception {
        String[] expectedPropertyNames = {"property1", "property2", "property3"};
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        DummyPropertyPointer propertyPointer = new DummyPropertyPointer(parentPointer);
        String[] actualPropertyNames = propertyPointer.getPropertyNames();
        assertArrayEquals(expectedPropertyNames, actualPropertyNames);
    }

    @Test
    public void testIsActualProperty() {
        assertTrue(propertyPointer.isActualProperty());
    }

    @Test
    public void testGetImmediateNode() {
        // Test when property pointer represents an actual property
        Object expectedValue = "testValue";
        Mockito.when(context.getValue(Mockito.any(JXPathContext.class), Mockito.anyInt())).thenReturn(expectedValue);
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        propertyPointer.setPropertyName("testProperty");
        assertEquals(expectedValue, propertyPointer.getImmediateNode());

        // Test when property pointer represents a whole collection
        List<String> expectedCollection = new ArrayList<>();
        expectedCollection.add("testValue1");
        expectedCollection.add("testValue2");
        Mockito.when(context.getValue(Mockito.any(JXPathContext.class), Mockito.eq(0))).thenReturn(expectedCollection);
        propertyPointer.setIndex(0);
        assertEquals(expectedCollection, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        // Test when property pointer represents an actual property
        assertFalse(propertyPointer.isCollection());

        // Test when property pointer represents a whole collection
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        propertyPointer.setIndex(0);
        assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testGetLength() throws Exception {
        int expectedLength = 2;
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        DummyPropertyPointer propertyPointer = new DummyPropertyPointer(parentPointer);
        assertEquals(expectedLength, propertyPointer.getLength());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLengthWhenUninitialized() {
        // Test when property pointer is uninitialized
        propertyPointer.setValue(null);
        propertyPointer.getLength();
    }

    @Test
    public void testGetImmediateValuePointer() {
        // Test when property pointer represents an actual property
        Object expectedValue = "testValue";
        Mockito.when(context.getValue(Mockito.any(JXPathContext.class), Mockito.anyInt())).thenReturn(expectedValue);
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        propertyPointer.setPropertyName("testProperty");
        NodePointer expectedValuePointer = NodePointer.newChildNodePointer(propertyPointer, "testProperty", expectedValue);
        NodePointer actualValuePointer = propertyPointer.getImmediateValuePointer();
        assertSame(expectedValuePointer, actualValuePointer);

        // Test when property pointer represents a whole collection
        List<String> expectedCollection = new ArrayList<>();
        expectedCollection.add("testValue1");
        expectedCollection.add("testValue2");
        Mockito.when(context.getValue(Mockito.any(JXPathContext.class), Mockito.eq(0))).thenReturn(expectedCollection);
        propertyPointer.setIndex(0);
        NodePointer expectedCollectionPointer = NodePointer.newChildNodePointer(propertyPointer, "testProperty", expectedCollection);
        NodePointer actualCollectionPointer = propertyPointer.getImmediateValuePointer();
        assertSame(expectedCollectionPointer, actualCollectionPointer);
    }

    @Test
    public void testCreatePath() throws Exception {
        // Test when property pointer represents an actual property
        Object expectedValue = "testValue";
        Mockito.when(context.getValue(Mockito.any(JXPathContext.class), Mockito.anyInt())).thenReturn(expectedValue);
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        propertyPointer.setPropertyName("testProperty");
        NodePointer expectedPath = NodePointer.newChildNodePointer(propertyPointer, "testProperty", expectedValue);
        NodePointer actualPath = propertyPointer.createPath(context);
        assertSame(expectedPath, actualPath);

        // Test when property pointer represents a whole collection
        List<String> expectedCollection = new ArrayList<>();
        expectedCollection.add("testValue1");
        expectedCollection.add("testValue2");
        Mockito.when(context.getValue(Mockito.any(JXPathContext.class), Mockito.eq(0))).thenReturn(expectedCollection);
        propertyPointer.setIndex(0);
        NodePointer expectedPathCollection = NodePointer.newChildNodePointer(propertyPointer, "testProperty", expectedCollection);
        NodePointer actualPathCollection = propertyPointer.createPath(context);
        assertSame(expectedPathCollection, actualPathCollection);
    }

    @Test
    public void testCreatePathWithValue() throws Exception {
        // Test when property pointer represents an actual property
        Object expectedValue = "testValue";
        Mockito.when(context.getValue(Mockito.any(JXPathContext.class), Mockito.anyInt())).thenReturn(expectedValue);
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        propertyPointer.setPropertyName("testProperty");
        NodePointer expectedPath = NodePointer.newChildNodePointer(propertyPointer, "testProperty", expectedValue);
        NodePointer actualPath = propertyPointer.createPath(context, expectedValue);
        assertSame(expectedPath, actualPath);

        // Test when property pointer represents a whole collection
        List<String> expectedCollection = new ArrayList<>();
        expectedCollection.add("testValue1");
        expectedCollection.add("testValue2");
        Mockito.when(context.getValue(Mockito.any(JXPathContext.class), Mockito.eq(0))).thenReturn(expectedCollection);
        propertyPointer.setIndex(0);
        NodePointer expectedPathCollection = NodePointer.newChildNodePointer(propertyPointer, "testProperty", expectedCollection);
        NodePointer actualPathCollection = propertyPointer.createPath(context, expectedCollection.get(0));
        assertSame(expectedPathCollection, actualPathCollection);
    }

    @Test
    public void testCreateChild() throws Exception {
        // Test when creating a child property pointer with a null name
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        NodePointer expectedChild = NodePointer.newChildNodePointer(propertyPointer, null, null);
        NodePointer actualChild = propertyPointer.createChild(context, null);
        assertSame(expectedChild, actualChild);

        // Test when creating a child property pointer with a valid name
        String childPropertyName = "testChildProperty";
        NodePointer expectedChildProperty = NodePointer.newChildNodePointer(propertyPointer, childPropertyName, null);
        NodePointer actualChildProperty = propertyPointer.createChild(context, new QName(null, childPropertyName));
        assertSame(expectedChildProperty, actualChildProperty);
    }

    @Test
    public void testCreateChildWithValue() throws Exception {
        // Test when creating a child property pointer with a null name and value
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        NodePointer expectedChild = NodePointer.newChildNodePointer(propertyPointer, null, null);
        NodePointer actualChild = propertyPointer.createChild(context, null, null);
        assertSame(expectedChild, actualChild);

        // Test when creating a child property pointer with a valid name and value
        String childPropertyName = "testChildProperty";
        Object childPropertyValue = "testChildValue";
        NodePointer expectedChildProperty = NodePointer.newChildNodePointer(propertyPointer, childPropertyName, childPropertyValue);
        NodePointer actualChildProperty = propertyPointer.createChild(context, new QName(null, childPropertyName), childPropertyValue);
        assertSame(expectedChildProperty, actualChildProperty);
    }

    @Test
    public void testCreateChildWithInvalidIndex() throws Exception {
        // Test when creating a child property pointer with an invalid index for an actual property
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        propertyPointer.setPropertyName("testProperty");
        assertNull(propertyPointer.createChild(context, null, -1));

        // Test when creating a child property pointer with an invalid index for a whole collection
        Mockito.when(parentPointer.getNode()).thenReturn(new DummyBean());
        propertyPointer.setIndex(0);
        assertNull(propertyPointer.createChild(context, null, -1));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer nodePointer1 = new DummyChildNodePointer();
        NodePointer nodePointer2 = new DummyChildNodePointer();
        NodePointer nodePointer3 = new DummyChildNodePointer();
        NodePointer nodePointer4 = new DummyChildNodePointer();

        int comparisonResult12 = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer2);
        int comparisonResult13 = propertyPointer.compareChildNodePointers(nodePointer1, nodePointer3);
        int comparisonResult23 = propertyPointer.compareChildNodePointers(nodePointer2, nodePointer3);
        int comparisonResult34 = propertyPointer.compareChildNodePointers(nodePointer3, nodePointer4);

        assertEquals(0, comparisonResult12);
        assertEquals(-1, comparisonResult13);
        assertEquals(1, comparisonResult13);
        assertEquals(0, comparisonResult23);
        assertEquals(-1, comparisonResult34);
    }
}

class DummyBean {
    public String property1;
    public String property2;
    public List<String> property3;
}

class DummyPropertyPointer extends PropertyPointer {
    public DummyPropertyPointer(NodePointer parent) {
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
}

class DummyChildNodePointer extends NodePointer {
    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }
}