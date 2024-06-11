import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_Equals() {
        Expression[] args = new Expression[]{new Constant("10"), new Constant("10")};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        EvalContext evalContext = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(evalContext));
    }

    @Test
    public void testComputeValue_LessThan() {
        Expression[] args = new Expression[]{new Constant("5"), new Constant("10")};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        EvalContext evalContext = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(evalContext));
    }

    @Test
    public void testComputeValue_GreaterThan() {
        Expression[] args = new Expression[]{new Constant("15"), new Constant("10")};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext evalContext = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(evalContext));
    }

    @Test
    public void testComputeValue_CollectionContainsElement() {
        Collection leftCollection = new ArrayList();
        leftCollection.add("element1");
        leftCollection.add("element2");
        Expression[] args = new Expression[]{new Constant(leftCollection), new Constant("element1")};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        EvalContext evalContext = new EvalContext();
        assertEquals(Boolean.TRUE, coreOperationRelationalExpression.computeValue(evalContext));
    }

    @Test
    public void testComputeValue_CollectionDoesNotContainElement() {
        Collection leftCollection = new ArrayList();
        leftCollection.add("element1");
        leftCollection.add("element2");
        Expression[] args = new Expression[]{new Constant(leftCollection), new Constant("element3")};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        EvalContext evalContext = new EvalContext();
        assertEquals(Boolean.FALSE, coreOperationRelationalExpression.computeValue(evalContext));
    }

}