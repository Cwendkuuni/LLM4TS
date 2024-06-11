import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationGreaterThanOrEqualTest {

 @Test
 public void testGreaterThanOrEqual() {
 InformationSource infoSource = new InformationSource() {
 @Override
 public InfoSet getInfoSet(String expression) {
 return new DummyInfoSet(expression);
 }
 };
 EvalContext context = new EvalContext(new DummyContext(), infoSource);

 Expression arg1 = new LiteralExpression(new Literal(10.5));
 Expression arg2 = new LiteralExpression(new Literal(5.0));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 Object result = operation.computeValue(context);

 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testNotGreaterThanOrEqual() {
 InformationSource infoSource = new InformationSource() {
 @Override
 public InfoSet getInfoSet(String expression) {
 return new DummyInfoSet(expression);
 }
 };
 EvalContext context = new EvalContext(new DummyContext(), infoSource);

 Expression arg1 = new LiteralExpression(new Literal(5.0));
 Expression arg2 = new LiteralExpression(new Literal(10.5));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 Object result = operation.computeValue(context);

 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testBothNegative() {
 InformationSource infoSource = new InformationSource() {
 @Override
 public InfoSet getInfoSet(String expression) {
 return new DummyInfoSet(expression);
 }
 };
 EvalContext context = new EvalContext(new DummyContext(), infoSource);

 Expression arg1 = new LiteralExpression(new Literal(-5.0));
 Expression arg2 = new LiteralExpression(new Literal(-10.5));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 Object result = operation.computeValue(context);

 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testOneNegative() {
 InformationSource infoSource = new InformationSource() {
 @Override
 public InfoSet getInfoSet(String expression) {
 return new DummyInfoSet(expression);
 }
 };
 EvalContext context = new EvalContext(new DummyContext(), infoSource);

 Expression arg1 = new LiteralExpression(new Literal(-5.0));
 Expression arg2 = new LiteralExpression(new Literal(10.5));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 Object result = operation.computeValue(context);

 assertEquals(Boolean.FALSE, result);
 }

 @Test
 public void testBothPositiveWithIntegers() {
 InformationSource infoSource = new InformationSource() {
 @Override
 public InfoSet getInfoSet(String expression) {
 return new DummyInfoSet(expression);
 }
 };
 EvalContext context = new EvalContext(new DummyContext(), infoSource);

 Expression arg1 = new LiteralExpression(new Literal(10));
 Expression arg2 = new LiteralExpression(new Literal(10));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 Object result = operation.computeValue(context);

 assertEquals(Boolean.TRUE, result);
 }

 @Test
 public void testBothPositiveWithFloats() {
 InformationSource infoSource = new InformationSource() {
 @Override
 public InfoSet getInfoSet(String expression) {
 return new DummyInfoSet(expression);
 }
 };
 EvalContext context = new EvalContext(new DummyContext(), infoSource);

 Expression arg1 = new LiteralExpression(new Literal(10.5));
 Expression arg2 = new LiteralExpression(new Literal(10.5));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 Object result = operation.computeValue(context);

 assertEquals(Boolean.TRUE, result);
 }

 @Test(expected = RuntimeException.class)
 public void testMismatchTypes() {
 InformationSource infoSource = new InformationSource() {
 @Override
 public InfoSet getInfoSet(String expression) {
 return new DummyInfoSet(expression);
 }
 };
 EvalContext context = new EvalContext(new DummyContext(), infoSource);

 Expression arg1 = new LiteralExpression(new Literal(10.5));
 Expression arg2 = new LiteralExpression(new Literal(5));
 CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
 operation.computeValue(context);
 }
}

class DummyContext {}

class DummyInfoSet implements InfoSet {
 String expression;

 DummyInfoSet(String expression) {
 this.expression = expression;
 }

 @Override
 public int getSize() {
 return 0;
 }

 @Override
 public boolean isEmpty() {
 return true;
 }

 @Override
 public String getExpression() {
 return expression;
 }
}

class InformationSource {
 private String expression;

 InformationSource() {
 }

 InformationSource(String expression) {
 this.expression = expression;
 }

 @Override
 public InfoSet getInfoSet(String expression) {
 return new DummyInfoSet(expression);
 }
}