import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class UnionContextTest {
    private UnionContext unionContext;
    private EvalContext mockParentContext;
    private EvalContext[] mockContexts;

    @Before
    public void setUp() {
        mockParentContext = Mockito.mock(EvalContext.class);
        mockContexts = new EvalContext[]{
                Mockito.mock(EvalContext.class),
                Mockito.mock(EvalContext.class)
        };
        unionContext = new UnionContext(mockParentContext, mockContexts);
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        int position = 1;

        Mockito.when(mockContexts[0].nextSet()).thenReturn(true);
        Mockito.when(mockContexts[0].nextNode()).thenReturn(true);
        Mockito.when(mockContexts[0].getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));

        Mockito.when(mockContexts[1].nextSet()).thenReturn(true);
        Mockito.when(mockContexts[1].nextNode()).thenReturn(true);
        Mockito.when(mockContexts[1].getCurrentNodePointer()).thenReturn(Mockito.mock(NodePointer.class));

        boolean result = unionContext.setPosition(position);

        assertEquals(true, result);
    }
}