
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        Node node = createXmlNode();
        TableMeta table = new TableMeta(node);
        assertEquals("TestTable", table.getName());
    }
    
    @Test
    public void testGetComments() {
        Node node = createXmlNode();
        TableMeta table = new TableMeta(node);
        assertNull(table.getComments());
    }
    
    @Test
    public void testGetColumns() {
        Node node = createXmlNode();
        TableMeta table = new TableMeta(node);
        assertEquals(0, table.getColumns().size());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        Node node = createXmlNode();
        TableMeta table = new TableMeta(node);
        assertNull(table.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        Node node = createXmlNode();
        TableMeta table = new TableMeta(node);
        assertNull(table.getRemoteSchema());
    }
    
    private Node createXmlNode() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            
            Element tableNode = doc.createElement("table");
            tableNode.setAttribute("name", "TestTable");
            
            doc.appendChild(tableNode);
            
            return tableNode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
