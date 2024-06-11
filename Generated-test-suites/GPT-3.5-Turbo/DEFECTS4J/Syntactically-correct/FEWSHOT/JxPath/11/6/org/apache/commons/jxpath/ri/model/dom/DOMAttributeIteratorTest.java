package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = new NodePointer() {
            @Override
            public Object getNode() {
                return null;
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return 0;
            }

            @Override
            public NodeIterator childNodeIterator(QName name, boolean reverse, NodePointer startWith) {
                return null;
            }

            @Override
            public NodeIterator attributeIterator(QName name) {
                return null;
            }

            @Override
            public NodePointer namespacePointer(String prefix) {
                return null;
            }

            @Override
            public String getNamespaceURI(String prefix) {
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
            public QName getName(int index) {
                return null;
            }

            @Override
            public Object getBaseValue() {
                return null;
            }

            @Override
            public MutableNodePointer createPath(String path) {
                return null;
            }
        };

        name = new QName("test", "prefix", "namespaceURI");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        assertTrue(iterator.attributes instanceof ArrayList);
        assertEquals(0, iterator.position);
    }

    @Test
    public void testTestAttr() {
        Attr attr1 = new Attr() {
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
            public TypeInfo getSchemaTypeInfo() {
                return null;
            }

            @Override
            public boolean isId() {
                return false;
            }
        };

        QName testName1 = new QName("test", "prefix", "namespaceURI");
        assertTrue(iterator.testAttr(attr1, testName1));

        QName testName2 = new QName("test", null, "namespaceURI");
        assertTrue(iterator.testAttr(attr1, testName2));

        QName testName3 = new QName("test", "prefix", null);
        assertTrue(iterator.testAttr(attr1, testName3));

        QName testName4 = new QName("test", null, null);
        assertTrue(iterator.testAttr(attr1, testName4));

        QName testName5 = new QName("test", "otherPrefix", "namespaceURI");
        assertFalse(iterator.testAttr(attr1, testName5));

        QName testName6 = new QName("test", "prefix", "otherNamespaceURI");
        assertFalse(iterator.testAttr(attr1, testName6));

        QName testName7 = new QName("otherTest", "prefix", "namespaceURI");
        assertFalse(iterator.testAttr(attr1, testName7));

        Attr attr2 = new Attr() {
            @Override
            public String getName() {
                return "xmlns";
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
            public TypeInfo getSchemaTypeInfo() {
                return null;
            }

            @Override
            public boolean isId() {
                return false;
            }
        };

        assertFalse(iterator.testAttr(attr2, testName1));

        Attr attr3 = new Attr() {
            @Override
            public String getName() {
                return "xmlns:prefix";
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
            public TypeInfo getSchemaTypeInfo() {
                return null;
            }

            @Override
            public boolean isId() {
                return false;
            }
        };

        assertFalse(iterator.testAttr(attr3, testName1));
    }

    @Test
    public void testEqualStrings() {
        assertTrue(iterator.equalStrings(null, null));
        assertFalse(iterator.equalStrings(null, ""));
        assertFalse(iterator.equalStrings("", null));
        assertTrue(iterator.equalStrings("test", "test"));
        assertFalse(iterator.equalStrings("test", "otherTest"));
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element() {
            @Override
            public String getTagName() {
                return null;
            }

            @Override
            public String getAttribute(String name) {
                return null;
            }

            @Override
            public void setAttribute(String name, String value) {
            }

            @Override
            public void removeAttribute(String name) {
            }

            @Override
            public Attr getAttributeNode(String name) {
                return null;
            }

            @Override
            public Attr setAttributeNode(Attr newAttr) {
                return null;
            }

            @Override
            public Attr removeAttributeNode(Attr oldAttr) {
                return null;
            }

            @Override
            public NodeList getElementsByTagName(String name) {
                return null;
            }

            @Override
            public String getAttributeNS(String namespaceURI, String localName) {
                return null;
            }

            @Override
            public void setAttributeNS(String namespaceURI, String qualifiedName, String value) {
            }

            @Override
            public void removeAttributeNS(String namespaceURI, String localName) {
            }

            @Override
            public Attr getAttributeNodeNS(String namespaceURI, String localName) {
                return null;
            }

            @Override
            public Attr setAttributeNodeNS(Attr newAttr) {
                return null;
            }

            @Override
            public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
                return null;
            }

            @Override
            public boolean hasAttribute(String name) {
                return false;
            }

            @Override
            public boolean hasAttributeNS(String namespaceURI, String localName) {
                return false;
            }

            @Override
            public TypeInfo getSchemaTypeInfo() {
                return null;
            }

            @Override
            public void setIdAttribute(String name, boolean isId) {
            }

            @Override
            public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) {
            }

            @Override
            public void setIdAttributeNode(Attr idAttr, boolean isId) {
            }
        };

        QName name1 = new QName("test", "prefix", "namespaceURI");
        assertEquals(null, iterator.getAttribute(element, name1));

        QName name2 = new QName("test", null, "namespaceURI");
        assertEquals(null, iterator.getAttribute(element, name2));

        QName name3 = new QName("test", "prefix", null);
        assertEquals(null, iterator.getAttribute(element, name3));

        QName name4 = new QName("test", null, null);
        assertEquals(null, iterator.getAttribute(element, name4));

        QName name5 = new QName("otherTest", "prefix", "namespaceURI");
        assertEquals(null, iterator.getAttribute(element, name5));
    }

    @Test
    public void testGetNodePointer() {
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(2));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.position);
        assertFalse(iterator.setPosition(0));
    }
}