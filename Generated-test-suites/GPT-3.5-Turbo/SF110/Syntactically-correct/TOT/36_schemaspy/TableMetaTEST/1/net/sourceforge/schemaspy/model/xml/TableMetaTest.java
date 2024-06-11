import static org.junit.Assert.*;
import org.junit.*;
import org.w3c.dom.*;

public class TableMetaTest {

    private Node tableNode;
    private TableMeta tableMeta;

    @Before
    public void setUp() {
        tableNode = createTableNode();
        tableMeta = new TableMeta(tableNode);
    }

    @Test
    public void testName() {
        assertEquals("table1", tableMeta.getName());
    }

    @Test
    public void testCommentsNull() {
        assertNull(tableMeta.getComments());
    }

    @Test
    public void testCommentsEmpty() {
        tableNode.getAttributes().getNamedItem("comments").setNodeValue("");
        assertNotNull(tableMeta.getComments());
        assertEquals("", tableMeta.getComments());
    }

    @Test
    public void testRemoteCatalogNull() {
        assertNull(tableMeta.getRemoteCatalog());
    }

    @Test
    public void testRemoteSchemaNull() {
        assertNull(tableMeta.getRemoteSchema());
    }

    @Test
    public void testColumnsSize() {
        assertEquals(2, tableMeta.getColumns().size());
    }

    @Test
    public void testColumn1Name() {
        assertEquals("column1", tableMeta.getColumns().get(0).getName());
    }

    @Test
    public void testColumn2Name() {
        assertEquals("column2", tableMeta.getColumns().get(1).getName());
    }

    private Node createTableNode() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element tableElement = document.createElement("table");
        Attr nameAttr = document.createAttribute("name");
        nameAttr.setValue("table1");
        tableElement.setAttributeNode(nameAttr);

        Element columnElement1 = document.createElement("column");
        Attr nameAttr1 = document.createAttribute("name");
        nameAttr1.setValue("column1");
        columnElement1.setAttributeNode(nameAttr1);
        tableElement.appendChild(columnElement1);

        Element columnElement2 = document.createElement("column");
        Attr nameAttr2 = document.createAttribute("name");
        nameAttr2.setValue("column2");
        columnElement2.setAttributeNode(nameAttr2);
        tableElement.appendChild(columnElement2);

        return tableElement;
    }
}