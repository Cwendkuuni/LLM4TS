package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import static org.junit.Assert.*;

public class TableMetaTest {
    
    @Test
    public void testConstructor() {
        // Test case 1: No column nodes
        Node tableNode1 = new Node("table");
        TableMeta tableMeta1 = new TableMeta(tableNode1);
        assertNotNull(tableMeta1);
        assertEquals("", tableMeta1.getName());
        assertNull(tableMeta1.getComments());
        assertEquals("", tableMeta1.getRemoteCatalog());
        assertEquals("", tableMeta1.getRemoteSchema());
        assertEquals(0, tableMeta1.getColumns().size());
        
        // Test case 2: With column nodes
        Node tableNode2 = new Node("table");
        NamedNodeMap attribs2 = tableNode2.getAttributes();
        attribs2.setNamedItem(new Node("name", "table1"));
        attribs2.setNamedItem(new Node("comments", "Test table"));
        attribs2.setNamedItem(new Node("remoteSchema", "schema1"));
        attribs2.setNamedItem(new Node("remoteCatalog", "catalog1"));
        Element column1 = new Element("column");
        Element column2 = new Element("column");
        Element column3 = new Element("column");
        NodeList columnNodes = new NodeList(column1, column2, column3);
        tableNode2.setChildNodes(columnNodes);
        TableMeta tableMeta2 = new TableMeta(tableNode2);
        assertNotNull(tableMeta2);
        assertEquals("table1", tableMeta2.getName());
        assertEquals("Test table", tableMeta2.getComments());
        assertEquals("schema1", tableMeta2.getRemoteSchema());
        assertEquals("catalog1", tableMeta2.getRemoteCatalog());
        assertEquals(3, tableMeta2.getColumns().size());
    }

    @Test
    public void testGetName() {
        Node tableNode = new Node("table");
        NamedNodeMap attribs = tableNode.getAttributes();
        attribs.setNamedItem(new Node("name", "table1"));
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("table1", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        Node tableNode = new Node("table");
        NamedNodeMap attribs = tableNode.getAttributes();
        attribs.setNamedItem(new Node("comments", "Test table"));
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("Test table", tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        Node tableNode = new Node("table");
        Element column1 = new Element("column");
        Element column2 = new Element("column");
        Element column3 = new Element("column");
        NodeList columnNodes = new NodeList(column1, column2, column3);
        tableNode.setChildNodes(columnNodes);
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals(3, tableMeta.getColumns().size());
    }

    @Test
    public void testGetRemoteCatalog() {
        Node tableNode = new Node("table");
        NamedNodeMap attribs = tableNode.getAttributes();
        attribs.setNamedItem(new Node("remoteCatalog", "catalog1"));
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("catalog1", tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        Node tableNode = new Node("table");
        NamedNodeMap attribs = tableNode.getAttributes();
        attribs.setNamedItem(new Node("remoteSchema", "schema1"));
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("schema1", tableMeta.getRemoteSchema());
    }
}