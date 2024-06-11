package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class TableMetaTest {

    @Test
    public void testGetName() {
        //typical use case
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        assertEquals(null,tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        //typical use case
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        assertEquals(null,tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        //typical use case
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        assertNotNull(tableMeta.getColumns());
    }

    @Test
    public void testGetRemoteCatalog() {
         //typical use case
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        assertEquals(null,tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        //typical use case
        TableMeta tableMeta = new TableMeta(null);
        tableMeta = new TableMeta(null);
        assertEquals(null, tableMeta.getRemoteSchema());
    }

}