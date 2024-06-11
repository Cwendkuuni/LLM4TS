import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import java.util.List;
import java.util.ArrayList;

public class TableMetaTest {

    @Test
    public void testTableMetaConstructor() {
        Document doc = createDocument();
        Element tableNode = doc.createElement("table");
        
        NamedNodeMap attribs = doc.createAttribute("name");
        attribs.setNodeValue("Table1");
        tableNode.setAttributes(attribs);
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("Table1", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertEquals(0, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testGetComments() {
        Document doc = createDocument();
        Element tableNode = doc.createElement("table");
        
        NamedNodeMap attribs = doc.createAttribute("name");
        attribs.setNodeValue("Table1");
        tableNode.setAttributes(attribs);
        
        attribs = doc.createAttribute("comments");
        attribs.setNodeValue("This is a table");
        tableNode.setAttributes(attribs);
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("Table1", tableMeta.getName());
        assertEquals("This is a table", tableMeta.getComments());
        assertEquals(0, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testGetColumns() {
        Document doc = createDocument();
        Element tableNode = doc.createElement("table");
        
        NamedNodeMap attribs = doc.createAttribute("name");
        attribs.setNodeValue("Table1");
        tableNode.setAttributes(attribs);
        
        Element columnNode = doc.createElement("column");
        
        attribs = doc.createAttribute("name");
        attribs.setNodeValue("Column1");
        columnNode.setAttributes(attribs);
        
        tableNode.appendChild(columnNode);
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("Table1", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertEquals(1, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
        
        TableColumnMeta columnMeta = tableMeta.getColumns().get(0);
        
        assertEquals("Column1", columnMeta.getName());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        Document doc = createDocument();
        Element tableNode = doc.createElement("table");
        
        NamedNodeMap attribs = doc.createAttribute("name");
        attribs.setNodeValue("Table1");
        tableNode.setAttributes(attribs);
        
        attribs = doc.createAttribute("remoteCatalog");
        attribs.setNodeValue("Catalog1");
        tableNode.setAttributes(attribs);
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("Table1", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertEquals(0, tableMeta.getColumns().size());
        assertEquals("Catalog1", tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testGetRemoteSchema() {
        Document doc = createDocument();
        Element tableNode = doc.createElement("table");
        
        NamedNodeMap attribs = doc.createAttribute("name");
        attribs.setNodeValue("Table1");
        tableNode.setAttributes(attribs);
        
        attribs = doc.createAttribute("remoteSchema");
        attribs.setNodeValue("Schema1");
        tableNode.setAttributes(attribs);
        
        TableMeta tableMeta = new TableMeta(tableNode);
        
        assertEquals("Table1", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertEquals(0, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertEquals("Schema1", tableMeta.getRemoteSchema());
    }
    
    // Helper method to create a dummy Document
    private Document createDocument() {
        return null;
    }
}