import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    private OperationsHelperImpl operationsHelper;
    private Document document;
    private Element rootElement;
    private Element childElement1;
    private Element childElement2;
    private Element grandchildElement;
    
    @Before
    public void setup() {
        operationsHelper = new OperationsHelperImpl();
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        rootElement = document.createElement("root");
        document.appendChild(rootElement);
        childElement1 = document.createElement("child1");
        rootElement.appendChild(childElement1);
        childElement2 = document.createElement("child2");
        rootElement.appendChild(childElement2);
        grandchildElement = document.createElement("grandchild");
        childElement1.appendChild(grandchildElement);
    }
    
    @Test
    public void testChildren_typical() {
        List<Node> children = operationsHelper.children(rootElement);
        assertEquals(2, children.size());
        assertEquals(childElement1, children.get(0));
        assertEquals(childElement2, children.get(1));
    }
    
    @Test
    public void testChildren_noChildren() {
        List<Node> children = operationsHelper.children(childElement1);
        assertTrue(children.isEmpty());
    }
    
    @Test
    public void testFind_typical() {
        Node found = operationsHelper.find(document, "root/child1");
        assertEquals(childElement1, found);
    }
    
    @Test
    public void testFind_invalidPath() {
        Node found = operationsHelper.find(document, "root/child3");
        assertNull(found);
    }
    
    @Test
    public void testFind_emptyDocument() {
        Document emptyDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Node found = operationsHelper.find(emptyDocument, "root");
        assertNull(found);
    }
    
    @Test
    public void testNodeWithName_typical() {
        List<Node> nodes = Arrays.asList(childElement1, childElement2);
        Node node = operationsHelper.nodeWithName(nodes, "child1");
        assertEquals(childElement1, node);
    }
    
    @Test
    public void testNodeWithName_nodeNotFound() {
        List<Node> nodes = Arrays.asList(childElement1, childElement2);
        Node node = operationsHelper.nodeWithName(nodes, "child3");
        assertNull(node);
    }
    
    @Test
    public void testSublistWithName_typical() {
        List<Node> nodes = Arrays.asList(childElement1, grandchildElement, childElement2);
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child1");
        assertEquals(1, sublist.size());
        assertEquals(childElement1, sublist.get(0));
    }
    
    @Test
    public void testSublistWithName_noNodesWithGivenName() {
        List<Node> nodes = Arrays.asList(childElement1, grandchildElement, childElement2);
        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child3");
        assertTrue(sublist.isEmpty());
    }
}