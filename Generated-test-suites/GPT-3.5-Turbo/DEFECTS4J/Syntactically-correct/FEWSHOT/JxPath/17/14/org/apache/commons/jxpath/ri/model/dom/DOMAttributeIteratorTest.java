package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;
    
    @Before
    public void setUp() {
        Element element = new ElementStub();
        parent = new NodePointerStub(element);
        name = new QName("attribute", "http://www.example.org/schema");
        
        iterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        
        assertNull(nodePointer);
    }
    
    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);
        
        assertEquals(false, result);
    }
    
    private class ElementStub implements Element {
        private NamedNodeMap attributes = new NamedNodeMapStub();
        
        @Override
        public String getNodeName() {
            return null;
        }

        @Override
        public String getNodeValue() {
            return null;
        }

        @Override
        public void setNodeValue(String nodeValue) {}

        @Override
        public short getNodeType() {
            return 0;
        }

        @Override
        public Node getParentNode() {
            return null;
        }

        @Override
        public NodeList getChildNodes() {
            return null;
        }

        @Override
        public Node getFirstChild() {
            return null;
        }

        @Override
        public Node getLastChild() {
            return null;
        }

        @Override
        public Node getPreviousSibling() {
            return null;
        }

        @Override
        public Node getNextSibling() {
            return null;
        }

        @Override
        public NamedNodeMap getAttributes() {
            return attributes;
        }

        @Override
        public Document getOwnerDocument() {
            return null;
        }

        @Override
        public Node insertBefore(Node newChild, Node refChild) {
            return null;
        }

        @Override
        public Node replaceChild(Node newChild, Node oldChild) {
            return null;
        }

        @Override
        public Node removeChild(Node oldChild) {
            return null;
        }

        @Override
        public Node appendChild(Node newChild) {
            return null;
        }

        @Override
        public boolean hasChildNodes() {
            return false;
        }

        @Override
        public Node cloneNode(boolean deep) {
            return null;
        }

        @Override
        public void normalize() {}

        @Override
        public boolean isSupported(String feature, String version) {
            return false;
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
        public void setPrefix(String prefix) {}

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public boolean hasAttributes() {
            return false;
        }

        @Override
        public String getBaseURI() {
            return null;
        }

        @Override
        public short compareDocumentPosition(Node other) throws DOMException {
            return 0;
        }

        @Override
        public String getTextContent() throws DOMException {
            return null;
        }

        @Override
        public void setTextContent(String textContent) throws DOMException {}

        @Override
        public boolean isSameNode(Node other) {
            return false;
        }

        @Override
        public String lookupPrefix(String namespaceURI) {
            return null;
        }

        @Override
        public boolean isDefaultNamespace(String namespaceURI) {
            return false;
        }

        @Override
        public String lookupNamespaceURI(String prefix) {
            return null;
        }

        @Override
        public boolean isEqualNode(Node arg) {
            return false;
        }

        @Override
        public Object getFeature(String feature, String version) {
            return null;
        }

        @Override
        public Object setUserData(String key, Object data, UserDataHandler handler) {
            return null;
        }

        @Override
        public Object getUserData(String key) {
            return null;
        }
    }
    
    private class NamedNodeMapStub implements NamedNodeMap {
        private List<Attr> attrs = new ArrayList<>();
        
        public NamedNodeMapStub() {
            Attr attr = new AttrStub();
            attrs.add(attr);
        }
        
        @Override
        public Node getNamedItem(String name) {
            return null;
        }

        @Override
        public Node setNamedItem(Node arg) throws DOMException {
            return null;
        }

        @Override
        public Node removeNamedItem(String name) throws DOMException {
            return null;
        }

        @Override
        public Node item(int index) {
            return attrs.get(index);
        }

        @Override
        public int getLength() {
            return attrs.size();
        }

        @Override
        public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
            return null;
        }

        @Override
        public Node setNamedItemNS(Node arg) throws DOMException {
            return null;
        }

        @Override
        public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
            return null;
        }
    }
    
    private class AttrStub implements Attr {
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
        public void setValue(String value) throws DOMException {}

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

        @Override
        public String getNodeName() {
            return null;
        }

        @Override
        public String getNodeValue() throws DOMException {
            return null;
        }

        @Override
        public void setNodeValue(String nodeValue) throws DOMException {}

        @Override
        public short getNodeType() {
            return 0;
        }

        @Override
        public Node getParentNode() {
            return null;
        }

        @Override
        public NodeList getChildNodes() {
            return null;
        }

        @Override
        public Node getFirstChild() {
            return null;
        }

        @Override
        public Node getLastChild() {
            return null;
        }

        @Override
        public Node getPreviousSibling() {
            return null;
        }

        @Override
        public Node getNextSibling() {
            return null;
        }

        @Override
        public NamedNodeMap getAttributes() {
            return null;
        }

        @Override
        public Document getOwnerDocument() {
            return null;
        }

        @Override
        public Node insertBefore(Node newChild, Node refChild) throws DOMException {
            return null;
        }

        @Override
        public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
            return null;
        }

        @Override
        public Node removeChild(Node oldChild) throws DOMException {
            return null;
        }

        @Override
        public Node appendChild(Node newChild) throws DOMException {
            return null;
        }

        @Override
        public boolean hasChildNodes() {
            return false;
        }

        @Override
        public Node cloneNode(boolean deep) {
            return null;
        }

        @Override
        public void normalize() {}

        @Override
        public boolean isSupported(String feature, String version) {
            return false;
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
        public void setPrefix(String prefix) throws DOMException {}

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public boolean hasAttributes() {
            return false;
        }

        @Override
        public String getBaseURI() {
            return null;
        }

        @Override
        public short compareDocumentPosition(Node other) throws DOMException {
            return 0;
        }

        @Override
        public String getTextContent() throws DOMException {
            return null;
        }

        @Override
        public void setTextContent(String textContent) throws DOMException {}

        @Override
        public boolean isSameNode(Node other) {
            return false;
        }

        @Override
        public String lookupPrefix(String namespaceURI) {
            return null;
        }

        @Override
        public boolean isDefaultNamespace(String namespaceURI) {
            return false;
        }

        @Override
        public String lookupNamespaceURI(String prefix) {
            return null;
        }

        @Override
        public boolean isEqualNode(Node arg) {
            return false;
        }

        @Override
        public Object getFeature(String feature, String version) {
            return null;
        }

        @Override
        public Object setUserData(String key, Object data, UserDataHandler handler) {
            return null;
        }

        @Override
        public Object getUserData(String key) {
            return null;
        }
    }
}