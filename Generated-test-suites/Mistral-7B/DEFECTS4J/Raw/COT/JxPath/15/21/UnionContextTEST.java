import org.apache.commons.jxpath.Axis;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext context1;
 private EvalContext context2;
 private UnionContext unionContext;

 @Test
 public void testUnionContextConstruction() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet());
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

 assertEquals(2, unionContext.contexts.length);
 }

 @Test
 public void testGetDocumentOrder() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet());
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

 assertEquals(1, unionContext.getDocumentOrder());
 }

 @Test
 public void testSetPositionHappyPath() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet());
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

 context1.add(Axis.SELF, new NodePointer(1));
 context1.add(Axis.SELF, new NodePointer(2));
 context2.add(Axis.SELF, new NodePointer(3));
 context2.add(Axis.SELF, new NodePointer(4));

 unionContext.setPosition(1);
 unionContext.nextSet();
 unionContext.nextNode();

 NodePointer expectedNodePointer = context1.getCurrentNodePointer();
 NodePointer actualNodePointer = unionContext.getCurrentNodePointer();

 assertTrue(expectedNodePointer.equals(actualNodePointer));
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPositionBeforePreparation() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet());
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

 unionContext.setPosition(1);
 }

 @Test
 public void testSetPositionOutOfRange() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet());
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

 context1.add(Axis.SELF, new NodePointer(1));
 context1.add(Axis.SELF, new NodePointer(2));
 context2.add(Axis.SELF, new NodePointer(3));
 context2.add(Axis.SELF, new NodePointer(4));

 unionContext.setPosition(5);
 }

 @Test
 public void testSetPositionSamePositionInBothContexts() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet());
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

 context1.add(Axis.SELF, new NodePointer(1));
 context1.add(Axis.SELF, new NodePointer(2));
 context2.add(Axis.SELF, new NodePointer(1));
 context2.add(Axis.SELF, new NodePointer(2));

 unionContext.setPosition(1);
 unionContext.nextSet();
 unionContext.nextNode();

 NodePointer expectedNodePointer1 = context1.getCurrentNodePointer();
 NodePointer expectedNodePointer2 = context2.getCurrentNodePointer();
 NodePointer actualNodePointer = unionContext.getCurrentNodePointer();

 assertTrue(expectedNodePointer1.equals(actualNodePointer) || expectedNodePointer2.equals(actualNodePointer));
 }

 @Test
 public void testSetPositionDifferentNodesInBothContexts() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet());
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

 context1.add(Axis.SELF, new NodePointer(1));
 context1.add(Axis.SELF, new NodePointer(2));
 context2.add(Axis.SELF, new NodePointer(3));
 context2.add(Axis.SELF, new NodePointer(4));

 unionContext.setPosition(1);
 unionContext.nextSet();
 unionContext.nextNode();

 NodePointer expectedNodePointer1 = context1.getCurrentNodePointer();
 NodePointer expectedNodePointer2 = context2.getCurrentNodePointer();
 NodePointer actualNodePointer1 = unionContext.getCurrentNodePointer();

 assertFalse(expectedNodePointer1.equals(actualNodePointer1));
 assertFalse(expectedNodePointer2.equals(actualNodePointer1));

 assertTrue(expectedNodePointer1.equals(expectedNodePointer1) && expectedNodePointer2.equals(expectedNodePointer2));
 }

 @Test
 public void testSetPositionAfterFirstContextIsExhausted() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet());
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

 context1.add(Axis.SELF, new NodePointer(1));
 context1.add(Axis.SELF, new NodePointer(2));
 context2.add(Axis.SELF, new NodePointer(3));
 context2.add(Axis.SELF, new NodePointer(4));
 context1.nextSet();
 context1.nextNode();

 unionContext.setPosition(1);
 unionContext.nextSet();
 unionContext.nextNode();

 NodePointer expectedNodePointer2 = context2.getCurrentNodePointer();
 NodePointer actualNodePointer = unionContext.getCurrentNodePointer();

 assertTrue(expectedNodePointer2.equals(actualNodePointer));
 }

 @Test
 public void testSetPositionAfterSecondContextIsExhausted() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet());
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] {context1, context2});

 context1.add(Axis.SELF, new NodePointer(1));
 context1.add(Axis.SELF, new NodePointer(2));
 context2.add(Axis.SELF, new NodePointer(3));
 context2.add(Axis.SELF, new NodePointer(4));
 context2.nextSet();
 context2.nextNode();

 unionContext.setPosition(1);
 unionContext.nextSet();
 unionContext.nextNode();

 NodePointer expectedNodePointer1 = context1.getCurrentNodePointer();
 NodePointer actualNodePointer = unionContext.getCurrentNodePointer();

 assertTrue(expectedNodePointer1.equals(actualNodePointer));
 }
}