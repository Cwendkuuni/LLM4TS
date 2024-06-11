import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Nodes;
import org.apache.commons.jxpath.ri.SimpleNode;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThan() {
 InfoSet infoSet = new Nodes("6.0", "7.5", "5.0");
 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new SimpleNode("number1", Double.parseDouble("6.0"), infoSet),
 new SimpleNode("number2", Double.parseDouble("7.5"), infoSet));

 EvaluationContext context = new EvaluationContext(infoSet);
 Object result = lessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLessThanWithEqual() {
 InfoSet infoSet = new Nodes("5.5", "6.0");
 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new SimpleNode("number1", Double.parseDouble("5.5"), infoSet),
 new SimpleNode("number2", Double.parseDouble("6.0"), infoSet));

 EvaluationContext context = new EvaluationContext(infoSet);
 Object result = lessThan.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testLessThanWithNegativeNumbers() {
 InfoSet infoSet = new Nodes("3.0", "1.5");
 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new SimpleNode("number1", Double.parseDouble("3.0"), infoSet),
 new SimpleNode("number2", Double.parseDouble("-1.5"), infoSet));

 EvaluationContext context = new EvaluationContext(infoSet);
 Object result = lessThan.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanWithInvalidArguments() {
 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 null, new SimpleNode("number2", Double.parseDouble("5.0"), null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanWithInvalidArguments2() {
 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new SimpleNode("number1", Double.parseDouble("6.0"), null),
 null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLessThanWithMixedTypes() {
 CoreOperationLessThan lessThan = new CoreOperationLessThan(
 new SimpleNode("number1", Double.parseDouble("6.0"), null),
 new SimpleNode("string1", "Apache Commons", null));
 }
}