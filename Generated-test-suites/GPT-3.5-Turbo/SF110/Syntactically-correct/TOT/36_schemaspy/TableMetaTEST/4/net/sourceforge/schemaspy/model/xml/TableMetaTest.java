import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
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
    public void testGetName() {
        String expected = "table1";
        String actual = tableMeta.getName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetComments() {
        String expected = "This is a table";
        String actual = tableMeta.getComments();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetColumns() {
        List<TableColumnMeta> expected = createExpectedColumns();
        List<TableColumnMeta> actual = tableMeta.getColumns();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRemoteCatalog() {
        String expected = "catalog1";
        String actual = tableMeta.getRemoteCatalog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRemoteSchema() {
        String expected = "schema1";
        String actual = tableMeta.getRemoteSchema();
        assertEquals(expected, actual);
    }
    
    private Node createTableNode() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        
        Element tableElement = doc.createElement("table");
        tableElement.setAttribute("name", "table1");
        tableElement.setAttribute("comments", "This is a table");
        tableElement.setAttribute("remoteCatalog", "catalog1");
        tableElement.setAttribute("remoteSchema", "schema1");
        
        Element columnElement1 = doc.createElement("column");
        columnElement1.setAttribute("name", "column1");
        tableElement.appendChild(columnElement1);
        
        Element columnElement2 = doc.createElement("column");
        columnElement2.setAttribute("name", "column2");
        tableElement.appendChild(columnElement2);
        
        return tableElement;
    }
    
    private List<TableColumnMeta> createExpectedColumns() {
        List<TableColumnMeta> expected = new ArrayList<TableColumnMeta>();
        expected.add(new TableColumnMeta(createColumnNode("column1")));
        expected.add(new TableColumnMeta(createColumnNode("column2")));
        return expected;
    }
    
    private Node createColumnNode(String columnName) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        
        Element columnElement = doc.createElement("column");
        columnElement.setAttribute("name", columnName);
        
        return columnElement;
    }
}