import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TableMetaTest {
    private TableMeta tableMeta;

    @Before
    public void setUp() {
        Element tableNode = createTableNode();
        tableMeta = new TableMeta(tableNode);
    }

    @Test
    public void testGetName() {
        String expected = "table1";
        String actual = tableMeta.getName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetComments() {
        String expected = "This is a table";
        String actual = tableMeta.getComments();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetColumns() {
        int expectedSize = 3;
        int actualSize = tableMeta.getColumns().size();
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testGetRemoteCatalog() {
        String expected = "catalog";
        String actual = tableMeta.getRemoteCatalog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetRemoteSchema() {
        String expected = "schema";
        String actual = tableMeta.getRemoteSchema();
        assertEquals(expected, actual);
    }

    private Element createTableNode() {
        Document document = createDocument();
        Element tableNode = document.createElement("table");
        NamedNodeMap attribs = document.createAttribute("name");
        attribs.getNamedItem("name").setNodeValue("table1");
        attribs.getNamedItem("comments").setNodeValue("This is a table");
        attribs.getNamedItem("remoteCatalog").setNodeValue("catalog");
        attribs.getNamedItem("remoteSchema").setNodeValue("schema");
        tableNode.setAttributes(attribs);

        Element columnNode1 = document.createElement("column");
        tableNode.appendChild(columnNode1);

        Element columnNode2 = document.createElement("column");
        tableNode.appendChild(columnNode2);

        Element columnNode3 = document.createElement("column");
        tableNode.appendChild(columnNode3);

        return tableNode;
    }

    private Document createDocument() {
        // Code to create a new XML document
    }
}