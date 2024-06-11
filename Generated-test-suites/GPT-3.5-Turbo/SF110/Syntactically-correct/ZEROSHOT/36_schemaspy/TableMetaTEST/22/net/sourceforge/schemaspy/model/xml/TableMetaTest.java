package net.sourceforge.schemaspy.model.xml;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class TableMetaTest {

    private TableMeta tableMeta;

    @Before
    public void setUp() throws Exception {
        // Creating a mock tableNode for testing
        Element element = new Element() {
            @Override
            public String getTagName() {
                return "table";
            }

            @Override
            public NamedNodeMap getAttributes() {
                // Creating a mock NamedNodeMap for testing
                NamedNodeMap attribs = new NamedNodeMap() {
                    @Override
                    public Node getNamedItem(String name) {
                        if (name.equals("name")) {
                            return new Node() {
                                @Override
                                public String getNodeName() {
                                    return "name";
                                }

                                @Override
                                public String getNodeValue() {
                                    return "table1";
                                }

                                @Override
                                public void setNodeValue(String nodeValue) {

                                }

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
                                public void normalize() {

                                }

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
                                public void setPrefix(String prefix) {

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
                                public short compareDocumentPosition(Node other) {
                                    return 0;
                                }

                                @Override
                                public String getTextContent() {
                                    return null;
                                }

                                @Override
                                public void setTextContent(String textContent) {

                                }

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
                            };
                        }
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
                        return null;
                    }

                    @Override
                    public int getLength() {
                        return 0;
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
                };
                return attribs;
            }
        };

        tableMeta = new TableMeta(element);
    }

    @Test
    public void getName() {
        assertEquals("table1", tableMeta.getName());
    }

    @Test
    public void getComments() {
        assertNull(tableMeta.getComments());
    }

    @Test
    public void getColumns() {
        assertNotNull(tableMeta.getColumns());
        assertEquals(0, tableMeta.getColumns().size());
    }

    @Test
    public void getRemoteCatalog() {
        assertNull(tableMeta.getRemoteCatalog());
    }

    @Test
    public void getRemoteSchema() {
        assertNull(tableMeta.getRemoteSchema());
    }
}