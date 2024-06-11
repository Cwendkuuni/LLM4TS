import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Test when defaultReturnValue is not null
        Object defaultReturnValue = new Object();
        Mockito.when(returnsSmartNulls.delegate.answer(Mockito.any())).thenReturn(defaultReturnValue);
        Object result = returnsSmartNulls.answer(Mockito.any());
        assert(result == defaultReturnValue);
        
        // Test when defaultReturnValue is null and type is mockable
        Class<?> mockableType = returnsSmartNulls.getClass();
        Mockito.when(returnsSmartNulls.delegate.answer(Mockito.any())).thenReturn(null);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(mockableType)).thenReturn(true);
        Object smartNull = new Object();
        Mockito.when(ClassImposterizer.INSTANCE.imposterise(Mockito.any(), Mockito.any())).thenReturn(smartNull);
        result = returnsSmartNulls.answer(Mockito.any());
        assert(result == smartNull);
        
        // Test when defaultReturnValue is null and type is not mockable
        Class<?> unmockableType = Object.class;
        Mockito.when(returnsSmartNulls.delegate.answer(Mockito.any())).thenReturn(null);
        Mockito.when(ClassImposterizer.INSTANCE.canImposterise(unmockableType)).thenReturn(false);
        result = returnsSmartNulls.answer(Mockito.any());
        assert(result == null);
    }
    
}