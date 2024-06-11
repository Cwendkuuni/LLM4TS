import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext context1;
 private EvalContext context2;
 private EvalContext parentContext;
 private UnionContext unionContext;

 @Test
 public void testUnionContext() {
  // Initialize contexts
  context1 = new EvalContext(new BasicNodeSet(), null);
  context1.pushContext(new BasicNodeSet());
  context1.addNode("context1-node1", null);
  context1.addNode("context1-node2", null);

  context2 = new EvalContext(new BasicNodeSet(), null);
  context2.pushContext(new BasicNodeSet());
  context2.addNode("context2-node1", null);
  context2.addNode("context2-node3", null);

  parentContext = new EvalContext(new BasicNodeSet(), null);

  // Initialize UnionContext
  unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

  // Test getDocumentOrder
  assertEquals(1, unionContext.getDocumentOrder());

  // Test setPosition
  unionContext.setPosition(0);
  NodePointer nodePointer = unionContext.getCurrentNodePointer();
  assertEquals("context1-node1", nodePointer.getNodeName());

  unionContext.nextSet();
  nodePointer = unionContext.getCurrentNodePointer();
  assertEquals("context1-node2", nodePointer.getNodeName());

  unionContext.nextSet();
  nodePointer = unionContext.getCurrentNodePointer();
  assertEquals("context2-node1", nodePointer.getNodeName());

  unionContext.nextSet();
  nodePointer = unionContext.getCurrentNodePointer();
  assertEquals("context2-node3", nodePointer.getNodeName());

  // Test nextNode
  unionContext.nextNode();
  nodePointer = unionContext.getCurrentNodePointer();
  fail("Expected end of set");
 }
}