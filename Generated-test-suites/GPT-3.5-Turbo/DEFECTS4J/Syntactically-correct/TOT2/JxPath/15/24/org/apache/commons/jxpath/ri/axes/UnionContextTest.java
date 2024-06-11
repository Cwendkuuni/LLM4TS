import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {
    
    private UnionContext unionContext;
    private EvalContext context1;
    private EvalContext context2;

    @Before
    public void setUp() {
        context1 = new EvalContext();
        context2 = new EvalContext();
        unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullParent() {
        new UnionContext(null, new EvalContext[]{null});
    }

    @Test
    public void testSetPosition() {
        assertTrue(unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionWithoutPreparation() {
        assertFalse(unionContext.setPosition(1));
    }

    @Test
    public void testSetPositionAfterPreparation() {
        unionContext.setPosition(1);
        assertTrue(unionContext.setPosition(2));
    }

}