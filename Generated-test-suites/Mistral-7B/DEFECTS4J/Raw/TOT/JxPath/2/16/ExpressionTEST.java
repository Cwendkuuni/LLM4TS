import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

 @Test
 public void testContextDependentExpression() {
 Expression expression = new ContextDependentExpression();
 EvalContext context = new EvalContext(new CompilerContext());

 // Context-dependent expression should return different values for different contexts
 NodePointer contextNode = new NodePointer(new QName("root"), null, null);
 context.setCurrentNodePointer(contextNode);
 Object result1 = expression.computeValue(context);

 NodePointer differentContextNode = new NodePointer(new QName("differentRoot"), null, null);
 context.setCurrentNodePointer(differentContextNode);
 Object result2 = expression.computeValue(context);

 assertNotSame(result1, result2);
 }

 @Test
 public void testContextIndependentExpression() {
 Expression expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(new CompilerContext());
 Object result = expression.computeValue(context);

 // Context-independent expression should return the same value for the same context
 EvalContext sameContext = new EvalContext(new CompilerContext());
 Object resultSameContext = expression.computeValue(sameContext);

 assertEquals(result, resultSameContext);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterateOnContextDependentExpression() {
 Expression expression = new ContextDependentExpression();
 EvalContext context = new EvalContext(new CompilerContext());
 try {
 Iterator iterator = expression.iterate(context);
 iterator.next();
 fail("Should not be able to iterate on a context-dependent expression");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testIterateOnContextIndependentExpression() {
 Expression expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(new CompilerContext());
 Iterator iterator = expression.iterate(context);
 Object result = iterator.next();

 // Verify that the expression returns a single value
 assertNotNull(result);
 }

 @Test
 public void testComputeValueOnContextIndependentExpression() {
 Expression expression = new ContextIndependentExpression();
 EvalContext context = new EvalContext(new CompilerContext());
 Object result = expression.computeValue(context);

 // Verify that the expression returns the same value when computed directly
 Object resultDirect = expression.compute(context);
 assertEquals(result, resultDirect);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIterateOnContextIndependentExpressionWithEmptyResult() {
 Expression expression = new ContextIndependentExpressionWithEmptyResult();
 EvalContext context = new EvalContext(new CompilerContext());
 try {
 Iterator iterator = expression.iterate(context);
 iterator.next();
 fail("Should not be able to iterate on a context-independent expression that returns an empty result");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }
}

// Context-dependent expression
class ContextDependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return true;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return context.getCurrentNodePointer().getNodeName().getLocalPart();
 }
}

// Context-independent expression
class ContextIndependentExpression extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return ONE;
 }
}

// Context-independent expression that returns an empty result
class ContextIndependentExpressionWithEmptyResult extends Expression {
 @Override
 public boolean computeContextDependent() {
 return false;
 }

 @Override
 public Object computeValue(EvalContext context) {
 return EMPTY_LIST;
 }
}