
package net.sourceforge.schemaspy.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

    @Test
    public void testTableMetaConstructor() {
        // Create a sample table node
        Document document = null; // modify this line to create a sample document object
        Element tableNode = document.createElement("table");
        tableNode.setAttribute("name", "table1");
        tableNode.setAttribute("comments", "comments1");
        tableNode.setAttribute("remoteSchema", "remoteSchema1");
        tableNode.setAttribute("remoteCatalog", "remoteCatalog1");

        // Call the constructor
        TableMeta tableMeta = new TableMeta(tableNode);

        // Verify the values are initialized correctly
        assertEquals("table1", tableMeta.getName());
        assertEquals("comments1", tableMeta.getComments());
        assertEquals("remoteSchema1", tableMeta.getRemoteSchema());
        assertEquals("remoteCatalog1", tableMeta.getRemoteCatalog());
        assertNotNull(tableMeta.getColumns());
        assertEquals(0, tableMeta.getColumns().size());
    }

    @Test
    public void testGetName() {
        // Create a sample table node
        Document document = null; // modify this line to create a sample document object
        Element tableNode = document.createElement("table");
        tableNode.setAttribute("name", "table1");

        // Call the constructor
        TableMeta tableMeta = new TableMeta(tableNode);

        // Verify the name is returned correctly
        assertEquals("table1", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        // Create a sample table node
        Document document = null; // modify this line to create a sample document object
        Element tableNode = document.createElement("table");
        tableNode.setAttribute("comments", "comments1");

        // Call the constructor
        TableMeta tableMeta = new TableMeta(tableNode);

        // Verify the comments are returned correctly
        assertEquals("comments1", tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        // Create a sample table node with columns
        Document document = null; // modify this line to create a sample document object
        Element tableNode = document.createElement("table");
        NodeList columnNodes = document.getElementsByTagName("column");
        for (int i = 0; i < 3; i++) {
            Element columnNode = document.createElement("column");
            columnNodes.appendChild(columnNode);
        }
        tableNode.appendChild(columnNodes);

        // Call the constructor
        TableMeta tableMeta = new TableMeta(tableNode);

        // Verify the columns are returned correctly
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(3, columns.size());
        for (TableColumnMeta column : columns) {
            assertNotNull(column);
        }
    }

    @Test
    public void testGetRemoteCatalog() {
        // Create a sample table node
        Document document = null; // modify this line to create a sample document object
        Element tableNode = document.createElement("table");
        tableNode.setAttribute("remoteCatalog", "remoteCatalog1");

        // Call the constructor
        TableMeta tableMeta = new TableMeta(tableNode);

        // Verify the remote catalog is returned correctly
        assertEquals("remoteCatalog1", tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        // Create a sample table node
        Document document = null; // modify this line to create a sample document object
        Element tableNode = document.createElement("table");
        tableNode.setAttribute("remoteSchema", "remoteSchema1");

        // Call the constructor
        TableMeta tableMeta = new TableMeta(tableNode);

        // Verify the remote schema is returned correctly
        assertEquals("remoteSchema1", tableMeta.getRemoteSchema());
    }

}
