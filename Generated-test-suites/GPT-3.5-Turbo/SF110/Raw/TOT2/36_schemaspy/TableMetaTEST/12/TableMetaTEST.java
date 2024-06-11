import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta table = new TableMeta();
        table.setName("TestTable");
        assertEquals("TestTable", table.getName());
    }

    @Test
    public void testGetComments() {
        TableMeta table = new TableMeta();
        table.setComments("This is a test table");
        assertEquals("This is a test table", table.getComments());
    }

    @Test
    public void testGetColumns() {
        TableMeta table = new TableMeta();
        List<TableColumnMeta> columns = new ArrayList<>();
        table.setColumns(columns);
        assertEquals(columns, table.getColumns());
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta table = new TableMeta();
        table.setRemoteCatalog("TestCatalog");
        assertEquals("TestCatalog", table.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta table = new TableMeta();
        table.setRemoteSchema("TestSchema");
        assertEquals("TestSchema", table.getRemoteSchema());
    }

    @Test
    public void testTableMetaConstructor() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element tableNode = doc.createElement("table");
        tableNode.setAttribute("name", "TestTable");
        TableMeta table = new TableMeta(tableNode);
        assertEquals("TestTable", table.getName());
    }
}