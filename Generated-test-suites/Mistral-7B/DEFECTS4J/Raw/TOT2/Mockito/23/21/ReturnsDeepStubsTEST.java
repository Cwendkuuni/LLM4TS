import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        Object mock = Mockito.mock(Object.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);

        Object result = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMock().equals(mock)));

        assertEquals(mock, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testNonMockableType() {
        Object mock = Mockito.mock(String.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);

        ReturnsDeepStubs deepStubs = spy(returnsDeepStubs);
        doThrow(new IllegalStateException("Type is not mockable")).when(deepStubs).answer(any(InvocationOnMock.class));

        when(mockitoCore.isTypeMockable(String.class)).thenReturn(false);

        Object result = deepStubs.answer(any(InvocationOnMock.class));

        verify(deepStubs).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testDeepStubMatching() {
        Object mock = Mockito.mock(Object.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(stubbedInvocationMatcher.answer(any(InvocationOnMock.class))).thenReturn(mock);

        when(mockitoCore.mock(Object.class, withSettingsUsing(returnTypeGenericMetadata))).thenReturn(mock);

        Object result = returnsDeepStubs.answer(any(InvocationOnMock.class));

        assertEquals(mock, result);
        verify(stubbedInvocationMatcher, times(1)).answer(any(InvocationOnMock.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testNoStubbedInvocations() {
        Object mock = Mockito.mock(Object.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(container.getStubbedInvocations()).thenReturn(new Object[]{});

        Object result = returnsDeepStubs.answer(any(InvocationOnMock.class));

        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());
        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());

        // This verification should fail
        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());
    }

    @Test
    public void testDifferentInvocations() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{
                argThat(stubbedInvocationMatcher1 -> stubbedInvocationMatcher1.matches(Mockito.argThat(invocation -> invocation.getMock().equals(mock1)))),
                argThat(stubbedInvocationMatcher2 -> stubbedInvocationMatcher2.matches(Mockito.argThat(invocation -> invocation.getMock().equals(mock2))))
        });

        Object result1 = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMock().equals(mock1)));
        Object result2 = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMock().equals(mock2)));

        assertEquals(mock1, result1);
        assertEquals(mock2, result2);
        verify(container, times(2)).addAnswer(any(Answer.class), anyBoolean());
    }
}