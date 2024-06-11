import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader("<root><child></child></root>")));
        
        List<Node> children = helper.children(document.getDocumentElement());
        
        assertEquals(1, children.size());
        assertEquals("child", children.get(0).getNodeName());
    }

    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader("<root><child><grandchild></grandchild></child></root>")));
        
        Node found = helper.find(document, "root/child");
        
        assertNotNull(found);
        assertEquals("child", found.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader("<root><child><grandchild></grandchild></child></root>")));
        
        List<Node> children = helper.children(document.getDocumentElement());
        Node node = helper.nodeWithName(children, "child");
        
        assertNotNull(node);
        assertEquals("child", node.getNodeName());
    }

    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader("<root><child></child><child></child></root>")));
        
        List<Node> children = helper.children(document.getDocumentElement());
        List<Node> sublist = helper.sublistWithName(children, "child");
        
        assertEquals(2, sublist.size());
        for (Node node : sublist) {
            assertEquals("child", node.getNodeName());
        }
    }
}