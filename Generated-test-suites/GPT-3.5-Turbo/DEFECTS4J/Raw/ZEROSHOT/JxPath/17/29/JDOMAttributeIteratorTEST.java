import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator jdomAttributeIterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        jdomAttributeIterator = new JDOMAttributeIterator(parent, new QName("test"));
    }

    @Test
    public void testGetNodePointer() {
        jdomAttributeIterator.setPosition(1);
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
    }

    @Test
    public void testGetPosition() {
        Assert.assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Assert.assertFalse(jdomAttributeIterator.setPosition(0));
        Assert.assertTrue(jdomAttributeIterator.setPosition(1));
        Assert.assertEquals(1, jdomAttributeIterator.getPosition());
        Assert.assertFalse(jdomAttributeIterator.setPosition(2));
    }
}