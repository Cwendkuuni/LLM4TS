import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodeSetContext;
import org.apache.commons.jxpath.RITestBase;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest extends RITestBase {

    @Test
    public void testUnionContext() {
        EvalContext contextA = createContext("contextA.xml");
        EvalContext contextB = createContext("contextB.xml");

        UnionContext unionContext = new UnionContext(getContext(), new EvalContext[]{contextA, contextB});

        assertTrue(unionContext.getContexts.length > 0);
        assertNotNull(unionContext.getNodeSet());

        unionContext.setPosition(0);
        NodePointer nodeA = unionContext.getCurrentNodePointer();
        assertNotNull(nodeA);
        assertTrue(nodeA.getDocument().getDocumentElement().getNodeName().equals("documentA"));

        unionContext.nextSet();
        unionContext.setPosition(0);
        NodePointer nodeB = unionContext.getCurrentNodePointer();
        assertNotNull(nodeB);
        assertTrue(nodeB.getDocument().getDocumentElement().getNodeName().equals("documentB"));

        unionContext.nextSet();
        assertFalse(unionContext.hasNextSet());

        unionContext.nextNode();
        assertNull(unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        NodePointer nodeC = unionContext.getCurrentNodePointer();
        assertNotNull(nodeC);
        assertTrue(nodeC.getDocument().getDocumentElement().getNodeName().equals("documentA"));

        unionContext.nextNode();
        NodePointer nodeD = unionContext.getCurrentNodePointer();
        assertNotNull(nodeD);
        assertTrue(nodeD.getDocument().getDocumentElement().getNodeName().equals("documentB"));

        unionContext.nextNode();
        assertNull(unionContext.getCurrentNodePointer());

        unionContext.previousSet();
        unionContext.setPosition(0);
        NodePointer nodeE = unionContext.getCurrentNodePointer();
        assertNotNull(nodeE);
        assertTrue(nodeE.getDocument().getDocumentElement().getNodeName().equals("documentA"));

        unionContext.nextNode();
        NodePointer nodeF = unionContext.getCurrentNodePointer();
        assertNotNull(nodeF);
        assertTrue(nodeF.getDocument().getDocumentElement().getNodeName().equals("documentB"));

        unionContext.nextNode();
        assertNull(unionContext.getCurrentNodePointer());
    }

    private EvalContext createContext(String fileName) {
        String xml = getClass().getResource(fileName).getFile();
        return new EvalContext(getContext(), new StringReader(xml));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setContextClass(UnionContext.class);
    }

}