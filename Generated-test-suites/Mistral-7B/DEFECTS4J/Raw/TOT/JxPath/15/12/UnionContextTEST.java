import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class UnionContextTest {

    private static final String XML_TEST_DATA = "" +
            "<root>\n" +
            "  <a xml:id=\"a1\">A1</a>\n" +
            "  <b xml:id=\"b1\">B1</b>\n" +
            "  <c xml:id=\"c1\">C1</c>\n" +
            "  <a xml:id=\"a2\">A2</a>\n" +
            "  <b xml:id=\"b2\">B2</b>\n" +
            "</root>";

    @Test
    public void testUnionContext_UnionTwoEmptyContexts() {
        Document document = JXPathContext.newDocument(XML_TEST_DATA);
        EvalContext contextA = new EvalContext(document, null);
        EvalContext contextB = new EvalContext(document, null);
        UnionContext unionContext = new UnionContext(contextA, new EvalContext[]{contextB});

        List<NodePointer> nodes = new ArrayList<>();
        unionContext.nextSet();
        while (unionContext.nextNode()) {
            nodes.add(unionContext.getCurrentNodePointer());
            unionContext.next();
        }
        Assert.assertEquals(0, nodes.size());
    }

    @Test
    public void testUnionContext_UnionTwoContextsWithCommonNodes() {
        Document document = JXPathContext.newDocument(XML_TEST_DATA);
        EvalContext contextA = new EvalContext(document.getDocumentElement());
        contextA.pushVariable("varA", document.getElementsByTagName("a").item(0));
        EvalContext contextB = new EvalContext(document.getDocumentElement());
        contextB.pushVariable("varB", document.getElementsByTagName("a").item(1));
        UnionContext unionContext = new UnionContext(contextA, new EvalContext[]{contextB});

        List<NodePointer> nodes = new ArrayList<>();
        unionContext.nextSet();
        while (unionContext.nextNode()) {
            nodes.add(unionContext.getCurrentNodePointer());
            unionContext.next();
        }
        List<NodePointer> expectedNodes = List.of(contextA.getCurrentNodePointer(), contextB.getCurrentNodePointer());
        Assert.assertArrayEquals(expectedNodes.stream().mapToLong(NodePointer::getNode).mapToInt(Node::getNodeValue).toArray(),
                nodes.stream().mapToInt(NodePointer::getNodeValue).toArray());
    }

    @Test
    public void testUnionContext_UnionTwoContextsWithDifferentRoots() {
        Document documentA = JXPathContext.newDocument("<root><a>A</a></root>");
        Document documentB = JXPathContext.newDocument("<root><b>B</b></root>");
        EvalContext contextA = new EvalContext(documentA.getDocumentElement());
        EvalContext contextB = new EvalContext(documentB.getDocumentElement());
        UnionContext unionContext = new UnionContext(contextA, new EvalContext[]{contextB});

        List<NodePointer> nodes = new ArrayList<>();
        unionContext.nextSet();
        while (unionContext.nextNode()) {
            nodes.add(unionContext.getCurrentNodePointer());
            unionContext.next();
        }
        Assert.assertEquals(2, nodes.size());
        Assert.assertFalse(nodes.get(0).getNode().isSameNode(nodes.get(1).getNode()));
    }

    @Test(expected = IllegalStateException.class)
    public void testUnionContext_UnionContextWithItself() {
        Document document = JXPathContext.newDocument(XML_TEST_DATA);
        EvalContext context = new EvalContext(document.getDocumentElement());
        UnionContext unionContext = new UnionContext(context, new EvalContext[]{context});
        unionContext.nextSet();
        unionContext.nextNode();
    }

    @Test
    public void testUnionContext_UnionContextWithNullContext() {
        Document document = JXPathContext.newDocument(XML_TEST_DATA);
        EvalContext context = new EvalContext(document.getDocumentElement());
        UnionContext unionContext = new UnionContext(context, null);
        List<NodePointer> nodes = new ArrayList<>();
        unionContext.nextSet();
        while (unionContext.nextNode()) {
            nodes.add(unionContext.getCurrentNodePointer());
            unionContext.next();
        }
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals(context.getCurrentNodePointer(), nodes.get(0));
    }
}