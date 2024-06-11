package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class TableMetaTest {

    @Test
    public void testConstructorWithTableNode() {
        // Create a test tableNode
        Node tableNode = new TableNodeStub("testTable", "Test table comments", "testSchema", "testCatalog");
        TableMeta tableMeta = new TableMeta(tableNode);

        // Check that the values are set correctly
        assertEquals("testTable", tableMeta.getName());
        assertEquals("Test table comments", tableMeta.getComments());
        assertEquals("testCatalog", tableMeta.getRemoteCatalog());
        assertEquals("testSchema", tableMeta.getRemoteSchema());

        // Check that the columns are parsed correctly
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(2, columns.size());
        assertEquals("column1", columns.get(0).getName());
        assertEquals("column2", columns.get(1).getName());
    }

    @Test
    public void testGettersAndSetters() {
        // Create a TableMeta object
        TableMeta tableMeta = new TableMeta(null);

        // Test the getters and setters
        tableMeta.setName("testTable");
        assertEquals("testTable", tableMeta.getName());

        tableMeta.setComments("Test table comments");
        assertEquals("Test table comments", tableMeta.getComments());

        tableMeta.setRemoteCatalog("testCatalog");
        assertEquals("testCatalog", tableMeta.getRemoteCatalog());

        tableMeta.setRemoteSchema("testSchema");
        assertEquals("testSchema", tableMeta.getRemoteSchema());

        List<TableColumnMeta> columns = new ArrayList<>();
        columns.add(new TableColumnMeta());
        columns.add(new TableColumnMeta());
        tableMeta.setColumns(columns);
        assertEquals(columns, tableMeta.getColumns());
    }

    private class TableNodeStub implements Node {
        private String name;
        private String comments;
        private String schema;
        private String catalog;

        public TableNodeStub(String name, String comments, String schema, String catalog) {
            this.name = name;
            this.comments = comments;
            this.schema = schema;
            this.catalog = catalog;
        }

        public short getNodeType() {
            return 0;
        }

        public String getNodeName() {
            return name;
        }

        public String getNodeValue() {
            return null;
        }

        public Node getParentNode() {
            return null;
        }

        public NodeList getChildNodes() {
            return null;
        }

        public Node getFirstChild() {
            return null;
        }

        public Node getLastChild() {
            return null;
        }

        public Node getPreviousSibling() {
            return null;
        }

        public Node getNextSibling() {
            return null;
        }

        public NamedNodeMap getAttributes() {
            return new NamedNodeMapStub();
        }

        public Document getOwnerDocument() {
            return null;
        }

        public Node insertBefore(Node newChild, Node refChild) {
            return null;
        }

        public Node replaceChild(Node newChild, Node oldChild) {
            return null;
        }

        public Node removeChild(Node oldChild) {
            return null;
        }

        public Node appendChild(Node newChild) {
            return null;
        }

        public boolean hasChildNodes() {
            return false;
        }

        public Node cloneNode(boolean deep) {
            return null;
        }

        public void normalize() {

        }

        public boolean isSupported(String feature, String version) {
            return false;
        }

        public String getNamespaceURI() {
            return null;
        }

        public String getPrefix() {
            return null;
        }

        public void setPrefix(String prefix) {

        }

        public String getLocalName() {
            return null;
        }

        public boolean hasAttributes() {
            return false;
        }

        public String getBaseURI() {
            return null;
        }

        public short compareDocumentPosition(Node other) {
            return 0;
        }

        public String getTextContent() {
            return null;
        }

        public void setTextContent(String textContent) {

        }

        public boolean isSameNode(Node other) {
            return false;
        }

        public String lookupPrefix(String namespaceURI) {
            return null;
        }

        public boolean isDefaultNamespace(String namespaceURI) {
            return false;
        }

        public String lookupNamespaceURI(String prefix) {
            return null;
        }

        public boolean isEqualNode(Node arg) {
            return false;
        }

        public Object getFeature(String feature, String version) {
            return null;
        }

        public Object setUserData(String key, Object data, UserDataHandler handler) {
            return null;
        }

        public Object getUserData(String key) {
            return null;
        }
    }

    private class NamedNodeMapStub implements NamedNodeMap {
        public Node getNamedItem(String name) {
            if (name.equals("name")) {
                return createNodeWithNodeValue("name", "column1");
            } else if (name.equals("comments") || name.equals("remarks")) {
                return createNodeWithNodeValue("comments", "column1 comments");
            } else if (name.equals("remoteSchema")) {
                return createNodeWithNodeValue("remoteSchema", "column1Schema");
            } else if (name.equals("remoteCatalog")) {
                return createNodeWithNodeValue("remoteCatalog", "column1Catalog");
            }
            return null;
        }

        public Node setNamedItem(Node item) {
            return null;
        }

        public Node removeNamedItem(String name) {
            return null;
        }

        public Node item(int index) {
            if (index == 0) {
                return createNodeWithNodeValue("name", "column1");
            } else if (index == 1) {
                return createNodeWithNodeValue("name", "column2");
            }
            return null;
        }

        public int getLength() {
            return 2;
        }

        public Node getNamedItemNS(String namespaceURI, String localName) {
            return null;
        }

        public Node setNamedItemNS(Node arg) {
            return null;
        }

        public Node removeNamedItemNS(String namespaceURI, String localName) {
            return null;
        }

        private Node createNodeWithNodeValue(String nodeName, String nodeValue) {
            return new Node() {
                public short getNodeType() {
                    return 0;
                }

                public String getNodeName() {
                    return nodeName;
                }

                public String getNodeValue() {
                    return nodeValue;
                }

                public Node getParentNode() {
                    return null;
                }

                public NodeList getChildNodes() {
                    return null;
                }

                public Node getFirstChild() {
                    return null;
                }

                public Node getLastChild() {
                    return null;
                }

                public Node getPreviousSibling() {
                    return null;
                }

                public Node getNextSibling() {
                    return null;
                }

                public NamedNodeMap getAttributes() {
                    return null;
                }

                public Document getOwnerDocument() {
                    return null;
                }

                public Node insertBefore(Node newChild, Node refChild) {
                    return null;
                }

                public Node replaceChild(Node newChild, Node oldChild) {
                    return null;
                }

                public Node removeChild(Node oldChild) {
                    return null;
                }

                public Node appendChild(Node newChild) {
                    return null;
                }

                public boolean hasChildNodes() {
                    return false;
                }

                public Node cloneNode(boolean deep) {
                    return null;
                }

                public void normalize() {

                }

                public boolean isSupported(String feature, String version) {
                    return false;
                }

                public String getNamespaceURI() {
                    return null;
                }

                public String getPrefix() {
                    return null;
                }

                public void setPrefix(String prefix) {

                }

                public String getLocalName() {
                    return null;
                }

                public boolean hasAttributes() {
                    return false;
                }

                public String getBaseURI() {
                    return null;
                }

                public short compareDocumentPosition(Node other) {
                    return 0;
                }

                public String getTextContent() {
                    return null;
                }

                public void setTextContent(String textContent) {

                }

                public boolean isSameNode(Node other) {
                    return false;
                }

                public String lookupPrefix(String namespaceURI) {
                    return null;
                }

                public boolean isDefaultNamespace(String namespaceURI) {
                    return false;
                }

                public String lookupNamespaceURI(String prefix) {
                    return null;
                }

                public boolean isEqualNode(Node arg) {
                    return false;
                }

                public Object getFeature(String feature, String version) {
                    return null;
                }

                public Object setUserData(String key, Object data, UserDataHandler handler) {
                    return null;
                }

                public Object getUserData(String key) {
                    return null;
                }
            };
        }
    }
}