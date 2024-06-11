import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {
  
  private TableMeta tableMeta;
  private Node mockNode;
  
  @Before
  public void setUp() {
    mockNode = Mockito.mock(Node.class);
    NamedNodeMap mockAttributeMap = Mockito.mock(NamedNodeMap.class);
    Node mockNameNode = Mockito.mock(Node.class);
    Node mockCommentsNode = Mockito.mock(Node.class);
    Node mockRemoteSchemaNode = Mockito.mock(Node.class);
    Node mockRemoteCatalogNode = Mockito.mock(Node.class);
    Element mockChildElement = Mockito.mock(Element.class);
    NodeList mockColumnNodes = Mockito.mock(NodeList.class);
    
    Mockito.when(mockNode.getAttributes()).thenReturn(mockAttributeMap);
    Mockito.when(mockAttributeMap.getNamedItem("name")).thenReturn(mockNameNode);
    Mockito.when(mockAttributeMap.getNamedItem("comments")).thenReturn(mockCommentsNode);
    Mockito.when(mockAttributeMap.getNamedItem("remoteSchema")).thenReturn(mockRemoteSchemaNode);
    Mockito.when(mockAttributeMap.getNamedItem("remoteCatalog")).thenReturn(mockRemoteCatalogNode);
    Mockito.when(mockAttributeMap.getNamedItem("remarks")).thenReturn(null);
    Mockito.when(mockNameNode.getNodeValue()).thenReturn("TestTable");
    Mockito.when(mockCommentsNode.getNodeValue()).thenReturn("Table comments");
    Mockito.when(mockRemoteSchemaNode.getNodeValue()).thenReturn("TestSchema");
    Mockito.when(mockRemoteCatalogNode.getNodeValue()).thenReturn("TestCatalog");
    Mockito.when(mockNode.getChildNodes()).thenReturn(mockChildElement);
    Mockito.when(mockChildElement.getElementsByTagName("column")).thenReturn(mockColumnNodes);
    Mockito.when(mockColumnNodes.getLength()).thenReturn(3);
    
    List<TableColumnMeta> columns = new ArrayList<TableColumnMeta>();
    columns.add(new TableColumnMeta(mockNode));
    columns.add(new TableColumnMeta(mockNode));
    columns.add(new TableColumnMeta(mockNode));
    
    tableMeta = new TableMeta(mockNode);
  }
  
  @Test
  public void testGetName() {
    assertEquals("TestTable", tableMeta.getName());
  }
  
  @Test
  public void testGetComments() {
    assertEquals("Table comments", tableMeta.getComments());
  }
  
  @Test
  public void testGetComments_NullValue() {
    assertNull(tableMeta.getComments());
  }
  
  @Test
  public void testGetColumns() {
    List<TableColumnMeta> expected = new ArrayList<TableColumnMeta>();
    expected.add(new TableColumnMeta(mockNode));
    expected.add(new TableColumnMeta(mockNode));
    expected.add(new TableColumnMeta(mockNode));
    
    assertEquals(expected, tableMeta.getColumns());
  }
  
  @Test
  public void testGetRemoteCatalog() {
    assertEquals("TestCatalog", tableMeta.getRemoteCatalog());
  }
  
  @Test
  public void testGetRemoteCatalog_NullValue() {
    assertNull(tableMeta.getRemoteCatalog());
  }
  
  @Test
  public void testGetRemoteSchema() {
    assertEquals("TestSchema", tableMeta.getRemoteSchema());
  }
  
  @Test
  public void testGetRemoteSchema_NullValue() {
    assertNull(tableMeta.getRemoteSchema());
  }
  
}