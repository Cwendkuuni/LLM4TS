import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.axes.NodeSetContext;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;

/**
 * Test class for UnionContext
 */
public class UnionContextTest {

 private EvalContext context1;
 private EvalContext context2;
 private UnionContext unionContext;

 @Before
 public void setUp() {
 // Setup EvalContexts
 context1 = new EvalContext(new NodeSetContext(new BasicNodeSet()), null);
 context2 = new EvalContext(new NodeSetContext(new BasicNodeSet()), null);

 // Setup UnionContext
 unionContext = new UnionContext(null, new EvalContext[] { context1, context2 });
 }

 /**
 * Test method for {@link UnionContext#UnionContext(EvalContext, EvalContext[])} .
 */
 @Test
 public void testUnionContextConstructor() {
 // Test if the constructor is called correctly
 assertEquals(2, unionContext.contexts.length);
 }

 /**
 * Test method for {@link UnionContext#getDocumentOrder()}.
 */
 @Test
 public void testGetDocumentOrder() {
 // Test base case
 assertEquals(1, unionContext.getDocumentOrder());

 // Test when contexts array is empty
 unionContext = new UnionContext(null, new EvalContext[0]);
 assertEquals(0, unionContext.getDocumentOrder());
 }

 /**
 * Test method for {@link UnionContext#setPosition(int)}.
 */
 @Test
 public void testSetPosition() {
 // Test base case
 unionContext.setPosition(0);
 assertTrue(unionContext.getNodeSet().contains(unionContext.getCurrentNodePointer()));

 // Test edge case with positioned nodes in both contexts
 context1.pushNode(new NodePointer(1));
 context2.pushNode(new NodePointer(2));
 unionContext.setPosition(1);
 assertTrue(unionContext.getNodeSet().contains(context1.getCurrentNodePointer()));
 assertFalse(unionContext.getNodeSet().contains(context2.getCurrentNodePointer()));

 // Test exception case when position is out of bounds
 unionContext.setPosition(-1);
 assertFalse(unionContext.getNodeSet().contains(unionContext.getCurrentNodePointer()));
 }

 /**
 * Test method for {@link UnionContext#UnionContext(EvalContext, EvalContext[])}.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testUnionContextConstructorNullContexts() {
 // Test with null contexts array
 UnionContext unionContext = new UnionContext(null, null);
 }

 /**
 * Test method for {@link UnionContext#UnionContext(EvalContext, EvalContext[])}.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testUnionContextConstructorNullContext() {
 // Test with null context
 UnionContext unionContext = new UnionContext(null, new EvalContext[] { context1 });
 }
}