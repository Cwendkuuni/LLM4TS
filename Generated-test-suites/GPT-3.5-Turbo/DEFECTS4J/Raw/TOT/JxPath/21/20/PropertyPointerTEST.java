import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Assert;
import org.junit.Test;

public class PropertyPointerTest {

    @Test
    public void testGetPropertyIndex() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        Assert.assertEquals(PropertyPointer.UNSPECIFIED_PROPERTY, propertyPointer.getPropertyIndex());
        propertyPointer.setPropertyIndex(1);
        Assert.assertEquals(1, propertyPointer.getPropertyIndex());
    }

    @Test
    public void testGetBean() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        Assert.assertNull(propertyPointer.getBean());
        propertyPointer.setBean("testBean");
        Assert.assertEquals("testBean", propertyPointer.getBean());
    }

    @Test
    public void testGetName() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        Assert.assertEquals(new QName(null, ""), propertyPointer.getName());
        propertyPointer.setPropertyName("propertyName");
        Assert.assertEquals(new QName(null, "propertyName"), propertyPointer.getName());
    }

    @Test
    public void testGetImmediateNode() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        Assert.assertEquals(Index.WHOLE_COLLECTION, propertyPointer.getIndex());
        Assert.assertNull(propertyPointer.getImmediateNode());

        propertyPointer.setIndex(1);
        Assert.assertEquals(1, propertyPointer.getIndex());
        Assert.assertEquals(Index.WHOLE_COLLECTION, propertyPointer.getImmediateNode());
    }

    @Test
    public void testIsCollection() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        Assert.assertFalse(propertyPointer.isCollection());
        propertyPointer.setBean("testBean");
        Assert.assertTrue(propertyPointer.isCollection());
    }

    @Test
    public void testIsLeaf() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        Assert.assertTrue(propertyPointer.isLeaf());
        propertyPointer.setBean(new Object());
        Assert.assertTrue(propertyPointer.isLeaf());
    }

    @Test
    public void testGetLength() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        Assert.assertEquals(-1, propertyPointer.getLength());
        propertyPointer.setBean(new Object[]{"a", "b", "c"});
        Assert.assertEquals(3, propertyPointer.getLength());
    }

    @Test
    public void testGetImmediateValuePointer() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        NodePointer expectedNodePointer = new NodePointer();
        propertyPointer.setIndex(Index.WHOLE_COLLECTION);

        Assert.assertEquals(expectedNodePointer, propertyPointer.getImmediateValuePointer());
    }

    @Test
    public void testCreatePath() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        JXPathContext context = new JXPathContext();
        propertyPointer.createPath(context);

        Assert.assertNotNull(propertyPointer.getNode());
    }

    @Test
    public void testCreatePathWithValue() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        JXPathContext context = new JXPathContext();
        propertyPointer.createPath(context, "testValue");

        Assert.assertNotNull(propertyPointer.getNode());
        Assert.assertEquals("testValue", propertyPointer.getNode());
    }

    @Test
    public void testCreateChildWithNull() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        JXPathContext context = new JXPathContext();
        QName name = new QName(null, "child");
        NodePointer expectedNodePointer = propertyPointer.createChild(context, name, 0);

        Assert.assertEquals(expectedNodePointer.getNode(), propertyPointer.getNode());
    }

    @Test
    public void testHashCode() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        Assert.assertEquals(parent.hashCode() + Index.WHOLE_COLLECTION + propertyPointer.getPropertyIndex(),
                propertyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);

        Assert.assertTrue(propertyPointer.equals(propertyPointer));

        Assert.assertFalse(propertyPointer.equals(null));

        Assert.assertFalse(propertyPointer.equals(new Object()));

        PropertyPointer differentParent = new PropertyPointer(new NodePointer());
        Assert.assertFalse(propertyPointer.equals(differentParent));

        PropertyPointer differentPropertyIndex = new PropertyPointer(parent);
        differentPropertyIndex.setPropertyIndex(1);
        Assert.assertFalse(propertyPointer.equals(differentPropertyIndex));

        PropertyPointer differentPropertyName = new PropertyPointer(parent);
        differentPropertyName.setPropertyName("differentProperty");
        Assert.assertFalse(propertyPointer.equals(differentPropertyName));

        PropertyPointer differentIndex = new PropertyPointer(parent);
        differentIndex.setIndex(1);
        Assert.assertFalse(propertyPointer.equals(differentIndex));

        PropertyPointer sameProperties = new PropertyPointer(parent);
        sameProperties.setPropertyIndex(propertyPointer.getPropertyIndex());
        sameProperties.setPropertyName(propertyPointer.getPropertyName());
        Assert.assertTrue(propertyPointer.equals(sameProperties));
    }

    @Test
    public void testCompareChildNodePointers() {
        NodePointer parent = new NodePointer();
        PropertyPointer propertyPointer = new PropertyPointer(parent);
        NodePointer pointer1 = new NodePointer();
        NodePointer pointer2 = new NodePointer();
        
        // Assuming getValuePointer() is a method consistently used in PropertyPointer class
        Assert.assertEquals(Pointer.getValuePointer().compareChildNodePointers(pointer1, pointer2),
                propertyPointer.compareChildNodePointers(pointer1, pointer2));
    }
}