
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TableMetaTest {

  @Test
  public void testGetName() {
    Node tableNode = createDummyTableNode();
    TableMeta tableMeta = new TableMeta(tableNode);

    String name = tableMeta.getName();

    assertEquals("table_name", name);
  }

  @Test
  public void testGetComments() {
    Node tableNode = createDummyTableNode();
    TableMeta tableMeta = new TableMeta(tableNode);

    String comments = tableMeta.getComments();

    assertEquals("table_comments", comments);
  }

  @Test
  public void testGetColumns() {
    Node tableNode = createDummyTableNode();
    TableMeta tableMeta = new TableMeta(tableNode);

    List<TableColumnMeta> columns = tableMeta.getColumns();

    assertEquals(2, columns.size());
  }

  @Test
  public void testGetRemoteCatalog() {
    Node tableNode = createDummyTableNode();
    TableMeta tableMeta = new TableMeta(tableNode);

    String remoteCatalog = tableMeta.getRemoteCatalog();

    assertEquals("remote_catalog", remoteCatalog);
  }

  @Test
  public void testGetRemoteSchema() {
    Node tableNode = createDummyTableNode();
    TableMeta tableMeta = new TableMeta(tableNode);

    String remoteSchema = tableMeta.getRemoteSchema();

    assertEquals("remote_schema", remoteSchema);
  }

  private Node createDummyTableNode() {
    // Create a dummy tableNode for testing
    // You can modify this method to create different test cases
    // and edge cases for the TableMeta class
    // Here, we are creating a basic tableNode with some attributes
    Element tableElement = new Element("table");
    tableElement.setAttribute("name", "table_name");
    tableElement.setAttribute("comments", "table_comments");
    tableElement.setAttribute("remoteCatalog", "remote_catalog");
    tableElement.setAttribute("remoteSchema", "remote_schema");

    return tableElement;
  }

}

