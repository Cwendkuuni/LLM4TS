import static org.junit.Assert.*;
import org.junit.Test;
import java.util.logging.*;
import java.util.*;
import org.w3c.dom.*;

public class TableMetaTest {
    
    @Test
    public void testGetName() {
        Node tableNode = new Node();
        tableNode.getAttributes().setNamedItem("name", "testName");
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("testName", tableMeta.getName());
    }
    
    @Test
    public void testGetComments() {
        Node tableNode = new Node();
        tableNode.getAttributes().setNamedItem("comments", "testComments");
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("testComments", tableMeta.getComments());
    }
    
    @Test
    public void testGetColumns() {
        Node tableNode = new Node();
        Element columnElement1 = new Element();
        Element columnElement2 = new Element();
        Element columnElement3 = new Element();
        NodeList columnNodes = new NodeList();
        columnNodes.item(i) = columnElement1;
        columnNodes.item(i+1) = columnElement2;
        columnNodes.item(i+2) = columnElement3;
        tableNode.getChildNodes().appendChild(columnNodes);
        TableMeta tableMeta = new TableMeta(tableNode);
        List<TableColumnMeta> expectedColumns = new ArrayList<TableColumnMeta>();
        expectedColumns.add(new TableColumnMeta(columnElement1));
        expectedColumns.add(new TableColumnMeta(columnElement2));
        expectedColumns.add(new TableColumnMeta(columnElement3));
        assertEquals(expectedColumns, tableMeta.getColumns());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        Node tableNode = new Node();
        tableNode.getAttributes().setNamedItem("remoteCatalog", "testRemoteCatalog");
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("testRemoteCatalog", tableMeta.getRemoteCatalog());
    }
    
    @Test
    public void testGetRemoteSchema() {
        Node tableNode = new Node();
        tableNode.getAttributes().setNamedItem("remoteSchema", "testRemoteSchema");
        TableMeta tableMeta = new TableMeta(tableNode);
        assertEquals("testRemoteSchema", tableMeta.getRemoteSchema());
    }
}