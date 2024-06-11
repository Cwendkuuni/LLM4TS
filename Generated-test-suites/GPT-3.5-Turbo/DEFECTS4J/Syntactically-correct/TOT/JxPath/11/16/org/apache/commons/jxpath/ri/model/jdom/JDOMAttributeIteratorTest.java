import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName("test", "attr");
        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetPosition() {
        Assert.assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
        iterator.setPosition(5);
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertFalse(iterator.setPosition(-1));
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertTrue(iterator.setPosition(3));
        Assert.assertFalse(iterator.setPosition(4));
    }

    @Test
    public void testGetNodePointer() {
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Assert.assertEquals(parent, ((JDOMAttributePointer)nodePointer).getParent());
        Assert.assertTrue(nodePointer.getNode() instanceof Attribute);
        Assert.assertEquals(name, ((Attribute)nodePointer.getNode()).getQName());
    }

    @Test(expected = NullPointerException.class)
    public void testGetNodePointer_NullParent() {
        iterator = new JDOMAttributeIterator(null, name);
        iterator.getPosition();
    }

    @Test
    public void testGetNodePointer_EmptyAttributes() {
        parent.setNode(new Element());
        iterator = new JDOMAttributeIterator(parent, name);
        iterator.setPosition(1);
        Assert.assertNull(iterator.getNodePointer());
    }
}