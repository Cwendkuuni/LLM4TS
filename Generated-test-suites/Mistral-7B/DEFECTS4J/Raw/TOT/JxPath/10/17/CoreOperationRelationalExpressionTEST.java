import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationArgument;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.StaticContext;
import org.apache.commons.jxpath.ri.compiler.StaticContextFactory;
import org.apache.commons.jxpath.ri.compiler.TestSupport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private ExpressionFactory expressionFactory;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        expressionFactory = new ExpressionFactory();
    }

    @Test
    public void testGreaterThan() {
        CoreOperation expression = expressionFactory.createGreaterThan(
                createExpression("1.0"), createExpression("2.0")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanOrEqual() {
        CoreOperation expression = expressionFactory.createGreaterThanOrEqual(
                createExpression("1.0"), createExpression("1.0")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testLessThan() {
        CoreOperation expression = expressionFactory.createLessThan(
                createExpression("1.0"), createExpression("2.0")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testLessThanOrEqual() {
        CoreOperation expression = expressionFactory.createLessThanOrEqual(
                createExpression("1.0"), createExpression("1.0")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGreaterThanWithNonNumericValues() {
        CoreOperation expression = expressionFactory.createGreaterThan(
                createExpression("StringA"), createExpression("StringB")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanWithInvalidArguments() {
        CoreOperation expression = expressionFactory.createGreaterThan(
                createExpression(null), createExpression("2.0")
        );

        expression.computeValue(new StaticContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreaterThanWithInvalidArguments2() {
        CoreOperation expression = expressionFactory.createGreaterThan(
                createExpression("1.0"), null
        );

        expression.computeValue(new StaticContext());
    }

    @Test
    public void testContains() {
        CoreOperation expression = expressionFactory.createContains(
                createIterator("1", "2", "3"), createExpression("2")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testContainsWithEmptyIterators() {
        CoreOperation expression = expressionFactory.createContains(
                createIterator(), createExpression("2")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testContainsWithSingleElementIterator() {
        CoreOperation expression = expressionFactory.createContains(
                createIterator("1"), createExpression("1")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testContainsWithNonMatchingValues() {
        CoreOperation expression = expressionFactory.createContains(
                createIterator("1", "2", "3"), createExpression("4")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testFindMatch() {
        CoreOperation expression = expressionFactory.createFindMatch(
                createIterator("1", "2", "3"), createIterator("2", "3")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testFindMatchWithEmptyIterators() {
        CoreOperation expression = expressionFactory.createFindMatch(
                createIterator(), createIterator()
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testFindMatchWithNonMatchingIterators() {
        CoreOperation expression = expressionFactory.createFindMatch(
                createIterator("1", "2", "3"), createIterator("4", "5")
        );

        CoreOperationRelationalExpression relationalExpression = (CoreOperationRelationalExpression) expression;
        StaticContext context = StaticContextFactory.newInstance();

        Object result = relationalExpression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    private CoreOperationArgument createExpression(Object value) {
        List<CoreOperationArgument> arguments = new ArrayList<>();
        arguments.add(CoreOperationArgument.newInstance(
                new ExpressionFactory.ConstantExpression(value)
        ));
        return CoreOperationArgument.newInstance(arguments);
    }

    private Iterator createIterator() {
        return Collections.emptyIterator();
    }

    private Iterator createIterator(String... values) {
        List<Object> list = new ArrayList<>();
        for (String value : values) {
            list.add(value);
        }
        return list.iterator();
    }
}