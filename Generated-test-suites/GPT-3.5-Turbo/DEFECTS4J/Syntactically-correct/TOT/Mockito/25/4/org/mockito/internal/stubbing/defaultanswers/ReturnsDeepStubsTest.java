
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

public class ReturnsDeepStubsTest {

    // Test case for answer() method
    @Test
    public void testAnswer() throws Throwable {
        // Prepare mocks and stubs
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        GenericMetadataSupport returnTypeMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = mock(Class.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);

        // Configure mock and stub interactions
        when(invocation.getMock()).thenReturn(mock(Serializable.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(returnTypeMetadata.rawType()).thenReturn(rawType);
        when(rawType.toString()).thenReturn("java.lang.String");
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(new MockCreationValidator().isTypeMockable(rawType)).thenReturn(true);
        when(delegate.returnValueFor(rawType)).thenReturn("test");

        // Create instance of ReturnsDeepStubs class
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        deepStubs.delegate = delegate;

        // Test the answer() method
        Object result = deepStubs.answer(invocation);

        // Verify the interactions
        verify(invocation).getMock();
        verify(handler).getInvocationContainer();
        verify(returnTypeMetadata).rawType();
        verify(rawType).toString();
        verify(container).getInvocationForStubbing();
        verify(new MockCreationValidator()).isTypeMockable(rawType);
        verify(delegate).returnValueFor(rawType);

        // Check the result
        assertEquals("test", result);
    }

    // Test case for getMock() method - Matching invocation for verification
    @Test
    public void testGetMock_MatchingInvocation() throws Throwable {
        // Prepare mocks and stubs
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        // Configure mock and stub interactions
        when(invocation.getMock()).thenReturn(mock(Serializable.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("matched");

        // Create instance of ReturnsDeepStubs class
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // Test the getMock() method
        Object result = deepStubs.getMock(invocation);

        // Verify the interactions
        verify(invocation).getMock();
        verify(handler).getInvocationContainer();
        verify(container).getStubbedInvocations();
        verify(container, times(0)).addAnswer(any(), anyBoolean());
        verify(invocation).matches(stubbedInvocationMatcher.getInvocation());
        verify(stubbedInvocationMatcher).answer(invocation);

        // Check the result
        assertEquals("matched", result);
    }

    // Test case for getMock() method - Deep stub
    @Test
    public void testGetMock_DeepStub() throws Throwable {
        // Prepare mocks and stubs
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        ReturnsDeepStubs deepStubs = mock(ReturnsDeepStubs.class);
        Class<?> clz = mock(Class.class);

        // Configure mock and stub interactions
        when(invocation.getMock()).thenReturn(mock(Serializable.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getReturnType()).thenReturn(clz);
        when(org.mockito.Mockito.mock(clz, deepStubs)).thenReturn("mocked");
        when(deepStubs.recordDeepStubMock(invocation, container)).thenCallRealMethod();
        when(container.addAnswer(any(), anyBoolean())).thenReturn(null);

        // Create instance of ReturnsDeepStubs class
        ReturnsDeepStubs deepStubsInstance = new ReturnsDeepStubs();

        // Test the getMock() method
        Object result = deepStubsInstance.getMock(invocation);

        // Verify the interactions
        verify(invocation, times(2)).getMock();
        verify(handler, times(2)).getInvocationContainer();
        verify(container).getInvocationForStubbing();
        verify(container).addAnswer(any(), anyBoolean());
        verify(deepStubs).recordDeepStubMock(invocation, container);
        verify(clz).getCanonicalName();

        // Check the result
        assertEquals("mocked", result);
    }

    // Test case for recordDeepStubMock() method
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Prepare mocks and stubs
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Class<?> clz = mock(Class.class);
        Object mock = mock(Object.class);
        Answer<Object> answer = mock(Answer.class);

        // Configure mock and stub interactions
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getReturnType()).thenReturn(clz);
        when(org.mockito.Mockito.mock(clz, answer)).thenReturn(mock);
        when(container.addAnswer(answer, false)).thenReturn(null);

        // Create instance of ReturnsDeepStubs class
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // Test the recordDeepStubMock() method
        Object result = deepStubs.recordDeepStubMock(invocation, container);

        // Verify the interactions
        verify(invocation).getMethod();
        verify(invocation).getMock();
        verify(invocation.getMethod()).getReturnType();
        verify(org.mockito.Mockito).mock(clz, answer);
        verify(container).addAnswer(answer, false);
        verify(mock, times(0)).toString();

        // Check the result
        assertEquals(mock, result);
    }

    // Test case for actualParameterizedType() method
    @Test
    public void testActualParameterizedType() throws Throwable {
        // Prepare mock
        CreationSettings mockSettings = mock(CreationSettings.class);
        MockUtil mockUtil = mock(MockUtil.class);
        GenericMetadataSupport supportMock = mock(GenericMetadataSupport.class);
        Object mock = mock(Object.class);

        // Configure mock interactions
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mock);

        // Create instance of ReturnsDeepStubs class
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        deepStubs.mockUtil = mockUtil;

        // Test the actualParameterizedType() method
        GenericMetadataSupport result = deepStubs.actualParameterizedType(mock);

        // Verify the interactions
        verify(mockUtil).getMockHandler(mock);
        verify(handler).getMockSettings();
        verify(mockSettings).getTypeToMock();

        // Check the result
        assertEquals(supportMock, result);
    }

}
