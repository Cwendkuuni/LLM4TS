import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_InterfaceMethodNotMockable_ReturnsEmptyValues() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        // mockitoCore.isTypeMockable(rawType) returns false for Object
        Object result = deepStubs.answer(invocation);
        
        verify(invocation, times(1)).getMock();
        // Verify that delegate.returnValueFor(rawType) is called
        verify(delegate, times(1)).returnValueFor(rawType);
    }

    @Test
    public void testAnswer_InterfaceMethodMockable_ReturnsMock() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(GenericsNest.class));
        
        // mockitoCore.isTypeMockable(rawType) returns true for GenericsNest.class
        Object result = deepStubs.answer(invocation);
        
        verify(invocation, times(1)).getMock();
        // Verify that getMock and recordDeepStubMock are called
        verify(mockitoCore, times(1)).mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata));
        verify(container, times(1)).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void testGetMock_MatchingInvocation_ReturnsAnswer() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(GenericsNest.class));
        // Stubbing the container to have a matching invocation
        when(container.getInvocationForStubbing().matches(any(InvocationOnMock.class))).thenReturn(true);
        
        Object result = deepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        verify(invocation, times(1)).getMock();
        verify(container, times(1)).getStubbedInvocations();
        // Verify that answer is called on the stubbed invocation matcher
        verify(stubbedInvocationMatcher, times(1)).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testGetMock_NoMatchingInvocation_ReturnsMock() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(GenericsNest.class));
        // Stubbing the container to have a non-matching invocation
        when(container.getInvocationForStubbing().matches(any(InvocationOnMock.class))).thenReturn(false);
        
        Object result = deepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        verify(invocation, times(1)).getMock();
        verify(container, times(1)).getStubbedInvocations();
        // Verify that createNewDeepStubMock and recordDeepStubMock are called
        verify(mockitoCore, times(1)).mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata));
        verify(container, times(1)).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void testCreateNewDeepStubMock_ReturnsMock() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        
        Object result = deepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        
        // Verify that mockitoCore.mock is called with the correct parameters
        verify(mockitoCore, times(1)).mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing_ReturnsMockSettings() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        
        MockSettings result = deepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        // Verify that the correct MockSettings are returned based on the rawExtraInterfaces length
        if (returnTypeGenericMetadata.rawExtraInterfaces().length > 0) {
            verify(result, times(1)).extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces());
        } else {
            verify(result, times(0)).extraInterfaces(any(Class[].class));
        }
        // Verify that the correct defaultAnswer is set
        verify(result, times(1)).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_ReturnsDeepStubsAnswer() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        
        ReturnsDeepStubs result = deepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        // Verify that the returned ReturnsDeepStubs has the correct actualParameterizedType
        verify(result, times(1)).actualParameterizedType(eq(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubMock_ReturnsMock() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        when(mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata))).thenReturn(mock);
        Answer<Object> answer = mock(Answer.class);
        
        Object result = deepStubs.recordDeepStubMock(mock, container);
        
        verify(container, times(1)).addAnswer(answer, false);
    }
}