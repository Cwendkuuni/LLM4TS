
import org.junit.*;
import static org.junit.Assert.*;
import net.sf.xisemele.impl.OperationsHelperImpl;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {
    private OperationsHelperImpl operationsHelper;
    private Document document;

    @Before
    public void setUp() {
        operationsHelper = new OperationsHelperImpl();
        document = createSampleDocument();
    }

    private Document createSampleDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Root element
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            // Child element 1
            Element child1 = doc.createElement("child1");
            rootElement.appendChild(child1);

            // Child element 2
            Element child2 = doc.createElement("child2");
            rootElement.appendChild(child2);

            // Grandchild element 1
            Element grandchild1 = doc.createElement("grandchild1");
            child2.appendChild(grandchild1);

            // Child element 3
            Element child3 = doc.createElement("child3");
            rootElement.appendChild(child3);

            // Child element 4
            Element child4 = doc.createElement("child4");
            rootElement.appendChild(child4);

            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Test
    public void testChildren() {
        List<Node> children = operationsHelper.children(document.getDocumentElement());
        assertEquals(4, children.size());

        // Test for empty node
        children = operationsHelper.children(document.createElement("emptyNode"));
        assertEquals(0, children.size());
    }

    @Test
    public void testFind() {
        // Test for finding existing node
        Node found = operationsHelper.find(document, "root");
        assertNotNull(found);
        assertEquals("root", found.getNodeName());

        // Test for finding non-existing node
        found = operationsHelper.find(document, "invalidPath");
        assertNull(found);
    }

    @Test
    public void testNodeWithName() {
        List<Node> children = operationsHelper.children(document.getDocumentElement());

        // Test for finding existing node
        Node node = operationsHelper.nodeWithName(children, "child2");
        assertNotNull(node);
        assertEquals("child2", node.getNodeName());

        // Test for finding non-existing node
        node = operationsHelper.nodeWithName(children, "invalidNode");
        assertNull(node);
    }

    @Test
    public void testSublistWithName() {
        List<Node> children = operationsHelper.children(document.getDocumentElement());

        // Test for finding nodes with a given name
        List<Node> sublist = operationsHelper.sublistWithName(children, "child2");
        assertEquals(1, sublist.size());
        assertEquals("child2", sublist.get(0).getNodeName());

        // Test for finding nodes that don't exist
        sublist = operationsHelper.sublistWithName(children, "invalidNode");
        assertEquals(0, sublist.size());
    }

}

