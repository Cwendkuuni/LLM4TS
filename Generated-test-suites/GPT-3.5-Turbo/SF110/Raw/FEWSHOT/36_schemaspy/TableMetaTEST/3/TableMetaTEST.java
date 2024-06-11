
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TableMetaTest {

    private TableMeta tableMeta;
    
    @Before
    public void setUp() {
        Document document = mock(Document.class);
        tableMeta = new TableMeta(document);
    }
    
    @Test
    public void testGetName() {
        assertEquals("", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        assertNull(tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        assertEquals(new ArrayList<TableColumnMeta>(), tableMeta.getColumns());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        assertNull(tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        assertNull(tableMeta.getRemoteSchema());
    }
}
