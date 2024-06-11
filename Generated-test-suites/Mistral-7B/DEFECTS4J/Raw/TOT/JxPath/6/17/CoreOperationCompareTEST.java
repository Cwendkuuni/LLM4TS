import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationConstant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNumber;
import org.apache.commons.jxpath.ri.eval.EvalContext;
import org.apache.commons.jxpath.ri.iter.SimpleIterator;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

 @Test
 public void testEqualBoolean() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant(true),
 new CoreOperationConstant(true)
 );
 EvalContext context = new EvalContext();
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testEqualBooleanFalse() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant(true),
 new CoreOperationConstant(false)
 );
 EvalContext context = new EvalContext();
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualNumber() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationNumber(1.0),
 new CoreOperationNumber(1.0)
 );
 EvalContext context = new EvalContext();
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testEqualNumberDifferent() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationNumber(1.0),
 new CoreOperationNumber(2.0)
 );
 EvalContext context = new EvalContext();
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualString() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant("str1"),
 new CoreOperationConstant("str1")
 );
 EvalContext context = new EvalContext();
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testEqualStringDifferent() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant("str1"),
 new CoreOperationConstant("str2")
 );
 EvalContext context = new EvalContext();
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualCollectionWithSameValue() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant(new SimpleIterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "value";
 }
 }),
 new CoreOperationConstant(new SimpleIterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "value";
 }
 })
 );
 EvalContext context = new EvalContext();
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testEqualCollectionWithDifferentValues() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant(new SimpleIterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "value1";
 }
 }),
 new CoreOperationConstant(new SimpleIterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "value2";
 }
 })
 );
 EvalContext context = new EvalContext();
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualCollectionWithNullValue() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant(new SimpleIterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return null;
 }
 }),
 new CoreOperationConstant(new SimpleIterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return null;
 }
 })
 );
 EvalContext context = new EvalContext();
 assertTrue(op.evaluate(context));
 }

 @Test
 public void testEqualCollectionWithNullValueAndNonNullValue() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant(new SimpleIterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return "value";
 }
 }),
 new CoreOperationConstant(new SimpleIterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return null;
 }
 })
 );
 EvalContext context = new EvalContext();
 assertFalse(op.evaluate(context));
 }

 @Test
 public void testEqualPointer() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant(new Pointer("//node()", null, null)),
 new CoreOperationConstant(new Pointer("//node()", null, null))
 );
 JXPathContext jxpathContext = new JXPathContext(new Object());
 assertTrue(op.evaluate(new EvalContext(jxpathContext)));
 }

 @Test
 public void testEqualPointerDifferent() {
 CoreOperationCompare op = new CoreOperationCompare(
 new CoreOperationConstant(new Pointer("//node1()", null, null)),
 new CoreOperationConstant(new Pointer("//node2()", null, null))
 );
 JXPathContext jxpathContext = new JXPathContext(new Object());
 assertFalse(op.evaluate(new EvalContext(jxpathContext)));
 }
}