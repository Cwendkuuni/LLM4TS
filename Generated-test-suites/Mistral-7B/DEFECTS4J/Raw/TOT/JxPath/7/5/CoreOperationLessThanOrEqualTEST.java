import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.core.Context;
import org.apache.commons.jxpath.ri.core.ContextFactory;
import org.apache.commons.jxpath.ri.core.ContextManager;
import org.apache.commons.jxpath.ri.core.Extensions;
import org.apache.commons.jxpath.ri.core.Variables;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWithNumbers() {
 Expression arg1 = new Literal(5.0);
 Expression arg2 = new Literal(7.0);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

 Context context = ContextFactory.makeRootContext();
 context.setVariable("x", new Variables.VariableRef("x"));
 context.setVariable("y", new Variables.VariableRef("y"));
 context.setExtension("fn:double", new Extensions.DoubleFunction());

 InfoSet infoSet = ContextManager.createContextInfoSet(context, new NodeInfo[]{});
 EvalContext evalContext = new EvalContext(infoSet, context);
 Object result = op.computeValue(evalContext);

 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanOrEqualWithNumbersNotSatisfied() {
 Expression arg1 = new Literal(5.0);
 Expression arg2 = new Literal(4.0);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

 Context context = ContextFactory.makeRootContext();
 context.setVariable("x", new Variables.VariableRef("x"));
 context.setVariable("y", new Variables.VariableRef("y"));
 context.setExtension("fn:double", new Extensions.DoubleFunction());

 InfoSet infoSet = ContextManager.createContextInfoSet(context, new NodeInfo[]{});
 EvalContext evalContext = new EvalContext(infoSet, context);
 Object result = op.computeValue(evalContext);

 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanOrEqualWithNaN() {
 Expression arg1 = new Literal(Double.NaN);
 Expression arg2 = new Literal(5.0);
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

 Context context = ContextFactory.makeRootContext();
 context.setVariable("x", new Variables.VariableRef("x"));
 context.setVariable("y", new Variables.VariableRef("y"));
 context.setExtension("fn:double", new Extensions.DoubleFunction());

 InfoSet infoSet = ContextManager.createContextInfoSet(context, new NodeInfo[]{});
 EvalContext evalContext = new EvalContext(infoSet, context);
 Object result = op.computeValue(evalContext);

 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanOrEqualWithInvalidTypes() {
 Expression arg1 = new Literal(5);
 Expression arg2 = new Literal("seven");
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(arg1, arg2);

 Context context = ContextFactory.makeRootContext();
 context.setVariable("x", new Variables.VariableRef("x"));
 context.setVariable("y", new Variables.VariableRef("y"));
 context.setExtension("fn:double", new Extensions.DoubleFunction());

 InfoSet infoSet = ContextManager.createContextInfoSet(context, new NodeInfo[]{});
 EvalContext evalContext = new EvalContext(infoSet, context);
 Object result = op.computeValue(evalContext);
 }
}