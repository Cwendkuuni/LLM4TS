import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {

    private MockHandler<Object> handler;
    private MockHandler.Interface<Object> mockInterface;
    private Invocation invocation;

    @Before
    public void setUp() {
        handler = new MockHandler<Object>();
        mockInterface = mock(MockHandler.Interface.class);
        invocation = mock(Invocation.class);
    }

    @Test
    public void testHandle_stubVoidInvocation() throws Throwable {
        when(invocation.getMethodForStubbing()).thenReturn(null);
        handler.handle(invocation);
        verify(mockInterface, times(1)).stubVoidMethod();
    }

    @Test
    public void testHandle_verifyInvocation() throws Throwable {
        when(mockInterface.verifyInvocation()).thenReturn(null);
        handler.handle(invocation);
        verify(mockInterface, times(1)).verifyInvocation();
    }
    
    @Test
    public void testHandle_findAnswerForInvocationWithStubbedInvocation() throws Throwable {
        when(mockInterface.findAnswerForInvocation(invocation)).thenReturn(null);
        handler.handle(invocation);
        verify(mockInterface, times(1)).findAnswerForInvocation(invocation);
    }

    @Test
    public void testHandle_findAnswerForInvocationWithoutStubbedInvocation() throws Throwable {
        when(mockInterface.findAnswerForInvocation(invocation)).thenReturn("answer");
        handler.handle(invocation);
        verify(mockInterface, times(1)).findAnswerForInvocation(invocation);
        verify(mockInterface, times(1)).captureArgumentsFrom(invocation);
        verify(mockInterface, times(1)).answer(invocation);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        handler.setAnswersForStubbing(answers);
        verify(handler.getInvocationContainer(), times(1)).setAnswersForStubbing(answers);
    }
    
    interface Interface<T> {
        void stubVoidMethod();
        void verifyInvocation();
        Object findAnswerForInvocation(Invocation invocation);
        void captureArgumentsFrom(Invocation invocation);
        Object answer(Invocation invocation);
    }
}