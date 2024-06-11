package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.name = "testName";
        
        assertEquals("testName", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.comments = "testComments";
        
        assertEquals("testComments", tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        TableMeta tableMeta = new TableMeta(null);
        List<TableColumnMeta> columns = new ArrayList<>();
        columns.add(new TableColumnMeta(null));
        tableMeta.columns = columns;
        
        assertEquals(columns, tableMeta.getColumns());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.remoteCatalog = "testCatalog";
        
        assertEquals("testCatalog", tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta tableMeta = new TableMeta(null);
        tableMeta.remoteSchema = "testSchema";
        
        assertEquals("testSchema", tableMeta.getRemoteSchema());
    }
}