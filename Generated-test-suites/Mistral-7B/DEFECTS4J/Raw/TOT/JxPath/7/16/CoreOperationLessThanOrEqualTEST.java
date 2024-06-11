import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testLessThanOrEqual_True() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericExpression(new ConstantExpression(1.0)),
                new CoreNumericExpression(new ConstantExpression(2.0)));
        InfoSet infoSet = new InfoSet();
        EvaluationContext context = new EvaluationContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanOrEqual_False() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericExpression(new ConstantExpression(3.0)),
                new CoreNumericExpression(new ConstantExpression(2.0)));
        InfoSet infoSet = new InfoSet();
        EvaluationContext context = new EvaluationContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testLessThanOrEqual_Equal() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericExpression(new ConstantExpression(2.0)),
                new CoreNumericExpression(new ConstantExpression(2.0)));
        InfoSet infoSet = new InfoSet();
        EvaluationContext context = new EvaluationContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testLessThanOrEqual_NaN() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericExpression(new ConstantExpression(Double.NaN)),
                new CoreNumericExpression(new ConstantExpression(Double.NaN)));
        InfoSet infoSet = new InfoSet();
        EvaluationContext context = new EvaluationContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @ParameterizedTest(name = "{index}: testLessThanOrEqual_Integer_{0}LessThanOrEqual_{1}")
    @ArgumentsSource(IntegerLessThanOrEqualArguments.class)
    public void testLessThanOrEqual_Integer(int a, int b) {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericExpression(new ConstantExpression(a)),
                new CoreNumericExpression(new ConstantExpression(b)));
        InfoSet infoSet = new InfoSet();
        EvaluationContext context = new EvaluationContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(InfoSetUtil.booleanValue(result), a <= b);
    }

    @ParameterizedTest(name = "{index}: testLessThanOrEqual_Double_{0}LessThanOrEqual_{1}")
    @ArgumentsSource(DoubleLessThanOrEqualArguments.class)
    public void testLessThanOrEqual_Double(double a, double b) {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(
                new CoreNumericExpression(new ConstantExpression(a)),
                new CoreNumericExpression(new ConstantExpression(b)));
        InfoSet infoSet = new InfoSet();
        EvaluationContext context = new EvaluationContext(infoSet);
        Object result = operation.computeValue(context);
        assertEquals(InfoSetUtil.booleanValue(result), a <= b);
    }

    private static Stream<Arguments> DoubleLessThanOrEqualArguments() {
        return Stream.of(
                arguments(1.0, 1.0),
                arguments(1.0, 2.0),
                arguments(1.0, 1.5),
                arguments(1.0, Double.NaN),
                arguments(Double.NaN, 1.0),
                arguments(Double.NaN, Double.NaN)
        );
    }

    private static Stream<Arguments> IntegerLessThanOrEqualArguments() {
        return Stream.of(
                arguments(1, 1),
                arguments(1, 2),
                arguments(1, 1),
                arguments(Integer.MIN_VALUE, Integer.MIN_VALUE),
                arguments(Integer.MAX_VALUE, Integer.MAX_VALUE),
                arguments(Integer.MIN_VALUE, Integer.MAX_VALUE),
                arguments(Integer.MAX_VALUE, Integer.MIN_VALUE)
        );
    }

    private static Arguments arguments(double a, double b) {
        return Arguments.of(new Object[]{a, b});
    }

    private static Arguments arguments(int a, int b) {
        return Arguments.of(new Object[]{a, b});
    }
}