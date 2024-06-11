import org.junit.*;
import static org.junit.Assert.*;
import net.sourceforge.schemaspy.model.xml.*;

public class TableMetaTest {

  @Test
  public void testTableMetaConstructor() {
    // Typical use case
    TableMeta tableMeta = new TableMeta(/* tableNode */);
    assertEquals("Test table name", "Expected table name", tableMeta.getName());
    assertEquals("Test comments", "Expected comments", tableMeta.getComments());
    assertNotNull("Test columns", tableMeta.getColumns());
    assertEquals("Test remote catalog", "Expected remote catalog", tableMeta.getRemoteCatalog());
    assertEquals("Test remote schema", "Expected remote schema", tableMeta.getRemoteSchema());
  }
  
  @Test
  public void testGetName() {
    // Typical use case
    TableMeta tableMeta = new TableMeta(/* tableNode */);
    String name = tableMeta.getName();
    assertEquals("Test table name", "Expected table name", name);
  }
  
  @Test
  public void testGetComments() {
    // Typical use case
    TableMeta tableMeta = new TableMeta(/* tableNode */);
    String comments = tableMeta.getComments();
    assertEquals("Test comments", "Expected comments", comments);
  }
  
  @Test
  public void testGetColumns() {
    // Typical use case
    TableMeta tableMeta = new TableMeta(/* tableNode */);
    List<TableColumnMeta> columns = tableMeta.getColumns();
    assertNotNull("Test columns", columns);
    assertTrue("Test columns size", columns.size() > 0);
  }
  
  @Test
  public void testGetRemoteCatalog() {
    // Typical use case
    TableMeta tableMeta = new TableMeta(/* tableNode */);
    String remoteCatalog = tableMeta.getRemoteCatalog();
    assertEquals("Test remote catalog", "Expected remote catalog", remoteCatalog);
  }
  
  @Test
  public void testGetRemoteSchema() {
    // Typical use case
    TableMeta tableMeta = new TableMeta(/* tableNode */);
    String remoteSchema = tableMeta.getRemoteSchema();
    assertEquals("Test remote schema", "Expected remote schema", remoteSchema);
  }
}