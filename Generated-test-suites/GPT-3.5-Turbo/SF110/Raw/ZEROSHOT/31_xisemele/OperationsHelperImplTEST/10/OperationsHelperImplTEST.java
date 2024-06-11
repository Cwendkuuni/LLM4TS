import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        NodeList children = operationsHelper.children(root);

        assertEquals(3, children.getLength());
        assertEquals("child1", children.item(0).getNodeName());
        assertEquals("child2", children.item(1).getNodeName());
        assertEquals("child3", children.item(2).getNodeName());
    }

    @Test
    public void testFind() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        Element child3 = document.createElement("child3");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        Node found = operationsHelper.find(document, "root/child2");

        assertNotNull(found);
        assertEquals("child2", found.getNodeName());
    }

    @Test
    public void testNodeWithName() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        Element child3 = document.createElement("child");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        NodeList children = root.getChildNodes();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            nodes.add(children.item(i));
        }

        Node node = operationsHelper.nodeWithName(nodes, "child");

        assertNotNull(node);
        assertEquals("child", node.getNodeName());
    }

    @Test
    public void testSublistWithName() {
        OperationsHelperImpl operationsHelper = new OperationsHelperImpl();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("root");
        Element child1 = document.createElement("child");
        Element child2 = document.createElement("child");
        Element child3 = document.createElement("child");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);

        NodeList children = root.getChildNodes();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            nodes.add(children.item(i));
        }

        List<Node> sublist = operationsHelper.sublistWithName(nodes, "child");

        assertNotNull(sublist);
        assertEquals(3, sublist.size());
        assertEquals("child", sublist.get(0).getNodeName());
        assertEquals("child", sublist.get(1).getNodeName());
        assertEquals("child", sublist.get(2).getNodeName());
    }
}