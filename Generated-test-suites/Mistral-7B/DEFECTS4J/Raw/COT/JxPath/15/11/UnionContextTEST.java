package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.apache.commons.jxpath.ri.model.NodeSetContext.NodeSetType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

 private NodeSetContext nodeSetContext;
 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext unionContext;

 @Before
 public void setup() {
 contextA = new EvalContext(new BasicNodeSet(), new NodeSetType[]{NodeSetType.NODE_SET, NodeSetType.NODE_SET});
 contextB = new EvalContext(new BasicNodeSet(), new NodeSetType[]{NodeSetType.NODE_SET, NodeSetType.NODE_SET});
 nodeSetContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
 }

 @Test
 public void testGetDocumentOrder() {
 assertEquals(1, nodeSetContext.getDocumentOrder());
 }

 @Test
 public void testSetPosition() {
 // Basic functionality test
 List<NodePointer> expectedNodes = new ArrayList<>();
 expectedNodes.addAll(Arrays.asList(contextA.getFirstNode(), contextB.getFirstNode()));
 for (int i = 0; i < 3; i++) {
 NodePointer currentNode = nodeSetContext.nextNode();
 assertEquals(expectedNodes.remove(0), currentNode);
 }

 // Edge cases and exception handling tests
 assertFalse(nodeSetContext.nextNode()); // no more nodes after all nodes are consumed
 assertFalse(nodeSetContext.nextSet()); // context doesn't support setting a new node set
 assertFalse(nodeSetContext.setPosition(-1)); // invalid position
}

}