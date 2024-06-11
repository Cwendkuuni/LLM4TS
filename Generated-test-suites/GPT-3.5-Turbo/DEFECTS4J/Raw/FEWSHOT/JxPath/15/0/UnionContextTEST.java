import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = Mockito.mock(EvalContext.class);
        contexts[1] = Mockito.mock(EvalContext.class);

        UnionContext unionContext = new UnionContext(null, contexts);
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext[] contexts = new EvalContext[2];
        contexts[0] = Mockito.mock(EvalContext.class);
        contexts[1] = Mockito.mock(EvalContext.class);
        Mockito.when(contexts[0].nextSet()).thenReturn(true);
        Mockito.when(contexts[1].nextSet()).thenReturn(true);
        Mockito.when(contexts[0].nextNode()).thenReturn(true);
        Mockito.when(contexts[0].getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));

        UnionContext unionContext = new UnionContext(null, contexts);
        unionContext.setPosition(1);
        Mockito.verify(contexts[0], Mockito.times(1)).nextSet();
        Mockito.verify(contexts[1], Mockito.times(1)).nextSet();
        Mockito.verify(contexts[0], Mockito.times(1)).nextNode();
        Mockito.verify(contexts[0], Mockito.times(1)).getCurrentNodePointer();
    }
}