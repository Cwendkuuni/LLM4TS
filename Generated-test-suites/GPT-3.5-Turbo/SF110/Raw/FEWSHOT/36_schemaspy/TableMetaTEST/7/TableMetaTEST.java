package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TableMetaTest {
    @Test
    public void testGetName() {
        Node tableNode = createTableNode("name", "comments", "remoteCatalog", "remoteSchema", createColumnNodes());
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("name", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        Node tableNode = createTableNode("name", "comments", "remoteCatalog", "remoteSchema", createColumnNodes());
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("comments", tableMeta.getComments());
    }
    
    @Test
    public void testGetComments_Null() {
        Node tableNode = createTableNode("name", null, "remoteCatalog", "remoteSchema", createColumnNodes());
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertNull(tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        Node tableNode = createTableNode("name", "comments", "remoteCatalog", "remoteSchema", createColumnNodes());
        TableMeta tableMeta = new TableMeta(tableNode);
        
        List<TableColumnMeta> expectedColumns = createColumnNodes();
        
        assertEquals(expectedColumns, tableMeta.getColumns());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        Node tableNode = createTableNode("name", "comments", "remoteCatalog", "remoteSchema", createColumnNodes());
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("remoteCatalog", tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteCatalog_Null() {
        Node tableNode = createTableNode("name", "comments", null, "remoteSchema", createColumnNodes());
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertNull(tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        Node tableNode = createTableNode("name", "comments", "remoteCatalog", "remoteSchema", createColumnNodes());
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("remoteSchema", tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testGetRemoteSchema_Null() {
        Node tableNode = createTableNode("name", "comments", "remoteCatalog", null, createColumnNodes());
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertNull(tableMeta.getRemoteSchema());
    }
    
    private Node createTableNode(String name, String comments, String remoteCatalog, String remoteSchema, List<TableColumnMeta> columnNodes) {
        // Create a mock tableNode
        Node tableNode = createMockNode();
        
        // Set attributes on the tableNode
        tableNode.setAttributes(createMockAttributes(name, comments, remoteCatalog, remoteSchema));
        
        // Append columnNodes to tableNode
        for (TableColumnMeta columnNode : columnNodes) {
            tableNode.appendChild(columnNode.getNode());
        }
        
        return tableNode;
    }
    
    private Node createMockNode() {
        // Create a mock Node object here
    }
    
    private NamedNodeMap createMockAttributes(String name, String comments, String remoteCatalog, String remoteSchema) {
        // Create a mock NamedNodeMap object here
    }
    
    private List<TableColumnMeta> createColumnNodes() {
        List<TableColumnMeta> columnNodes = new ArrayList<>();
        
        // Create columnNodes here
        
        return columnNodes;
    }
}