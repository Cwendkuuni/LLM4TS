import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NullPropertyPointerTest {

    private NullPropertyPointer nullPropertyPointer;
    private NodePointer parent;

    @Before
    public void setup() {
        parent = new NodePointer();
        nullPropertyPointer = new NullPropertyPointer(parent);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("*", nullPropertyPointer.getName().getLocalPart());
    }

    @Test
    public void testSetPropertyIndex() {
        nullPropertyPointer.setPropertyIndex(0);
        // No assertion as the method doesn't return any result or change any state
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
    public void testGetValuePointer() {
        NodePointer valuePointer = nullPropertyPointer.getValuePointer();
        Assert.assertNotNull(valuePointer);
        Assert.assertEquals(NullPointer.class, valuePointer.getClass());
        Assert.assertEquals("*", valuePointer.getName().getLocalPart());
        Assert.assertEquals(nullPropertyPointer, valuePointer.getParent());
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
    public void testSetValueWithNullParent() {
        nullPropertyPointer.setParent(null);
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithContainerParent() {
        nullPropertyPointer.setValue("value");
    }

    @Test(expected = JXPathInvalidAccessException.class)
    public void testSetValueWithNonChangeableLocation() {
        NodePointer nonChangeablePointer = new NodePointer();
        nonChangeablePointer.setPropertyName("nonChangeable");
        nullPropertyPointer.setParent(nonChangeablePointer);
        nullPropertyPointer.setValue("value");
    }

    @Test
    public void testCreatePath() {
        JXPathContext context = new JXPathContext();
        NodePointer newPath = nullPropertyPointer.createPath(context);
        Assert.assertNotNull(newPath);
        Assert.assertEquals(parent, newPath.getParent());
        Assert.assertEquals("*", newPath.getName().getLocalPart());
    }

    @Test
    public void testCreatePathWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer newPathWithValue = nullPropertyPointer.createPath(context, "value");
        Assert.assertNotNull(newPathWithValue);
        Assert.assertEquals(parent, newPathWithValue.getParent());
        Assert.assertEquals("*", newPathWithValue.getName().getLocalPart());
        Assert.assertEquals("value", newPathWithValue.getValue());
    }

    @Test
    public void testCreateChild() {
        JXPathContext context = new JXPathContext();
        NodePointer newChild = nullPropertyPointer.createChild(context, new QName("child"), 0);
        Assert.assertNotNull(newChild);
        Assert.assertEquals(parent, newChild.getParent());
        Assert.assertEquals("child", newChild.getName().getLocalPart());
        Assert.assertEquals(0, newChild.getIndex());
    }

    @Test
    public void testCreateChildWithValue() {
        JXPathContext context = new JXPathContext();
        NodePointer newChildWithValue = nullPropertyPointer.createChild(context, new QName("child"), 0, "value");
        Assert.assertNotNull(newChildWithValue);
        Assert.assertEquals(parent, newChildWithValue.getParent());
        Assert.assertEquals("child", newChildWithValue.getName().getLocalPart());
        Assert.assertEquals(0, newChildWithValue.getIndex());
        Assert.assertEquals("value", newChildWithValue.getValue());
    }

    @Test
    public void testGetPropertyName() {
        nullPropertyPointer.setPropertyName("property");
        Assert.assertEquals("property", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetPropertyName() {
        nullPropertyPointer.setPropertyName("property");
        Assert.assertEquals("property", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testSetNameAttributeValue() {
        nullPropertyPointer.setNameAttributeValue("attributeValue");
        Assert.assertEquals("attributeValue", nullPropertyPointer.getPropertyName());
    }

    @Test
    public void testIsCollection() {
        nullPropertyPointer.setPropertyIndex(0);
        Assert.assertTrue(nullPropertyPointer.isCollection());
    }

    @Test
    public void testGetPropertyCount() {
        Assert.assertEquals(0, nullPropertyPointer.getPropertyCount());
    }

    @Test
    public void testGetPropertyNames() {
        String[] propertyNames = nullPropertyPointer.getPropertyNames();
        Assert.assertNotNull(propertyNames);
        Assert.assertEquals(0, propertyNames.length);
    }

    @Test
    public void testAsPath() {
        nullPropertyPointer.setPropertyName("property");
        nullPropertyPointer.setPropertyIndex(0);
        String path = nullPropertyPointer.asPath();
        Assert.assertEquals("parent[property='property'][1]", path);
    }
}

class NodePointer extends NullPropertyPointer {
    // This class is created to mock the parent NodePointer since it's an abstract class
    public NodePointer() {
        super(null);
    }
}