import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        QName expectedName = new QName("*");
        QName actualName = nullPropertyPointer.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetPropertyIndex() {
        int index = 5;
        nullPropertyPointer.setPropertyIndex(index);
        // No assertion as this method does not have a return value or side effects.
    }

    @Test
    public void testGetLength() {
        int expectedLength = 0;
        int actualLength = nullPropertyPointer.getLength();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetBaseValue() {
        Object expectedValue = null;
        Object actualValue = nullPropertyPointer.getBaseValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetImmediateNode() {
        Object expectedNode = null;
        Object actualNode = nullPropertyPointer.getImmediateNode();
        assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testIsLeaf() {
        boolean expectedValue = true;
        boolean actualValue = nullPropertyPointer.isLeaf();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetValuePointer() {
        NodePointer expectedPointer = new NodePointer();
        NodePointer actualPointer = nullPropertyPointer.getValuePointer();
        assertEquals(expectedPointer, actualPointer);
        assertEquals(new QName(nullPropertyPointer.getPropertyName()), actualPointer.getPropertyName());
    }

    @Test
    public void testIsActualProperty() {
        boolean expectedValue = false;
        boolean actualValue = nullPropertyPointer.isActualProperty();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsActual() {
        boolean expectedValue = false;
        boolean actualValue = nullPropertyPointer.isActual();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsContainer() {
        boolean expectedValue = true;
        boolean actualValue = nullPropertyPointer.isContainer();
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNullParent() {
        Object value = "test";
        nullPropertyPointer.setValue(value);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithInvalidParent() {
        Object value = "test";
        NodePointer parent = new NodePointer() {
            public boolean isContainer() { return false; }
        };
        nullPropertyPointer = new NullPropertyPointer(parent);
        nullPropertyPointer.setValue(value);
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer expectedPointer = null;
        NodePointer actualPointer = nullPropertyPointer.createPath(context);
        assertEquals(expectedPointer, actualPointer);
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        Object value = "test";
        NodePointer expectedPointer = null;
        NodePointer actualPointer = nullPropertyPointer.createPath(context, value);
        assertEquals(expectedPointer, actualPointer);
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 0;
        NodePointer expectedPointer = null;
        NodePointer actualPointer = nullPropertyPointer.createChild(context, name, index);
        assertEquals(expectedPointer, actualPointer);
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        QName name = new QName("child");
        int index = 0;
        Object value = "test";
        NodePointer expectedPointer = null;
        NodePointer actualPointer = nullPropertyPointer.createChild(context, name, index, value);
        assertEquals(expectedPointer, actualPointer);
    }

    @Test
    public void testGetPropertyName() {
        String expectedName = "*";
        String actualName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "test";
        nullPropertyPointer.setPropertyName(propertyName);
        String expectedName = "test";
        String actualName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testSetNameAttributeValue() {
        String attributeValue = "test";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        String expectedName = "test";
        String actualName = nullPropertyPointer.getPropertyName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testIsCollection() {
        boolean expectedValue = false;
        boolean actualValue = nullPropertyPointer.isCollection();
        assertEquals(expectedValue, actualValue);
        nullPropertyPointer.setPropertyIndex(5);
        expectedValue = true;
        actualValue = nullPropertyPointer.isCollection();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetPropertyCount() {
        int expectedCount = 0;
        int actualCount = nullPropertyPointer.getPropertyCount();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetPropertyNames() {
        String[] expectedNames = new String[0];
        String[] actualNames = nullPropertyPointer.getPropertyNames();
        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void testAsPath() {
        String expectedPath = "nullPointer[@name='*']";
        String actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
        nullPropertyPointer.setNameAttributeValue("test");
        nullPropertyPointer.setPropertyIndex(0);
        expectedPath = "nullPointer[@name='test'][1]";
        actualPath = nullPropertyPointer.asPath();
        assertEquals(expectedPath, actualPath);
    }
}