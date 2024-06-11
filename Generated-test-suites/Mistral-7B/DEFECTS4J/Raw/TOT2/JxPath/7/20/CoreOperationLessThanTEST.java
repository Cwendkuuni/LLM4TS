package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

@Test
public void testLessThan() {
    CoreOperationLessThan operation = new CoreOperationLessThan(
            new ConstantExpression(new Double(5.0)),
            new ConstantExpression(new Double(3.0))
    );

    EvalContext context = new EvalContext();
    InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
    context.setInfoSet(infoSet);

    boolean result = operation.computeValue(context).booleanValue();

    assertTrue("Expected result to be true", result);
}

@Test(expected = IllegalArgumentException.class)
public void testNullArg1() {
    CoreOperationLessThan operation = new CoreOperationLessThan(
            null,
            new ConstantExpression(new Double(3.0))
    );

    EvalContext context = new EvalContext();
    InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
    context.setInfoSet(infoSet);

    operation.computeValue(context);
}

@Test(expected = IllegalArgumentException.class)
public void testNullArg2() {
    CoreOperationLessThan operation = new CoreOperationLessThan(
            new ConstantExpression(new Double(5.0)),
            null
    );

    EvalContext context = new EvalContext();
    InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
    context.setInfoSet(infoSet);

    operation.computeValue(context);
}

@Test
public void testLessThanWithNaN() {
    CoreOperationLessThan operation = new CoreOperationLessThan(
            new ConstantExpression(new Double(Double.NaN)),
            new ConstantExpression(new Double(3.0))
    );

    EvalContext context = new EvalContext();
    InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
    context.setInfoSet(infoSet);

    boolean result = operation.computeValue(context).booleanValue();

    assertFalse("Expected result to be false", result);
}

@Test
public void testLessThanWithZeroAndNegativeNumber() {
    CoreOperationLessThan operation = new CoreOperationLessThan(
            new ConstantExpression(new Double(0.0)),
            new ConstantExpression(new Double(-1.0))
    );

    EvalContext context = new EvalContext();
    InfoSet infoSet = InfoSetUtil.createEmptyInfoSet();
    context.setInfoSet(infoSet);

    boolean result = operation.computeValue(context).booleanValue();

    assertTrue("Expected result to be true", result);
}

}