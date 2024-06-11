import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    
    private DOMAttributeIterator domAttributeIterator;
    private NodePointer parent;
    private QName name;
    
    @Before
    public void setUp() {
        Element element = new ElementMock();
        parent = new NodePointerMock(element);
        name = new QName("test", "test", "prefix");
        domAttributeIterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = domAttributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(parent, nodePointer.getParent());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, domAttributeIterator.getPosition());
        domAttributeIterator.setPosition(1);
        assertEquals(1, domAttributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertFalse(domAttributeIterator.setPosition(0));
        assertTrue(domAttributeIterator.setPosition(1));
        assertTrue(domAttributeIterator.setPosition(2));
        assertFalse(domAttributeIterator.setPosition(3));
    }
    
    private class ElementMock implements Element {
        
        @Override
        public String getNodeName() {
            return null;
        }
        
        @Override
        public String getNodeValue() {
            return null;
        }
        
        @Override
        public void setNodeValue(String arg0) {
            
        }
        
        @Override
        public short getNodeType() {
            return Node.ELEMENT_NODE;
        }
        
        @Override
        public Node getParentNode() {
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
            return new NamedNodeMapMock();
        }
        
        @Override
        public Document getOwnerDocument() {
            return null;
        }
        
        @Override
        public Node insertBefore(Node arg0, Node arg1) throws DOMException {
            return null;
        }
        
        @Override
        public Node replaceChild(Node arg0, Node arg1) throws DOMException {
            return null;
        }
        
        @Override
        public Node removeChild(Node arg0) throws DOMException {
            return null;
        }
        
        @Override
        public Node appendChild(Node arg0) throws DOMException {
            return null;
        }
        
        @Override
        public boolean hasChildNodes() {
            return false;
        }
        
        @Override
        public Node cloneNode(boolean arg0) {
            return null;
        }
        
        @Override
        public void normalize() {
        }
        
        @Override
        public boolean isSupported(String arg0, String arg1) {
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
        public void setPrefix(String arg0) throws DOMException {
        }
        
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
        public short compareDocumentPosition(Node arg0) throws DOMException {
            return 0;
        }
        
        @Override
        public String getTextContent() throws DOMException {
            return null;
        }
        
        @Override
        public void setTextContent(String arg0) throws DOMException {
        }
        
        @Override
        public boolean isSameNode(Node arg0) {
            return false;
        }
        
        @Override
        public String lookupPrefix(String arg0) {
            return null;
        }
        
        @Override
        public boolean isDefaultNamespace(String arg0) {
            return false;
        }
        
        @Override
        public String lookupNamespaceURI(String arg0) {
            return null;
        }
        
        @Override
        public boolean isEqualNode(Node arg0) {
            return false;
        }
        
        @Override
        public Object getFeature(String arg0, String arg1) {
            return null;
        }
        
        @Override
        public Object setUserData(String arg0, Object arg1, UserDataHandler arg2) {
            return null;
        }
        
        @Override
        public Object getUserData(String arg0) {
            return null;
        }
        
        @Override
        public String getTagName() {
            return null;
        }
        
        @Override
        public String getAttribute(String arg0) {
            return null;
        }
        
        @Override
        public void setAttribute(String arg0, String arg1) throws DOMException {
        }
        
        @Override
        public void removeAttribute(String arg0) throws DOMException {
        }
        
        @Override
        public Attr getAttributeNode(String arg0) {
            return null;
        }
        
        @Override
        public Attr setAttributeNode(Attr arg0) throws DOMException {
            return null;
        }
        
        @Override
        public Attr removeAttributeNode(Attr arg0) throws DOMException {
            return null;
        }
        
        @Override
        public NodeList getElementsByTagName(String arg0) {
            return null;
        }
        
        @Override
        public String getAttributeNS(String arg0, String arg1) {
            return null;
        }
        
        @Override
        public void setAttributeNS(String arg0, String arg1, String arg2) throws DOMException {
        }
        
        @Override
        public void removeAttributeNS(String arg0, String arg1) throws DOMException {
        }
        
        @Override
        public Attr getAttributeNodeNS(String arg0, String arg1) {
            return null;
        }
        
        @Override
        public Attr setAttributeNodeNS(Attr arg0) throws DOMException {
            return null;
        }
        
        @Override
        public NodeList getElementsByTagNameNS(String arg0, String arg1) {
            return null;
        }
        
        @Override
        public boolean hasAttribute(String arg0) {
            return false;
        }
        
        @Override
        public boolean hasAttributeNS(String arg0, String arg1) {
            return false;
        }
        
        private class NamedNodeMapMock implements NamedNodeMap {
            
            @Override
            public Node getNamedItem(String arg0) {
                return null;
            }
            
            @Override
            public Node setNamedItem(Node arg0) throws DOMException {
                return null;
            }
            
            @Override
            public Node removeNamedItem(String arg0) throws DOMException {
                return null;
            }
            
            @Override
            public Node item(int arg0) {
                return null;
            }
            
            @Override
            public int getLength() {
                return 1;
            }
            
            @Override
            public Node getNamedItemNS(String arg0, String arg1) throws DOMException {
                return null;
            }
            
            @Override
            public Node setNamedItemNS(Node arg0) throws DOMException {
                return null;
            }
            
            @Override
            public Node removeNamedItemNS(String arg0, String arg1) throws DOMException {
                return null;
            }
            
        }
        
    }
    
    private class NodePointerMock implements NodePointer {
        
        private Node node;
        
        public NodePointerMock(Node node) {
            this.node = node;
        }
        
        @Override
        public Node getNode() {
            return node;
        }
        
        @Override
        public NodePointer getParent() {
            return null;
        }
        
        @Override
        public QName getName() {
            return null;
        }
        
        @Override
        public boolean isCollection() {
            return false;
        }
        
        @Override
        public int getLength() {
            return 1;
        }
        
        @Override
        public Object getImmediateNode() {
            return null;
        }
        
        @Override
        public Object getValue() {
            return null;
        }
        
        @Override
        public String getTextValue() {
            return null;
        }
        
        @Override
        public boolean isLeaf() {
            return false;
        }
        
        @Override
        public boolean isActual() {
            return false;
        }
        
        @Override
        public void setValue(Object arg0) {
        }
        
        @Override
        public void remove() {
        }
        
        @Override
        public void setValue(Object arg0, boolean arg1) {
        }
        
        @Override
        public int compareChildNodePointers(NodePointer arg0, NodePointer arg1) {
            return 0;
        }
        
        @Override
        public String asPath() {
            return null;
        }
        
        @Override
        public String getNamespaceURI(String arg0) {
            return null;
        }
        
        @Override
        public String getNamespaceURI() {
            return null;
        }
        
        @Override
        public String getNamespacePrefix(String arg0) {
            return null;
        }
        
        @Override
        public Object getRootNode() {
            return null;
        }
        
        @Override
        public Object getBaseValue() {
            return null;
        }
        
        @Override
        public boolean isDefaultValue() {
            return false;
        }
        
        @Override
        public NodePointer getValuePointer() {
            return null;
        }
        
        @Override
        public boolean testNode(Node arg0) {
            return false;
        }
        
    }
}