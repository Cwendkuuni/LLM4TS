package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = createParentNodePointer();
        name = createQName();
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        assertTrue(iterator.attributes.isEmpty());
        assertEquals(0, iterator.position);
    }

    @Test
    public void testGetNodePointer() {
        // Test when attributes list is empty
        assertNull(iterator.getNodePointer());

        // Test when attributes list is not empty
        iterator.attributes.add(createAttribute());
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        assertEquals(parent, attributePointer.parent);
        assertEquals(createAttribute(), attributePointer.attribute);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());

        iterator.position = 5;
        assertEquals(5, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Test when position is within the range of attributes list indices
        iterator.attributes.add(createAttribute());
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.position);

        // Test when position is smaller than 1
        iterator.position = 5;
        assertTrue(iterator.setPosition(-5));
        assertEquals(-5, iterator.position);

        // Test when position is greater than the size of attributes list
        iterator.position = 5;
        assertTrue(iterator.setPosition(10));
        assertEquals(10, iterator.position);
    }

    private NodePointer createParentNodePointer() {
        return new NodePointer() {

            @Override
            public Object getBaseValue() {
                return null;
            }

            @Override
            public Object getImmediateNode() {
                return null;
            }

            @Override
            public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return 0;
            }

            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object value) {
            }

            @Override
            public void remove() {
            }

            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public void setValue(Attr attr, Object value) {
            }

            @Override
            public Object getNamespaceURI(String prefix) {
                return null;
            }
        };
    }

    private QName createQName() {
        return new QName("test", "testPrefix");
    }

    private Attr createAttribute() {
        return new Attr() {

            @Override
            public String getName() {
                return null;
            }

            @Override
            public boolean getSpecified() {
                return false;
            }

            @Override
            public String getValue() {
                return null;
            }

            @Override
            public void setValue(String value) {
            }

            @Override
            public Element getOwnerElement() {
                return null;
            }

            @Override
            public Node getNextSibling() {
                return null;
            }

            @Override
            public Node getPreviousSibling() {
                return null;
            }

            @Override
            public Node getParentNode() {
                return null;
            }

            @Override
            public String getNamespaceURI() {
                return null;
            }

            @Override
            public String getPrefix() {
                return null;
            }

            @Override
            public String getLocalName() {
                return null;
            }

            @Override
            public Node getLastChild() {
                return null;
            }

            @Override
            public Node getFirstChild() {
                return null;
            }

            @Override
            public NamedNodeMap getAttributes() {
                return null;
            }

            @Override
            public short getNodeType() {
                return 0;
            }

            @Override
            public Node getFirstChild() {
                return null;
            }

            @Override
            public String getNodeName() {
                return null;
            }
        };
    }

}