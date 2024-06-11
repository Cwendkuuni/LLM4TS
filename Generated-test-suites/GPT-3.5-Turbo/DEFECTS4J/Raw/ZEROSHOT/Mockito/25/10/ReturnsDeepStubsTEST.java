
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void answer_shouldReturnDelegateValueIfRawTypeNotMockable() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        @SuppressWarnings("unchecked")
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);
        when(container.getInvocationForStubbing().matches(any())).thenReturn(false);
        doReturn(true).when(new MockCreationValidator()).isTypeMockable(any(Class.class));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        verify(handler, never()).getMock();
        verify(container, never()).addAnswer(any(), anyBoolean());
    }

    @Test
    public void answer_shouldReturnMockIfInvocationMatches() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        @SuppressWarnings("unchecked")
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock(Object.class));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        verify(handler, never()).getMock();
        verify(container, never()).addAnswer(any(), anyBoolean());
    }

    @Test
    public void answer_shouldReturnDeepStubMockIfInvocationDoesNotMatch() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        @SuppressWarnings("unchecked")
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);
        when(container.getInvocationForStubbing().matches(any())).thenReturn(false);
        when(new MockCreationValidator().isTypeMockable(any(Class.class))).thenReturn(true);
        when(new GenericMetadataSupport().inferFrom(any(Class.class))).thenReturn(mock(GenericMetadataSupport.class));
        when(org.mockito.Mockito.mock(any(Class.class), eq(returnsDeepStubs))).thenReturn(mock(Object.class));
        doAnswer(invocation -> {
            ((Answer) invocation.getArgument(0)).answer(invocation.getArgument(1));
            return null;
        }).when(container).addAnswer(any(Answer.class), anyBoolean());

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        verify(org.mockito.Mockito, times(1)).mock(any(Class.class), eq(returnsDeepStubs));
        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());
    }
}
