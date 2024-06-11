import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.RITest;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnionContextTest extends RITest {

 private EvalContext context1;
 private EvalContext context2;
 private EvalContext unionContext;

 @Test
 public void testUnionContext() {
 // Set up contexts 1 and 2
 context1 = createContext("/context1.xml", "/root/child1/*[1]");
 context2 = createContext("/context2.xml", "/root/child2/*[1]");

 // Create union context
 unionContext = new UnionContext(getContext(), new EvalContext[]{context1, context2});

 // Test getDocumentOrder
 assertEquals(1, unionContext.getDocumentOrder());

 // Test setting position
 int position = 0;
 unionContext.nextSet(); // Move to first set
 unionContext.nextNode(); // Move to first node in first set
 assertEquals(context1.getCurrentNodePointer(), unionContext.getCurrentNodePointer());

 // Move to the next node in the union context
 unionContext.nextNode(); // Move to first node in second set
 assertEquals(context2.getCurrentNodePointer(), unionContext.getCurrentNodePointer());

 // Move to next node in the second set
 unionContext.nextNode();
 assertEquals(context2.getCurrentNodePointer(), unionContext.getCurrentNodePointer());

 // Move to next node in the first set
 unionContext.nextNode();
 assertEquals(context1.getCurrentNodePointer(), unionContext.getCurrentNodePointer());

 // Move to next node in the first set again
 unionContext.nextNode();
 assertEquals(context1.getCurrentNodePointer(), unionContext.getCurrentNodePointer());

 // Move to the end of the union context
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextNode();
 unionContext.nextNode();
 unionContext.nextNode(); // Move past the end of the second set
 assertEquals(context1.getCurrentNodePointer(), unionContext.getCurrentNodePointer());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnionContextNullContexts() {
 // Test with null contexts
 unionContext = new UnionContext(getContext(), (EvalContext[]) null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnionContextEmptyContexts() {
 // Test with empty contexts
 unionContext = new UnionContext(getContext(), new EvalContext[0]);
 }

 private EvalContext createContext(String resource, String expression) {
 EvaluationContext evalContext = createEvaluationContext(resource);
 EvalContext context = new EvaluationContext(evalContext, expression, new BasicNodeSet());
 return context;
 }
}