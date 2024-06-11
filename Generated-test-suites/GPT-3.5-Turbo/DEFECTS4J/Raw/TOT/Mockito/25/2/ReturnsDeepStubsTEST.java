import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
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

import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_validReturnType_genericNest() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Mock the return type
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(GenericsNest.class);

        // Mock the actualParameterizedType
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Map.class);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(mockUtil.getMockHandler(any())).thenReturn(handler);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        
        when(returnsDeepStubs.actualParameterizedType(any())).thenReturn(returnTypeGenericMetadata);
        when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        verify(returnTypeGenericMetadata).rawType();
        verify(handler).getMockSettings();
        verify(mockSettings).getTypeToMock();
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(mockUtil).getMockHandler(any());
        assertNotNull(result);
    }

    @Test
    public void testAnswer_invalidReturnType_genericNest() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Mock the return type
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(GenericsNest.class);

        // Mock the actualParameterizedType
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(String.class); // Invalid type
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(mockUtil.getMockHandler(any())).thenReturn(handler);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        
        when(returnsDeepStubs.actualParameterizedType(any())).thenReturn(returnTypeGenericMetadata);
        when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        verify(returnTypeGenericMetadata).rawType();
        verify(handler).getMockSettings();
        verify(mockSettings).getTypeToMock();
        verify(mockUtil, never()).getMockHandler(invocation.getMock());
        verify(mockUtil, never()).getMockHandler(any());
        assertNotNull(result);
    }
    
    @Test
    public void testGetMock_matchingInvocation() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        when(mockUtil.getMockHandler(any())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        
        // Mock the stubbedInvocationMatcher to match the invocation
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        
        // Mock the answer method of stubbedInvocationMatcher
        Answer<Object> answer = mock(Answer.class);
        when(stubbedInvocationMatcher.answer(invocation)).thenAnswer(answer);
        
        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(handler).getInvocationContainer();
        verify(container).getInvocationForStubbing();
        verify(container).getStubbedInvocations();
        verify(container.getInvocationForStubbing()).matches(stubbedInvocationMatcher.getInvocation());
        verify(stubbedInvocationMatcher).answer(invocation);
        assertNotNull(result);
    }
    
    @Test
    public void testGetMock_nonMatchingInvocation() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        when(mockUtil.getMockHandler(any())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        // Mock the stubbedInvocationMatcher to not match the invocation
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);
        
        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(handler).getInvocationContainer();
        verify(container).getInvocationForStubbing();
        verify(container).getStubbedInvocations();
        verify(container.getInvocationForStubbing()).matches(stubbedInvocationMatcher.getInvocation());
        verify(stubbedInvocationMatcher, never()).answer(invocation);
        assertNotNull(result);
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        org.mockito.Mockito mockitoMock = mock(org.mockito.Mockito.class);
        when(invocation.getMethod()).thenReturn(mockitoMock.getClass().getMethod("mock", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(org.mockito.Mockito.class);

        // Mock the mock method
        doAnswer(invocationOnMock -> {
            Object answerMock = invocationOnMock.getArgument(0);
            assertEquals(org.mockito.Mockito.class, invocationOnMock.getArgument(0));
            return answerMock;
        }).when(mockitoMock).mock(eq(org.mockito.Mockito.class), any());

        // Mock the addAnswer method
        doAnswer(invocationOnMock -> {
            Answer<Object> answer = invocationOnMock.getArgument(0);
            boolean isVoidMethod = invocationOnMock.getArgument(1);
            assertNotNull(answer);
            assertFalse(isVoidMethod);
            return true;
        }).when(container).addAnswer(any(), anyBoolean());
        
        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Assert
        verify(invocation, atLeastOnce()).getMethod();
        verify(container).addAnswer(any(), anyBoolean());
        verify(mockitoMock).mock(eq(org.mockito.Mockito.class), any());
        assertNotNull(result);
    }
    
    @Test
    public void testRecordDeepStubMock_invalidReturnType() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        org.mockito.Mockito mockitoMock = mock(org.mockito.Mockito.class);
        when(invocation.getMethod()).thenReturn(mockitoMock.getClass().getMethod("mock", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(String.class); // Invalid type
        
        // Mock the mock method
        doAnswer(invocationOnMock -> {
            Object answerMock = invocationOnMock.getArgument(0);
            fail("Should not call mock method for invalid return type");
            return answerMock;
        }).when(mockitoMock).mock(eq(org.mockito.Mockito.class), any());

        // Mock the addAnswer method
        doAnswer(invocationOnMock -> {
            fail("Should not call addAnswer method for invalid return type");
            return true;
        }).when(container).addAnswer(any(), anyBoolean());
        
        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Assert
        verify(invocation, atLeastOnce()).getMethod();
        verify(container, never()).addAnswer(any(), anyBoolean());
        verify(mockitoMock, never()).mock(eq(org.mockito.Mockito.class), any());
        assertNotNull(result);
    }
}