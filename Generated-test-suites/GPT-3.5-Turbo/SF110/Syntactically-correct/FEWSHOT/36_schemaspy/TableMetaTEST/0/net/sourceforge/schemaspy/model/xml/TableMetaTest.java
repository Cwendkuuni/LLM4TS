package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.List;

public class TableMetaTest {

    @Test
    public void testConstructor() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);

        assertEquals("table1", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertNotNull(tableMeta.getColumns());
        assertEquals(2, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }

    @Test
    public void testGetName() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);

        assertEquals("table1", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getComments());

        tableNode = createTableNodeWithComments();
        tableMeta = new TableMeta(tableNode);
        assertEquals("Table comment", tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);

        assertNotNull(tableMeta.getColumns());
        assertEquals(2, tableMeta.getColumns().size());
    }

    @Test
    public void testGetRemoteCatalog() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getRemoteCatalog());

        tableNode = createTableNodeWithRemoteCatalog();
        tableMeta = new TableMeta(tableNode);
        assertEquals("remoteCatalog", tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getRemoteSchema());

        tableNode = createTableNodeWithRemoteSchema();
        tableMeta = new TableMeta(tableNode);
        assertEquals("remoteSchema", tableMeta.getRemoteSchema());
    }

    private Node createTableNode() {
        // create table XML node without optional attributes
        return null;
    }

    private Node createTableNodeWithComments() {
        // create table XML node with comments attribute
        return null;
    }

    private Node createTableNodeWithRemoteCatalog() {
        // create table XML node with remoteCatalog attribute
        return null;
    }

    private Node createTableNodeWithRemoteSchema() {
        // create table XML node with remoteSchema attribute
        return null;
    }
}