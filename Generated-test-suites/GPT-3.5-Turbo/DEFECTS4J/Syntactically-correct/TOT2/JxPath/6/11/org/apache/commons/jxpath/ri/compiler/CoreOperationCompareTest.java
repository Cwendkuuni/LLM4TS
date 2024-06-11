import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperation = new CoreOperationCompare(arg1, arg2);
        boolean result = coreOperation.equal(context, leftExpression, rightExpression);
        // Add assertions here
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperation = new CoreOperationCompare(arg1, arg2);
        boolean result = coreOperation.contains(iterator, value);
        // Add assertions here
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperation = new CoreOperationCompare(arg1, arg2);
        boolean result = coreOperation.findMatch(leftIterator, rightIterator);
        // Add assertions here
    }

    @Test
    public void testEqualObjectObject() {
        CoreOperationCompare coreOperation = new CoreOperationCompare(arg1, arg2);
        boolean result = coreOperation.equalObjectObject(object1, object2);
        // Add assertions here
    }

    // Add more test cases for other public methods if needed

}