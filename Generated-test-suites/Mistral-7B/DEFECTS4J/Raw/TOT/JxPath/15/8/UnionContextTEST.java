import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext contextA, contextB;
 private UnionContext unionContext;
 private Document document;

 @Before
 public void setUp() throws Exception {
 document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

 contextA = new EvalContext(document, new BasicNodeSet());
 contextB = new EvalContext(document, new BasicNodeSet());

 Element rootA = document.createElement("rootA");
 rootA.setAttribute("id", "rootA");
 document.appendChild(rootA);

 Element a = document.createElement("a");
 a.setAttribute("id", "a");
 rootA.appendChild(a);

 Element b = document.createElement("b");
 b.setAttribute("id", "b");
 rootA.appendChild(b);

 contextA.pushContext(rootA);
 contextA.setDocument(document);

 contextB.pushContext(rootA);
 contextB.setDocument(document);

 unionContext = new UnionContext(contextA, new EvalContext[]{contextB});
 }

 @Test
 public void testUnionContextGetDocumentOrder() {
 assertEquals(1, unionContext.getDocumentOrder());
 }

 @Test
 public void testUnionContextSetPosition_SingleNode_BothContextsHaveCurrentNode() {
 unionContext.setPosition(0);
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();

 assertEquals(nodeA.getNode(), nodeB.getNode());
 }

 @Test
 public void testUnionContextSetPosition_SingleNode_OneContextHasCurrentNode() {
 contextB.nextSet();
 contextB.nextNode();
 unionContext.setPosition(0);
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();

 assertNotEquals(nodeA.getNode(), nodeB.getNode());
 assertEquals(nodeA.getNode(), unionContext.getCurrentNode());
 }

 @Test
 public void testUnionContextSetPosition_SingleNode_NoNodeInBothContexts() {
 contextA.nextSet();
 contextB.nextSet();
 unionContext.setPosition(0);
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();

 assertNull(nodeA);
 assertNull(nodeB);
 assertNull(unionContext.getCurrentNode());
 }

 @Test
 public void testUnionContextSetPosition_EmptyContexts() {
 contextA.nextSet();
 contextA.nextSet(); // empty context
 contextB.nextSet();
 contextB.nextSet(); // empty context
 unionContext.setPosition(0);
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();

 assertNull(nodeA);
 assertNull(nodeB);
 assertNull(unionContext.getCurrentNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnionContextSetPosition_MismatchedDocument() {
 EvalContext contextC = new EvalContext(document, new BasicNodeSet());
 unionContext = new UnionContext(contextA, new EvalContext[]{contextC});
 unionContext.setPosition(0);
 }

 @Test
 public void testUnionContextSetPosition_MultipleNodes() {
 unionContext.setPosition(1);
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();

 assertNotEquals(nodeA.getNode(), nodeB.getNode());
 assertEquals(nodeB.getNode(), unionContext.getCurrentNode());
 }
}