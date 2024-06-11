import static org.junit.Assert.*;
import org.junit.Test;
import java.util.logging.*;
import java.util.*;
import org.w3c.dom.*;

public class TableMetaTest {
    
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
        assertNull(tableMeta.getComments());
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
        assertNull(tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getRemoteSchema());
    }
    
    // Additional test cases for edge cases and exception handling
    
    @Test
    public void testGetCommentsWithEmptyCommentsAttribute() {
        Node tableNode = createTableNodeWithEmptyComments();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getComments());
    }
    
    @Test
    public void testGetCommentsWithNonEmptyCommentsAttribute() {
        Node tableNode = createTableNodeWithNonEmptyComments();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("Table comments", tableMeta.getComments());
    }
    
    // Helper methods to create test nodes
    
    private Node createTableNode() {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        
        Element tableElement = document.createElement("table");
        tableElement.setAttribute("name", "table_name");
        
        Element columnElement1 = document.createElement("column");
        tableElement.appendChild(columnElement1);
        
        Element columnElement2 = document.createElement("column");
        tableElement.appendChild(columnElement2);
        
        document.appendChild(tableElement);
        
        return tableElement;
    }
    
    private Node createTableNodeWithEmptyComments() {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        
        Element tableElement = document.createElement("table");
        tableElement.setAttribute("name", "table_name");
        tableElement.setAttribute("comments", "");
        
        Element columnElement1 = document.createElement("column");
        tableElement.appendChild(columnElement1);
        
        Element columnElement2 = document.createElement("column");
        tableElement.appendChild(columnElement2);
        
        document.appendChild(tableElement);
        
        return tableElement;
    }
    
    private Node createTableNodeWithNonEmptyComments() {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        
        Element tableElement = document.createElement("table");
        tableElement.setAttribute("name", "table_name");
        tableElement.setAttribute("comments", "Table comments");
        
        Element columnElement1 = document.createElement("column");
        tableElement.appendChild(columnElement1);
        
        Element columnElement2 = document.createElement("column");
        tableElement.appendChild(columnElement2);
        
        document.appendChild(tableElement);
        
        return tableElement;
    }
    
}