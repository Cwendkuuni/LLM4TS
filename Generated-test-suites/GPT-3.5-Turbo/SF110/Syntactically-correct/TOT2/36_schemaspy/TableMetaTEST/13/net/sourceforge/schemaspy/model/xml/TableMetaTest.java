package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta tableMeta = new TableMeta(null); // Creating a table meta object
        tableMeta.getName();
        
        assertNotNull(tableMeta.getName()); // Checking if name is not null
    }

    @Test
    public void testGetComments() {
        TableMeta tableMeta = new TableMeta(null); // Creating a table meta object
        tableMeta.getComments();
        
        assertNotNull(tableMeta.getComments()); // Checking if comments is not null
    }

    @Test
    public void testGetColumns() {
        TableMeta tableMeta = new TableMeta(null); // Creating a table meta object
        List<TableColumnMeta> columns = tableMeta.getColumns();
        
        assertNotNull(columns); // Checking if columns list is not null
        assertEquals(0, columns.size()); // Ensuring the size of the columns list is 0
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta tableMeta = new TableMeta(null); // Creating a table meta object
        tableMeta.getRemoteCatalog();
        
        assertNotNull(tableMeta.getRemoteCatalog()); // Checking if remoteCatalog is not null
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta tableMeta = new TableMeta(null); // Creating a table meta object
        tableMeta.getRemoteSchema();
        
        assertNotNull(tableMeta.getRemoteSchema()); // Checking if remoteSchema is not null
    }
}