import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        parent = new NodePointer() {
            @Override
            public Object getNode() {
                return null;
            }

            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public QName getName(int index) {
                return null;
            }

            @Override
            public NodePointer getValuePointer() {
                return null;
            }

            @Override
            public Object getBaseValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return null;
            }

            @Override
            public NodePointer getValuePointer(int index) {
                return null;
            }

            @Override
            public boolean isActual() {
                return false;
            }

            @Override
            public boolean isCollectionElement() {
                return false;
            }

        };
        name = new QName("test", "attr");
        attributeIterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();
        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = attributeIterator.setPosition(1);
        Assert.assertFalse(result);
    }
}