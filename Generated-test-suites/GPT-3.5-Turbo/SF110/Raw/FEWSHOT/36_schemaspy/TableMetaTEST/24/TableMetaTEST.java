package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class TableMetaTest {
    private Document document;
    private Element tableNode;

    @Before
    public void setUp() {
        // Set up the XML document
        document = DocumentHelper.createDocument();
        Element root = document.createElement("root");
        tableNode = document.createElement("table");
        tableNode.setAttribute("name", "table1");
        tableNode.setAttribute("comments", "Test Comments");
        tableNode.setAttribute("remoteCatalog", "remoteCatalog");
        tableNode.setAttribute("remoteSchema", "remoteSchema");

        Element column1 = document.createElement("column");
        column1.setAttribute("name", "column1");

        Element column2 = document.createElement("column");
        column2.setAttribute("name", "column2");

        tableNode.appendChild(column1);
        tableNode.appendChild(column2);

        root.appendChild(tableNode);
        document.appendChild(root);
    }

    @Test
    public void testConstructor() {
        TableMeta tableMeta = new TableMeta(tableNode);

        assertEquals("table1", tableMeta.getName());
        assertEquals("Test Comments", tableMeta.getComments());
        assertEquals("remoteCatalog", tableMeta.getRemoteCatalog());
        assertEquals("remoteSchema", tableMeta.getRemoteSchema());

        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertNotNull(columns);
        assertEquals(2, columns.size());
        assertEquals("column1", columns.get(0).getName());
        assertEquals("column2", columns.get(1).getName());
    }
}