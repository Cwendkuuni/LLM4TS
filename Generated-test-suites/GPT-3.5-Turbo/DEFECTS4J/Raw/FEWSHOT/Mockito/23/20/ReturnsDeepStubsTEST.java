
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;

import org.mockito.MockSettings;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();

        // Mock creation is mocked
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(handler.getMock());
        when(handler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);

        // Invocation matches stubbed invocation
        when(handler.getInvocationContainer().getInvocationForStubbing().matches(any())).thenReturn(true);
        when(handler.getInvocationContainer().getInvocationForStubbing().matches(invocation)).thenReturn(true);
        when(handler.getInvocationContainer().getInvocationForStubbing().matches(handler.getInvocationContainer().getStubbedInvocations()[0].getInvocation())).thenReturn(true);
        when(handler.getInvocationContainer().getStubbedInvocations()[0].answer(invocation)).thenReturn("mockedData");

        // Raw type not mockable
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(any())).thenReturn(false);

        // Raw type mock creation
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(handler.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);

        // Generic type mock creation
        MockSettings mockSettings = mock(MockSettings.class);
        CreationSettings creationSettings = mock(CreationSettings.class);
        when(mockUtil.getMockHandler(creationSettings.getTypeToMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);
        when(mockitoCore.mock(creationSettings.getTypeToMock(), withSettingsUsing(returnTypeGenericMetadata))).thenReturn("mockedGenericData");

        // Deep stub
        when(handler.getInvocationContainer().addAnswer(any(Answer.class), anyBoolean())).thenReturn(null);
        

        Object result = returnsDeepStubs.answer(invocation);
        
        assertEquals("mockedData", result);
    }

    private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
        MockSettings mockSettings =
                returnTypeGenericMetadata.rawExtraInterfaces().length > 0 ?
                withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())
                : withSettings();

        return mockSettings
                .defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    private ReturnsDeepStubs returnsDeepStubsAnswerUsing(final GenericMetadataSupport returnTypeGenericMetadata) {
        return new ReturnsDeepStubs() {
            @Override
            protected GenericMetadataSupport actualParameterizedType(Object mock) {
                return returnTypeGenericMetadata;
            }
        };
    }

    private Object recordDeepStubMock(final Object mock, InvocationContainerImpl container) throws Throwable {

        container.addAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mock;
            }
        }, false);

        return mock;
    }
	
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport mock = mock(GenericMetadataSupport.class);

        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(mock);

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mock(InternalMockHandler.class));

        returnsDeepStubs.actualParameterizedType(mockSettings);

        verify(mockUtil).getMockHandler(mock);
    }

}
