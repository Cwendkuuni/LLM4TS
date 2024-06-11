import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Expression left = new Constant(5);
        Expression right = new Constant(5);

        assertTrue(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        Object value = 2;

        assertTrue(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(4, 5, 2).iterator();

        assertTrue(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualPointer() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Pointer pointer1 = new Pointer("test1");
        Pointer pointer2 = new Pointer("test1");

        assertTrue(coreOperationCompare.equal(pointer1, pointer2));
    }
}