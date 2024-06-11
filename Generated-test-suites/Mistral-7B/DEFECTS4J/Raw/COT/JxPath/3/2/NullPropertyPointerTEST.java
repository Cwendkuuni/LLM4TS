import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.junit.Assert.*;

public class NullPropertyPointerTest {

    @Test
    public void testNullPropertyPointerConstructor() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        assertNotNull(nullPropertyPointer);
    }

    @Test
    public void testGetName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        QName expectedName = new QName("*");
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName.getLocalPart(), actualName.getLocalPart());
        assertEquals(expectedName.getNamespaceURI(), actualName.getNamespaceURI());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyIndex() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        nullPropertyPointer.setPropertyIndex(1);
    }

    @Test
    public void testGetLength() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        int expectedLength = 0;
        int actualLength = nullPropertyPointer.getLength();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetBaseValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        Object expectedBaseValue = null;
        Object actualBaseValue = nullPropertyPointer.getBaseValue();
        assertEquals(expectedBaseValue, actualBaseValue);
    }

    @Test
    public void testGetImmediateNode() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        Object expectedImmediateNode = null;
        Object actualImmediateNode = nullPropertyPointer.getImmediateNode();
        assertEquals(expectedImmediateNode, actualImmediateNode);
    }

    @Test
    public void testIsLeaf() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        boolean expectedIsLeaf = true;
        boolean actualIsLeaf = nullPropertyPointer.isLeaf();
        assertEquals(expectedIsLeaf, actualIsLeaf);
    }

    @Test
    public void testgetValuePointer() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        NullPointer expectedNullPointer = new NullPointer(nullPropertyPointer, new QName("*"));
        NullPointer actualNullPointer = nullPropertyPointer.getValuePointer();
        assertEquals(expectedNullPointer.getNodeType(), actualNullPointer.getNodeType());
        assertEquals(expectedNullPointer.getParentNode(), actualNullPointer.getParentNode());
        assertEquals(expectedNullPointer.getName(), actualNullPointer.getName());
    }

    @Test
    public void testIsActualProperty() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        boolean expectedIsActualProperty = false;
        boolean actualIsActualProperty = nullPropertyPointer.isActualProperty();
        assertEquals(expectedIsActualProperty, actualIsActualProperty);
    }

    @Test
    public void testIsActual() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        boolean expectedIsActual = false;
        boolean actualIsActual = nullPropertyPointer.isActual();
        assertEquals(expectedIsActual, actualIsActual);
    }

    @Test
    public void testIsContainer() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        boolean expectedIsContainer = true;
        boolean actualIsContainer = nullPropertyPointer.isContainer();
        assertEquals(expectedIsContainer, actualIsContainer);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        NodePointer expectedNullPointer = new NullPointer(nullPropertyPointer, new QName("*"));
        NodePointer actualNullPointer = nullPropertyPointer.createPath(context);
        assertEquals(expectedNullPointer.getNodeType(), actualNullPointer.getNodeType());
        assertEquals(expectedNullPointer.getParentNode(), actualNullPointer.getParentNode());
        assertEquals(expectedNullPointer.getName(), actualNullPointer.getName());
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        Object value = new Object();
        NodePointer expectedNullPointer = new NullPointer(nullPropertyPointer, new QName("*"), value);
        NodePointer actualNullPointer = nullPropertyPointer.createPath(context, value);
        assertEquals(expectedNullPointer.getNodeType(), actualNullPointer.getNodeType());
        assertEquals(expectedNullPointer.getParentNode(), actualNullPointer.getParentNode());
        assertEquals(expectedNullPointer.getName(), actualNullPointer.getName());
        assertEquals(expectedNullPointer.getValue(), actualNullPointer.getValue());
    }

    @Test
    public void testCreateChild() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        int index = 0;
        NodePointer expectedNullPointer = nullPropertyPointer.createPath(context);
        NodePointer actualNullPointer = nullPropertyPointer.createChild(context, name, index);
        assertEquals(expectedNullPointer.getNodeType(), actualNullPointer.getNodeType());
        assertEquals(expectedNullPointer.getParentNode(), actualNullPointer.getParentNode());
        assertEquals(expectedNullPointer.getName(), actualNullPointer.getName());
    }

    @Test
    public void testCreateChildWithValue() {
        NodePointer parent = new NodePointer(null);
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
        JXPathContext context = new JXPathContext();
        QName name = new QName("test");
        int index = 0;
        Object value = new Object();
        NodePointer expectedNullPointer = nullPropertyPointer.createPath(context);
        NodePointer actualNullPointer = nullPropertyPointer.createChild(context, name, index, value);
        assertEquals(expectedNullPointer.getNodeType(), actualNullPointer.getNodeType());
        assertEquals(expectedNullPointer.getParentNode(), actualNullPointer.getParentNode());
        assertEquals(expectedNullPointer.getName(), actualNullPointer.getName());
        assertEquals(expectedNullPointer.getValue(), actualNullPointer.getValue());
    }

    @Test
    public void testSetPropertyName() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        String expectedPropertyName = "test";
        nullPropertyPointer.setPropertyName(expectedPropertyName);
        String actualPropertyName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    @Test
    public void testSetNameAttributeValue() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        String expectedAttributeValue = "test";
        nullPropertyPointer.setNameAttributeValue(expectedAttributeValue);
        String actualAttributeValue = nullPropertyPointer.getPropertyName();
        assertEquals(expectedAttributeValue, actualAttributeValue);
        assertEquals(false, nullPropertyPointer.isCollection());
    }

    @Test
    public void testIsCollection() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        int expectedPropertyCount = 0;
        int actualPropertyCount = nullPropertyPointer.getPropertyCount();
        assertEquals(expectedPropertyCount, actualPropertyCount);
    }

    @Test
    public void testGetPropertyNames() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        String[] expectedPropertyNames = new String[0];
        String[] actualPropertyNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expectedPropertyNames, actualPropertyNames);
    }

    @Test
    public void testAsPath() {
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(null));
        String expectedPath = "/nullPropertyPointer";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }
}