import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathConstants;
import org.apache.commons.jxpath.ri.parser.AbstractParser;
import org.apache.commons.jxpath.ri.util.ExpressionUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

 private CoreOperationGreaterThan expression;
 private InfoSet infoSet;
 private EvaluationContext context;

 @Before
 public void setup() {
 CompilerContext compilerContext = new CompilerContext();
 AbstractParser parser = new AbstractParser();
 expression = (CoreOperationGreaterThan) parser.parseExpression("$x > $y", compilerContext).evaluate(compilerContext);
 infoSet = ExpressionUtil.createDoublesInfoSet(new double[] { 5.0, 3.0 });
 context = new EvaluationContext(new EvalContext());
 context.setInfoSet(infoSet);
 }

 @Test
 public void testGreaterThan() {
 Object result = expression.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqual() {
 InfoSet infoSetLessThanOrEqual = ExpressionUtil.createDoublesInfoSet(new double[] { 5.0, 5.0 });
 EvaluationContext contextLessThanOrEqual = new EvaluationContext(new EvalContext());
 contextLessThanOrEqual.setInfoSet(infoSetLessThanOrEqual);
 Object resultLessThanOrEqual = expression.computeValue(contextLessThanOrEqual);
 assertFalse(resultLessThanOrEqual instanceof Boolean);
 assertNull(resultLessThanOrEqual);
 }

 @Test(expected = ClassCastException.class)
 public void testInvalidType() {
 InfoSet infoSetInvalidType = ExpressionUtil.createInfoSetsInfoSet(
 new InfoSet[] { ExpressionUtil.createStringInfoSet("5"), ExpressionUtil.createStringInfoSet("3") });
 EvaluationContext contextInvalidType = new EvaluationContext(new EvalContext());
 contextInvalidType.setInfoSet(infoSetInvalidType);
 expression.computeValue(contextInvalidType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArg() {
 expression = new CoreOperationGreaterThan(null, new ConstantExpression(3.0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullArg2() {
 expression = new CoreOperationGreaterThan(new ConstantExpression(3.0), null);
 }
}