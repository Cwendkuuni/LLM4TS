import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class UnionContextTest {

    @Test
    public void testGetDocumentOrder() {
        // Create the two mock contexts
        EvalContext context1 = mock(EvalContext.class);
        EvalContext context2 = mock(EvalContext.class);
        
        // Create the UnionContext with the mock contexts
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        
        // Set the expected behavior for getDocumentOrder
        when(context1.getDocumentOrder()).thenReturn(0);
        when(context2.getDocumentOrder()).thenReturn(1);
        
        // Verify the expected behavior
        assert unionContext.getDocumentOrder() == 1;
        
        // Verify that the getDocumentOrder method was called on each mock context
        verify(context1, times(1)).getDocumentOrder();
        verify(context2, times(1)).getDocumentOrder();
    }

    @Test
    public void testSetPosition_PreparedFalse() {
        // Create the two mock contexts
        EvalContext context1 = mock(EvalContext.class);
        EvalContext context2 = mock(EvalContext.class);
        
        // Create the UnionContext with the mock contexts
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        
        // Set the expected behavior for setPosition on the mock contexts
        when(context1.setPosition(1)).thenReturn(true);
        when(context2.setPosition(1)).thenReturn(true);
        
        // Verify the expected behavior
        assert unionContext.setPosition(1);
        
        // Verify that the setPosition method was called on each mock context
        verify(context1, times(1)).setPosition(1);
        verify(context2, times(1)).setPosition(1);
    }

    @Test
    public void testSetPosition_PreparedTrue() {
        // Create the two mock contexts
        EvalContext context1 = mock(EvalContext.class);
        EvalContext context2 = mock(EvalContext.class);
        
        // Create the UnionContext with the mock contexts
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        
        // Set the prepared flag to true
        unionContext.setPrepared(true);
        
        // Set the expected behavior for setPosition on the mock contexts
        when(context1.setPosition(1)).thenReturn(true);
        when(context2.setPosition(1)).thenReturn(true);
        
        // Verify the expected behavior
        assert unionContext.setPosition(1);
        
        // Verify that the setPosition method was called on each mock context
        verify(context1, never()).setPosition(1);
        verify(context2, never()).setPosition(1);
    }

    @Test
    public void testSetPosition_PreparedFalse_NotAllPositionsTrue() {
        // Create the two mock contexts
        EvalContext context1 = mock(EvalContext.class);
        EvalContext context2 = mock(EvalContext.class);
        
        // Create the UnionContext with the mock contexts
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        
        // Set the expected behavior for setPosition on the first mock context
        when(context1.setPosition(1)).thenReturn(true);
        when(context1.setPosition(2)).thenReturn(true);
        
        // Set the expected behavior for setPosition on the second mock context
        when(context2.setPosition(1)).thenReturn(true);
        when(context2.setPosition(2)).thenReturn(false);
        
        // Verify the expected behavior
        assert !unionContext.setPosition(2);
        
        // Verify that the setPosition method was called on each mock context
        verify(context1, times(1)).setPosition(1);
        verify(context1, times(1)).setPosition(2);
        verify(context2, times(1)).setPosition(1);
        verify(context2, times(1)).setPosition(2);
    }
}