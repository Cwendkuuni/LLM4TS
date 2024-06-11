
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression coreOpRelExpr = new CoreOperationRelationalExpression(args) {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
        
        assertEquals(3, coreOpRelExpr.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[]{new Expression(), new Expression()};
        CoreOperationRelationalExpression coreOpRelExpr = new CoreOperationRelationalExpression(args) {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
        
        assertFalse(coreOpRelExpr.isSymmetric());
    }
}
