import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TableMetaTest {

    @Test
    public void testGetName() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        String expectedName = "table_name";
        assertEquals(expectedName, tableMeta.getName());
    }

    @Test
    public void testGetComments() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        String expectedComments = "table_comments";
        assertEquals(expectedComments, tableMeta.getComments());
    }

    @Test
    public void testGetColumns() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals(2, tableMeta.getColumns().size());
    }

    @Test
    public void testGetRemoteCatalog() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        String expectedRemoteCatalog = "remote_catalog";
        assertEquals(expectedRemoteCatalog, tableMeta.getRemoteCatalog());
    }

    @Test
    public void testGetRemoteSchema() {
        Node tableNode = createTableNode();
        TableMeta tableMeta = new TableMeta(tableNode);
        String expectedRemoteSchema = "remote_schema";
        assertEquals(expectedRemoteSchema, tableMeta.getRemoteSchema());
    }

    private Node createTableNode() {
        // Create a dummy table node for testing
        NamedNodeMap attributes = new NamedNodeMap();
        attributes.setNamedItem(new TestNodeItem("name", "table_name"));
        attributes.setNamedItem(new TestNodeItem("comments", "table_comments"));
        attributes.setNamedItem(new TestNodeItem("remoteCatalog", "remote_catalog"));
        attributes.setNamedItem(new TestNodeItem("remoteSchema", "remote_schema"));
        return new TestNode(attributes);
    }

    private class TestNode implements Node {
        private NamedNodeMap attributes;

        public TestNode(NamedNodeMap attributes) {
            this.attributes = attributes;
        }

        // Implement other Node methods as needed

        @Override
        public NamedNodeMap getAttributes() {
            return attributes;
        }
    }

    private class TestNodeItem implements Node {
        private String name;
        private String value;

        public TestNodeItem(String name, String value) {
            this.name = name;
            this.value = value;
        }

        // Implement other Node methods as needed

        @Override
        public String getNodeName() {
            return name;
        }

        @Override
        public String getNodeValue() {
            return value;
        }
    }

}