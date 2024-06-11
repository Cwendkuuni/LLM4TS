import static org.junit.Assert.*;
import org.junit.Test;

public class UnionContextTest {

	@Test
	public void testGetDocumentOrder() {
		EvalContext[] contexts = new EvalContext[2];
		contexts[0] = new EvalContext();
		contexts[1] = new EvalContext();
		
		UnionContext unionContext = new UnionContext(null, contexts);
		int expectedResult = 1;
		int actualResult = unionContext.getDocumentOrder();
		
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testSetPosition() {
		EvalContext[] contexts = new EvalContext[2];
		contexts[0] = new EvalContext();
		contexts[1] = new EvalContext();
		
		UnionContext unionContext = new UnionContext(null, contexts);
		boolean expectedResult = true;
		boolean actualResult = unionContext.setPosition(0);
		
		assertEquals(expectedResult, actualResult);
	}

}