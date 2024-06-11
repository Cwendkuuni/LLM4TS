import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new MockPointer());
        assertTrue(expression.computeValue(context));

        context.setSingleNodePointer(new MockPointer("test"));
        assertFalse(expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        assertFalse(expression.isSymmetric());
    }

    private class MockExpression extends Expression {
        public MockExpression() {
            super(null);
        }
        public Object computeValue(EvalContext context) {
            return null;
        }
        protected int getPrecedence() {
            return 0;
        }
    }

    private class MockPointer extends NodePointer {
        public MockPointer() {
            super(null);
        }
        public MockPointer(String value) {
            super(value);
        }
        public boolean isCollection() {
            return false;
        }
        protected Object getValue() {
            return null;
        }
        protected Object getImmediateNode() {
            return null;
        }
        protected boolean isActual() {
            return false;
        }
        protected boolean isLeaf() {
            return false;
        }
        protected NodePointer createChild(Object bean) {
            return null;
        }
        protected NodePointer createChild(Object bean, QName name, int index) {
            return null;
        }
    }
}