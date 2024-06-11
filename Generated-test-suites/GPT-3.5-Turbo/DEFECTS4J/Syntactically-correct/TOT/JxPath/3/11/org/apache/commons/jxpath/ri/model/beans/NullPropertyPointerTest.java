import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class NullPropertyPointerTest {
   
    private NullPropertyPointer nullPropertyPointer;
    private NodePointer parent;
    private JXPathContext context;
    
    @Before
    public void setUp() {
        parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
        context = JXPathContext.newContext(parent);
    }

    @Test
    public void testGetName() {
        QName expected = new QName("*");
        QName actual = nullPropertyPointer.getName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyIndex() {
        int index = 1;
        nullPropertyPointer.setPropertyIndex(index);
        int actual = nullPropertyPointer.getIndex();
        Assert.assertEquals(index, actual);
    }

    @Test
    public void testGetLength() {
        int expected = 0;
        int actual = nullPropertyPointer.getLength();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetBaseValue() {
        Object expected = null;
        Object actual = nullPropertyPointer.getBaseValue();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetImmediateNode() {
        Object expected = null;
        Object actual = nullPropertyPointer.getImmediateNode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsLeaf() {
        boolean expected = true;
        boolean actual = nullPropertyPointer.isLeaf();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetValuePointer() {
        NodePointer expected = new NullPointer(nullPropertyPointer, new QName(nullPropertyPointer.getPropertyName()));
        NodePointer actual = nullPropertyPointer.getValuePointer();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActualProperty() {
        boolean expected = false;
        boolean actual = nullPropertyPointer.isActualProperty();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsActual() {
        boolean expected = false;
        boolean actual = nullPropertyPointer.isActual();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsContainer() {
        boolean expected = true;
        boolean actual = nullPropertyPointer.isContainer();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NullParent() {
        nullPropertyPointer.setValue(new Object());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValue_NotChangeableLocation() {
        NodePointer propertyOwnerPointer = new PropertyOwnerPointer(parent);
        nullPropertyPointer.setParent(propertyOwnerPointer);
        nullPropertyPointer.setValue(new Object());
    }

    @Test
    public void testCreatePath() {
        NodePointer expected = parent.createPath(context);
        NodePointer actual = nullPropertyPointer.createPath(context);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreatePathWithValue() {
        Object value = new Object();
        NodePointer expected = parent.createPath(context);
        expected.setValue(value);
        NodePointer actual = nullPropertyPointer.createPath(context, value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateChild() {
        QName name = new QName("test");
        int index = 0;
        NodePointer expected = nullPropertyPointer.createPath(context).createChild(context, name, index);
        NodePointer actual = nullPropertyPointer.createChild(context, name, index);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateChildWithValue() {
        QName name = new QName("test");
        int index = 0;
        Object value = new Object();
        NodePointer expected = nullPropertyPointer.createPath(context).createChild(context, name, index, value);
        NodePointer actual = nullPropertyPointer.createChild(context, name, index, value);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        String expected = "*";
        String actual = nullPropertyPointer.getPropertyName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPropertyName() {
        String propertyName = "test";
        nullPropertyPointer.setPropertyName(propertyName);
        String actual = nullPropertyPointer.getPropertyName();
        Assert.assertEquals(propertyName, actual);
    }

    @Test
    public void testSetNameAttributeValue() {
        String attributeValue = "test";
        nullPropertyPointer.setNameAttributeValue(attributeValue);
        String actual = nullPropertyPointer.getPropertyName();
        Assert.assertEquals(attributeValue, actual);
    }

    @Test
    public void testIsCollection() {
        boolean expected = false;
        boolean actual = nullPropertyPointer.isCollection();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyCount() {
        int expected = 0;
        int actual = nullPropertyPointer.getPropertyCount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyNames() {
        String[] expected = new String[0];
        String[] actual = nullPropertyPointer.getPropertyNames();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPath() {
        String expected = nullPropertyPointer.asPath();
        String actual = nullPropertyPointer.asPath();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEscape() {
        String string = "test";
        String expected = string;
        String actual = nullPropertyPointer.escape(string);
        Assert.assertEquals(expected, actual);
    }
}