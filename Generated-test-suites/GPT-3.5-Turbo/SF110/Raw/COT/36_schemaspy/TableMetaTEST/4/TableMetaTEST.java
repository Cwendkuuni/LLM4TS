import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        // Arrange
        Node tableNode = createTableNode("table", null);
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Act
        String name = tableMeta.getName();
        
        // Assert
        assertEquals("table", name);
    }
    
    @Test
    public void testGetComments_WithCommentsAttribute() {
        // Arrange
        Node tableNode = createTableNode("table", "comments");
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Act
        String comments = tableMeta.getComments();
        
        // Assert
        assertEquals("comments", comments);
    }
    
    @Test
    public void testGetComments_WithRemarksAttribute() {
        // Arrange
        Node tableNode = createTableNode("table", null);
        NamedNodeMap attribs = tableNode.getAttributes();
        attribs.setNamedItem(createAttributeNode("remarks", "remarks comments"));
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Act
        String comments = tableMeta.getComments();
        
        // Assert
        assertEquals("remarks comments", comments);
    }
    
    @Test
    public void testGetComments_WithoutCommentsOrRemarksAttribute() {
        // Arrange
        Node tableNode = createTableNode("table", null);
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Act
        String comments = tableMeta.getComments();
        
        // Assert
        assertNull(comments);
    }
    
    @Test
    public void testGetColumns() {
        // Arrange
        Node tableNode = createTableNode("table", null);
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Act
        List<TableColumnMeta> columns = tableMeta.getColumns();
        
        // Assert
        assertTrue(columns.isEmpty());
    }
    
    @Test
    public void testGetRemoteCatalog_WithRemoteCatalogAttribute() {
        // Arrange
        Node tableNode = createTableNode("table", null);
        NamedNodeMap attribs = tableNode.getAttributes();
        attribs.setNamedItem(createAttributeNode("remoteCatalog", "catalog"));
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Act
        String remoteCatalog = tableMeta.getRemoteCatalog();
        
        // Assert
        assertEquals("catalog", remoteCatalog);
    }
    
    @Test
    public void testGetRemoteCatalog_WithoutRemoteCatalogAttribute() {
        // Arrange
        Node tableNode = createTableNode("table", null);
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Act
        String remoteCatalog = tableMeta.getRemoteCatalog();
        
        // Assert
        assertNull(remoteCatalog);
    }
    
    @Test
    public void testGetRemoteSchema_WithRemoteSchemaAttribute() {
        // Arrange
        Node tableNode = createTableNode("table", null);
        NamedNodeMap attribs = tableNode.getAttributes();
        attribs.setNamedItem(createAttributeNode("remoteSchema", "schema"));
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Act
        String remoteSchema = tableMeta.getRemoteSchema();
        
        // Assert
        assertEquals("schema", remoteSchema);
    }
    
    @Test
    public void testGetRemoteSchema_WithoutRemoteSchemaAttribute() {
        // Arrange
        Node tableNode = createTableNode("table", null);
        TableMeta tableMeta = new TableMeta(tableNode);
        
        // Act
        String remoteSchema = tableMeta.getRemoteSchema();
        
        // Assert
        assertNull(remoteSchema);
    }
    
    // Helper methods for test cases
    
    private Node createTableNode(String tableName, String comments) {
        Document document = XMLUtils.createDocument();
        Element tableElement = document.createElement("table");
        tableElement.setAttribute("name", tableName);
        if (comments != null) {
            tableElement.setAttribute("comments", comments);
        }
        document.appendChild(tableElement);
        return tableElement;
    }
    
    private Node createAttributeNode(String attributeName, String attributeValue) {
        Document document = XMLUtils.createDocument();
        Element attributeElement = document.createElement("attribute");
        attributeElement.setAttribute("name", attributeName);
        attributeElement.setTextContent(attributeValue);
        return attributeElement;
    }
}