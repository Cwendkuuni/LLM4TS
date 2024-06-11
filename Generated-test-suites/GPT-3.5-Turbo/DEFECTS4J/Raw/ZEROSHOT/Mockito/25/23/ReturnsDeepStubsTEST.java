import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.MockSettings;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Setup
        ReturnsEmptyValues returnsEmptyValuesMock = mock(ReturnsEmptyValues.class);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadataSupportMock = mock(GenericMetadataSupport.class);
        Class rawTypeMock = mock(Class.class);
        MockUtil mockUtilMock = mock(MockUtil.class);
        InternalMockHandler<Object> internalMockHandlerMock = mock(InternalMockHandler.class);
        InvocationContainerImpl invocationContainerImplMock = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcherMock = mock(StubbedInvocationMatcher.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test
        when(invocationMock.getMock()).thenReturn("mock");
        when(returnsEmptyValuesMock.returnValueFor(any())).thenReturn("emptyValue");
        when(mockUtilMock.getMockHandler("mock")).thenReturn(internalMockHandlerMock);
        when(internalMockHandlerMock.getInvocationContainer()).thenReturn(invocationContainerImplMock);
        when(invocationContainerImplMock.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcherMock));
        when(invocationContainerImplMock.getInvocationForStubbing()).thenReturn(invocationMock);
        when(invocationMock.getMethod()).thenReturn(null);
        when(genericMetadataSupportMock.rawType()).thenReturn(rawTypeMock);
        when(mockUtilMock.getMockHandler(any())).thenReturn(internalMockHandlerMock);
        when(internalMockHandlerMock.getInvocationContainer()).thenReturn(invocationContainerImplMock);
        when(invocationContainerImplMock.addAnswer(any(), anyBoolean())).thenReturn(null);
        when(rawTypeMock.getReturnType()).thenReturn(null);

        Object result = returnsDeepStubs.answer(invocationMock);

        // Verify
        verify(invocationMock, times(1)).getMock();
        verify(returnsEmptyValuesMock, times(1)).returnValueFor(any());
        verify(mockUtilMock, times(1)).getMockHandler(any());
        verify(internalMockHandlerMock, times(1)).getInvocationContainer();
        verify(invocationContainerImplMock, times(1)).getStubbedInvocations();
        verify(invocationContainerImplMock, times(1)).getInvocationForStubbing();
        verify(invocationMock, times(1)).getMethod();
        verify(genericMetadataSupportMock, times(1)).rawType();
        verify(mockUtilMock, times(1)).getMockHandler(any());
        verify(internalMockHandlerMock, times(1)).getInvocationContainer();
        verify(invocationContainerImplMock, times(1)).addAnswer(any(), anyBoolean());
        verify(rawTypeMock, times(1)).getReturnType();

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        // Setup
        ReturnsEmptyValues returnsEmptyValuesMock = mock(ReturnsEmptyValues.class);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        MockUtil mockUtilMock = mock(MockUtil.class);
        InternalMockHandler<Object> internalMockHandlerMock = mock(InternalMockHandler.class);
        InvocationContainerImpl invocationContainerImplMock = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcherMock = mock(StubbedInvocationMatcher.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test
        when(invocationMock.getMock()).thenReturn("mock");
        when(mockUtilMock.getMockHandler("mock")).thenReturn(internalMockHandlerMock);
        when(internalMockHandlerMock.getInvocationContainer()).thenReturn(invocationContainerImplMock);
        when(invocationContainerImplMock.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcherMock));
        when(invocationContainerImplMock.getInvocationForStubbing()).thenReturn(invocationMock);
        when(invocationMock.matches(any())).thenReturn(false);

        Object result = returnsDeepStubs.getMock(invocationMock);

        // Verify
        verify(invocationMock, times(1)).getMock();
        verify(mockUtilMock, times(1)).getMockHandler(any());
        verify(internalMockHandlerMock, times(1)).getInvocationContainer();
        verify(invocationContainerImplMock, times(1)).getStubbedInvocations();
        verify(invocationContainerImplMock, times(1)).getInvocationForStubbing();
        verify(invocationMock, times(1)).matches(stubbedInvocationMatcherMock.getInvocation());

        // Assert
        assertNull(result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Setup
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        InvocationContainerImpl invocationContainerImplMock = mock(InvocationContainerImpl.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test
        when(invocationMock.getMethod()).thenReturn(null);
        when(invocationMock.getMethod().getReturnType()).thenReturn(null);
        when(invocationContainerImplMock.addAnswer(any(), anyBoolean())).thenReturn(null);

        Object result = returnsDeepStubs.recordDeepStubMock(invocationMock, invocationContainerImplMock);

        // Verify
        verify(invocationMock, times(1)).getMethod();
        verify(invocationMock, times(1)).getMethod().getReturnType();
        verify(invocationContainerImplMock, times(1)).addAnswer(any(), anyBoolean());

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        // Setup
        Object mock = mock(Object.class);
        CreationSettings creationSettingsMock = mock(CreationSettings.class);
        MockUtil mockUtilMock = mock(MockUtil.class);
        GenericMetadataSupport genericMetadataSupportMock = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test
        when(mockUtilMock.getMockHandler(mock).getMockSettings()).thenReturn(creationSettingsMock);
        when(creationSettingsMock.getTypeToMock()).thenReturn(mock);
        when(genericMetadataSupportMock.inferFrom(mock)).thenReturn(genericMetadataSupportMock);
        when(genericMetadataSupportMock.rawType()).thenReturn(null);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Verify
        verify(mockUtilMock, times(1)).getMockHandler(mock);
        verify(creationSettingsMock, times(1)).getTypeToMock();
        verify(genericMetadataSupportMock, times(1)).inferFrom(mock);
        verify(genericMetadataSupportMock, times(1)).rawType();

        // Assert
        assertNotNull(result);
    }
}