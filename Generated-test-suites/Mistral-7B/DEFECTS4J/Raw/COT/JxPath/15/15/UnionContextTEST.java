import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

 private EvalContext createEvalContext() {
 return new EvalContext(new BasicNodeSet()) {
 };
 }

 @Test
 public void testUnionContextConstructors() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext contexts[] = new EvalContext[2];
 contexts[0] = createEvalContext();
 contexts[1] = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, contexts);
 assertNotNull(unionContext);
 }

 @Test
 public void testGetDocumentOrder() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext contexts[] = new EvalContext[2];
 contexts[0] = createEvalContext();
 contexts[1] = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, contexts);
 assertTrue(unionContext.getDocumentOrder() > 0);
 }

 @Test
 public void testSetPosition_success() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext contexts[] = new EvalContext[2];
 contexts[0] = createEvalContext();
 contexts[1] = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, contexts);
 unionContext.setPosition(0);
 assertTrue(unionContext.isAtCurrentNode());
 }

 @Test
 public void testSetPosition_failure() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext contexts[] = new EvalContext[2];
 contexts[0] = createEvalContext();
 contexts[1] = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, contexts);
 unionContext.setPosition(100);
 assertFalse(unionContext.isAtCurrentNode());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPosition_invalidIndex() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext contexts[] = new EvalContext[2];
 contexts[0] = createEvalContext();
 contexts[1] = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, contexts);
 unionContext.setPosition(-1);
 }

 @Test
 public void testSetPosition_noMoreElements() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext contexts[] = new EvalContext[2];
 contexts[0] = createEvalContext();
 contexts[1] = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, contexts);
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextNode();
 unionContext.nextNode();
 unionContext.setPosition(0);
 assertFalse(unionContext.isAtCurrentNode());
 }

 @Test
 public void testSetPosition_sameContext() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext context1 = createEvalContext();
 EvalContext context2 = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
 context1.nextSet();
 context1.nextNode();
 context1.nextNode();
 context1.nextNode();
 unionContext.setPosition(context1.getPosition());
 assertTrue(unionContext.isAtCurrentNode());
 }

 @Test
 public void testSetPosition_differentContext() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext context1 = createEvalContext();
 EvalContext context2 = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
 context1.nextSet();
 context1.nextNode();
 context1.nextNode();
 context1.nextNode();
 context2.nextSet();
 context2.nextNode();
 context2.nextNode();
 context2.nextNode();
 unionContext.setPosition(context2.getPosition());
 assertFalse(unionContext.isAtCurrentNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPosition_unpreparedContext() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext contexts[] = new EvalContext[2];
 contexts[0] = createEvalContext();
 contexts[1] = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, contexts);
 unionContext.setPosition(0);
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextNode();
 unionContext.nextNode();
 assertFalse(unionContext.isAtCurrentNode());
 }

 @Test
 public void testSetPosition_sameNodePointer() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext context1 = createEvalContext();
 EvalContext context2 = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
 NodePointer nodePointer1 = context1.getCurrentNodePointer();
 NodePointer nodePointer2 = context2.getCurrentNodePointer();
 unionContext.setPosition(nodePointer1);
 assertTrue(unionContext.isAtCurrentNode());
 unionContext.setPosition(nodePointer2);
 assertTrue(unionContext.isAtCurrentNode());
 }

 @Test
 public void testSetPosition_differentNodePointer() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext context1 = createEvalContext();
 EvalContext context2 = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
 NodePointer nodePointer1 = context1.getCurrentNodePointer();
 NodePointer nodePointer2 = context2.getCurrentNodePointer();
 unionContext.setPosition(nodePointer1);
 assertFalse(unionContext.isAtCurrentNode());
 unionContext.setPosition(nodePointer2);
 assertFalse(unionContext.isAtCurrentNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPosition_invalidNodePointer() {
 EvalContext parentContext = new EvalContext(new BasicNodeSet()) {
 };
 EvalContext contexts[] = new EvalContext[2];
 contexts[0] = createEvalContext();
 contexts[1] = createEvalContext();
 UnionContext unionContext = new UnionContext(parentContext, contexts);
 NodePointer nodePointer = new NodePointer(new Object());
 unionContext.setPosition(nodePointer);
 }
}