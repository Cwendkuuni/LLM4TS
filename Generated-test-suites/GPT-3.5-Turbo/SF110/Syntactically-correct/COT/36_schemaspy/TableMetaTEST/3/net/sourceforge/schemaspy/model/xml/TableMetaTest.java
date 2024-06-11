import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TableMetaTest {
    
    @Test
    public void testName() {
        TableMeta table = new TableMeta();
        String expected = "TestTable";
        table.setName(expected);
        String actual = table.getName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComments() {
        TableMeta table = new TableMeta();
        String expected = "This is a test table";
        table.setComments(expected);
        String actual = table.getComments();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testColumns() {
        TableMeta table = new TableMeta();
        List<TableColumnMeta> expected = new ArrayList<TableColumnMeta>();
        expected.add(new TableColumnMeta("Column1"));
        expected.add(new TableColumnMeta("Column2"));
        table.setColumns(expected);
        List<TableColumnMeta> actual = table.getColumns();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoteCatalog() {
        TableMeta table = new TableMeta();
        String expected = "TestCatalog";
        table.setRemoteCatalog(expected);
        String actual = table.getRemoteCatalog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRemoteSchema() {
        TableMeta table = new TableMeta();
        String expected = "TestSchema";
        table.setRemoteSchema(expected);
        String actual = table.getRemoteSchema();
        assertEquals(expected, actual);
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testEmptyName() {
        TableMeta table = new TableMeta();
        String expected = "";
        table.setName(expected);
        String actual = table.getName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNullComments() {
        TableMeta table = new TableMeta();
        String expected = null;
        table.setComments(expected);
        String actual = table.getComments();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEmptyColumns() {
        TableMeta table = new TableMeta();
        List<TableColumnMeta> expected = new ArrayList<TableColumnMeta>();
        table.setColumns(expected);
        List<TableColumnMeta> actual = table.getColumns();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNullRemoteCatalog() {
        TableMeta table = new TableMeta();
        String expected = null;
        table.setRemoteCatalog(expected);
        String actual = table.getRemoteCatalog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNullRemoteSchema() {
        TableMeta table = new TableMeta();
        String expected = null;
        table.setRemoteSchema(expected);
        String actual = table.getRemoteSchema();
        assertEquals(expected, actual);
    }
}