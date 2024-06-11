package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {
    
    private Node tableNode;
    private NamedNodeMap attribs;
    
    @Before
    public void setup() {
        tableNode = mock(Node.class);
        attribs = mock(NamedNodeMap.class);
        when(tableNode.getAttributes()).thenReturn(attribs);
    }
    
    @Test
    public void testConstructor() {
        when(attribs.getNamedItem("name")).thenReturn(createNode("name","testTable"));
        when(attribs.getNamedItem("comments")).thenReturn(createNode("comments","testComments"));
        when(attribs.getNamedItem("remoteSchema")).thenReturn(createNode("remoteSchema","testRemoteSchema"));
        when(attribs.getNamedItem("remoteCatalog")).thenReturn(createNode("remoteCatalog","testRemoteCatalog"));

        NodeList columnNodes = mock(NodeList.class);
        Element element = mock(Element.class);
        when(tableNode.getChildNodes()).thenReturn(element);
        when(element.getElementsByTagName("column")).thenReturn(columnNodes);
        when(columnNodes.getLength()).thenReturn(2);

        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("testTable", tableMeta.getName());
        assertEquals("testComments", tableMeta.getComments());
        assertEquals("testRemoteSchema", tableMeta.getRemoteSchema());
        assertEquals("testRemoteCatalog", tableMeta.getRemoteCatalog());
        assertEquals(2, tableMeta.getColumns().size());
    }
    
    @Test
    public void testConstructorWithNoComments() {
        when(attribs.getNamedItem("name")).thenReturn(createNode("name","testTable"));
        when(attribs.getNamedItem("comments")).thenReturn(null);
        when(attribs.getNamedItem("remarks")).thenReturn(null);
        when(attribs.getNamedItem("remoteSchema")).thenReturn(createNode("remoteSchema","testRemoteSchema"));
        when(attribs.getNamedItem("remoteCatalog")).thenReturn(createNode("remoteCatalog","testRemoteCatalog"));

        NodeList columnNodes = mock(NodeList.class);
        Element element = mock(Element.class);
        when(tableNode.getChildNodes()).thenReturn(element);
        when(element.getElementsByTagName("column")).thenReturn(columnNodes);
        when(columnNodes.getLength()).thenReturn(2);

        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("testTable", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertEquals("testRemoteSchema", tableMeta.getRemoteSchema());
        assertEquals("testRemoteCatalog", tableMeta.getRemoteCatalog());
        assertEquals(2, tableMeta.getColumns().size());
    }
    
    @Test
    public void testGetName() {
        when(attribs.getNamedItem("name")).thenReturn(createNode("name","testTable"));
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("testTable", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        when(attribs.getNamedItem("comments")).thenReturn(createNode("comments","testComments"));
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("testComments", tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        NodeList columnNodes = mock(NodeList.class);
        Element element = mock(Element.class);
        when(tableNode.getChildNodes()).thenReturn(element);
        when(element.getElementsByTagName("column")).thenReturn(columnNodes);
        when(columnNodes.getLength()).thenReturn(2);
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals(2, tableMeta.getColumns().size());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        when(attribs.getNamedItem("remoteCatalog")).thenReturn(createNode("remoteCatalog","testRemoteCatalog"));
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("testRemoteCatalog", tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        when(attribs.getNamedItem("remoteSchema")).thenReturn(createNode("remoteSchema","testRemoteSchema"));
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("testRemoteSchema", tableMeta.getRemoteSchema());
    }
    
    private Node createNode(String name, String value) {
        Node node = mock(Node.class);
        when(node.getNodeName()).thenReturn(name);
        when(node.getNodeValue()).thenReturn(value);
        return node;
    }
}