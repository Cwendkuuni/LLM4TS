import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Pointer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperation leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        CoreOperation rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        CoreOperationCompare compareOp = new CoreOperationCompare(leftExp, rightExp);
        EvalContext context = new EvalContext();
        boolean result = compareOp.equal(context, leftExp, rightExp);
        assertTrue(result);

        // Test with different types
        leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return "Test";
            }
        };

        rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        compareOp = new CoreOperationCompare(leftExp, rightExp);
        assertFalse(compareOp.equal(context, leftExp, rightExp));

        // Test with null values
        leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        compareOp = new CoreOperationCompare(leftExp, rightExp);
        assertTrue(compareOp.equal(context, leftExp, rightExp));

        leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        compareOp = new CoreOperationCompare(leftExp, rightExp);
        assertFalse(compareOp.equal(context, leftExp, rightExp));

        // Test with Collection types
        CoreOperation collectionLeftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<Object>() {{
                    add(new Integer(1));
                    add(new Integer(2));
                }};
            }
        };

        CoreOperation collectionRightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<Object>() {{
                    add(new Integer(1));
                    add(new Integer(2));
                }};
            }
        };

        compareOp = new CoreOperationCompare(collectionLeftExp, collectionRightExp);
        assertTrue(compareOp.equal(context, collectionLeftExp, collectionRightExp));

        // Test with different collections
        CoreOperation collectionLeftExp2 = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<Object>() {{
                    add(new Integer(1));
                    add(new Integer(2));
                }};
            }
        };

        CoreOperation collectionRightExp2 = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new HashSet<Object>() {{
                    add(new Integer(2));
                    add(new Integer(1));
                }};
            }
        };

        compareOp = new CoreOperationCompare(collectionLeftExp2, collectionRightExp2);
        assertFalse(compareOp.equal(context, collectionLeftExp2, collectionRightExp2));
    }
}