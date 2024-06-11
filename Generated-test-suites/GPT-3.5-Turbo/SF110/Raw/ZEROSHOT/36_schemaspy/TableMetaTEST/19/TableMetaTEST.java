import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import org.w3c.dom.*;

public class TableMetaTest {
    
    @Test
    public void testTableMetaConstructor() {
        Node tableNode = createTableNode();
        TableMeta table = new TableMeta(tableNode);
        
        assertNotNull(table);
        assertEquals("TestTable", table.getName());
        assertNull(table.getComments());
        assertEquals(0, table.getColumns().size());
        assertNull(table.getRemoteCatalog());
        assertNull(table.getRemoteSchema());
    }
    
    @Test
    public void testGetName() {
        Node tableNode = createTableNode();
        TableMeta table = new TableMeta(tableNode);
        
        assertEquals("TestTable", table.getName());
    }
    
    @Test
    public void testGetCommentsWithNoCommentsAttribute() {
        Node tableNode = createTableNode();
        TableMeta table = new TableMeta(tableNode);
        
        assertNull(table.getComments());
    }
    
    @Test
    public void testGetCommentsWithEmptyCommentsAttribute() {
        Node tableNode = createTableNodeWithComments("");
        TableMeta table = new TableMeta(tableNode);
        
        assertNull(table.getComments());
    }
    
    @Test
    public void testGetCommentsWithNonEmptyCommentsAttribute() {
        Node tableNode = createTableNodeWithComments("Test comments");
        TableMeta table = new TableMeta(tableNode);
        
        assertEquals("Test comments", table.getComments());
    }
    
    @Test
    public void testGetColumns() {
        Node tableNode = createTableNodeWithColumns(3);
        TableMeta table = new TableMeta(tableNode);
        
        assertEquals(3, table.getColumns().size());
    }
    
    @Test
    public void testGetRemoteCatalogWithNoRemoteCatalogAttribute() {
        Node tableNode = createTableNode();
        TableMeta table = new TableMeta(tableNode);
        
        assertNull(table.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteCatalogWithRemoteCatalogAttribute() {
        Node tableNode = createTableNodeWithRemoteCatalog("TestCatalog");
        TableMeta table = new TableMeta(tableNode);
        
        assertEquals("TestCatalog", table.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchemaWithNoRemoteSchemaAttribute() {
        Node tableNode = createTableNode();
        TableMeta table = new TableMeta(tableNode);
        
        assertNull(table.getRemoteSchema());
    }
    
    @Test
    public void testGetRemoteSchemaWithRemoteSchemaAttribute() {
        Node tableNode = createTableNodeWithRemoteSchema("TestSchema");
        TableMeta table = new TableMeta(tableNode);
        
        assertEquals("TestSchema", table.getRemoteSchema());
    }
    
    private Node createTableNode() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            
            Element tableElement = doc.createElement("table");
            doc.appendChild(tableElement);
            
            Attr nameAttr = doc.createAttribute("name");
            nameAttr.setValue("TestTable");
            tableElement.setAttributeNode(nameAttr);
            
            return tableElement;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Node createTableNodeWithComments(String comments) {
        Node tableNode = createTableNode();
        
        Document doc = tableNode.getOwnerDocument();
        
        Attr commentsAttr = doc.createAttribute("comments");
        commentsAttr.setValue(comments);
        tableNode.getAttributes().setNamedItem(commentsAttr);
        
        return tableNode;
    }
    
    private Node createTableNodeWithColumns(int columnCount) {
        Node tableNode = createTableNode();
        
        Document doc = tableNode.getOwnerDocument();
        
        for (int i = 0; i < columnCount; i++) {
            Element columnElement = doc.createElement("column");
            tableNode.appendChild(columnElement);
        }
        
        return tableNode;
    }
    
    private Node createTableNodeWithRemoteCatalog(String remoteCatalog) {
        Node tableNode = createTableNode();
        
        Document doc = tableNode.getOwnerDocument();
        
        Attr remoteCatalogAttr = doc.createAttribute("remoteCatalog");
        remoteCatalogAttr.setValue(remoteCatalog);
        tableNode.getAttributes().setNamedItem(remoteCatalogAttr);
        
        return tableNode;
    }
    
    private Node createTableNodeWithRemoteSchema(String remoteSchema) {
        Node tableNode = createTableNode();
        
        Document doc = tableNode.getOwnerDocument();
        
        Attr remoteSchemaAttr = doc.createAttribute("remoteSchema");
        remoteSchemaAttr.setValue(remoteSchema);
        tableNode.getAttributes().setNamedItem(remoteSchemaAttr);
        
        return tableNode;
    }
}