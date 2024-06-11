package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TableMetaTest {
    @Test
    public void testTableMetaConstructor() {
        // Test case 1: tableNode contains all attributes
        Node tableNode1 = createTableNodeWithAllAttributes();
        TableMeta tableMeta1 = new TableMeta(tableNode1);
        assertEquals("table1", tableMeta1.getName());
        assertEquals("comments1", tableMeta1.getComments());
        assertEquals("remoteCatalog1", tableMeta1.getRemoteCatalog());
        assertEquals("remoteSchema1", tableMeta1.getRemoteSchema());
        
        // Test case 2: tableNode does not contain comments attribute
        Node tableNode2 = createTableNodeWithoutCommentsAttribute();
        TableMeta tableMeta2 = new TableMeta(tableNode2);
        assertEquals("table2", tableMeta2.getName());
        assertNull(tableMeta2.getComments());
        assertEquals("remoteCatalog2", tableMeta2.getRemoteCatalog());
        assertEquals("remoteSchema2", tableMeta2.getRemoteSchema());
        
        // Test case 3: tableNode does not contain remoteSchema and remoteCatalog attributes
        Node tableNode3 = createTableNodeWithoutRemoteSchemaAndCatalogAttributes();
        TableMeta tableMeta3 = new TableMeta(tableNode3);
        assertEquals("table3", tableMeta3.getName());
        assertNull(tableMeta3.getRemoteCatalog());
        assertNull(tableMeta3.getRemoteSchema());
    }
    
    @Test
    public void testTableMetaGetters() {
        // Test case 1: tableMeta with comments and columns
        TableMeta tableMeta1 = createTableMetaWithCommentsAndColumns();
        assertEquals("table1", tableMeta1.getName());
        assertEquals("comments1", tableMeta1.getComments());
        assertEquals(2, tableMeta1.getColumns().size());
        assertEquals("column1", tableMeta1.getColumns().get(0).getName());
        assertEquals("column2", tableMeta1.getColumns().get(1).getName());
        assertEquals("remoteCatalog1", tableMeta1.getRemoteCatalog());
        assertEquals("remoteSchema1", tableMeta1.getRemoteSchema());
        
        // Test case 2: tableMeta without comments and columns
        TableMeta tableMeta2 = createTableMetaWithoutCommentsAndColumns();
        assertEquals("table2", tableMeta2.getName());
        assertNull(tableMeta2.getComments());
        assertEquals(0, tableMeta2.getColumns().size());
        assertEquals("remoteCatalog2", tableMeta2.getRemoteCatalog());
        assertEquals("remoteSchema2", tableMeta2.getRemoteSchema());
    }
    
    private Node createTableNodeWithAllAttributes() {
        // create table node with all attributes
        // <table name="table1" comments="comments1" remoteSchema="remoteSchema1" remoteCatalog="remoteCatalog1">...</table>
        Element tableElement = new Element("table");
        tableElement.setAttribute("name", "table1");
        tableElement.setAttribute("comments", "comments1");
        tableElement.setAttribute("remoteSchema", "remoteSchema1");
        tableElement.setAttribute("remoteCatalog", "remoteCatalog1");
        
        // add column nodes to table node
        Element columnElement1 = new Element("column");
        columnElement1.setAttribute("name", "column1");
        Element columnElement2 = new Element("column");
        columnElement2.setAttribute("name", "column2");
        tableElement.appendChild(columnElement1);
        tableElement.appendChild(columnElement2);
        
        return tableElement;
    }
    
    private Node createTableNodeWithoutCommentsAttribute() {
        // create table node without comments attribute
        // <table name="table2" remoteSchema="remoteSchema2" remoteCatalog="remoteCatalog2">...</table>
        Element tableElement = new Element("table");
        tableElement.setAttribute("name", "table2");
        tableElement.setAttribute("remoteSchema", "remoteSchema2");
        tableElement.setAttribute("remoteCatalog", "remoteCatalog2");
        
        // add column nodes to table node
        Element columnElement1 = new Element("column");
        columnElement1.setAttribute("name", "column1");
        Element columnElement2 = new Element("column");
        columnElement2.setAttribute("name", "column2");
        tableElement.appendChild(columnElement1);
        tableElement.appendChild(columnElement2);
        
        return tableElement;
    }
    
    private Node createTableNodeWithoutRemoteSchemaAndCatalogAttributes() {
        // create table node without remoteSchema and remoteCatalog attributes
        // <table name="table3">...</table>
        Element tableElement = new Element("table");
        tableElement.setAttribute("name", "table3");
        
        // add column nodes to table node
        Element columnElement1 = new Element("column");
        columnElement1.setAttribute("name", "column1");
        Element columnElement2 = new Element("column");
        columnElement2.setAttribute("name", "column2");
        tableElement.appendChild(columnElement1);
        tableElement.appendChild(columnElement2);
        
        return tableElement;
    }
    
    private TableMeta createTableMetaWithCommentsAndColumns() {
        // create table node with comments and columns
        Node tableNode = createTableNodeWithAllAttributes();
        
        // create TableMeta object
        TableMeta tableMeta = new TableMeta(tableNode);
        
        return tableMeta;
    }
    
    private TableMeta createTableMetaWithoutCommentsAndColumns() {
        // create table node without comments and columns
        Node tableNode = createTableNodeWithoutCommentsAttribute();
        
        // create TableMeta object
        TableMeta tableMeta = new TableMeta(tableNode);
        
        return tableMeta;
    }
}