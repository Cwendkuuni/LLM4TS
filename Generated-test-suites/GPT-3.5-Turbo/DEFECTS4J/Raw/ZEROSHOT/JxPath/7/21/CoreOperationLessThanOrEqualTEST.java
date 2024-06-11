import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setContextNode(arg1);
        context.setContextPosition(1);
        context.setContextSize(2);

        Object result = lessThanOrEqual.computeValue(context);

        // Tests for different scenarios
        // Modify the values as per your requirement

        // Scenario: l and r are equal
        double l1 = 1.0;
        double r1 = 1.0;
        context.setExpression(arg1);
        context.setValue(Double.valueOf(l1));
        double expectedResult1 = InfoSetUtil.doubleValue(arg1.computeValue(context));
        context.setExpression(arg2);
        context.setValue(Double.valueOf(r1));
        double expectedResult2 = InfoSetUtil.doubleValue(arg2.computeValue(context));

        Object expectedValue1 = expectedResult1 <= expectedResult2 ? Boolean.TRUE : Boolean.FALSE;
        assertEquals(expectedValue1, result);

        // Scenario: l is less than r
        double l2 = 1.0;
        double r2 = 2.0;
        context.setExpression(arg1);
        context.setValue(Double.valueOf(l2));
        double expectedResult3 = InfoSetUtil.doubleValue(arg1.computeValue(context));
        context.setExpression(arg2);
        context.setValue(Double.valueOf(r2));
        double expectedResult4 = InfoSetUtil.doubleValue(arg2.computeValue(context));

        Object expectedValue2 = expectedResult3 <= expectedResult4 ? Boolean.TRUE : Boolean.FALSE;
        assertEquals(expectedValue2, result);

        // Scenario: l is greater than r
        double l3 = 2.0;
        double r3 = 1.0;
        context.setExpression(arg1);
        context.setValue(Double.valueOf(l3));
        double expectedResult5 = InfoSetUtil.doubleValue(arg1.computeValue(context));
        context.setExpression(arg2);
        context.setValue(Double.valueOf(r3));
        double expectedResult6 = InfoSetUtil.doubleValue(arg2.computeValue(context));

        Object expectedValue3 = expectedResult5 <= expectedResult6 ? Boolean.TRUE : Boolean.FALSE;
        assertEquals(expectedValue3, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(arg1, arg2);

        String expectedSymbol = "<=";
        String result = lessThanOrEqual.getSymbol();

        assertEquals(expectedSymbol, result);
    }
}