import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod_case1() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext evalContext = new EvalContext();
        // Test for two equal values
        assertTrue(coreOperationCompare.equal(evalContext, new Expression(5), new Expression(5)));
    }

    @Test
    public void testEqualMethod_case2() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext evalContext = new EvalContext();
        // Test for two different values
        assertFalse(coreOperationCompare.equal(evalContext, new Expression(5), new Expression(10)));
    }

    @Test
    public void testEqualMethod_case3() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext evalContext = new EvalContext();
        // Test for comparing Pointers
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();
        assertTrue(coreOperationCompare.equal(evalContext, new Expression(pointer1), new Expression(pointer1)));
        assertFalse(coreOperationCompare.equal(evalContext, new Expression(pointer1), new Expression(pointer2)));
    }

    // Add more test cases for other methods as necessary

}