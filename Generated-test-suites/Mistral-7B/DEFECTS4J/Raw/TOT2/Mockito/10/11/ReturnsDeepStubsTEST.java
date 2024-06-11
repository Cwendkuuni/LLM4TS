import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.InvocationOnMock;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.invocation.InvocationContainerImpl;
import org.mockito.internal.stubbing.matchers.StubbedInvocationMatcher;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.junit.Assert.assertEquals;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(mock.answer(invocation)).thenReturn("Test Value");

        String result = (String) mock.answer(invocation);
        verify(mock).answer(invocation);
        assertEquals("Test Value", result);
    }

    @Test
    public void testDeepStubMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        InvocationContainerImpl container = spy(new InvocationContainerImpl());

        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{});
        when(mockitoCore().mock(String.class)).thenReturn(mock);

        when(mock.deepStub(invocation, Mockito.mock(GenericMetadataSupport.class))).thenReturn("Test Value");

        String result = (String) mock.answer(invocation);
        verify(mock).deepStub(invocation, Mockito.any(GenericMetadataSupport.class));
        assertEquals("Test Value", result);
    }

    @Test(expected = RuntimeException.class)
    public void testUnMockableReturnType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(int[].class);

        mock.answer(invocation);
    }

    @Test(expected = IOException.class)
    public void testAnswerMethodWithException() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        when(mock.answer(invocation)).thenThrow(new IOException());

        mock.answer(invocation);
    }
}