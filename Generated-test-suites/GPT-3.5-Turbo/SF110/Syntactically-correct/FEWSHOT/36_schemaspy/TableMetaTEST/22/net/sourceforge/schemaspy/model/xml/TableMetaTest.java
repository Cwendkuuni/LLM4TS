
package net.sourceforge.schemaspy.model.xml;

import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TableMetaTest {

    @Test
    public void testGetName() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("testName", tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(2, columns.size());
    }

    @Test
    public void testGetRemoteCatalog() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertNull(tableMeta.getRemoteSchema());
    }

    private Node createTableNode() {
        // Create a mock table XML node for testing
        // Assumes a table with name "testName" and 2 column elements

        // Mock table node attributes
        NamedNodeMap attribs = new MockNamedNodeMap();
        MockNode nameNode = new MockNode("name");
        nameNode.setNodeValue("testName");
        attribs.setNamedItem(nameNode);

        // Mock table node
        MockNode tableNode = new MockNode("table");
        tableNode.setAttributes(attribs);

        // Mock column nodes
        MockElement column1 = new MockElement("column");
        MockElement column2 = new MockElement("column");

        // Add column nodes to tableNode child nodes
        tableNode.appendChild(column1);
        tableNode.appendChild(column2);

        return tableNode;
    }

    // Mock classes for testing
    private static class MockNamedNodeMap implements NamedNodeMap {
        // Mock implementation of NamedNodeMap
    }

    private static class MockNode implements Node {
        private String nodeName;
        private String nodeValue;

        MockNode(String nodeName) {
            this.nodeName = nodeName;
        }

        public void setNodeValue(String nodeValue) {
            this.nodeValue = nodeValue;
        }

        // Other overridden methods of Node interface
        // ...

    }

    private static class MockElement extends MockNode implements Element {
        // Mock implementation of Element
    }

}

