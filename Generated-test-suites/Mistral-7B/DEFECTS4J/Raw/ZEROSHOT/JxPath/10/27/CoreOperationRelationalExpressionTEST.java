import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGreaterThanExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreConstantExpression(5), new CoreVariableExpression("x"));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testGreaterThanOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreConstantExpression(5), new CoreVariableExpression("x"));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testLessThanExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreConstantExpression(5), new CoreVariableExpression("x"));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testLessThanOrEqualExpression() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqualExpression(new CoreConstantExpression(5), new CoreVariableExpression("x"));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareDoubleValues() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreConstantExpression(5.5), new CoreConstantExpression(5.0));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareDoubleValuesEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreConstantExpression(5.5), new CoreConstantExpression(5.5));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareDoubleValuesNotEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreConstantExpression(5.5), new CoreConstantExpression(5.6));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareDoubleValuesLess() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreConstantExpression(5.5), new CoreConstantExpression(5.6));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareDoubleValuesGreater() {
 CoreOperationRelationalExpression expression = new CoreOperationLessThanExpression(new CoreConstantExpression(5.5), new CoreConstantExpression(5.4));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareCollections() {
 List list1 = new ArrayList();
 list1.add(new CoreConstantExpression(5));
 list1.add(new CoreConstantExpression(6));
 List list2 = new ArrayList();
 list2.add(new CoreConstantExpression(6));
 list2.add(new CoreConstantExpression(5));

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreListExpression(list1), new CoreListExpression(list2));
 Object result = expression.computevalue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareCollectionsEqual() {
 List list1 = new ArrayList();
 list1.add(new CoreConstantExpression(5));
 List list2 = new ArrayList();
 list2.add(new CoreConstantExpression(5));

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreListExpression(list1), new CoreListExpression(list2));
 Object result = expression.computevalue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareCollectionsNotEqual() {
 List list1 = new ArrayList();
 list1.add(new CoreConstantExpression(5));
 list1.add(new CoreConstantExpression(6));
 List list2 = new ArrayList();
 list2.add(new CoreConstantExpression(5));

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreListExpression(list1), new CoreListExpression(list2));
 Object result = expression.computevalue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareCollectionsSubset() {
 List list1 = new ArrayList();
 list1.add(new CoreConstantExpression(5));
 List list2 = new ArrayList();
 list2.add(new CoreConstantExpression(5));
 list2.add(new CoreConstantExpression(6));

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreListExpression(list1), new CoreListExpression(list2));
 Object result = expression.computevalue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareCollectionsNotSubset() {
 List list1 = new ArrayList();
 list1.add(new CoreConstantExpression(5));
 List list2 = new ArrayList();
 list2.add(new CoreConstantExpression(6));
 list2.add(new CoreConstantExpression(5));

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new CoreListExpression(list1), new CoreListExpression(list2));
 Object result = expression.computevalue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareIterators() {
 Iterator it1 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(5);
 }
 };
 Iterator it2 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(6);
 }
 };

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(it1, it2);
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareIteratorsEqual() {
 Iterator it1 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(5);
 }
 };
 Iterator it2 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(5);
 }
 };

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(it1, it2);
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareIteratorsNotEqual() {
 Iterator it1 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(5);
 }
 };
 Iterator it2 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(6);
 }
 };

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(it1, it2);
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareIteratorsSubset() {
 Iterator it1 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(5);
 }
 };
 Iterator it2 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(5);
 }
 };

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(it1, it2);
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareIteratorsNotSubset() {
 Iterator it1 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(5);
 }
 };
 Iterator it2 = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(6);
 }
 };

 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(it1, it2);
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareInitialContexts() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new InitialContext(new Object[0]), new InitialContext(new Object[0]));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareInitialContextsEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new InitialContext(new Object[0]), new InitialContext(new Object[0]));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testCompareSelfContexts() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new SelfContext(), new SelfContext());
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testCompareSelfContextsEqual() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqualExpression(new SelfContext(), new SelfContext());
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testReduceInitialContext() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new InitialContext(new Object[0]), new CoreConstantExpression(5));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testReduceCollection() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new CoreListExpression(Collections.singletonList(new CoreConstantExpression(5))), new CoreListExpression(Collections.singletonList(new CoreConstantExpression(6))));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testReduceIterator() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(5);
 }
 }, new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new CoreConstantExpression(6);
 }
 });
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testReduceSelfContext() {
 CoreOperationRelationalExpression expression = new CoreOperationGreaterThanExpression(new SelfContext(), new CoreConstantExpression(5));
 Object result = expression.computeValue(new EvalContext(new Object[0]));
 Assert.assertEquals(Boolean.FALSE, result);
 }

}