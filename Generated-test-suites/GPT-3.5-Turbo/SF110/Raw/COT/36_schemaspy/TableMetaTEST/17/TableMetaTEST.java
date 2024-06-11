
import static org.junit.Assert.*;
import java.util.logging.*;
import java.util.*;
import org.w3c.dom.*;

public class TableMetaTest {

    @Test
    public void testGetName() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("Table1", tableMeta.getName());
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
        assertTrue(tableMeta.getColumns().isEmpty());
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
    
    private Node createTableNode() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        
        Element tableElement = document.createElement("table");
        tableElement.setAttribute("name", "Table1");
        
        document.appendChild(tableElement);
        
        return tableElement;
    }
}
