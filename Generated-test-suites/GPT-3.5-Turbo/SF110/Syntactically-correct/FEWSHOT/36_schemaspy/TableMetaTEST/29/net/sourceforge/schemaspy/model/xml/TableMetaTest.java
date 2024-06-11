
package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TableMetaTest {
    @Test
    public void testGetName() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("tableName", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertNull(tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(3, columns.size());
        
        // Assert columns
    }
    
    @Test
    public void testGetRemoteCatalog() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertNull(tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertNull(tableMeta.getRemoteSchema());
    }
    
    // Helper method to create a sample table node
    private Node createTableNode() {
        // Create table node and attributes
        Element tableElement = new MockElement("table");
        tableElement.setAttribute("name", "tableName");
        
        // Create column nodes and attributes
        Element columnElement1 = new MockElement("column");
        columnElement1.setAttribute("name", "column1");
        Element columnElement2 = new MockElement("column");
        columnElement2.setAttribute("name", "column2");
        Element columnElement3 = new MockElement("column");
        columnElement3.setAttribute("name", "column3");
        
        // Add column nodes to table node
        tableElement.appendChild(columnElement1);
        tableElement.appendChild(columnElement2);
        tableElement.appendChild(columnElement3);
        
        return tableElement;
    }
    
    private class MockElement extends Element {
        private String tagName;
        private NamedNodeMap attributes;
        private List<Node> childNodes;
        
        public MockElement(String tagName) {
            this.tagName = tagName;
            this.attributes = new NamedNodeMap();
            this.childNodes = new ArrayList<Node>();
        }
        
        @Override
        public String getTagName() {
            return tagName;
        }
        
        @Override
        public NamedNodeMap getAttributes() {
            return attributes;
        }
        
        @Override
        public NodeList getChildNodes() {
            return new NodeList() {
                @Override
                public int getLength() {
                    return childNodes.size();
                }
                
                @Override
                public Node item(int index) {
                    return childNodes.get(index);
                }
            };
        }
        
        public void setAttribute(String name, String value) {
            attributes.setNamedItem(new MockAttr(name, value));
        }
        
        public void appendChild(Node node) {
            childNodes.add(node);
        }
    }
    
    private class MockAttr extends Node {
        private String name;
        private String value;
        
        public MockAttr(String name, String value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public String getNodeName() {
            return name;
        }
        
        @Override
        public String getNodeValue() {
            return value;
        }
    }
}
