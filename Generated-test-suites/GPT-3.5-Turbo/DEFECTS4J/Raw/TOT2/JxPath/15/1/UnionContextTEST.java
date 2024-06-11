import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class UnionContextTest {

    @Test
    public void testConstructor() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        EvalContext[] contexts = new EvalContext[]{Mockito.mock(EvalContext.class), Mockito.mock(EvalContext.class)};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        
        assertEquals(parentContext, unionContext.getParentContext());
        assertArrayEquals(contexts, unionContext.getContexts());
        assertFalse(unionContext.isPrepared());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        EvalContext[] contexts = new EvalContext[]{Mockito.mock(EvalContext.class)};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertEquals(0, unionContext.getDocumentOrder());
        
        EvalContext[] multipleContexts = new EvalContext[]{Mockito.mock(EvalContext.class), Mockito.mock(EvalContext.class)};
        UnionContext multipleUnionContext = new UnionContext(parentContext, multipleContexts);
        
        assertEquals(1, multipleUnionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        EvalContext[] contexts = new EvalContext[]{Mockito.mock(EvalContext.class), Mockito.mock(EvalContext.class)};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertFalse(unionContext.isPrepared());
        assertTrue(unionContext.setPosition(0));
        assertTrue(unionContext.isPrepared());
    }

    @Test
    public void testSetPositionWithEmptyNodeSet() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        EvalContext[] emptyContexts = new EvalContext[]{};
        UnionContext emptyUnionContext = new UnionContext(parentContext, emptyContexts);

        assertFalse(emptyUnionContext.isPrepared());
        assertFalse(emptyUnionContext.setPosition(0));
        assertFalse(emptyUnionContext.isPrepared());
    }
}