package net.sourceforge.schemaspy.model.xml;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

public class TableMetaTest {
    
    private TableMeta tableMeta;
    
    @Before
    public void setUp() {
        Element tableNode = createTableNode();
        tableMeta = new TableMeta(tableNode);
    }
    
    @Test
    public void testGetName() {
        String name = tableMeta.getName();
        assertEquals("Table1", name);
    }
    
    @Test
    public void testGetComments() {
        String comments = tableMeta.getComments();
        assertNull(comments);
    }
    
    @Test
    public void testGetColumns() {
        List<TableColumnMeta> columns = tableMeta.getColumns();
        assertEquals(2, columns.size());
    }
    
    @Test
    public void testGetRemoteCatalog() {
        String remoteCatalog = tableMeta.getRemoteCatalog();
        assertNull(remoteCatalog);
    }
    
    @Test
    public void testGetRemoteSchema() {
        String remoteSchema = tableMeta.getRemoteSchema();
        assertNull(remoteSchema);
    }
    
    private Element createTableNode() {
        Element tableNode = new Element() {
            @Override
            public NodeList getElementsByTagName(String name) {
                NodeList nodeList = new NodeList() {
                    @Override
                    public Node item(int index) {
                        return null;
                    }
                    
                    @Override
                    public int getLength() {
                        return 2;
                    }
                };
                return nodeList;
            }
            
            @Override
            public NamedNodeMap getAttributes() {
                NamedNodeMap attribs = new NamedNodeMap() {
                    @Override
                    public Node getNamedItem(String name) {
                        if (name.equals("name")) {
                            Node node = new Node() {
                                @Override
                                public String getNodeName() {
                                    return "name";
                                }
                                
                                @Override
                                public String getNodeValue() {
                                    return "Table1";
                                }
                                
                                // Implement other methods
                            };
                            return node;
                        } else if (name.equals("comments")) {
                            return null;
                        } else if (name.equals("remarks")) {
                            return null;
                        } else if (name.equals("remoteSchema")) {
                            return null;
                        } else if (name.equals("remoteCatalog")) {
                            return null;
                        }
                        return null;
                    }
                    
                    // Implement other methods
                };
                
                return attribs;
            }
            
            // Implement other methods
        };
        
        return tableNode;
    }
}