
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Serializable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        InvocationOnMock invocation3 = mock(InvocationOnMock.class);
        InvocationOnMock invocation4 = mock(InvocationOnMock.class);
        InvocationOnMock invocation5 = mock(InvocationOnMock.class);
        
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(genericMetadata.rawType()).thenReturn(Object.class);
        when(genericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        when(genericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);
        
        CreationSettings creationSettings = mock(CreationSettings.class);
        when(creationSettings.getTypeToMock()).thenReturn(Object.class);
        
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation1.getMock())).thenReturn(mock(InternalMockHandler.class));
        
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(true);
        
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        
        StubbedInvocationMatcher stubbedInvocationMatcher1 = mock(StubbedInvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocationMatcher2 = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher1, stubbedInvocationMatcher2});
        
        when(invocation1.getMethod()).thenReturn(getClass().getDeclaredMethod("testAnswer"));
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getArguments()).thenReturn(new Object[0]);
        
        when(invocation2.getMethod()).thenReturn(getClass().getDeclaredMethod("testAnswer"));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getArguments()).thenReturn(new Object[0]);
        
        when(invocation3.getMethod()).thenReturn(getClass().getDeclaredMethod("testAnswer"));
        when(invocation3.getMock()).thenReturn(mock(Object.class));
        when(invocation3.getArguments()).thenReturn(new Object[0]);
        
        when(invocation4.getMethod()).thenReturn(getClass().getDeclaredMethod("testAnswer"));
        when(invocation4.getMock()).thenReturn(mock(Object.class));
        when(invocation4.getArguments()).thenReturn(new Object[0]);
        
        when(invocation5.getMethod()).thenReturn(getClass().getDeclaredMethod("testAnswer"));
        when(invocation5.getMock()).thenReturn(mock(Object.class));
        when(invocation5.getArguments()).thenReturn(new Object[0]);
        
        when(container.getInvocationForStubbing()).thenReturn(invocation1);

        when(stubbedInvocationMatcher1.getInvocation()).thenReturn(invocation2);
        when(stubbedInvocationMatcher2.getInvocation()).thenReturn(invocation3);

        when(stubbedInvocationMatcher1.answer(invocation1)).thenReturn(mock(Object.class));
        when(stubbedInvocationMatcher2.answer(invocation1)).thenReturn(mock(Object.class));
        
        when(creationSettings.getTypeToMock()).thenReturn(Object.class);
        
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(true);
        
        when(handler.getInvocationContainer()).thenReturn(container);
        
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher1, stubbedInvocationMatcher2});
        
        when(container.getInvocationForStubbing()).thenReturn(invocation1);
        
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        
        assertNotNull(returnsDeepStubs.answer(invocation1));
        
        when(container.getInvocationForStubbing()).thenReturn(invocation4);
        
        ReturnsDeepStubs returnsDeepStubsSpy = mock(ReturnsDeepStubs.class);

        assertNotNull(returnsDeepStubsSpy.answer(invocation4));
        
        when(invocation5.getMock()).thenReturn(mock(Object.class));
        
        assertSame(invocation5.getMock(), returnsDeepStubs.answer(invocation5));
    }
}
