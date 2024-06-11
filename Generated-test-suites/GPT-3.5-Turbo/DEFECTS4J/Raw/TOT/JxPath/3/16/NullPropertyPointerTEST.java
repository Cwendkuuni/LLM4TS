import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {

    private NodePointer parentPointer;
    private NullPropertyPointer nullPropertyPointer;

    @Before
    public void setup() {
        parentPointer = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parentPointer);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("*", nullPropertyPointer.getName().toString());
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("test");
        Assert.assertEquals("test", nullPropertyPointer.getPropertyName());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNullParent() {
        nullPropertyPointer.setValue("test");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueNonContainerParent() {
        NodePointer nonContainerParentPointer = new NodePointer();
        nonContainerParentPointer.setContainer(false);
        nullPropertyPointer = new NullPropertyPointer(nonContainerParentPointer);
        nullPropertyPointer.setValue("test");
    }

    @Test
    public void testIsActualProperty() {
        Assert.assertFalse(nullPropertyPointer.isActualProperty());
    }

    @Test
    public void testIsActual() {
        Assert.assertFalse(nullPropertyPointer.isActual());
    }

    @Test
    public void testIsContainer() {
        Assert.assertTrue(nullPropertyPointer.isContainer());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(5);
    }

    @Test
    public void testGetLength() {
        Assert.assertEquals(0, nullPropertyPointer.getLength());
    }

    @Test
    public void testGetBaseValue() {
        Assert.assertNull(nullPropertyPointer.getBaseValue());
    }

    @Test
    public void testGetImmediateNode() {
        Assert.assertNull(nullPropertyPointer.getImmediateNode());
    }

    @Test
    public void testIsLeaf() {
        Assert.assertTrue(nullPropertyPointer.isLeaf());
    }

    @Test
    public void testIsCollection() {
        Assert.assertFalse(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        Assert.assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        Assert.assertArrayEquals(new String[0], nullPropertyPointer.getPropertyNames());
    }

    @Test
    public void testAsPath() {
        nullPropertyPointer.setNameAttributeValue("test");
        nullPropertyPointer.setPropertyIndex(2);
        Assert.assertEquals("parent[@name='test'][3]", nullPropertyPointer.asPath());
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testCreatePathNullParent() {
        nullPropertyPointer.createPath(new JXPathContext(), "testValue");
    }

    @Test
    public void testCreatePathAttributeName() {
        parentPointer.createPath(new JXPathContext());
        nullPropertyPointer.setNameAttributeValue("test");
        NodePointer pathNodePointer = nullPropertyPointer.createPath(new JXPathContext());
        Assert.assertEquals("test", pathNodePointer.getName().toString());
    }

    @Test
    public void testCreatePathIndex() {
        parentPointer.createPath(new JXPathContext());
        nullPropertyPointer.setPropertyIndex(2);
        NodePointer pathNodePointer = nullPropertyPointer.createPath(new JXPathContext());
        Assert.assertEquals("[3]", pathNodePointer.asPath());
    }

    @Test
    public void testCreatePathIndexValue() {
        parentPointer.createPath(new JXPathContext());
        nullPropertyPointer.setPropertyIndex(2);
        NodePointer pathNodePointer = nullPropertyPointer.createPath(new JXPathContext(), "testValue");
        Assert.assertEquals("[3]", pathNodePointer.asPath());
        Assert.assertEquals("testValue", pathNodePointer.getValue());
    }

    @Test
    public void testCreateChild() {
        parentPointer.createPath(new JXPathContext());
        nullPropertyPointer.setNameAttributeValue("test");
        NodePointer childNodePointer = nullPropertyPointer.createChild(new JXPathContext(), new QName("child"), 1);
        Assert.assertEquals("test/child[2]", childNodePointer.asPath());
    }

    @Test
    public void testCreateChildValue() {
        parentPointer.createPath(new JXPathContext());
        nullPropertyPointer.setNameAttributeValue("test");
        NodePointer childNodePointer = nullPropertyPointer.createChild(new JXPathContext(), new QName("child"), 1, "testValue");
        Assert.assertEquals("test/child[2]", childNodePointer.asPath());
        Assert.assertEquals("testValue", childNodePointer.getValue());
    }
}