
package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import static org.junit.Assert.*;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("TestTable");
        assertEquals("TestTable", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        TableMeta tableMeta = new TableMeta();
        tableMeta.setComments("This is a test table");
        assertEquals("This is a test table", tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        TableMeta tableMeta = new TableMeta();
        TableColumnMeta columnMeta1 = new TableColumnMeta();
        TableColumnMeta columnMeta2 = new TableColumnMeta();
        tableMeta.getColumns().add(columnMeta1);
        tableMeta.getColumns().add(columnMeta2);
        assertEquals(2, tableMeta.getColumns().size());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta tableMeta = new TableMeta();
        tableMeta.setRemoteCatalog("TestCatalog");
        assertEquals("TestCatalog", tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta tableMeta = new TableMeta();
        tableMeta.setRemoteSchema("TestSchema");
        assertEquals("TestSchema", tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testConstructor() {
        // Test empty tableNode
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertEquals(0, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
        
        // Test tableNode with name and columns
        tableNode = createTableNodeWithColumns("TestTable", 2);
        tableMeta = new TableMeta(tableNode);
        assertEquals("TestTable", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertEquals(2, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
        
        // Test tableNode with name, comments, and columns
        tableNode = createTableNodeWithCommentsAndColumns("TestTable", "This is a test table", 3);
        tableMeta = new TableMeta(tableNode);
        assertEquals("TestTable", tableMeta.getName());
        assertEquals("This is a test table", tableMeta.getComments());
        assertEquals(3, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
        
        // Test tableNode with name, remoteSchema, remoteCatalog, and columns
        tableNode = createTableNodeWithRemoteSchemaRemoteCatalogAndColumns("TestTable", "TestSchema", "TestCatalog", 4);
        tableMeta = new TableMeta(tableNode);
        assertEquals("TestTable", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertEquals("TestSchema", tableMeta.getRemoteSchema());
        assertEquals("TestCatalog", tableMeta.getRemoteCatalog());
        assertEquals(4, tableMeta.getColumns().size());
    }
    
    private Node createTableNodeWithColumns(String tableName, int numColumns) {
        Element tableNode = Document.createElement("table");
        Attr nameAttribute = Document.createAttribute("name");
        nameAttribute.setNodeValue(tableName);
        tableNode.setAttributeNode(nameAttribute);
        
        for (int i = 0; i < numColumns; i++) {
           Element columnNode = Document.createElement("column");
           tableNode.appendChild(columnNode);
        }
        
        return tableNode;
    }
    
    private Node createTableNodeWithCommentsAndColumns(String tableName, String comments, int numColumns) {
        Element tableNode = Document.createElement("table");
        Attr nameAttribute = Document.createAttribute("name");
        nameAttribute.setNodeValue(tableName);
        tableNode.setAttributeNode(nameAttribute);
        
        Attr commentsAttribute = Document.createAttribute("comments");
        commentsAttribute.setNodeValue(comments);
        tableNode.setAttributeNode(commentsAttribute);
        
        for (int i = 0; i < numColumns; i++) {
           Element columnNode = Document.createElement("column");
           tableNode.appendChild(columnNode);
        }
        
        return tableNode;
    }
    
    private Node createTableNodeWithRemoteSchemaRemoteCatalogAndColumns(String tableName, String remoteSchema, String remoteCatalog, int numColumns) {
        Element tableNode = Document.createElement("table");
        Attr nameAttribute = Document.createAttribute("name");
        nameAttribute.setNodeValue(tableName);
        tableNode.setAttributeNode(nameAttribute);
        
        Attr remoteSchemaAttribute = Document.createAttribute("remoteSchema");
        remoteSchemaAttribute.setNodeValue(remoteSchema);
        tableNode.setAttributeNode(remoteSchemaAttribute);
        
        Attr remoteCatalogAttribute = Document.createAttribute("remoteCatalog");
        remoteCatalogAttribute.setNodeValue(remoteCatalog);
        tableNode.setAttributeNode(remoteCatalogAttribute);
        
        for (int i = 0; i < numColumns; i++) {
           Element columnNode = Document.createElement("column");
           tableNode.appendChild(columnNode);
        }
        
        return tableNode;
    }
}
