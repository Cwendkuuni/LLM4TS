import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;
import java.util.logging.*;

public class TableMetaTest {

    private TableMeta tableMeta;

    @Before
    public void setUp() {
        Node tableNode = createTableNode();
        tableMeta = new TableMeta(tableNode);
    }

    @Test
    public void testGetName() {
        String expectedName = "TestTable";
        String actualName = tableMeta.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetComments() {
        String expectedComments = "Test Comments";
        String actualComments = tableMeta.getComments();
        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testGetColumns() {
        List<TableColumnMeta> expectedColumns = createExpectedColumns();
        List<TableColumnMeta> actualColumns = tableMeta.getColumns();
        assertEquals(expectedColumns.size(), actualColumns.size());
        for (int i = 0; i < expectedColumns.size(); i++) {
            assertEquals(expectedColumns.get(i), actualColumns.get(i));
        }
    }

    @Test
    public void testGetRemoteCatalog() {
        String expectedRemoteCatalog = "TestRemoteCatalog";
        String actualRemoteCatalog = tableMeta.getRemoteCatalog();
        assertEquals(expectedRemoteCatalog, actualRemoteCatalog);
    }

    @Test
    public void testGetRemoteSchema() {
        String expectedRemoteSchema = "TestRemoteSchema";
        String actualRemoteSchema = tableMeta.getRemoteSchema();
        assertEquals(expectedRemoteSchema, actualRemoteSchema);
    }

    private Node createTableNode() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        Element tableElement = doc.createElement("table");
        doc.appendChild(tableElement);

        Attr nameAttr = doc.createAttribute("name");
        nameAttr.setValue("TestTable");
        tableElement.setAttributeNode(nameAttr);

        Attr commentsAttr = doc.createAttribute("comments");
        commentsAttr.setValue("Test Comments");
        tableElement.setAttributeNode(commentsAttr);

        Attr remoteSchemaAttr = doc.createAttribute("remoteSchema");
        remoteSchemaAttr.setValue("TestRemoteSchema");
        tableElement.setAttributeNode(remoteSchemaAttr);

        Attr remoteCatalogAttr = doc.createAttribute("remoteCatalog");
        remoteCatalogAttr.setValue("TestRemoteCatalog");
        tableElement.setAttributeNode(remoteCatalogAttr);

        Element columnElement1 = doc.createElement("column");
        tableElement.appendChild(columnElement1);

        Attr colNameAttr1 = doc.createAttribute("name");
        colNameAttr1.setValue("Column1");
        columnElement1.setAttributeNode(colNameAttr1);

        Element columnElement2 = doc.createElement("column");
        tableElement.appendChild(columnElement2);

        Attr colNameAttr2 = doc.createAttribute("name");
        colNameAttr2.setValue("Column2");
        columnElement2.setAttributeNode(colNameAttr2);

        return tableElement;
    }

    private List<TableColumnMeta> createExpectedColumns() {
        List<TableColumnMeta> columns = new ArrayList<>();
        TableColumnMeta column1 = new TableColumnMeta(createColumnNode("Column1"));
        TableColumnMeta column2 = new TableColumnMeta(createColumnNode("Column2"));
        columns.add(column1);
        columns.add(column2);
        return columns;
    }

    private Node createColumnNode(String columnName) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        Element columnElement = doc.createElement("column");

        Attr nameAttr = doc.createAttribute("name");
        nameAttr.setValue(columnName);
        columnElement.setAttributeNode(nameAttr);

        return columnElement;
    }
}