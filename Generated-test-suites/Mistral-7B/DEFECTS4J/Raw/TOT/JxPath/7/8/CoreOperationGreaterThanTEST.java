import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CoreOperationGreaterThanTest {

    @Test
    public void testGreaterThan() {
        // Test with double values
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new CoreNumberExpression(5.0), new CoreNumberExpression(3.0)
        );

        EvalContext context = new EvalContext(new CompilerContext(), new InfoSet(), new ArrayList<>());
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with double values, small difference
        expression = new CoreOperationGreaterThan(
                new CoreNumberExpression(5.0001), new CoreNumberExpression(5.0)
        );

        context = new EvalContext(new CompilerContext(), new InfoSet(), new ArrayList<>());
        result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        // Test with double values, equal
        expression = new CoreOperationGreaterThan(
                new CoreNumberExpression(5.0), new CoreNumberExpression(5.0)
        );

        context = new EvalContext(new CompilerContext(), new InfoSet(), new ArrayList<>());
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        // Test with different data types
        expression = new CoreOperationGreaterThan(
                new CoreNumberExpression(5.0), new CoreStringExpression("5")
        );

        context = new EvalContext(new CompilerContext(), new InfoSet(), new ArrayList<>());
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        // Test with null values
        expression = new CoreOperationGreaterThan(
                new CoreNumberExpression(5.0), new CoreNullExpression()
        );

        context = new EvalContext(new CompilerContext(), new InfoSet(), new ArrayList<>());
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);

        // Test with empty sequence
        expression = new CoreOperationGreaterThan(
                new CoreNumberExpression(5.0), new CoreEmptySequenceExpression()
        );

        context = new EvalContext(new CompilerContext(), new InfoSet(), new ArrayList<>());
        result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testErrorScenarios() {
        // Test with invalid number of arguments
        CoreOperationGreaterThan expression = null;
        try {
            expression = new CoreOperationGreaterThan(new CoreNumberExpression(5.0));
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertNotNull(expression);

        // Test with invalid argument types
        expression = new CoreOperationGreaterThan(
                new CoreStringExpression("5"), new CoreNumberExpression(3.0)
        );

        EvalContext context = new EvalContext(new CompilerContext(), new InfoSet(), new ArrayList<>());
        Object result = expression.computeValue(context);
        assertFalse(result instanceof Boolean);
        Assert.assertThrows(ClassCastException.class, () -> InfoSetUtil.doubleValue(args[0].computeValue(context)));
    }

    @Test
    public void testSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
                new CoreNumberExpression(5.0), new CoreNumberExpression(3.0)
        );

        String symbol = expression.getSymbol();
        assertEquals(">", symbol);
    }
}