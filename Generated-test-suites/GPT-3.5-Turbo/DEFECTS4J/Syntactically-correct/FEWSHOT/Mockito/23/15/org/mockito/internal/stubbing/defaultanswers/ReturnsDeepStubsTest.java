package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    
    private ReturnsEmptyValues delegate = new ReturnsEmptyValues();
    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    private MockitoCore mockitoCore = new MockitoCore();
    private MockUtil mockUtil = new MockUtil();
    
    @Test
    public void testAnswer() throws Throwable {
        // Setup
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = mock(Class.class);

        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(mockitoCore.isTypeMockable(rawType)).thenReturn(true);
        
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.getMethod()).thenReturn(Method.class);

        Object expected = new Object();
        when(stubbedInvocationMatcher.getInvocation().matches(invocation)).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(expected);
        
        // Run the test
        Object result = returnsDeepStubs.answer(invocation);

        // Verify the results
        assertSame(expected, result);
    }

    @Test
    public void testAnswer_mockTypeNotMockable() throws Throwable {
        // Setup
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = mock(Class.class);

        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(mockitoCore.isTypeMockable(rawType)).thenReturn(false);
        
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.getMethod()).thenReturn(Method.class);

        Object expected = new Object();
        when(stubbedInvocationMatcher.getInvocation().matches(invocation)).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(expected);
        
        // Run the test
        Object result = returnsDeepStubs.answer(invocation);

        // Verify the results
        assertSame(expected, result);
    }

    @Test
    public void testAnswer_noMatchingInvocation() throws Throwable {
        // Setup
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = mock(Class.class);

        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(mockitoCore.isTypeMockable(rawType)).thenReturn(true);
        
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.getMethod()).thenReturn(Method.class);

        when(stubbedInvocationMatcher.getInvocation().matches(invocation)).thenReturn(false);
        
        // Run the test
        Object result = returnsDeepStubs.answer(invocation);

        // Verify the results
        verify(mockitoCore).mock(rawType, withSettingsUsing(returnTypeGenericMetadata));
        verify(mockitoCore, never()).mock(rawType, withSettings());
    }

    @Test
    public void testCreateNewDeepStubMock() throws Throwable {
        // Setup
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = mock(Class.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(mockitoCore.mock(
                returnTypeGenericMetadata.rawType(),
                withSettingsUsing(returnTypeGenericMetadata)
        )).thenReturn(null);
        
        // Run the test
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Verify the results
        assertNull(result);
    }
    
    @Test
    public void testWithSettingsUsing_noExtraInterfaces() throws Throwable {
        // Setup
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[0]);
        final MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        assertEquals(true, result.getDefaultAnswer() instanceof ReturnsDeepStubs);
    }
    
    @Test
    public void testWithSettingsUsing_withExtraInterfaces() throws Throwable {
        // Setup
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?>[] rawExtraInterfaces = new Class[] { Object.class };

        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(rawExtraInterfaces);
        final MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        assertEquals(true, result.getDefaultAnswer() instanceof ReturnsDeepStubs);
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        // Setup
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(null);
        
        // Run the test
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Verify the results
        assertNotNull(result);
    }
    
    @Test
    public void testActualParameterizedType() throws Throwable {
        // Setup
        Object mock = mock(Object.class);
        Object typeToMock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(mockitoCore);
        when(mockitoCore.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(typeToMock);
        when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(typeToMock.getClass()).thenReturn(Class.class);

        // Run the test
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Verify the results
        assertNotNull(result);
    }
}