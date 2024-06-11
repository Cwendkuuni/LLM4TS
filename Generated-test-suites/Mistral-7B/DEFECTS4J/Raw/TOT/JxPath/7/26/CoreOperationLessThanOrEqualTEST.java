import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.expressions.Expression;
import org.apache.commons.jxpath.ri.expressions.VariableRef;
import org.apache.commons.jxpath.ri.expressions.core.CoreOperationLessThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

 @Test
 public void testLessThanOrEqualWhenBothValuesAreNumbersAndFirstIsLessThanSecond() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new VariableRef("a"), new VariableRef("b"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext();
 context.setCurrentNode(new NodeInfo(infoSet, "root", null));
 context.setVariable("a", 5.0);
 context.setVariable("b", 10.0);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWhenBothValuesAreNumbersAndFirstIsGreaterThanSecond() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new VariableRef("a"), new VariableRef("b"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext();
 context.setCurrentNode(new NodeInfo(infoSet, "root", null));
 context.setVariable("a", 15.0);
 context.setVariable("b", 10.0);
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWhenBothValuesAreNumbersAndBothAreEqual() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new VariableRef("a"), new VariableRef("b"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext();
 context.setCurrentNode(new NodeInfo(infoSet, "root", null));
 context.setVariable("a", 5.0);
 context.setVariable("b", 5.0);
 Object result = op.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWhenFirstValueIsNaN() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new VariableRef("a"), new VariableRef("b"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext();
 context.setCurrentNode(new NodeInfo(infoSet, "root", null));
 context.setVariable("a", Double.NaN);
 context.setVariable("b", 10.0);
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWhenSecondValueIsNaN() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new VariableRef("a"), new VariableRef("b"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext();
 context.setCurrentNode(new NodeInfo(infoSet, "root", null));
 context.setVariable("a", 5.0);
 context.setVariable("b", Double.NaN);
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testLessThanOrEqualWhenBothValuesAreStrings() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new VariableRef("a"), new VariableRef("b"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext();
 context.setCurrentNode(new NodeInfo(infoSet, "root", null));
 context.setVariable("a", "5");
 context.setVariable("b", "10");
 Object result = op.computeValue(context);
 assertFalse(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanOrEqualWhenArgumentsHaveDifferentTypes() {
 CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(
 new VariableRef("a"), new VariableRef("b"));
 InfoSet infoSet = new InfoSet();
 EvalContext context = new EvalContext();
 context.setCurrentNode(new NodeInfo(infoSet, "root", null));
 context.setVariable("a", 5.0);
 context.setVariable("b", "10");
 op.computeValue(context);
 }
}