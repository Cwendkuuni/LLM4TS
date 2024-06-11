import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNull(nullPropertyPointer.getValuePointer());
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setPropertyName("testProperty");
        QName expectedName = new QName("testProperty");
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName.getLocalPart(), actualName.getLocalPart());
        assertEquals(expectedName.getNamespaceURI(), actualName.getNamespaceURI());
    }

    @Test
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setPropertyIndex(1);
        int expectedIndex = 1;
        int actualIndex = nullPropertyPointer.getPropertyIndex();
        assertEquals(expectedIndex, actualIndex);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        int actualLength = nullPropertyPointer.getLength();
        fail("Method getLength() should throw UnsupportedOperationException");
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        Object expectedBaseValue = null;
        Object actualBaseValue = nullPropertyPointer.getBaseValue();
        assertEquals(expectedBaseValue, actualBaseValue);
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        Object expectedImmediateNode = null;
        Object actualImmediateNode = nullPropertyPointer.getImmediateNode();
        assertEquals(expectedImmediateNode, actualImmediateNode);
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        boolean expectedLeaf = true;
        boolean actualLeaf = nullPropertyPointer.isLeaf();
        assertEquals(expectedLeaf, actualLeaf);
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        NodePointer expectedPath = new NodePointer(new Object());
        NodePointer actualPath = nullPropertyPointer.createPath(context);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testSetValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        Object value = new Object();
        try {
            nullPropertyPointer.setValue(value);
            fail("Expected JXPathInvalidAccessException");
        } catch (JXPathInvalidAccessException e) {
            // Expected
        }
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        Object value = new Object();
        NodePointer expectedPath = new NodePointer(new Object());
        NodePointer actualPath = nullPropertyPointer.createPath(context, value);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        QName name = new QName("testChild");
        int index = 0;
        NodePointer expectedChild = new NodePointer(new Object());
        NodePointer actualChild = nullPropertyPointer.createChild(context, name, index);
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        QName name = new QName("testChild");
        int index = 0;
        Object value = new Object();
        NodePointer expectedChild = new NodePointer(value);
        NodePointer actualChild = nullPropertyPointer.createChild(context, name, index, value);
        assertEquals(expectedChild, actualChild);
    }

    @Test
    public void testGetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setPropertyName("testProperty");
        String expectedPropertyName = "testProperty";
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        nullPropertyPointer.setPropertyName("testProperty");
        String expectedPropertyName = "testProperty";
        nullPropertyPointer.setPropertyName("newTestProperty");
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        boolean expectedCollection = false;
        boolean actualCollection = nullPropertyPointer.isCollection();
        assertEquals(expectedCollection, actualCollection);
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        int expectedPropertyCount = 0;
        int actualPropertyCount = nullPropertyPointer.getPropertyCount();
        assertEquals(expectedPropertyCount, actualPropertyCount);
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new Object()));
        String[] expectedPropertyNames = new String[0];
        String[] actualPropertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expectedPropertyNames, actualPropertyNames);
    }

    @Test
    public void testAsPath() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        JXPathContext context = new JXPathContext(parent.getNode());
        String expectedPath = "";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testAsPathWithByNameAttribute() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        JXPathContext context = new JXPathContext(parent.getNode());
        String expectedPath = "@testAttribute";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testAsPathWithIndex() {
        NodePointer parent = new NodePointer(new Object());
        NodePointer child = new NodePointer(new Object(), new QName("testChild"));
        NodePointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext(parent.getNode());
        nullPropertyPointer.setPropertyName("testChild");
        nullPropertyPointer.setIndex(1);
        String expectedPath = "testChild[1]";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameAttributeValueWithNullPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue(null);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNameAttributeValueWithEmptyPropertyName() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyNameWithNonEmptyPropertyNameAndByNameAttributeFalse() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setPropertyName("testProperty");
        String newPropertyName = "newTestProperty";
        nullPropertyPointer.setPropertyName(newPropertyName);
        fail("Expected UnsupportedOperationException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetPropertyNameWithNonEmptyPropertyNameAndByNameAttributeTrue() {
        NodePointer parent = new NodePointer(new Object());
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setNameAttributeValue("testAttribute");
        nullPropertyPointer.setPropertyName("newTestProperty");
        fail("Expected UnsupportedOperationException");
    }
}