import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class ExpressionTest {

    @Test
    public void testGetExternalFunctions() {
        Expression exp = new Expression("test");
        List externalFunctions = new ArrayList();
        externalFunctions.add("func1");
        externalFunctions.add("func2");
        externalFunctions.add("func3");
        exp.externalFunctions = externalFunctions;
        
        assertEquals("getExternalFunctions should return list of external functions", externalFunctions, exp.getExternalFunctions());
    }

    @Test
    public void testDoEvaluationStack() {
        Expression exp = new Expression("test");
        exp.stack = new ExpressionStack();
        exp.stack.push("1");
        exp.stack.push("+");
        exp.stack.push("2");
        
        exp.doEvaluationStack();
        
        assertEquals("Evaluation should return correct result", "1 2 +", exp.stack.printStack());
    }

    // Include more test cases for other methods

}