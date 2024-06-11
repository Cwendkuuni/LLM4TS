import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    private NodePointer nodePointer;
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element element = new MockElement();
        this.nodePointer = new NodePointer(new MockNode("element"), null);
        this.attributeIterator = new DOMAttributeIterator(this.nodePointer, new QName("*"));
    }

    @Test
    public void testConstructorWithWildcardName() {
        Assert.assertNotNull(attributeIterator);
    }

    @Test
    public void testConstructorWithSpecificName() {
        Element element = new MockElement();
        this.nodePointer = new NodePointer(new MockNode("element"), null);
        QName name = new QName("test");
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(this.nodePointer, name);
        List<Attr> attributes = attributeIterator.getAttributes();
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals(name.getName(), attributes.get(0).getNodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParent() {
        NodePointer parent = null;
        QName name = new QName("test");
        new DOMAttributeIterator(parent, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        NodePointer parent = new NodePointer(new MockNode("element"), null);
        new DOMAttributeIterator(parent, null);
    }

    @Test
    public void testNext() {
        AttributeNextTest attributeNextTest = new AttributeNextTest();
        AttributeNextTest attributeNextTest2 = new AttributeNextTest();
        Assert.assertTrue(attributeIterator.hasNext());
        Assert.assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        attributeIterator.next();
        Assert.assertFalse(attributeIterator.hasNext());
        Assert.assertSame(attributeNextTest, attributeIterator.next());
        Assert.assertSame(attributeNextTest2, attributeIterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoMoreAttributes() {
        AttributeNextTest attributeNextTest = new AttributeNextTest();
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();
    }

    @Test
    public void testSetPosition() {
        AttributeNextTest attributeTest = new AttributeNextTest();
        AttributeNextTest attributeTest2 = new AttributeNextTest();
        attributeIterator.next();
        attributeIterator.next();
        Assert.assertTrue(attributeIterator.setPosition(0));
        Assert.assertSame(attributeTest, attributeIterator.getNodePointer().getNode());
        Assert.assertTrue(attributeIterator.setPosition(2));
        Assert.assertSame(attributeTest2, attributeIterator.getNodePointer().getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidPosition() {
        attributeIterator.setPosition(-1);
    }

    @Test
    public void testCurrentPosition() {
        AttributeNextTest attributeTest = new AttributeNextTest();
        AttributeNextTest attributeTest2 = new AttributeNextTest();
        attributeIterator.next();
        attributeIterator.next();
        Assert.assertEquals(1, attributeIterator.getPosition());
        attributeIterator.next();
        Assert.assertEquals(2, attributeIterator.getPosition());
        attributeIterator.next();
        Assert.assertEquals(3, attributeIterator.getPosition());
        attributeIterator.next();
        Assert.assertEquals(4, attributeIterator.getPosition());
        attributeIterator.setPosition(1);
        Assert.assertEquals(2, attributeIterator.getPosition());
    }

    private static class MockElement extends Element {
        @Override
        public String getNodeName() {
            return "mockElement";
        }
    }

    private static class MockNode implements Node {
        private String name;

        public MockNode(String name) {
            this.name = name;
        }

        @Override
        public String getNodeName() {
            return this.name;
        }

        @Override
        public int getNodeType() {
            return Node.ELEMENT_NODE;
        }

        @Override
        public Node getParentNode() {
            return null;
        }

        @Override
        public NamedNodeMap getAttributes() {
            List<Attr> attributes = new ArrayList<>();
            Attr attribute1 = new Attr("attr1", "value1");
            Attr attribute2 = new Attr("attr2", "value2");
            Attr attribute3 = new Attr("attr3", "value3");
            Attr attribute4 = new Attr("attr4", "value4");
            attributes.add(attribute1);
            attributes.add(attribute2);
            attributes.add(attribute3);
            attributes.add(attribute4);
            return new MockNamedNodeMap(attributes);
        }
    }

    private static class MockNamedNodeMap implements NamedNodeMap {
        private List<Attr> attributes;

        public MockNamedNodeMap(List<Attr> attributes) {
            this.attributes = attributes;
        }

        @Override
        public int getLength() {
            return attributes.size();
        }

        @Override
        public Node item(int index) {
            return attributes.get(index);
        }
    }

    private static class AttributeNextTest extends Attr {
        public AttributeNextTest() {
            super("attr", "value");
        }
    }
}