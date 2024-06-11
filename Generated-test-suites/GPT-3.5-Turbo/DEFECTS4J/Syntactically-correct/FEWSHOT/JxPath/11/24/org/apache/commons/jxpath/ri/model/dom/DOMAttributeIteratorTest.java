
package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator attributeIterator;
    private static final QName TEST_ATTRIBUTE_NAME = new QName("testNamespace", "testName");
    private static final QName TEST_WILDCARD_NAME = new QName("testNamespace", "*");

    @Before
    public void setUp() {
        NodePointer parent = createMockNodePointer();
        attributeIterator = new DOMAttributeIterator(parent, TEST_ATTRIBUTE_NAME);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        assertEquals(TEST_ATTRIBUTE_NAME, ((DOMAttributePointer) nodePointer).getName());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
        attributeIterator.setPosition(3);
        assertEquals(3, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeIterator.setPosition(0));
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
        assertTrue(attributeIterator.setPosition(2));
        assertEquals(2, attributeIterator.getPosition());
        assertFalse(attributeIterator.setPosition(3));
    }

    private NodePointer createMockNodePointer() {
        return new NodePointer() {
            @Override
            public Node getNode() {
                return createMockNode();
            }

            @Override
            public String getNamespaceURI(String prefix) {
                return "testNamespace";
            }

            // Mock methods not relevant for testing

        };
    }

    private Node createMockNode() {
        return new Element() {
            @Override
            public NamedNodeMap getAttributes() {
                return createMockAttributemap();
            }

            // Mock methods not relevant for testing
        };
    }

    private NamedNodeMap createMockAttributemap() {
        return new NamedNodeMap() {
            @Override
            public int getLength() {
                return 2;
            }

            @Override
            public Attr item(int index) {
                return createMockAttribute();
            }

            // Mock methods not relevant for testing

        };
    }

    private Attr createMockAttribute() {
        return new Attr() {
            @Override
            public String getNamespaceURI() {
                return "testNamespace";
            }

            @Override
            public String getName() {
                return "testName";
            }

            // Mock methods not relevant for testing

        };
    }
}
