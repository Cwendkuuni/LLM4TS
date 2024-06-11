
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;

import java.io.Serializable;

import org.junit.Test;
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

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    private InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
    private InvocationContainerImpl container = mock(InvocationContainerImpl.class);

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        when(invocation.getMock()).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(container.getStubbedInvocations()).thenReturn(createStubbedInvocations());
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        Object result = returnsDeepStubs.answer(invocation);

        // Verify the result
        assertEquals(result, mockHandler);

        // Verify the method calls
        verify(invocation).getMock();
        verify(mockHandler).getInvocationContainer();
        verify(returnTypeGenericMetadata).rawType();
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        when(invocation.getMock()).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(createStubbedInvocations());
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Verify the result
        assertEquals(result, mockHandler);

        // Verify the method calls
        verify(invocation).getMock();
        verify(mockHandler).getInvocationContainer();
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
    }

    @Test
    public void testCreateNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        CreationSettings mockitoSettings = mock(CreationSettings.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);
        when(returnTypeGenericMetadata.rawExtraInterfaces().length).thenReturn(0);
        when(mockitoSettings.getTypeToMock()).thenReturn(Object.class);
        when(new MockUtil().getMockHandler(Object.class).getMockSettings()).thenReturn(mockitoSettings);
        when(mockitoSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockitoSettings);

        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Verify the result
        assertNotNull(result);

        // Verify the method calls
        verify(returnTypeGenericMetadata, times(2)).rawType();
        verify(returnTypeGenericMetadata).rawExtraInterfaces();
        verify(mockitoSettings).getTypeToMock();
        verify(new MockUtil()).getMockHandler(Object.class);
        verify(mockitoSettings).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});
        when(returnTypeGenericMetadata.rawExtraInterfaces().length).thenReturn(1);
        when(mockSettings.extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockSettings);

        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Verify the result
        assertNotNull(result);

        // Verify the method calls
        verify(returnTypeGenericMetadata).rawExtraInterfaces();
        verify(returnTypeGenericMetadata).rawExtraInterfaces();
        verify(mockSettings).extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces());
        verify(mockSettings).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    private StubbedInvocationMatcher[] createStubbedInvocations() {
        // Create an array of StubbedInvocationMatcher objects
        StubbedInvocationMatcher[] invocations = new StubbedInvocationMatcher[1];
        invocations[0] = mock(StubbedInvocationMatcher.class);
        return invocations;
    }

    private ReturnsDeepStubs returnsDeepStubsAnswerUsing(final GenericMetadataSupport returnTypeGenericMetadata) {
        return new ReturnsDeepStubs() {
            @Override
            protected GenericMetadataSupport actualParameterizedType(Object mock) {
                return returnTypeGenericMetadata;
            }
        };
    }

}

