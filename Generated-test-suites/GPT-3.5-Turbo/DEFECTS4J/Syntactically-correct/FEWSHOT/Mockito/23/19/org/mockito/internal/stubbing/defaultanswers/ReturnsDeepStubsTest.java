package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.delegate = delegate;
        returnsDeepStubs.mockitoCore = mockitoCore;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = mock(Class.class);
        
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(mockitoCore.isTypeMockable(rawType)).thenReturn(true);
        when(mockitoCore.mock(rawType, withSettingsUsing(returnTypeGenericMetadata))).thenReturn(mock(Object.class));
        
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{mock(StubbedInvocationMatcher.class)});
        when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
        
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mock(InternalMockHandler.class));
        
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(mockUtil.getMockHandler(any()).getMockSettings()).thenReturn(mockSettings);
        
        returnsDeepStubs.recordDeepStubMock(mock(Object.class), container);
        
        returnsDeepStubs.answer(invocation);

        verify(invocation, times(1)).getMock();
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(mockitoCore, times(1)).isTypeMockable(rawType);
        verify(mockitoCore, times(1)).mock(rawType, withSettingsUsing(returnTypeGenericMetadata));
        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());
    }

    private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
        MockSettings mockSettings = mock(MockSettings.class);
        when(mockSettings.extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(any(Answer.class))).thenReturn(mockSettings);
        return mockSettings;
    }
    
}