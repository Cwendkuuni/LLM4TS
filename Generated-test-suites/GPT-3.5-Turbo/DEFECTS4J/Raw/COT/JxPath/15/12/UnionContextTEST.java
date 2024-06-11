import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnionContextTest {

    private UnionContext unionContext;

    @Before
    public void setUp() {
        EvalContext context1 = new EvalContext(); // replace with actual EvalContext
        EvalContext context2 = new EvalContext(); // replace with actual EvalContext

        unionContext = new UnionContext(null, new EvalContext[] {context1, context2});
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        unionContext.setPosition(1);

        // Add assertions here
    }
}