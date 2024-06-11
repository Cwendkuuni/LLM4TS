
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TableMetaTest {
    
    private TableMeta tableMeta;
    private Node tableNode;
    
    @Before
    public void setUp() {
        // create mock tableNode for testing
        tableNode = createMockTableNode();
        tableMeta = new TableMeta(tableNode);
    }
    
    // Helper method to create mock tableNode
    private Node createMockTableNode() {
        Element element = Mockito.mock(Element.class);
        NamedNodeMap attributes = Mockito.mock(NamedNodeMap.class);
        Mockito.when(attributes.getNamedItem("name")).thenReturn(new MockNode("name", "TestTable"));
        Mockito.when(attributes.getNamedItem("comments")).thenReturn(new MockNode("comments", "Test comments"));
        Mockito.when(attributes.getNamedItem("remoteCatalog")).thenReturn(new MockNode("remoteCatalog", "TestCatalog"));
        Mockito.when(attributes.getNamedItem("remoteSchema")).thenReturn(new MockNode("remoteSchema", "TestSchema"));
        Mockito.when(element.getAttributes()).thenReturn(attributes);
        
        Element columnElement1 = Mockito.mock(Element.class);
        NamedNodeMap columnAttributes1 = Mockito.mock(NamedNodeMap.class);
        Mockito.when(columnAttributes1.getNamedItem("name")).thenReturn(new MockNode("name", "Column1"));
        Mockito.when(columnAttributes1.getNamedItem("comments")).thenReturn(new MockNode("comments", "Column 1 comments"));
        Mockito.when(columnElement1.getAttributes()).thenReturn(columnAttributes1);
        
        Element columnElement2 = Mockito.mock(Element.class);
        NamedNodeMap columnAttributes2 = Mockito.mock(NamedNodeMap.class);
        Mockito.when(columnAttributes2.getNamedItem("name")).thenReturn(new MockNode("name", "Column2"));
        Mockito.when(columnAttributes2.getNamedItem("comments")).thenReturn(new MockNode("comments", "Column 2 comments"));
        Mockito.when(columnElement2.getAttributes()).thenReturn(columnAttributes2);
        
        NodeList columnNodes = Mockito.mock(NodeList.class);
        Mockito.when(columnNodes.getLength()).thenReturn(2);
        Mockito.when(columnNodes.item(0)).thenReturn(columnElement1);
        Mockito.when(columnNodes.item(1)).thenReturn(columnElement2);
        
        Element tableNode = Mockito.mock(Element.class);
        Mockito.when(tableNode.getAttributes()).thenReturn(attributes);
        Mockito.when(tableNode.getChildNodes()).thenReturn(columnNodes);
        
        return tableNode;
    }
    
    // MockNode to create test nodes with attributes
    class MockNode implements Node {
        
        private String nodeName;
        private String nodeValue;
        
        public MockNode(String nodeName, String nodeValue) {
            this.nodeName = nodeName;
            this.nodeValue = nodeValue;
        }
        
        // Implement other methods of Node interface
        
        @Override
        public String getNodeName() {
            return nodeName;
        }
        
        @Override
        public String getNodeValue() {
            return nodeValue;
        }
        
        // ...
    }
    
    @Test
    public void testGetName() {
        assertEquals("TestTable", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        assertEquals("Test comments", tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(2, columns.size());
        assertEquals("Column1", columns.get(0).getName());
        assertEquals("Column 1 comments", columns.get(0).getComments());
        assertEquals("Column2", columns.get(1).getName());
        assertEquals("Column 2 comments", columns.get(1).getComments());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        assertEquals("TestCatalog", tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        assertEquals("TestSchema", tableMeta.getRemoteSchema());
    }
    
}
