import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationContainerImpl;
import org.mockito.internal.stubbing.AnswerWithType;
import org.mockito.internal.stubbing.AnswerWithType.AnswerAndType;
import org.mockito.internal.stubbing.InvocationStubber;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

public class ReturnsDeepStubsTest {
    private ReturnsDeepStubs returnsDeepStubs;
    private Object mock;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mock = mock(Object.class);
    }

    @Test
    public void testAnswerMethodWithMockObject() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        verify(mock).answer(invocation);
        assertSame(mock, result);
    }

    @Test
    public void testGetMockMethodWithMatchingStubbedInvocation() {
        // Given
        Object mock = mock(Object.class);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

        // When
        container.addAnswer(stubbedInvocationMatcher, false);
        when(stubbedInvocationMatcher.answer(any(InvocationOnMock.class))).thenReturn(mock);
        Object result = returnsDeepStubs.getMock(mockInvocation, mock(GenericMetadataSupport.class));

        // Then
        assertSame(mock, result);
    }

    @Test(expected = Throwable.class)
    public void testGetMockMethodWithNoMatchingStubbedInvocation() {
        // Given
        Object mock = mock(Object.class);
        when(mock.toString()).thenReturn("mockObject");
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        // When
        Object result = returnsDeepStubs.getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));

        // Then
        fail("Expected an exception to be thrown, but none was.");
    }

    @Test
    public void testRecordDeepStubMockMethodWithMatchingInvocation() {
        // Given
        Object mock = mock(Object.class);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        // When
        returnsDeepStubs.recordDeepStubMock(mock, container);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        doReturn(mock).when(mock).answer(invocation);

        // Then
        InvocationOnMock invocationToVerify = Mockito.mock(InvocationOnMock.class);
        verify(mock).answer(invocationToVerify);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRecordDeepStubMockMethodWithNullMock() {
        // Given
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(null);

        // When, and Then
        try {
            returnsDeepStubs.recordDeepStubMock(null, null);
        } catch (Throwable e) {
            assertInstanceOf(IllegalArgumentException.class, e);
        }
    }
}