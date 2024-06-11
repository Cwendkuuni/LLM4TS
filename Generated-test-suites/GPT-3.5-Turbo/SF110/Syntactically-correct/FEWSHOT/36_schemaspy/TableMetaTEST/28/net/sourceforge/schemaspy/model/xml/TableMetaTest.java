import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class TableMetaTest {
    
    @Test
    public void testTableMetaConstructor() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("table_name", tableMeta.getName());
        assertEquals("Table Comments", tableMeta.getComments());
        assertNotNull(tableMeta.getColumns());
        assertEquals(2, tableMeta.getColumns().size());
        assertEquals("remote_catalog", tableMeta.getRemoteCatalog());
        assertEquals("remote_schema", tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testGetName() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("table_name", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("Table Comments", tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertNotNull(tableMeta.getColumns());
        assertEquals(2, tableMeta.getColumns().size());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("remote_catalog", tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("remote_schema", tableMeta.getRemoteSchema());
    }
    
    private Node createTableNode() {
        Document doc = new MockDocument();
        Element tableElement = doc.createElement("table");
        
        tableElement.setAttribute("name", "table_name");
        tableElement.setAttribute("comments", "Table Comments");
        tableElement.setAttribute("remoteCatalog", "remote_catalog");
        tableElement.setAttribute("remoteSchema", "remote_schema");
        
        Element column1 = doc.createElement("column");
        column1.setAttribute("name", "column1_name");
        tableElement.appendChild(column1);
        
        Element column2 = doc.createElement("column");
        column2.setAttribute("name", "column2_name");
        tableElement.appendChild(column2);
        
        return tableElement;
    }
    
    private class MockDocument implements Document {
        // Implement required methods for testing
    }
}