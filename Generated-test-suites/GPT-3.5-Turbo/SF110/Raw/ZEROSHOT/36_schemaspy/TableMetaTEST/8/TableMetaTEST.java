import static org.junit.Assert.*;
import org.junit.*;
import org.w3c.dom.*;
import net.sourceforge.schemaspy.model.xml.TableMeta;

public class TableMetaTest {
  
  @Test
  public void testConstructor() {
    // Create a tableNode for testing
    Document doc = DocumentBuilder.newDocument();
    Element tableNode = doc.createElement("table");
    tableNode.setAttribute("name", "tableName");
    tableNode.setAttribute("comments", "tableComments");
    tableNode.setAttribute("remoteCatalog", "remoteCatalog");
    tableNode.setAttribute("remoteSchema", "remoteSchema");
    
    // Create a TableColumnMeta for testing
    Element columnNode = doc.createElement("column");
    columnNode.setAttribute("name", "columnName");
    columnNode.setAttribute("type", "columnType");
    tableNode.appendChild(columnNode);
    
    // Create a TableMeta instance and test the constructor
    TableMeta tableMeta = new TableMeta(tableNode);
    
    assertEquals("tableName", tableMeta.getName());
    assertEquals("tableComments", tableMeta.getComments());
    assertEquals("remoteCatalog", tableMeta.getRemoteCatalog());
    assertEquals("remoteSchema", tableMeta.getRemoteSchema());
    assertEquals(1, tableMeta.getColumns().size());
    
    TableColumnMeta columnMeta = tableMeta.getColumns().get(0);
    assertEquals("columnName", columnMeta.getName());
    assertEquals("columnType", columnMeta.getType());
  }
  
  @Test
  public void testGetName() {
    // Create a tableNode for testing
    Document doc = DocumentBuilder.newDocument();
    Element tableNode = doc.createElement("table");
    tableNode.setAttribute("name", "tableName");
    
    // Create a TableMeta instance and test the getName method
    TableMeta tableMeta = new TableMeta(tableNode);
    
    assertEquals("tableName", tableMeta.getName());
  }
  
  @Test
  public void testGetComments() {
    // Create a tableNode for testing
    Document doc = DocumentBuilder.newDocument();
    Element tableNode = doc.createElement("table");
    tableNode.setAttribute("comments", "tableComments");
    
    // Create a TableMeta instance and test the getComments method
    TableMeta tableMeta = new TableMeta(tableNode);
    
    assertEquals("tableComments", tableMeta.getComments());
  }
  
  @Test
  public void testGetColumns() {
    // Create a tableNode for testing
    Document doc = DocumentBuilder.newDocument();
    Element tableNode = doc.createElement("table");
    
    // Create a TableColumnMeta for testing
    Element columnNode1 = doc.createElement("column");
    columnNode1.setAttribute("name", "columnName1");
    columnNode1.setAttribute("type", "columnType1");
    tableNode.appendChild(columnNode1);
    
    Element columnNode2 = doc.createElement("column");
    columnNode2.setAttribute("name", "columnName2");
    columnNode2.setAttribute("type", "columnType2");
    tableNode.appendChild(columnNode2);
    
    // Create a TableMeta instance and test the getColumns method
    TableMeta tableMeta = new TableMeta(tableNode);
    
    assertEquals(2, tableMeta.getColumns().size());
    
    TableColumnMeta columnMeta1 = tableMeta.getColumns().get(0);
    assertEquals("columnName1", columnMeta1.getName());
    assertEquals("columnType1", columnMeta1.getType());
    
    TableColumnMeta columnMeta2 = tableMeta.getColumns().get(1);
    assertEquals("columnName2", columnMeta2.getName());
    assertEquals("columnType2", columnMeta2.getType());
  }
  
  @Test
  public void testGetRemoteCatalog() {
    // Create a tableNode for testing
    Document doc = DocumentBuilder.newDocument();
    Element tableNode = doc.createElement("table");
    tableNode.setAttribute("remoteCatalog", "remoteCatalog");
    
    // Create a TableMeta instance and test the getRemoteCatalog method
    TableMeta tableMeta = new TableMeta(tableNode);
    
    assertEquals("remoteCatalog", tableMeta.getRemoteCatalog());
  }
  
  @Test
  public void testGetRemoteSchema() {
    // Create a tableNode for testing
    Document doc = DocumentBuilder.newDocument();
    Element tableNode = doc.createElement("table");
    tableNode.setAttribute("remoteSchema", "remoteSchema");
    
    // Create a TableMeta instance and test the getRemoteSchema method
    TableMeta tableMeta = new TableMeta(tableNode);
    
    assertEquals("remoteSchema", tableMeta.getRemoteSchema());
  }
  
}