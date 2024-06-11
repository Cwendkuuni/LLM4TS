import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

    private Document document;
    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext unionContext;

    @Before
    public void setUp() {
        document = TestHelper.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element a = document.createElement("a");
        root.appendChild(a);

        Element b = document.createElement("b");
        root.appendChild(b);

        contextA = new EvalContext(document, new BasicNodeSet(document.getDocumentElement()));
        contextA.pushContext(root);
        contextA.pushContext(a);

        contextB = new EvalContext(document, new BasicNodeSet(document.getDocumentElement()));
        contextB.pushContext(root);
        contextB.pushContext(b);
    }

    @Test
    public void testUnionContext_emptyContexts() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
        assertNull(unionContext.getNodeSet());
    }

    @Test
    public void testUnionContext_singleContext() {
        UnionContext unionContext = new UnionContext(new EvalContext(document, new BasicNodeSet(document.getDocumentElement())), new EvalContext[0]);
        assertEquals(new BasicNodeSet(document.getDocumentElement()), unionContext.getNodeSet());
    }

    @Test
    public void testUnionContext_contextsWithSameNodes() {
        UnionContext unionContext = new UnionContext(contextA, contextB);
        List<NodePointer> expectedNodePointers = new ArrayList<>( Arrays.asList(
                contextA.getCurrentNodePointer(),
                contextB.getCurrentNodePointer()
        ));
        assertEquals(new BasicNodeSet(expectedNodePointers.toArray(new NodePointer[0])), unionContext.getNodeSet());
        assertEquals(2, unionContext.getDocumentOrder());
    }

    @Test
    public void testUnionContext_contextsWithDifferentNodes() {
        Element c = document.createElement("c");
        root.appendChild(c);

        contextB.pushContext(c);

        UnionContext unionContext = new UnionContext(contextA, contextB);
        List<NodePointer> expectedNodePointers = new ArrayList<>( Arrays.asList(
                contextA.getCurrentNodePointer(),
                contextB.getCurrentNodePointer()
        ));
        assertEquals(new BasicNodeSet(expectedNodePointers.toArray(new NodePointer[0])), unionContext.getNodeSet());
        assertEquals(2, unionContext.getDocumentOrder());
    }

    @Test
    public void testUnionContext_contextsWithNoNodes() {
        UnionContext unionContext = new UnionContext(contextA, contextB);
        List<NodePointer> expectedNodePointers = new ArrayList<>( Arrays.asList(
                contextA.getCurrentNodePointer()
        ));
        assertEquals(new BasicNodeSet(expectedNodePointers.toArray(new NodePointer[0])), unionContext.getNodeSet());
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testUnionContext_nextSet_emptySets() {
        UnionContext unionContext = new UnionContext(new EvalContext[0], new EvalContext[0]);
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContext_nextSet_singleSet() {
        UnionContext unionContext = new UnionContext(contextA, new EvalContext[0]);
        assertTrue(unionContext.nextSet());
    }

    @Test
    public void testUnionContext_nextSet_multipleSets() {
        UnionContext unionContext = new UnionContext(contextA, contextB);
        assertTrue(unionContext.nextSet());
    }

    @Test
    public void testUnionContext_nextNode_emptySets() {
        UnionContext unionContext = new UnionContext(new EvalContext[0], new EvalContext[0]);
        assertNull(unionContext.nextNode());
    }

    @Test
    public void testUnionContext_nextNode_singleSet() {
        UnionContext unionContext = new UnionContext(contextA, new EvalContext[0]);
        Element node = document.createElement("node");
        contextA.pushContext(node);
        contextA.nextNode();
        NodePointer expectedNodePointer = contextA.getCurrentNodePointer();
        NodePointer actualNodePointer = unionContext.nextNode();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testUnionContext_nextNode_multipleSets() {
        UnionContext unionContext = new UnionContext(contextA, contextB);
        Element nodeA = document.createElement("nodeA");
        contextA.pushContext(nodeA);
        contextA.nextNode();

        Element nodeB = document.createElement("nodeB");
        contextB.pushContext(nodeB);
        contextB.nextNode();

        NodePointer expectedNodePointerA = contextA.getCurrentNodePointer();
        NodePointer expectedNodePointerB = contextB.getCurrentNodePointer();
        NodePointer actualNodePointer = unionContext.nextNode();

        assertEquals(expectedNodePointerA, actualNodePointer);
    }
}