import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.MyExpression();
        args[1] = new CoreOperationRelationalExpressionTest.MyExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest.MyCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    private class MyExpression implements Expression {
    
        public Object computeValue(EvalContext context) {
            return true;
        }
    
        protected final int getPrecedence() {
            return 0;
        }
    
        protected final boolean isSymmetric() {
            return true;
        }
    
        protected abstract boolean evaluateCompare(int compare);
    }
    
    private class MyCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
    
        protected MyCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }
    
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }
}