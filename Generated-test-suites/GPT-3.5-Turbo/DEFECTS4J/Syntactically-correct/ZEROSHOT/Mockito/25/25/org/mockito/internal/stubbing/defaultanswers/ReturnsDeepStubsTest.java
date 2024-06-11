import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_returnsMockForNonMockableType() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(mock(Class.class));
        when(mockCreationValidator.isTypeMockable(any(Class.class))).thenReturn(false);
        when(delegate.returnValueFor(any(Class.class))).thenReturn(mock(Object.class));

        // Act
        Object result = returnsDeepStubs.answer(invocation, returnTypeGenericMetadata, mockUtil, mockCreationValidator, delegate);

        // Assert
        assertNotNull(result);
        verify(invocation, times(2)).getMock();
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(invocation).getMethod();
        verify(invocation, times(2)).getMock();
        verify(mockCreationValidator).isTypeMockable(any(Class.class));
        verify(delegate).returnValueFor(any(Class.class));
    }

    @Test
    public void testAnswer_returnsMockForMockableType() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(mock(Class.class));
        when(mockCreationValidator.isTypeMockable(any(Class.class))).thenReturn(true);
        when(mock(Mockito.mock(Class.class), returnsDeepStubs)).thenReturn(mock(Object.class));

        // Act
        Object result = returnsDeepStubs.answer(invocation, returnTypeGenericMetadata, mockUtil, mockCreationValidator, delegate);

        // Assert
        assertNotNull(result);
        verify(invocation, times(2)).getMock();
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(invocation).getMethod();
        verify(invocation, times(2)).getMock();
        verify(mockCreationValidator).isTypeMockable(any(Class.class));
        verify(Mockito).mock(any(Class.class), eq(returnsDeepStubs));
    }

    @Test
    public void testGetMock_matchFound_returnsStubbedResult() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock(Object.class));

        // Act
        Object result = returnsDeepStubs.getMock(invocation, mockUtil);

        // Assert
        assertNotNull(result);
        verify(invocation).getMock();
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(handler).getInvocationContainer();
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
        verify(container.getInvocationForStubbing()).matches(invocation);
        verify(stubbedInvocationMatcher).answer(invocation);
    }

    @Test
    public void testGetMock_noMatchFound_returnsRecordedMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        when(mock(Mockito.mock(Class.class), returnsDeepStubs)).thenReturn(mock(Object.class));

        // Act
        Object result = returnsDeepStubs.getMock(invocation, mockUtil);

        // Assert
        assertNotNull(result);
        verify(invocation).getMock();
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(handler).getInvocationContainer();
        verify(container).getStubbedInvocations();
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        java.lang.reflect.Method method = mock(java.lang.reflect.Method.class);
        Object mock = mock(Object.class);

        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(mock(Class.class));

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Assert
        assertNotNull(result);
        verify(invocation.getMethod()).getReturnType();
        verify(container).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        TypeToMock typeToMock = mock(TypeToMock.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(typeToMock);
        when(typeToMock.get()).thenReturn(mock(Class.class));
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadataSupport);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock, mockUtil);

        // Assert
        assertNotNull(result);
        verify(mockUtil).getMockHandler(mock);
        verify(handler).getMockSettings();
        verify(mockSettings).getTypeToMock();
        verify(typeToMock).get();
        verify(GenericMetadataSupport).inferFrom(mockSettings.getTypeToMock());
    }
}