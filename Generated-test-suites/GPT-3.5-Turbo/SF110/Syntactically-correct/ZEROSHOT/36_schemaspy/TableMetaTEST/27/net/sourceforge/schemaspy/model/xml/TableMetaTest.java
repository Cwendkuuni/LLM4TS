import static org.junit.Assert.*;
import org.junit.*;

public class TableMetaTest {

    @Test
    public void testGetName() {
        //Test when name is not null
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals(null, tableMeta.getName());

        //Test when name is not null
        tableNode = createTableNodeWithAttribute("name", "Table1");
        tableMeta = new TableMeta(tableNode);
        assertEquals("Table1", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        //Test when comments is not null
        Node tableNode = createTableNodeWithAttribute("comments", "This is a comment");
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("This is a comment", tableMeta.getComments());

        //Test when comments is null
        tableNode = null;
        tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        //Test when columns is empty
        Node tableNode = null;
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals(0, tableMeta.getColumns().size());

        //Test when columns is not empty
        tableNode = createTableNodeWithColumns(3);
        tableMeta = new TableMeta(tableNode);
        assertEquals(3, tableMeta.getColumns().size());
    }

    @Test
    public void testGetRemoteCatalog() {
        //Test when remoteCatalog is not null
        Node tableNode = createTableNodeWithAttribute("remoteCatalog", "Catalog1");
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("Catalog1", tableMeta.getRemoteCatalog());

        //Test when remoteCatalog is null
        tableNode = null;
        tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        //Test when remoteSchema is not null
        Node tableNode = createTableNodeWithAttribute("remoteSchema", "Schema1");
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("Schema1", tableMeta.getRemoteSchema());

        //Test when remoteSchema is null
        tableNode = null;
        tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getRemoteSchema());
    }

    private Node createTableNodeWithAttribute(String attributeName, String attributeValue) {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element tableNode = document.createElement("table");
        tableNode.setAttribute(attributeName, attributeValue);
        return tableNode;
    }

    private Node createTableNodeWithColumns(int numColumns) {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element tableNode = document.createElement("table");
        for (int i = 0; i < numColumns; i++) {
            Element columnNode = document.createElement("column");
            tableNode.appendChild(columnNode);
        }
        return tableNode;
    }
}