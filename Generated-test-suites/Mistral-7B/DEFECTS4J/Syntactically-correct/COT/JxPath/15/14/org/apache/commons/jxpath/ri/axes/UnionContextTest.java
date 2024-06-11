import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext getTestContext() {
 return new EvalContext(new BasicNodeSet(), null);
 }

 private EvalContext[] getTestContexts(int length) {
 EvalContext contexts[] = new EvalContext[length];
 for (int i = 0; i < length; i++) {
 contexts[i] = getTestContext();
 }
 return contexts;
 }

 @Test
 public void testUnionContext() {
 UnionContext unionContext = new UnionContext(null, getTestContexts(2));
 assertEquals(0, unionContext.getDocumentOrder());
 unionContext.setPosition(0);
 assertNotNull(unionContext.getDocumentNode(0));
 unionContext.setPosition(1);
 assertNotNull(unionContext.getDocumentNode(1));
 unionContext.setPosition(0); // Test going back to the first context
 assertNotNull(unionContext.getDocumentNode(0));
 unionContext.setPosition(2); // Out of bounds
 assertFalse(unionContext.setPosition(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithEmptyContexts() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
 unionContext.setPosition(0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionWithOneContext() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[1]);
 unionContext.setPosition(0);
 unionContext.setPosition(1);
 }

 @Test
 public void testGetDocumentOrder() {
 UnionContext unionContext = new UnionContext(null, getTestContexts(2));
 assertEquals(1, unionContext.getDocumentOrder());
 }
}