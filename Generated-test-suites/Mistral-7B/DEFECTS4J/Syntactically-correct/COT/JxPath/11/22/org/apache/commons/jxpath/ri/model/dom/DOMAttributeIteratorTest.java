import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(null, null, null, null);
        QName name = new QName("test", "test-prefix", "test-ns");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new NodePointer(null, null, null, null);
        QName name = new QName("test", null, null.getString());
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        NodePointer parent = new NodePointer(null, null, null, new Element() {
            @Override
            public String getNamespaceURI() {
                return "test-ns";
            }

            @Override
            public String getNodeName() {
                return "test-element";
            }

            @Override
            public String getAttributeNodeValue(String attrName) {
                return "test-value";
            }

            @Override
            public Attr getAttributeNode(String attrName) {
                return new Attr("test-attr", "test-value");
            }
        });
        QName name = new QName("test", null, "test-ns");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals("test-attr", iterator.getNodePointer().getNodeName().getName());
        Assert.assertEquals("test-value", iterator.getNodePointer().getNodeValue());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        NodePointer parent = new NodePointer(null, null, null, new Element() {
            @Override
            public String getNamespaceURI() {
                return "test-ns";
            }

            @Override
            public String getNodeName() {
                return "test-element";
            }

            @Override
            public Attr getAttributeNode(int index) {
                switch (index) {
                    case 0:
                        return new Attr("test1", "test-value1");
                    case 1:
                        return new Attr("test2", "test-value2");
                    case 2:
                        return new Attr("test3", "test-value3");
                    default:
                        return null;
                }
            }

            @Override
            public int getAttributeNodeCount() {
                return 3;
            }
        });
        QName name = new QName("test", null, "test-ns");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertEquals(3, iterator.getPosition());

        for (int i = 0; i < 3; i++) {
            Assert.assertNotNull(iterator.getNodePointer());
            Assert.assertEquals("test" + (i + 1), iterator.getNodePointer().getNodeName().getName());
            Assert.assertEquals("test-value" + (i + 1), iterator.getNodePointer().getNodeValue());
            iterator.next();
        }
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(4, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorOutOfBounds() {
        NodePointer parent = new NodePointer(null, null, null, new Element() {
            @Override
            public String getNamespaceURI() {
                return "test-ns";
            }

            @Override
            public String getNodeName() {
                return "test-element";
            }

            @Override
            public Attr getAttributeNode(int index) {
                switch (index) {
                    case 0:
                        return new Attr("test1", "test-value1");
                    case 1:
                        return new Attr("test2", "test-value2");
                    case 2:
                        return new Attr("test3", "test-value3");
                    default:
                        return null;
                }
            }

            @Override
            public int getAttributeNodeCount() {
                return 3;
            }
        });
        QName name = new QName("test", null, "test-ns");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test(expected = NullPointerException.class)
    public void testIteratorWithNullParent() {
        QName name = new QName("test", null, null.getString());
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, name);
    }

    @Test(expected = NullPointerException.class)
    public void testIteratorWithNullName() {
        NodePointer parent = new NodePointer(null, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithEmptyName() {
        NodePointer parent = new NodePointer(null, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", null, null.getString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNonExistentName() {
        NodePointer parent = new NodePointer(null, null, null, new Element() {
            @Override
            public String getNodeName() {
                return "test-element";
            }
        });
        DOMAttributeIterator iterator = new DOMAtributeIterator(parent, new QName("test", "non-existent-prefix", null.getString()));
    }
}