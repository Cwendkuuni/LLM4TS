import org.junit.Test;
import static org.junit.Assert.*;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta table = new TableMeta(createTableNode());
        assertEquals("Table1", table.getName());
    }

    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta(createTableNode());
        assertNull(table.getComments());
    }

    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta(createTableNode());
        assertEquals(3, table.getColumns().size());
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta(createTableNode());
        assertNull(table.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta(createTableNode());
        assertNull(table.getRemoteSchema());
    }

    private Node createTableNode() {
        // Create a sample XML structure for table node
        // Modify it based on the specific input requirements

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element tableElement = doc.createElement("table");
            tableElement.setAttribute("name", "Table1");

            doc.appendChild(tableElement);

            return tableElement;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;
    }

}