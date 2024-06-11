package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.logging.*;
import java.util.*;
import org.junit.Test;
import org.w3c.dom.*;

public class TableMetaTest {

    @Test
    public void testName() {
        Node tableNode = mock(Node.class);
        NamedNodeMap attribs = mock(NamedNodeMap.class);
        when(tableNode.getAttributes()).thenReturn(attribs);
        Node nameNode = mock(Node.class);
        when(attribs.getNamedItem("name")).thenReturn(nameNode);
        when(nameNode.getNodeValue()).thenReturn("test");
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("test", tableMeta.getName());
    }
    
    @Test
    public void testComments() {
        Node tableNode = mock(Node.class);
        NamedNodeMap attribs = mock(NamedNodeMap.class);
        when(tableNode.getAttributes()).thenReturn(attribs);
        Node commentsNode = mock(Node.class);
        when(attribs.getNamedItem("comments")).thenReturn(commentsNode);
        when(commentsNode.getNodeValue()).thenReturn("test comments");
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("test comments", tableMeta.getComments());
    }
    
    @Test
    public void testColumns() {
        Node tableNode = mock(Node.class);
        Element element = mock(Element.class);
        when(tableNode.getChildNodes()).thenReturn(element);
        NodeList columnNodes = mock(NodeList.class);
        when(element.getElementsByTagName("column")).thenReturn(columnNodes);
        when(columnNodes.getLength()).thenReturn(2);
        Node colNode1 = mock(Node.class);
        Node colNode2 = mock(Node.class);
        when(columnNodes.item(0)).thenReturn(colNode1);
        when(columnNodes.item(1)).thenReturn(colNode2);
        
        TableColumnMeta columnMeta1 = mock(TableColumnMeta.class);
        TableColumnMeta columnMeta2 = mock(TableColumnMeta.class);
        whenNew(TableColumnMeta.class).withArguments(colNode1).thenReturn(columnMeta1);
        whenNew(TableColumnMeta.class).withArguments(colNode2).thenReturn(columnMeta2);
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(2, columns.size());
        assertEquals(columnMeta1, columns.get(0));
        assertEquals(columnMeta2, columns.get(1));
    }
    
    @Test
    public void testRemoteCatalog() {
        Node tableNode = mock(Node.class);
        NamedNodeMap attribs = mock(NamedNodeMap.class);
        when(tableNode.getAttributes()).thenReturn(attribs);
        Node remoteCatalogNode = mock(Node.class);
        when(attribs.getNamedItem("remoteCatalog")).thenReturn(remoteCatalogNode);
        when(remoteCatalogNode.getNodeValue()).thenReturn("test catalog");
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("test catalog", tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testRemoteSchema() {
        Node tableNode = mock(Node.class);
        NamedNodeMap attribs = mock(NamedNodeMap.class);
        when(tableNode.getAttributes()).thenReturn(attribs);
        Node remoteSchemaNode = mock(Node.class);
        when(attribs.getNamedItem("remoteSchema")).thenReturn(remoteSchemaNode);
        when(remoteSchemaNode.getNodeValue()).thenReturn("test schema");
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("test schema", tableMeta.getRemoteSchema());
    }
}