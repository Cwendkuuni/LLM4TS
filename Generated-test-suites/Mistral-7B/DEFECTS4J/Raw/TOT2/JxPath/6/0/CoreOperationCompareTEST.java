
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object left = new Integer(5);
        Object right = new Integer(5);
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{new ConstantExpression(left), new ConstantExpression(right)});
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        assertEquals(true, op.compare(context));
    }

    @Test
    public void testNotEqual() {
        Object left = new Integer(5);
        Object right = new Integer(6);
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{new ConstantExpression(left), new ConstantExpression(right)});
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        assertEquals(false, op.compare(context));
    }

    @Test
    public void testEqualIterators() {
        Object left = new Integer[]{1, 2, 3};
        Object right = new Integer[]{1, 2, 3};
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{
            new IteratorExpression(new IteratorExpression(new ConstantExpression(left)), null),
            new IteratorExpression(new IteratorExpression(new ConstantExpression(right)), null)
        });
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        assertEquals(true, op.compare(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareNullLeft() {
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{null, new ConstantExpression(new Integer(5))});
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        op.compare(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareNullRight() {
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{new ConstantExpression(new Integer(5)), null});
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        op.compare(context);
    }

    @Test
    public void testCompareDifferentTypes() {
        Object left = new Integer(5);
        Object right = new String("5");
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{new ConstantExpression(left), new ConstantExpression(right)});
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        assertEquals(false, op.compare(context));
    }

    @Test
    public void testEqualCollections() {
        Object left = new Integer[]{1, 2, 3};
        Object right = new Integer[]{1, 2, 3};
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{
            new IteratorExpression(new IteratorExpression(new ConstantExpression(left)), null),
            new IteratorExpression(new IteratorExpression(new ConstantExpression(right)), null)
        });
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        assertEquals(true, op.compare(context));
    }

    @Test
    public void testNotEqualCollections() {
        Object left = new Integer[]{1, 2, 3};
        Object right = new Integer[]{1, 2, 4};
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{
            new IteratorExpression(new IteratorExpression(new ConstantExpression(left)), null),
            new IteratorExpression(new IteratorExpression(new ConstantExpression(right)), null)
        });
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        assertEquals(false, op.compare(context));
    }

    @Test
    public void testFindMatch() {
        Object left = new Integer[]{1, 2, 3};
        Object right = new Integer[]{3, 2, 1};
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{
            new IteratorExpression(new IteratorExpression(new ConstantExpression(left)), null),
            new IteratorExpression(new IteratorExpression(new ConstantExpression(right)), null)
        });
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        assertEquals(true, op.compare(context));
    }

    @Test
    public void testContains() {
        Object left = new Integer[]{1, 2, 3};
        Object right = new Integer(3);
        CoreOperationCompare op = new CoreOperationCompare(new Expression[]{
            new IteratorExpression(new IteratorExpression(new ConstantExpression(left)), null),
            new ConstantExpression(right)
        });
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());
        assertEquals(true, op.compare(context));
    }
}