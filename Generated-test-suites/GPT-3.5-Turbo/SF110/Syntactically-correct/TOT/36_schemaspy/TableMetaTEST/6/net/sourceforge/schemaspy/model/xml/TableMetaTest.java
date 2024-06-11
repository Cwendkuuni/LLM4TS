import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class TableMetaTest {

    @Test
    public void testTableMetaConstructor() {
        Node mockTableNode = createMockTableNode();
        
        TableMeta tableMeta = new TableMeta(mockTableNode);
        
        assertEquals("table", tableMeta.getName());
        assertEquals("test comments", tableMeta.getComments());
        assertEquals(2, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testTableMetaConstructor_NoComments() {
        Node mockTableNode = createMockTableNode_NoComments();
        
        TableMeta tableMeta = new TableMeta(mockTableNode);
        
        assertEquals("table", tableMeta.getName());
        assertNull(tableMeta.getComments());
        assertEquals(2, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testTableMetaConstructor_NoRemoteSchema() {
        Node mockTableNode = createMockTableNode_NoRemoteSchema();
        
        TableMeta tableMeta = new TableMeta(mockTableNode);
        
        assertEquals("table", tableMeta.getName());
        assertEquals("test comments", tableMeta.getComments());
        assertEquals(2, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testTableMetaConstructor_NoRemoteCatalog() {
        Node mockTableNode = createMockTableNode_NoRemoteCatalog();
        
        TableMeta tableMeta = new TableMeta(mockTableNode);
        
        assertEquals("table", tableMeta.getName());
        assertEquals("test comments", tableMeta.getComments());
        assertEquals(2, tableMeta.getColumns().size());
        assertNull(tableMeta.getRemoteCatalog());
        assertNull(tableMeta.getRemoteSchema());
    }
    
    @Test
    public void testGetName() {
        TableMeta tableMeta = createTableMeta();
        
        String name = tableMeta.getName();
        
        assertEquals("table", name);
    }
    
    @Test
    public void testGetComments() {
        TableMeta tableMeta = createTableMeta();
        
        String comments = tableMeta.getComments();
        
        assertEquals("test comments", comments);
    }
    
    @Test
    public void testGetColumns() {
        TableMeta tableMeta = createTableMeta();
        
        List<TableColumnMeta> columns = tableMeta.getColumns();
        
        assertEquals(2, columns.size());
        assertEquals("column1", columns.get(0).getName());
        assertEquals("column2", columns.get(1).getName());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        TableMeta tableMeta = createTableMeta();
        
        String remoteCatalog = tableMeta.getRemoteCatalog();
        
        assertNull(remoteCatalog);
    }
    
    @Test
    public void testGetRemoteSchema() {
        TableMeta tableMeta = createTableMeta();
        
        String remoteSchema = tableMeta.getRemoteSchema();
        
        assertNull(remoteSchema);
    }
    
    private Node createMockTableNode() {
        NamedNodeMap mockAttribs = createMockAttribs();
        Node mockTableNode = createMockNode(mockAttribs);
        return mockTableNode;
    }
    
    private Node createMockTableNode_NoComments() {
        NamedNodeMap mockAttribs = createMockAttribs_NoComments();
        Node mockTableNode = createMockNode(mockAttribs);
        return mockTableNode;
    }
    
    private Node createMockTableNode_NoRemoteSchema() {
        NamedNodeMap mockAttribs = createMockAttribs_NoRemoteSchema();
        Node mockTableNode = createMockNode(mockAttribs);
        return mockTableNode;
    }
    
    private Node createMockTableNode_NoRemoteCatalog() {
        NamedNodeMap mockAttribs = createMockAttribs_NoRemoteCatalog();
        Node mockTableNode = createMockNode(mockAttribs);
        return mockTableNode;
    }
    
    private NamedNodeMap createMockAttribs() {
        NamedNodeMap mockAttribs = new NamedNodeMapStub();
        mockAttribs.setNamedItem(createMockNameItem());
        mockAttribs.setNamedItem(createMockCommentsItem());
        mockAttribs.setNamedItem(createMockRemoteSchemaItem());
        mockAttribs.setNamedItem(createMockRemoteCatalogItem());
        return mockAttribs;
    }
    
    private NamedNodeMap createMockAttribs_NoComments() {
        NamedNodeMap mockAttribs = new NamedNodeMapStub();
        mockAttribs.setNamedItem(createMockNameItem());
        mockAttribs.setNamedItem(createMockRemoteSchemaItem());
        mockAttribs.setNamedItem(createMockRemoteCatalogItem());
        return mockAttribs;
    }
    
    private NamedNodeMap createMockAttribs_NoRemoteSchema() {
        NamedNodeMap mockAttribs = new NamedNodeMapStub();
        mockAttribs.setNamedItem(createMockNameItem());
        mockAttribs.setNamedItem(createMockCommentsItem());
        mockAttribs.setNamedItem(createMockRemoteCatalogItem());
        return mockAttribs;
    }
    
    private NamedNodeMap createMockAttribs_NoRemoteCatalog() {
        NamedNodeMap mockAttribs = new NamedNodeMapStub();
        mockAttribs.setNamedItem(createMockNameItem());
        mockAttribs.setNamedItem(createMockCommentsItem());
        mockAttribs.setNamedItem(createMockRemoteSchemaItem());
        return mockAttribs;
    }
    
    private Node createMockNode(NamedNodeMap attribs) {
        Node mockTableNode = new NodeStub();
        mockTableNode.setAttributes(attribs);
        mockTableNode.setChildNodes(createMockColumnNodes());
        return mockTableNode;
    }
    
    private NodeList createMockColumnNodes() {
        NodeListStub nodeList = new NodeListStub();
        nodeList.addItem(createMockColumnNode("column1"));
        nodeList.addItem(createMockColumnNode("column2"));
        return nodeList;
    }
    
    private Node createMockColumnNode(String name) {
        Node mockColumnNode = new NodeStub();
        NamedNodeMap mockAttribs = new NamedNodeMapStub();
        Node mockNameItem = new NodeStub();
        mockNameItem.setNodeValue(name);
        mockAttribs.setNamedItem(mockNameItem);
        mockColumnNode.setAttributes(mockAttribs);
        return mockColumnNode;
    }
    
    private Node createMockNameItem() {
        Node mockNameItem = new NodeStub();
        mockNameItem.setNodeValue("table");
        return mockNameItem;
    }
    
    private Node createMockCommentsItem() {
        Node mockCommentsItem = new NodeStub();
        mockCommentsItem.setNodeValue("test comments");
        return mockCommentsItem;
    }
    
    private Node createMockRemoteSchemaItem() {
        Node mockRemoteSchemaItem = new NodeStub();
        mockRemoteSchemaItem.setNodeValue("test remote schema");
        return mockRemoteSchemaItem;
    }
    
    private Node createMockRemoteCatalogItem() {
        Node mockRemoteCatalogItem = new NodeStub();
        mockRemoteCatalogItem.setNodeValue("test remote catalog");
        return mockRemoteCatalogItem;
    }
    
    private TableMeta createTableMeta() {
        Node mockTableNode = createMockTableNode();
        return new TableMeta(mockTableNode);
    }
}