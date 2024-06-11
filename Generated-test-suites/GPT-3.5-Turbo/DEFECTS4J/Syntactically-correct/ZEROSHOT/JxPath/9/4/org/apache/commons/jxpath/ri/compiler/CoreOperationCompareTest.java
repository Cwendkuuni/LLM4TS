import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

	@Test
	public void testEqual() {
		CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
		EvalContext context = new EvalContext();
		Expression left = new Expression();
		Expression right = new Expression();
		boolean result = coreOperationCompare.equal(context, left, right);
		// TODO: Add assertions
	}

	@Test
	public void testContains() {
		CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
		Iterator it = new Iterator();
		Object value = new Object();
		boolean result = coreOperationCompare.contains(it, value);
		// TODO: Add assertions
	}

	@Test
	public void testFindMatch() {
		CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
		Iterator lit = new Iterator();
		Iterator rit = new Iterator();
		boolean result = coreOperationCompare.findMatch(lit, rit);
		// TODO: Add assertions
	}

	@Test
	public void testEqual() {
		CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
		Object l = new Object();
		Object r = new Object();
		boolean result = coreOperationCompare.equal(l, r);
		// TODO: Add assertions
	}

}