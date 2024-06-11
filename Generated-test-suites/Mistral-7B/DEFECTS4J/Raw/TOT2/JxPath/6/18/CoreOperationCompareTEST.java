import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Pointer;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @org.junit.Test
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

        CoreOperationCompare compare = new CoreOperationCompare(leftExp, rightExp);
        EvalContext context = new EvalContext();

        assertTrue(compare.equal(context, leftExp, rightExp));

        leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(2);
            }
        };

        assertFalse(compare.equal(context, leftExp, rightExp));

        leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(null);
            }
        };

        rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        assertFalse(compare.equal(context, leftExp, rightExp));

        leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(null);
            }
        };

        assertFalse(compare.equal(context, leftExp, rightExp));

        leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new SelfContext();
            }
        };

        rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new SelfContext();
            }
        };

        assertTrue(compare.equal(context, leftExp, rightExp));

        leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new SelfContext();
            }
        };

        rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new InitialContext();
            }
        };

        assertFalse(compare.equal(context, leftExp, rightExp));

        leftExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        rightExp = new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                throw new RuntimeException();
            }
        };

        try {
            compare.equal(context, leftExp, rightExp);
            fail("Expected exception");
        } catch (RuntimeException e) {
            // Expected
        }
    }

    // Add test cases for other methods here

    // 
    // 
}