package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpressionPrecedence() {
        CoreOperationRelationalExpression expr1 = new CoreOperationRelationalExpression(new CoreOperation[] {});
        assertEquals(3, expr1.getPrecedence());

        CoreOperationRelationalExpression expr2 = new CoreOperationAdd(new CoreOperation[] {
                new CoreOperationRelationalExpression(new CoreOperation[] {}),
                new CoreOperationRelationalExpression(new CoreOperation[] {})
        });
        assertEquals(3, expr2.getPrecedence());

        CoreOperationRelationalExpression expr3 = new CoreOperationMultiply(new CoreOperation[] {
                new CoreOperationRelationalExpression(new CoreOperation[] {}),
                new CoreOperationRelationalExpression(new CoreOperation[] {})
        });
        assertEquals(2, expr3.getPrecedence());

        CoreOperationRelationalExpression expr4 = new CoreOperationDivide(new CoreOperation[] {
                new CoreOperationRelationalExpression(new CoreOperation[] {}),
                new CoreOperationRelationalExpression(new CoreOperation[] {})
        });
        assertEquals(2, expr4.getPrecedence());

        CoreOperationRelationalExpression expr5 = new CoreOperationMod(new CoreOperation[] {
                new CoreOperationRelationalExpression(new CoreOperation[] {}),
                new CoreOperationRelationalExpression(new CoreOperation[] {})
        });
        assertEquals(2, expr5.getPrecedence());

        CoreOperationRelationalExpression expr6 = new CoreOperationSubtract(new CoreOperation[] {
                new CoreOperationRelationalExpression(new CoreOperation[] {}),
                new CoreOperationRelationalExpression(new CoreOperation[] {})
        });
        assertEquals(3, expr6.getPrecedence());
    }

    @Test
    public void testCoreOperationRelationalExpressionSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan(new CoreOperation[] {
                new CoreOperationConstant(1),
                new CoreOperationConstant(2)
        });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationGreaterThanOrEqual(new CoreOperation[] {
                new CoreOperationConstant(1),
                new CoreOperationConstant(2)
        });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationLessThan(new CoreOperation[] {
                new CoreOperationConstant(1),
                new CoreOperationConstant(2)
        });
        assertFalse(expr.isSymmetric());

        expr = new CoreOperationLessThanOrEqual(new CoreOperation[] {
                new CoreOperationConstant(1),
                new CoreOperationConstant(2)
        });
        assertFalse(expr.isSymmetric());
    }
}