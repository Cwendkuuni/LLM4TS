import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        // Test when delegate answer returns non-null value
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn("Mock value");
        Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
        assertEquals("Mock value", result);

        // Test when delegate answer returns null value and canImposterise returns true
        Answer<Object> delegate2 = Mockito.mock(Answer.class);
        Mockito.when(delegate2.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);
        ClassImposterizer imposterizer = Mockito.mock(ClassImposterizer.class);
        Mockito.when(imposterizer.canImposterise(Mockito.any(Class.class))).thenReturn(true);

        Class<?> type = Mockito.mock(Class.class);
        Object result2 = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));

        assertTrue(result2 instanceof MethodInterceptor);

        // Test when delegate answer returns null value and canImposterise returns false
        Answer<Object> delegate3 = Mockito.mock(Answer.class);
        Mockito.when(delegate3.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);
        ClassImposterizer imposterizer2 = Mockito.mock(ClassImposterizer.class);
        Mockito.when(imposterizer2.canImposterise(Mockito.any(Class.class))).thenReturn(false);

        Class<?> type2 = Mockito.mock(Class.class);
        Object result3 = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));

        assertNull(result3);
    }
}