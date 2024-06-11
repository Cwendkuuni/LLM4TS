package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.RuntimeXPathContext;
import org.apache.commons.jxpath.ri.functions.FunctionEnvironment;
import org.apache.commons.jxpath.ri.functions.FunctionLibrary;
import org.apache.commons.jxpath.ri.parser.SimpleNamespaceContext;
import org.apache.commons.jxpath.ri.parser.XPathParser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CoreOperationLessThanTest {

    private CoreOperationLessThan expression;

    @BeforeAll
    void setup() {
        Expression arg1 = new LiteralExpression(new BigDecimal("5.5"));
        Expression arg2 = new LiteralExpression(new BigDecimal("7.7"));
        this.expression = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    void testLessThan() {
        InfoSet infoSet = new RuntimeXPathContext(new SimpleNamespaceContext(), new FunctionEnvironment(), new FunctionLibrary()).createInfoSet();
        EvalContext context = new EvalContext(new RuntimeXPathContext(new SimpleNamespaceContext(), new FunctionEnvironment(), new FunctionLibrary()), new XPathParser());

        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new BigDecimal("5.5"));
        arg2 = new LiteralExpression(new BigDecimal("5.5"));
        expression = new CoreOperationLessThan(arg1, arg2);

        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @ParameterizedTest
    @MethodSource("numericProvider")
    void testLessThanWithNumericValues(Double arg1, Double arg2, Boolean expected) {
        Expression arg1Exp = new LiteralExpression(new BigDecimal(arg1));
        Expression arg2Exp = new LiteralExpression(new BigDecimal(arg2));
        CoreOperationLessThan testExpression = new CoreOperationLessThan(arg1Exp, arg2Exp);

        InfoSet infoSet = new RuntimeXPathContext(new SimpleNamespaceContext(), new FunctionEnvironment(), new FunctionLibrary()).createInfoSet();
        EvalContext context = new EvalContext(new RuntimeXPathContext(new SimpleNamespaceContext(), new FunctionEnvironment(), new FunctionLibrary()), new XPathParser());

        Object result = testExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(expected, result);
    }

    @Test
    void testNegativeNumber() {
        Expression arg1 = new LiteralExpression(new BigDecimal("-5.5"));
        Expression arg2 = new LiteralExpression(new BigDecimal("7.7"));
        CoreOperationLessThan testExpression = new CoreOperationLessThan(arg1, arg2);

        InfoSet infoSet = new RuntimeXPathContext(new SimpleNamespaceContext(), new FunctionEnvironment(), new FunctionLibrary()).createInfoSet();
        EvalContext context = new EvalContext(new RuntimeXPathContext(new SimpleNamespaceContext(), new FunctionEnvironment(), new FunctionLibrary()), new XPathParser());

        Object result = testExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = new LiteralExpression(new BigDecimal("-5.5"));
        arg2 = new LiteralExpression(new BigDecimal("-7.7"));
        testExpression = new CoreOperationLessThan(arg1, arg2);

        result = testExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    void testNullArgument() {
        Expression arg1 = new LiteralExpression(null);
        Expression arg2 = new LiteralExpression(new BigDecimal("5.5"));
        CoreOperationLessThan testExpression = new CoreOperationLessThan(arg1, arg2);

        assertThrows(IllegalArgumentException.class, () -> testExpression.computeValue(new EvalContext(new RuntimeXPathContext(new SimpleNamespaceContext(), new FunctionEnvironment(), new FunctionLibrary()), new XPathParser())));
    }

    @Test
    void testNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(new BigDecimal("5.5"));
        CoreOperationLessThan testExpression = new CoreOperationLessThan(arg1, arg2);

        assertThrows(IllegalArgumentException.class, () -> testExpression.computeValue(new EvalContext(new RuntimeXPathContext(new SimpleNamespaceContext(), new FunctionEnvironment(), new FunctionLibrary()), new XPathParser())));
    }

    @Test
    void testInfiniteNumber() {
        Expression arg1 = new LiteralExpression(Double.POSITIVE_INFINITY);
        Expression arg2 = new LiteralExpression(new BigDecimal("5.5"));
        CoreOperationLessThan testExpression = new CoreOperationLessThan(arg1, arg2);

        assertThrows(IllegalArgumentException.class, () -> testExpression.computeValue(new EvalContext(new RuntimeXPathContext(new SimpleNamespaceContext(), new FunctionEnvironment(), new FunctionLibrary()), new XPathParser())));
    }

    private static List<Object[]> numericProvider() {
        return List.of(
                new Object[]{5.5d, 7.7d, Boolean.FALSE},
                new Object[]{5.5d, 5.5d, Boolean.FALSE},
                new Object[]{5.5d, 4.5d, Boolean.TRUE},
                new Object[]{-5.5d, 7.7d, Boolean.FALSE},
                new Object[]{-5.5d, -7.7d, Boolean.TRUE},
                new Object[]{-5.5d, 5.5d, Boolean.FALSE},
                new Object[]{Double.MIN_VALUE, Double.MIN_VALUE, Boolean.TRUE},
                new Object[]{Double.MIN_VALUE, Double.MAX_VALUE, Boolean.FALSE},
                new Object[]{Double.MAX_VALUE, Double.MAX_VALUE, Boolean.FALSE},
                new Object[]{Double.MAX_VALUE, Double.MIN_VALUE, Boolean.TRUE},
                new Object[]{Double.NaN, Double.NaN, Boolean.FALSE},
                new Object[]{Double.NaN, 5.5d, Boolean.FALSE}
        );
    }
}