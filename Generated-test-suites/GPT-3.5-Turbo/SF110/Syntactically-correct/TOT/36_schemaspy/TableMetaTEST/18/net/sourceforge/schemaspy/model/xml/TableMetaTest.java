import static org.junit.Assert.*;
import org.junit.Test;

public class TableMetaTest {

  @Test
  public void testGetName() {
    // Typical use case: TableMeta with name "table1"
    TableMeta tableMeta = new TableMeta(createTableNode("table1"));
    assertEquals("table1", tableMeta.getName());

    // Edge case: TableMeta with empty name
    tableMeta = new TableMeta(createTableNode(""));
    assertEquals("", tableMeta.getName());

    // Error scenario: TableMeta with null name
    try {
      tableMeta = new TableMeta(createTableNode(null));
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected exception
    }
  }

  @Test
  public void testGetComments() {
    // Typical use case: TableMeta with comments "This is the table"
    TableMeta tableMeta = new TableMeta(createTableNodeWithComments("This is the table"));
    assertEquals("This is the table", tableMeta.getComments());

    // Edge case: TableMeta with empty comments
    tableMeta = new TableMeta(createTableNodeWithComments(""));
    assertEquals("", tableMeta.getComments());

    // Error scenario: TableMeta without comments
    tableMeta = new TableMeta(createTableNode(""));;
    assertNull(tableMeta.getComments());
  }

  @Test
  public void testGetColumns() {
    // Typical use case: TableMeta with 3 columns
    TableMeta tableMeta = new TableMeta(createTableNodeWithColumns(3));
    assertEquals(3, tableMeta.getColumns().size());
    for (TableColumnMeta column : tableMeta.getColumns()) {
      assertNotNull(column);
    }

    // Edge case: TableMeta with 0 columns
    tableMeta = new TableMeta(createTableNodeWithColumns(0));
    assertEquals(0, tableMeta.getColumns().size());

    // Error scenario: TableMeta without columns
    tableMeta = new TableMeta(createTableNode(""));;
    assertTrue(tableMeta.getColumns().isEmpty());
  }

  @Test
  public void testGetRemoteCatalog() {
    // Typical use case: TableMeta with remote catalog "catalog1"
    TableMeta tableMeta = new TableMeta(createTableNodeWithRemoteCatalog("catalog1"));
    assertEquals("catalog1", tableMeta.getRemoteCatalog());

    // Edge case: TableMeta with empty remote catalog
    tableMeta = new TableMeta(createTableNodeWithRemoteCatalog(""));
    assertEquals("", tableMeta.getRemoteCatalog());

    // Error scenario: TableMeta without remote catalog
    tableMeta = new TableMeta(createTableNode(""));;
    assertNull(tableMeta.getRemoteCatalog());
  }

  @Test
  public void testGetRemoteSchema() {
    // Typical use case: TableMeta with remote schema "schema1"
    TableMeta tableMeta = new TableMeta(createTableNodeWithRemoteSchema("schema1"));
    assertEquals("schema1", tableMeta.getRemoteSchema());

    // Edge case: TableMeta with empty remote schema
    tableMeta = new TableMeta(createTableNodeWithRemoteSchema(""));
    assertEquals("", tableMeta.getRemoteSchema());

    // Error scenario: TableMeta without remote schema
    tableMeta = new TableMeta(createTableNode(""));;
    assertNull(tableMeta.getRemoteSchema());
  }

  // Helper methods to create table Nodes

  private Node createTableNode(String name) {
    Element tableElement = new Element();
    tableElement.setAttribute("name", name);
    return tableElement;
  }

  private Node createTableNodeWithComments(String comments) {
    Element tableElement = new Element();
    tableElement.setAttribute("name", "table1");
    tableElement.setAttribute("comments", comments);
    return tableElement;
  }

  private Node createTableNodeWithColumns(int columnCount) {
    Element tableElement = new Element();
    tableElement.setAttribute("name", "table1");
    Element columnsElement = new Element();
    for (int i = 0; i < columnCount; i++) {
      Element columnElement = new Element();
      columnsElement.appendChild(columnElement);
    }
    tableElement.appendChild(columnsElement);
    return tableElement;
  }

  private Node createTableNodeWithRemoteCatalog(String remoteCatalog) {
    Element tableElement = new Element();
    tableElement.setAttribute("name", "table1");
    tableElement.setAttribute("remoteCatalog", remoteCatalog);
    return tableElement;
  }

  private Node createTableNodeWithRemoteSchema(String remoteSchema) {
    Element tableElement = new Element();
    tableElement.setAttribute("name", "table1");
    tableElement.setAttribute("remoteSchema", remoteSchema);
    return tableElement;
  }

}