
package net.sourceforge.schemaspy.model.xml;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class TableMetaTest {

    private TableMeta tableMeta;

    @Before
    public void setUp() {
        NodeList columnNodes = new Element("table").getElementsByTagName("column");
        tableMeta = new TableMeta(columnNodes.item(0));
    }

    @Test
    public void testGetName() {
        assertEquals("table", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        assertNull(tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertNotNull(columns);
        assertEquals(0, columns.size());
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
