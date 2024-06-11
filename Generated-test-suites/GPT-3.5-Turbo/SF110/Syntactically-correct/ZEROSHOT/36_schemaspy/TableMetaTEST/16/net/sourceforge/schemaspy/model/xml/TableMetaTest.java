
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;
import java.util.logging.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class TableMetaTest {
    private TableMeta tableMeta;

    @Before
    public void setUp() {
        Logger logger = Logger.getLogger(TableMeta.class.getName());
        tableMeta = new TableMeta(getTableNode());
    }
    
    private Node getTableNode() {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element tableElement = document.createElement("table");
            Attr nameAttr = document.createAttribute("name");
            nameAttr.setValue("testTable");
            Attr commentsAttr = document.createAttribute("comments");
            commentsAttr.setValue("Test table comments");
            tableElement.setAttributeNode(nameAttr);
            tableElement.setAttributeNode(commentsAttr);
            return tableElement;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating tableNode", e);
        }
        return null;
    }

    @Test
    public void testGetName() {
        assertEquals("testTable", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        assertEquals("Test table comments", tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        List<TableColumnMeta> columns = tableMeta.getColumns();
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
