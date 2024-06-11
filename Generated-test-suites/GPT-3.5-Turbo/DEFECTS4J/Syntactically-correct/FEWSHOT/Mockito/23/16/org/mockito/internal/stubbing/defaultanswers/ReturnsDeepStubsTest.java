package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private MockitoCore mockitoCore;
    private ReturnsEmptyValues delegate;
    
    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mockitoCore = mock(MockitoCore.class);
        delegate = mock(ReturnsEmptyValues.class);
        returnsDeepStubs.mockitoCore = mockitoCore;
        returnsDeepStubs.delegate = delegate;
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        
        Object result = returnsDeepStubs.answer(invocation);
        
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(mockitoCore, times(1)).isTypeMockable(Object.class);
        verify(delegate, times(1)).returnValueFor(Object.class);
        verify(returnTypeGenericMetadata, times(1)).rawType();
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);
        
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(invocation.getMock()).thenReturn(mock);
        
        when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock);

        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        verify(handler, times(1)).getInvocationContainer();
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(new MockUtil().getMockHandler(invocation.getMock()), times(1)).getInvocationContainer();
        verify(container, times(1)).addAnswer(any(), eq(false));
        verify(container, times(1)).getInvocationForStubbing();
        verify(container, times(1)).getStubbedInvocations();
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetMock_notMockable() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);
        
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(delegate.returnValueFor(Object.class)).thenReturn(mock);

        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        verify(handler, times(1)).getInvocationContainer();
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(new MockUtil().getMockHandler(invocation.getMock()), times(1)).getInvocationContainer();
        verify(container, times(1)).addAnswer(any(), eq(false));
        verify(delegate, times(1)).returnValueFor(Object.class);
        verify(mockitoCore, times(1)).isTypeMockable(Object.class);
        verify(returnTypeGenericMetadata, times(1)).rawType();
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateNewDeepStubMock() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        
        verify(mockitoCore, times(1)).mock(Object.class, withSettings());
        assertNotNull(result);
    }
    
    @Test
    public void testWithSettingsUsing_noExtraInterfaces() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);
        
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);
        when(withSettings()).thenReturn(mockSettings);

        returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        verify(withSettings(), times(1)).extraInterfaces(new Class[0]);
    }
    
    @Test
    public void testWithSettingsUsing_extraInterfaces() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);
        
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});
        when(withSettings()).thenReturn(mockSettings);

        returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        verify(withSettings(), times(1)).extraInterfaces(new Class[]{Serializable.class});
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        assertNotNull(result);
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);
        returnsDeepStubs.recordDeepStubMock(mock, container);
        
        verify(container, times(1)).addAnswer(any(), eq(false));
    }
    
    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(GenericMetadataSupport.inferFrom(mock(Class.class))).thenReturn(genericMetadataSupport);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        verify(new MockUtil().getMockHandler(mock), times(1)).getMockSettings();
        verify(mockSettings, times(1)).getTypeToMock();
        verify(GenericMetadataSupport, times(1)).inferFrom(mock(Class.class));
        assertNotNull(result);
    }

}