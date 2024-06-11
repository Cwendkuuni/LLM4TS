import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_NullDefaultReturnValue() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);

        assertSame(null, result);
    }

    @Test
    public void testAnswer_NotNullDefaultReturnValue() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(new Object());

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.delegate = delegate;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testAnswer_MockableType() throws Throwable {
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(any(Class.class))).thenReturn(true);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.imposterizer = imposterizer;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testAnswer_NotMockableType() throws Throwable {
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(any(Class.class))).thenReturn(false);

        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.imposterizer = imposterizer;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

}