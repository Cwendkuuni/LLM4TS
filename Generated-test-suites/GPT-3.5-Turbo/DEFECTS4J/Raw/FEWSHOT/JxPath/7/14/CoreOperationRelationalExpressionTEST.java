package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args1 = {new Expression(), new Expression()};
        CoreOperationRelationalExpression core1 = new CoreOperationRelationalExpressionImpl(args1);
        assertEquals(3, core1.getPrecedence());

        Expression[] args2 = {new Expression()};
        CoreOperationRelationalExpression core2 = new CoreOperationRelationalExpressionImpl(args2);
        assertEquals(3, core2.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args1 = {new Expression(), new Expression()};
        CoreOperationRelationalExpression core1 = new CoreOperationRelationalExpressionImpl(args1);
        assertFalse(core1.isSymmetric());

        Expression[] args2 = {new Expression()};
        CoreOperationRelationalExpression core2 = new CoreOperationRelationalExpressionImpl(args2);
        assertFalse(core2.isSymmetric());
    }
}

class Expression {}

class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
    public CoreOperationRelationalExpressionImpl(Expression[] args) {
        super(args);
    }
}