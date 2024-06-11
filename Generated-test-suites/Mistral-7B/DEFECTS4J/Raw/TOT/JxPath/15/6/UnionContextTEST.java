import org.apache.commons.jxpath.JXPathContext;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

 private Document document;
 private EvalContext contextA;
 private EvalContext contextB;
 private UnionContext unionContext;

 @Before
 public void setUp() throws Exception {
 document = TestHelper.newDocument();

 contextA = new EvalContext(document, new BasicNodeSet());
 contextB = new EvalContext(document, new BasicNodeSet());

 contextA.push(new NodePointer(document.createElement("a")));
 contextB.push(new NodePointer(document.createElement("b")));
 contextB.push(new NodePointer(document.createElement("b")));
 contextB.push(new NodePointer(document.createElement("c")));

 unionContext = new UnionContext(contextA, new EvalContext[] { contextB });
 }

 @Test
 public void testGetDocumentOrder() {
 assertEquals(1, unionContext.getDocumentOrder());
 }

 @Test
 public void testSetPositionWithValidIndex() {
 // Index is valid within both contexts
 unionContext.setPosition(0);
 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();

 assertNotNull(nodeA);
 assertNotNull(nodeB);
 assertEquals(nodeB.getNode(), nodeA.getNode());
 }

 @Test
 public void testSetPositionWithValidIndexFromSecondContext() {
 // Index is valid within the second context but out of bounds in the first
 unionContext.setPosition(3);
 NodePointer nodeB = contextB.getCurrentNodePointer();

 assertFalse(contextA.hasNextSet());
 assertFalse(contextA.hasNextNode());
 assertNotNull(nodeB);
 assertEquals(nodeB.getNode(), unionContext.getCurrentNode().getNode());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithInvalidIndex() {
 // Index is out of bounds
 unionContext.setPosition(-1);
 }

 @Test
 public void testSetPositionAfterEndOfBothContexts() {
 // Index is greater than the total number of nodes in both contexts
 unionContext.setPosition(contextA.size() + contextB.size());

 assertFalse(contextA.hasNextSet());
 assertFalse(contextA.hasNextNode());
 assertFalse(contextB.hasNextSet());
 assertFalse(contextB.hasNextNode());
 assertNull(unionContext.getCurrentNode());
 }

 @Test
 public void testSetPositionAfterEndOfFirstContext() {
 // Index is greater than the number of nodes in the first context but within the second context
 unionContext.setPosition(contextA.size());
 NodePointer nodeB = contextB.getCurrentNodePointer();

 assertFalse(contextA.hasNextSet());
 assertFalse(contextA.hasNextNode());
 assertNotNull(nodeB);
 assertEquals(nodeB.getNode(), unionContext.getCurrentNode().getNode());
 }

 @Test
 public void testSetPositionWithDuplicatedNode() {
 // Node is present in both contexts
 contextA.setPosition(0);
 contextB.setPosition(1);
 unionContext.setPosition(0);

 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();
 NodePointer nodeUnion = unionContext.getCurrentNodePointer();

 assertNotNull(nodeA);
 assertNotNull(nodeB);
 assertNotNull(nodeUnion);
 assertEquals(nodeA.getNode(), nodeB.getNode());
 assertEquals(nodeA.getNode(), nodeUnion.getNode());
 }

 @Test
 public void testSetPositionWithDifferentNode() {
 // Nodes are different
 contextA.setPosition(0);
 contextB.setPosition(1);
 unionContext.setPosition(0);

 NodePointer nodeA = contextA.getCurrentNodePointer();
 NodePointer nodeB = contextB.getCurrentNodePointer();
 NodePointer nodeUnion = unionContext.getCurrentNodePointer();

 assertNotNull(nodeA);
 assertNotNull(nodeB);
 assertNotNull(nodeUnion);
 assertNotEquals(nodeA.getNode(), nodeB.getNode());
 assertNotEquals(nodeA.getNode(), nodeUnion.getNode());
 }

 @Test
 public void testSetPositionAfterEndOfFirstContextButBeforeEndOfSecond() {
 // Index is greater than the number of nodes in the first context but within the second context
 contextA.setPosition(contextA.size());
 contextB.setPosition(1);
 unionContext.setPosition(1);

 NodePointer nodeB = contextB.getCurrentNodePointer();
 NodePointer nodeUnion = unionContext.getCurrentNodePointer();

 assertFalse(contextA.hasNextSet());
 assertFalse(contextA.hasNextNode());
 assertNotNull(nodeB);
 assertEquals(nodeB.getNode(), nodeUnion.getNode());
 }
}