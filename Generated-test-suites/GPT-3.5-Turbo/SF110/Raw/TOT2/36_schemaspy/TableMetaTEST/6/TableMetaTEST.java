
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class TableMetaTest {

    @Test
    public void testGetName() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.name = "TestName";
        assertEquals("TestName", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.comments = "TestComments";
        assertEquals("TestComments", tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        TableMeta tableMeta = new TableMeta(null);
        List<TableColumnMeta> columns = new ArrayList<TableColumnMeta>();
        TableColumnMeta column1 = new TableColumnMeta(null);
        columns.add(column1);
        tableMeta.columns = columns;
        assertEquals(columns, tableMeta.getColumns());
    }

    @Test
    public void testGetRemoteCatalog() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.remoteCatalog = "TestCatalog";
        assertEquals("TestCatalog", tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.remoteSchema = "TestSchema";
        assertEquals("TestSchema", tableMeta.getRemoteSchema());
    }
}
