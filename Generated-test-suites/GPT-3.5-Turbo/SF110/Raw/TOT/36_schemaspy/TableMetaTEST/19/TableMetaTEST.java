import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

public class TableMetaTest {
  
  private TableMeta createTableMeta(Node tableNode){
    return new TableMeta(tableNode);
  }
  
  @Test
  public void testGetName() {
    Node tableNode = createMockNode();
    TableMeta tableMeta = createTableMeta(tableNode);
    
    String name = tableMeta.getName();
    
    assertNotNull(name);
    assertEquals("TestTable", name);
  }
  
  @Test
  public void testGetComments_WithComments() {
    Node tableNode = createMockNodeWithComments();
    TableMeta tableMeta = createTableMeta(tableNode);
    
    String comments = tableMeta.getComments();
    
    assertNotNull(comments);
    assertEquals("Test Comments", comments);
  }
  
  @Test
  public void testGetComments_WithoutComments() {
    Node tableNode = createMockNodeWithoutComments();
    TableMeta tableMeta = createTableMeta(tableNode);
    
    String comments = tableMeta.getComments();
    
    assertNull(comments);
  }
  
  @Test
  public void testGetColumns() {
    Node tableNode = createMockNode();
    TableMeta tableMeta = createTableMeta(tableNode);
    
    List<TableColumnMeta> columns = tableMeta.getColumns();
    
    assertNotNull(columns);
    assertEquals(2, columns.size());
  }
  
  @Test
  public void testGetRemoteCatalog_WithRemoteCatalog() {
    Node tableNode = createMockNodeWithRemoteCatalog();
    TableMeta tableMeta = createTableMeta(tableNode);
    
    String remoteCatalog = tableMeta.getRemoteCatalog();
    
    assertNotNull(remoteCatalog);
    assertEquals("TestCatalog", remoteCatalog);
  }
  
  @Test
  public void testGetRemoteCatalog_WithoutRemoteCatalog() {
    Node tableNode = createMockNodeWithoutRemoteCatalog();
    TableMeta tableMeta = createTableMeta(tableNode);
    
    String remoteCatalog = tableMeta.getRemoteCatalog();
    
    assertNull(remoteCatalog);
  }
  
  @Test
  public void testGetRemoteSchema_WithRemoteSchema() {
    Node tableNode = createMockNodeWithRemoteSchema();
    TableMeta tableMeta = createTableMeta(tableNode);
    
    String remoteSchema = tableMeta.getRemoteSchema();
    
    assertNotNull(remoteSchema);
    assertEquals("TestSchema", remoteSchema);
  }
  
  @Test
  public void testGetRemoteSchema_WithoutRemoteSchema() {
    Node tableNode = createMockNodeWithoutRemoteSchema();
    TableMeta tableMeta = createTableMeta(tableNode);
    
    String remoteSchema = tableMeta.getRemoteSchema();
    
    assertNull(remoteSchema);
  }
  
  private Node createMockNode(){
    Element tableElement = createMockElement();
    NamedNodeMap attribs = tableElement.getAttributes();
    
    attribs.setNamedItem(createMockAttribute("name", "TestTable"));
    attribs.setNamedItem(createMockAttribute("comments", "Test Comments"));
    attribs.setNamedItem(createMockAttribute("remoteSchema", "TestSchema"));
    attribs.setNamedItem(createMockAttribute("remoteCatalog", "TestCatalog"));
    
    NodeList columnNodes = tableElement.getElementsByTagName("column");
    for(int i = 0; i < 2; i++){
      Element columnElement = createMockElement();
      columnNodes.item(i).appendChild(columnElement);
    }
    
    return tableElement;
  }
  
  private Node createMockNodeWithComments(){
    Element tableElement = createMockElement();
    NamedNodeMap attribs = tableElement.getAttributes();
    
    attribs.setNamedItem(createMockAttribute("name", "TestTable"));
    attribs.setNamedItem(createMockAttribute("comments", "Test Comments"));
    attribs.setNamedItem(createMockAttribute("remoteSchema", "TestSchema"));
    attribs.setNamedItem(createMockAttribute("remoteCatalog", "TestCatalog"));
    
    NodeList columnNodes = tableElement.getElementsByTagName("column");
    for(int i = 0; i < 2; i++){
      Element columnElement = createMockElement();
      columnNodes.item(i).appendChild(columnElement);
    }
    
    return tableElement;
  }
  
  private Node createMockNodeWithoutComments(){
    Element tableElement = createMockElement();
    NamedNodeMap attribs = tableElement.getAttributes();
    
    attribs.setNamedItem(createMockAttribute("name", "TestTable"));
    attribs.setNamedItem(createMockAttribute("remoteSchema", "TestSchema"));
    attribs.setNamedItem(createMockAttribute("remoteCatalog", "TestCatalog"));
    
    NodeList columnNodes = tableElement.getElementsByTagName("column");
    for(int i = 0; i < 2; i++){
      Element columnElement = createMockElement();
      columnNodes.item(i).appendChild(columnElement);
    }
    
    return tableElement;
  }
  
  private Node createMockNodeWithRemoteCatalog(){
    Element tableElement = createMockElement();
    NamedNodeMap attribs = tableElement.getAttributes();
    
    attribs.setNamedItem(createMockAttribute("name", "TestTable"));
    attribs.setNamedItem(createMockAttribute("comments", "Test Comments"));
    attribs.setNamedItem(createMockAttribute("remoteSchema", "TestSchema"));
    
    NodeList columnNodes = tableElement.getElementsByTagName("column");
    for(int i = 0; i < 2; i++){
      Element columnElement = createMockElement();
      columnNodes.item(i).appendChild(columnElement);
    }
    
    return tableElement;
  }
  
  private Node createMockNodeWithoutRemoteCatalog(){
    Element tableElement = createMockElement();
    NamedNodeMap attribs = tableElement.getAttributes();
    
    attribs.setNamedItem(createMockAttribute("name", "TestTable"));
    attribs.setNamedItem(createMockAttribute("comments", "Test Comments"));
    attribs.setNamedItem(createMockAttribute("remoteSchema", "TestSchema"));
    
    NodeList columnNodes = tableElement.getElementsByTagName("column");
    for(int i = 0; i < 2; i++){
      Element columnElement = createMockElement();
      columnNodes.item(i).appendChild(columnElement);
    }
    
    return tableElement;
  }
  
  private Node createMockNodeWithRemoteSchema(){
    Element tableElement = createMockElement();
    NamedNodeMap attribs = tableElement.getAttributes();
    
    attribs.setNamedItem(createMockAttribute("name", "TestTable"));
    attribs.setNamedItem(createMockAttribute("comments", "Test Comments"));
    attribs.setNamedItem(createMockAttribute("remoteCatalog", "TestCatalog"));
    
    NodeList columnNodes = tableElement.getElementsByTagName("column");
    for(int i = 0; i < 2; i++){
      Element columnElement = createMockElement();
      columnNodes.item(i).appendChild(columnElement);
    }
    
    return tableElement;
  }
  
  private Node createMockNodeWithoutRemoteSchema(){
    Element tableElement = createMockElement();
    NamedNodeMap attribs = tableElement.getAttributes();
    
    attribs.setNamedItem(createMockAttribute("name", "TestTable"));
    attribs.setNamedItem(createMockAttribute("comments", "Test Comments"));
    attribs.setNamedItem(createMockAttribute("remoteCatalog", "TestCatalog"));
    
    NodeList columnNodes = tableElement.getElementsByTagName("column");
    for(int i = 0; i < 2; i++){
      Element columnElement = createMockElement();
      columnNodes.item(i).appendChild(columnElement);
    }
    
    return tableElement;
  }
  
  private Element createMockElement(){
    return new Element() {
      @Override
      public int getLength() {
        return 2;
      }
      
      @Override
      public Node item(int index) {
        return new Element() {
          
        };
      }
    };
  }
  
  private Node createMockAttribute(String name, String value){
    return new Node() {
      @Override
      public String getNodeName() {
        return name;
      }
      
      @Override
      public String getNodeValue() {
        return value;
      }
      
      // Other abstract methods are not needed for testing purposes
    };
  }
}