import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.util.InfoSetUtil;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

 @Test
 public void testGT() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{
 new ConstantExpression(new Integer(5)),
 new CoreOperationIdentifierExpression(new String("x"))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariable("x", new Integer(3));
 Object result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 context.setVariable("x", new Integer(5));
 result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertFalse((Boolean) result);
 }

 @Test
 public void testGE() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{
 new ConstantExpression(new Integer(5)),
 new CoreOperationIdentifierExpression(new String("x"))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare >= 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariable("x", new Integer(3));
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);

 context.setVariable("x", new Integer(5));
 result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLT() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{
 new ConstantExpression(new Integer(3)),
 new CoreOperationIdentifierExpression(new String("x"))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare < 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariable("x", new Integer(5));
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);

 context.setVariable("x", new Integer(3));
 result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testLE() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{
 new ConstantExpression(new Integer(3)),
 new CoreOperationIdentifierExpression(new String("x"))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare <= 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariable("x", new Integer(5));
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);

 context.setVariable("x", new Integer(3));
 result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testDoubleComparison() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{
 new ConstantExpression(new Double(5.5)),
 new CoreOperationIdentifierExpression(new String("x"))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare != 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariable("x", new Double(5.5));
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);

 context.setVariable("x", new Double(6.5));
 result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);

 context.setVariable("x", new Double(4.5));
 result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testComparisonWithCollection() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{
 new CoreOperationIdentifierExpression(new String("x")),
 new ConstantExpression(new Integer(5))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariable("x", Collections.singletonList(new Integer(3)));
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);

 context.setVariable("x", Arrays.asList(new Integer(3), new Integer(5)));
 result = expr.computeValue(context);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testComparisonWithEmptyCollection() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{
 new CoreOperationIdentifierExpression(new String("x")),
 new ConstantExpression(new Integer(5))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariable("x", Collections.emptyList());
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testComparisonWithEmptySet() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{
 new CoreOperationIdentifierExpression(new String("x")),
 new ConstantExpression(new Integer(5))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariable("x", Collections.emptySet());
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 }

 @Test
 public void testComparisonWithIterable() {
 CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[]{
 new CoreOperationIdentifierExpression(new String("x")),
 new ConstantExpression(new Integer(5))
 }) {
 @Override
 protected boolean evaluateCompare(int compare) {
 return compare > 0;
 }
 };
 EvalContext context = new EvalContext(new InitialContext());
 context.setVariable("x", new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return new Integer(3);
 }
 });
 Object result = expr.computeValue(context);
 assertFalse(result instanceof Boolean);
 }
}