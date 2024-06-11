import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.spy;

import org.junit.Test;

public class ReturnsSmartNullsTest {
    @Test
    public void testAnswer() throws Throwable {
        // Create a mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Create a ReturnsSmartNulls object
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Test when delegate answer returns null
        Answer<Object> delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals(null, result);
        verify(delegate, times(1)).answer(invocation);
        
        // Test when delegate answer returns non-null value
        when(delegate.answer(invocation)).thenReturn("result");
        result = returnsSmartNulls.answer(invocation);
        assertEquals("result", result);
        verify(delegate, times(1)).answer(invocation);
        
        // Test when ClassImposterizer.INSTANCE.canImposterise returns false
        ClassImposterizer impostrizer = mock(ClassImposterizer.class);
        when(impostrizer.canImposterise(any(Class.class))).thenReturn(false);
        ReturnsSmartNulls returnsSmartNullsSpy = spy(returnsSmartNulls);
        when(returnsSmartNullsSpy.getClassImposterizerInstance()).thenReturn(impostrizer);
        result = returnsSmartNullsSpy.answer(invocation);
        assertEquals(null, result);
        verify(impostrizer, times(1)).canImposterise(any(Class.class));
        
        // Test when ClassImposterizer.INSTANCE.canImposterise returns true
        when(impostrizer.canImposterise(any(Class.class))).thenReturn(true);
        when(delegate.answer(invocation)).thenReturn("result");
        when(returnsSmartNullsSpy.getClassImposterizerInstance()).thenReturn(impostrizer);
        result = returnsSmartNullsSpy.answer(invocation);
        assertEquals("SmartNull returned by unstubbed method on mock", result);
        verify(impostrizer, times(1)).canImposterise(any(Class.class));
        verify(delegate, times(2)).answer(invocation);
        
        // Test when new ObjectMethodsGuru().isToString returns true
        ObjectMethodsGuru guru = mock(ObjectMethodsGuru.class);
        when(guru.isToString(any(Method.class))).thenReturn(true);
        ReturnsSmartNulls returnsSmartNullsSpy2 = spy(returnsSmartNulls);
        when(returnsSmartNullsSpy2.getGuruInstance()).thenReturn(guru);
        result = returnsSmartNullsSpy2.answer(invocation);
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
        verify(guru, times(1)).isToString(any(Method.class));
        
        // Test throwing exception
        new Reporter().smartNullPointerException(invocation);
        ObjectMethodsGuru guru2 = mock(ObjectMethodsGuru.class);
        when(guru2.isToString(any(Method.class))).thenReturn(false);
        ReturnsSmartNulls returnsSmartNullsSpy3 = spy(returnsSmartNulls);
        when(returnsSmartNullsSpy3.getGuruInstance()).thenReturn(guru2);
        result = returnsSmartNullsSpy3.answer(invocation);
        assertEquals(null, result);
        verify(guru2, times(1)).isToString(any(Method.class));
    }
}