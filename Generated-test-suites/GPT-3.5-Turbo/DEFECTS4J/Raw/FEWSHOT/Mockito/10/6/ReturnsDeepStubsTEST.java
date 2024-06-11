package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    
    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        assertEquals(null, returnsDeepStubs.answer(invocation));
    }
    
    @Test
    public void testDeepStub() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        Mockito.when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[] {String.class});
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
        Mockito.when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        Mockito.when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[] {stubbedInvocationMatcher});
        Mockito.when(invocation.getMock()).thenReturn(container);
        assertNotNull(returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));
    }
    
    @Test
    public void testNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        assertNotNull(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        Mockito.when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[] {String.class});
        assertNotNull(returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        assertNotNull(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        assertNotNull(returnsDeepStubs.recordDeepStubAnswer(mock, container));
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = Mockito.mock(Object.class);
        assertNotNull(returnsDeepStubs.actualParameterizedType(mock));
    }

}