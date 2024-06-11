package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {
    @Test
    public void testGetName() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("testTable", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> expectedColumns = new ArrayList<>();
        expectedColumns.add(new TableColumnMeta(createColumnNode("column1")));
        expectedColumns.add(new TableColumnMeta(createColumnNode("column2")));
        assertEquals(expectedColumns, tableMeta.getColumns());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getRemoteSchema());
    }
    
    private Node createTableNode() {
        Document doc = createDocument();
        Element tableElement = doc.createElement("table");
        tableElement.setAttribute("name", "testTable");
        
        Element columnElement1 = doc.createElement("column");
        columnElement1.setAttribute("name", "column1");
        tableElement.appendChild(columnElement1);
        
        Element columnElement2 = doc.createElement("column");
        columnElement2.setAttribute("name", "column2");
        tableElement.appendChild(columnElement2);
        
        return tableElement;
    }
    
    private Node createColumnNode(String columnName) {
        Document doc = createDocument();
        Element columnElement = doc.createElement("column");
        columnElement.setAttribute("name", columnName);
        return columnElement;
    }
    
    private Document createDocument() {
        // create document and return
    }
}