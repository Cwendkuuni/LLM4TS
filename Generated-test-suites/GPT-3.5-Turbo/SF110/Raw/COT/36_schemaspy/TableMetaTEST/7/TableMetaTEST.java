
package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.*;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        // Setup
        Node tableNode = createTableNode("testTable");
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Test
        String name = tableMeta.getName();
        
        // Verify
        assertEquals("testTable", name);
    }
    
    @Test
    public void testGetComments() {
        // Setup
        Node tableNode = createTableNode("testTable");
        tableNode.getAttributes().setNamedItem(createAttribute("comments", "Test comments"));
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Test
        String comments = tableMeta.getComments();
        
        // Verify
        assertEquals("Test comments", comments);
    }
    
    @Test
    public void testGetCommentsNoAttribute() {
        // Setup
        Node tableNode = createTableNode("testTable");
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Test
        String comments = tableMeta.getComments();
        
        // Verify
        assertNull(comments);
    }
    
    @Test
    public void testGetColumns() {
        // Setup
        Node tableNode = createTableNode("testTable");
        tableNode.appendChild(createColumnNode());
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Test
        List<TableColumnMeta> columns = tableMeta.getColumns();
        
        // Verify
        assertEquals(1, columns.size());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        // Setup
        Node tableNode = createTableNode("testTable");
        tableNode.getAttributes().setNamedItem(createAttribute("remoteCatalog", "Test catalog"));
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Test
        String remoteCatalog = tableMeta.getRemoteCatalog();
        
        // Verify
        assertEquals("Test catalog", remoteCatalog);
    }
    
    @Test
    public void testGetRemoteCatalogNoAttribute() {
        // Setup
        Node tableNode = createTableNode("testTable");
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Test
        String remoteCatalog = tableMeta.getRemoteCatalog();
        
        // Verify
        assertNull(remoteCatalog);
    }
    
    @Test
    public void testGetRemoteSchema() {
        // Setup
        Node tableNode = createTableNode("testTable");
        tableNode.getAttributes().setNamedItem(createAttribute("remoteSchema", "Test schema"));
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Test
        String remoteSchema = tableMeta.getRemoteSchema();
        
        // Verify
        assertEquals("Test schema", remoteSchema);
    }
    
    @Test
    public void testGetRemoteSchemaNoAttribute() {
        // Setup
        Node tableNode = createTableNode("testTable");
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Test
        String remoteSchema = tableMeta.getRemoteSchema();
        
        // Verify
        assertNull(remoteSchema);
    }
    
    // Helper methods for creating test data
    
    private Node createTableNode(String name) {
        Document document = XmlUtils.createDocument();
        Element tableNode = document.createElement("table");
        tableNode.setAttribute("name", name);
        document.appendChild(tableNode);
        return tableNode;
    }
    
    private Node createColumnNode() {
        Document document = XmlUtils.createDocument();
        Element columnNode = document.createElement("column");
        return columnNode;
    }
    
    private Node createAttribute(String name, String value) {
        Document document = XmlUtils.createDocument();
        Attr attribute = document.createAttribute(name);
        attribute.setValue(value);
        return attribute;
    }
}
