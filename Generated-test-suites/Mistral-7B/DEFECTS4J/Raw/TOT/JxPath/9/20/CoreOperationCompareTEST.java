import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Literal;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 @Test
 public void testStringEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Literal("Hello"),
 new Literal("Hello")
 );

 JXPathContext context = new JXPathContext();
 context.setLenient(true);

 try {
 Object result = compare.evaluate(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testStringNotEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Literal("Hello"),
 new Literal("World")
 );

 JXPathContext context = new JXPathContext();
 context.setLenient(true);

 try {
 Object result = compare.evaluate(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testBooleanEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Literal(true),
 new Literal(true)
 );

 JXPathContext context = new JXPathContext();
 context.setLenient(true);

 try {
 Object result = compare.evaluate(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testBooleanNotEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Literal(true),
 new Literal(false)
 );

 JXPathContext context = new JXPathContext();
 context.setLenient(true);

 try {
 Object result = compare.evaluate(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testNumberEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Literal(1),
 new Literal(1)
 );

 JXPathContext context = new JXPathContext();
 context.setLenient(true);

 try {
 Object result = compare.evaluate(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testNumberNotEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Literal(1),
 new Literal(2)
 );

 JXPathContext context = new JXPathContext();
 context.setLenient(true);

 try {
 Object result = compare.evaluate(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testIteratorEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Literal(new ArrayIter(new Object[] {"A", "B", "C"})),
 new Literal(new ArrayIter(new Object[] {"A", "B", "C"}))
 );

 JXPathContext context = new JXPathContext();
 context.setLenient(true);

 try {
 Object result = compare.evaluate(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testIteratorNotEqual() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Literal(new ArrayIter(new Object[] {"A", "B", "C"})),
 new Literal(new ArrayIter(new Object[] {"A", "B", "D"}))
 );

 JXPathContext context = new JXPathContext();
 context.setLenient(true);

 try {
 Object result = compare.evaluate(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 } catch (EvaluationException e) {
 e.printStackTrace();
 }
 }

 @Test(expected = EvaluationException.class)
 public void testMismatchedTypes() {
 CoreOperationCompare compare = new CoreOperationCompare(
 new Literal(1),
 new Literal("Hello")
 );

 JXPathContext context = new JXPathContext();
 context.setLenient(true);

 try {
 Object result = compare.evaluate(context);
 } catch (EvaluationException e) {
 assertTrue(e.getMessage().contains("Types are not compatible"));
 }
 }
}