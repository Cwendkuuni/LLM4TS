import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression coreOp = new CoreOperationRelationalExpressionMock();
        assertEquals(3, coreOp.getPrecedence());
    }

}

class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
    
    public CoreOperationRelationalExpressionMock() {
        super(null);
    }

}