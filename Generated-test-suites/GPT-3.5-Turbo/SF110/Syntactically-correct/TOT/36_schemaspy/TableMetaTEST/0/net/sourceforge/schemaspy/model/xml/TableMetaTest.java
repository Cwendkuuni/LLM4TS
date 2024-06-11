
package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.w3c.dom.*;

public class TableMetaTest {

    @Test
    public void testTableMetaConstructor() {
        // Typical use case: Create TableMeta object with valid tableNode
        Node tableNode = createValidTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Assert that the object is initialized correctly
        assertEquals("table_name", tableMeta.getName());
        assertEquals("table_comments", tableMeta.getComments());
        assertEquals("remote_catalog", tableMeta.getRemoteCatalog());
        assertEquals("remote_schema", tableMeta.getRemoteSchema());
        assertEquals(1, tableMeta.getColumns().size());
    }

    @Test
    public void testTableMetaConstructor_InvalidTableNode() {
        // Error scenario: Create TableMeta object with null tableNode
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Assert that the object is initialized correctly
        assertNull(tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
        assertEquals(0, tableMeta.getColumns().size());
    }
    
    @Test
    public void testGetName() {
        // Typical use case: Get the name of the table
        Node tableNode = createValidTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Assert that the name is returned correctly
        assertEquals("table_name", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        // Typical use case: Get the comments of the table
        Node tableNode = createValidTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Assert that the comments are returned correctly
        assertEquals("table_comments", tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        // Typical use case: Get the list of columns of the table
        Node tableNode = createValidTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Assert that the list of columns is returned correctly
        assertEquals(1, tableMeta.getColumns().size());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        // Typical use case: Get the remote catalog of the table
        Node tableNode = createValidTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Assert that the remote catalog is returned correctly
        assertEquals("remote_catalog", tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        // Typical use case: Get the remote schema of the table
        Node tableNode = createValidTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Assert that the remote schema is returned correctly
        assertEquals("remote_schema", tableMeta.getRemoteSchema());
    }
    
    // Helper method to create a valid tableNode for testing
    private Node createValidTableNode() {
        Document document = createDocument();
        Element tableNode = document.createElement("table");
        tableNode.setAttribute("name", "table_name");
        tableNode.setAttribute("comments", "table_comments");
        tableNode.setAttribute("remoteCatalog", "remote_catalog");
        tableNode.setAttribute("remoteSchema", "remote_schema");
        Element columnNode = document.createElement("column");
        tableNode.appendChild(columnNode);
        return tableNode;
    }
    
    // Helper method to create a document for testing
    private Document createDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            return docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
