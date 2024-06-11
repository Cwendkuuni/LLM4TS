import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private JDOMAttributeIterator iterator;

    @Before
    public void setup() {
        parent = new NodePointer() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return null;
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public NodePointer createPath(JXPathContext context) {
                return null;
            }

            @Override
            public NodeIterator childIterator(QName name, QName nodeType,
                                              boolean reverse, NodePointer startWith) {
                return null;
            }

            @Override
            public NodeIterator attributeIterator(QName name) {
                return null;
            }
        };

        name = new QName("prefix", "name");

        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull("Expected null node pointer", nodePointer);
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();

        Assert.assertEquals("Expected position to be 0", 0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);

        Assert.assertFalse("Expected false result", result);
    }

    @Test
    public void testConstructorWithElement() {
        Element element = new Element("test");
        element.setAttribute("name", "value");

        parent = new NodePointer() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return element;
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public NodePointer createPath(JXPathContext context) {
                return null;
            }

            @Override
            public NodeIterator childIterator(QName name, QName nodeType,
                                              boolean reverse, NodePointer startWith) {
                return null;
            }

            @Override
            public NodeIterator attributeIterator(QName name) {
                return null;
            }
        };

        iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull("Expected non-null node pointer", nodePointer);
        Assert.assertEquals("Expected attribute value to match", "value", ((Attribute) nodePointer.getImmediateNode()).getValue());
    }

    @Test
    public void testConstructorWithElementAndPrefixXml() {
        Element element = new Element("test");
        element.setAttribute("name", "value");

        parent = new NodePointer() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return element;
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public NodePointer createPath(JXPathContext context) {
                return null;
            }

            @Override
            public NodeIterator childIterator(QName name, QName nodeType,
                                              boolean reverse, NodePointer startWith) {
                return null;
            }

            @Override
            public NodeIterator attributeIterator(QName name) {
                return null;
            }
        };

        name = new QName("xml", "name");

        iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull("Expected non-null node pointer", nodePointer);
        Assert.assertEquals("Expected attribute value to match", "value", ((Attribute) nodePointer.getImmediateNode()).getValue());
    }

    @Test
    public void testConstructorWithElementAndPrefixOther() {
        Element element = new Element("test");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "namespace"));
        element.setAttribute("name", "value");

        parent = new NodePointer() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return element;
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public NodePointer createPath(JXPathContext context) {
                return null;
            }

            @Override
            public NodeIterator childIterator(QName name, QName nodeType,
                                              boolean reverse, NodePointer startWith) {
                return null;
            }

            @Override
            public NodeIterator attributeIterator(QName name) {
                return null;
            }
        };

        name = new QName("prefix", "name");

        iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull("Expected non-null node pointer", nodePointer);
        Assert.assertEquals("Expected attribute value to match", "value", ((Attribute) nodePointer.getImmediateNode()).getValue());
    }

    @Test
    public void testConstructorWithElementAndWildcardName() {
        Element element = new Element("test");
        element.setAttribute("name", "value");

        parent = new NodePointer() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return element;
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public NodePointer createPath(JXPathContext context) {
                return null;
            }

            @Override
            public NodeIterator childIterator(QName name, QName nodeType,
                                              boolean reverse, NodePointer startWith) {
                return null;
            }

            @Override
            public NodeIterator attributeIterator(QName name) {
                return null;
            }
        };

        name = new QName(null, "*");

        iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull("Expected non-null node pointer", nodePointer);
        Assert.assertEquals("Expected attribute value to match", "value", ((Attribute) nodePointer.getImmediateNode()).getValue());
    }

    @Test
    public void testConstructorWithElementAndNoAttributes() {
        Element element = new Element("test");

        parent = new NodePointer() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return element;
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public NodePointer createPath(JXPathContext context) {
                return null;
            }

            @Override
            public NodeIterator childIterator(QName name, QName nodeType,
                                              boolean reverse, NodePointer startWith) {
                return null;
            }

            @Override
            public NodeIterator attributeIterator(QName name) {
                return null;
            }
        };

        name = new QName("prefix", "name");

        iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull("Expected null node pointer", nodePointer);
    }

    @Test
    public void testConstructorWithNonElementParent() {
        parent = new NodePointer() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return "test";
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public NodePointer createPath(JXPathContext context) {
                return null;
            }

            @Override
            public NodeIterator childIterator(QName name, QName nodeType,
                                              boolean reverse, NodePointer startWith) {
                return null;
            }

            @Override
            public NodeIterator attributeIterator(QName name) {
                return null;
            }
        };

        iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull("Expected null node pointer", nodePointer);
    }
}