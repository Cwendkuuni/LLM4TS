import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.classtest.CoreTestUtils;
import org.apache.commons.jxpath.ri.compilers.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compilers.CoreOperationOp;
import org.apache.commons.jxpath.ri.compilers.Expression;
import org.apache.commons.jxpath.ri.iter.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

 @Test
 public void testEqual() {
 Expression left = new CoreOperationOp("1", Operator.NUM);
 Expression right = new CoreOperationOp("1", Operator.NUM);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertTrue(compare.evaluate(jxpathContext));
 }

 @Test
 public void testUnequal() {
 Expression left = new CoreOperationOp("1", Operator.NUM);
 Expression right = new CoreOperationOp("2", Operator.NUM);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertFalse(compare.evaluate(jxpathContext));
 }

 @Test
 public void testEqualCollections() {
 Expression left = new CoreOperationOp(
 new Iterator[] { new CoreOperationOp("1", Operator.NUM), new CoreOperationOp("2", Operator.NUM) },
 Operator.ITERATOR);
 Expression right = new CoreOperationOp(
 new Iterator[] { new CoreOperationOp("1", Operator.NUM), new CoreOperationOp("2", Operator.NUM) },
 Operator.ITERATOR);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertTrue(compare.evaluate(jxpathContext));
 }

 @Test
 public void testUnequalCollections() {
 Expression left = new CoreOperationOp(
 new Iterator[] { new CoreOperationOp("1", Operator.NUM), new CoreOperationOp("2", Operator.NUM) },
 Operator.ITERATOR);
 Expression right = new CoreOperationOp(
 new Iterator[] { new CoreOperationOp("1", Operator.NUM), new CoreOperationOp("3", Operator.NUM) },
 Operator.ITERATOR);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertFalse(compare.evaluate(jxpathContext));
 }

 @Test
 public void testEqualBoolean() {
 Expression left = new CoreOperationOp("true", Operator.BOO);
 Expression right = new CoreOperationOp("true", Operator.BOO);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertTrue(compare.evaluate(jxpathContext));
 }

 @Test
 public void testUnequalBoolean() {
 Expression left = new CoreOperationOp("true", Operator.BOO);
 Expression right = new CoreOperationOp("false", Operator.BOO);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertFalse(compare.evaluate(jxpathContext));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnequalDifferentTypes() {
 Expression left = new CoreOperationOp("1", Operator.NUM);
 Expression right = new CoreOperationOp("true", Operator.BOO);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);
 }

 @Test
 public void testEqualString() {
 Expression left = new CoreOperationOp("Hello", Operator.STR);
 Expression right = new CoreOperationOp("Hello", Operator.STR);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertTrue(compare.evaluate(jxpathContext));
 }

 @Test
 public void testUnequalString() {
 Expression left = new CoreOperationOp("Hello", Operator.STR);
 Expression right = new CoreOperationOp("World", Operator.STR);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertFalse(compare.evaluate(jxpathContext));
 }

 @Test
 public void testNullLeft() {
 Expression left = new CoreOperationOp(null, Operator.NULL);
 Expression right = new CoreOperationOp("1", Operator.NUM);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertFalse(compare.evaluate(jxpathContext));
 }

 @Test
 public void testNullRight() {
 Expression left = new CoreOperationOp("1", Operator.NUM);
 Expression right = new CoreOperationOp(null, Operator.NULL);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context);

 assertFalse(compare.evaluate(jxpathContext));
 }

 @Test
 public void testSelfContext() {
 CoreTestUtils.setSelfContext(context);
 Expression left = new CoreOperationOp("1", Operator.NUM);
 Expression right = new CoreOperationOp("1", Operator.NUM);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context2 = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context2);

 assertTrue(compare.evaluate(jxpathContext));
 }

 @Test
 public void testInitialContext() {
 CoreTestUtils.setInitialContext(context);
 Expression left = new CoreOperationOp("1", Operator.NUM);
 Expression right = new CoreOperationOp("1", Operator.NUM);
 CoreOperationCompare compare = new CoreOperationCompare(left, right);

 EvaluationContext context2 = new EvaluationContext();
 JXPathContext jxpathContext = JXPathContext.newContext(context2);

 assertTrue(compare.evaluate(jxpathContext));
 }
}