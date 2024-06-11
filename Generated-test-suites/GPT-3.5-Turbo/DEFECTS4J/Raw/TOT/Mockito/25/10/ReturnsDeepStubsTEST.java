import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.util.*;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;
    private InvocationOnMock invocation;
    private GenericMetadataSupport genericMetadataSupport;
    private MockUtil mockUtil;
    private MockCreationValidator mockCreationValidator;
    
    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
        invocation = mock(InvocationOnMock.class);
        genericMetadataSupport = mock(GenericMetadataSupport.class);
        mockUtil = mock(MockUtil.class);
        mockCreationValidator = mock(MockCreationValidator.class);
    }
    
    @Test
    public void testAnswer_validReturnType() throws Throwable {
        // Arrange
        Class<?> returnType = List.class;
        when(invocation.getMock()).thenReturn(List.class);
        when(genericMetadataSupport.rawType()).thenReturn(returnType);
        when(mockUtil.getMockHandler(any())).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(invocation.getMock()).getMockSettings()).thenReturn(mock(CreationSettings.class));
        when(mockCreationValidator.isTypeMockable(returnType)).thenReturn(true);
        
        // Act
        Object result = returnsDeepStubs.answer(invocation);
        
        // Assert
        verify(invocation, times(1)).getMock();
        verify(genericMetadataSupport, times(1)).rawType();
        verify(mockUtil, times(3)).getMockHandler(any());
        verify(mockUtil, times(1)).getMockHandler(invocation.getMock());
        verify(mockUtil.getMockHandler(invocation.getMock()), times(1)).getInvocationContainer();
        verify(mockCreationValidator, times(1)).isTypeMockable(returnType);
        // Additional assertion
    }
    
    @Test
    public void testAnswer_invalidReturnType() throws Throwable {
        // Arrange
        Class<?> returnType = String.class;
        when(invocation.getMock()).thenReturn(List.class);
        when(genericMetadataSupport.rawType()).thenReturn(returnType);
        when(mockUtil.getMockHandler(any())).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(invocation.getMock()).getMockSettings()).thenReturn(mock(CreationSettings.class));
        when(mockCreationValidator.isTypeMockable(returnType)).thenReturn(false);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        
        // Act
        Object result = returnsDeepStubs.answer(invocation);
        
        // Assert
        verify(invocation, times(1)).getMock();
        verify(genericMetadataSupport, times(1)).rawType();
        verify(mockUtil, times(3)).getMockHandler(any());
        verify(mockUtil, times(1)).getMockHandler(invocation.getMock());
        verify(mockUtil.getMockHandler(invocation.getMock()), times(1)).getInvocationContainer();
        verify(mockCreationValidator, times(1)).isTypeMockable(returnType);
        verify(invocation, times(1)).getMethod();
        // Additional assertion
    }
    
    @Test
    public void testGetMock_stubsMatchInvocation() throws Throwable {
        // Arrange
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("MockedValue");
        
        // Act
        Object result = returnsDeepStubs.getMock(invocation);
        
        // Assert
        verify(invocation, times(1)).getMock();
        verify(mockUtil, times(1)).getMockHandler(invocation.getMock());
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
        // Additional assertions
    }
    
    @Test
    public void testGetMock_stubsNotMatchInvocation() throws Throwable {
        // Arrange
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);
        
        // Act
        Object result = returnsDeepStubs.getMock(invocation);
        
        // Assert
        verify(invocation, times(1)).getMock();
        verify(mockUtil, times(1)).getMockHandler(invocation.getMock());
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        verify(stubbedInvocationMatcher, times(0)).answer(invocation);
        // Additional assertions
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Arrange
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(mock(Invocation.class));
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));
        
        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        
        // Assert
        verify(invocation, times(1)).getMethod();
        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());
        verify(mockUtil, times(1)).getMockHandler(invocation.getMock());
        // Additional assertions
    }
    
    @Test
    public void testActualParameterizedType() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(mockUtil.getMockHandler(any())).thenReturn(mock(InternalMockHandler.class));
        
        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        // Assert
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(mockSettings, times(1)).getTypeToMock();
        // Additional assertions
    }
}