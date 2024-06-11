package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Serializable;

import static org.mockito.Mockito.withSettings;


public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        assertNull(returnsDeepStubs.answer(null));
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        assertNotNull(returnsDeepStubs.deepStub(invocation, null));
    }

    @Test
    public void testNewDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        assertNotNull(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        assertNotNull(returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        assertNotNull(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        assertNotNull(returnsDeepStubs.recordDeepStubAnswer(null, container));
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);

        assertNotNull(returnsDeepStubs.actualParameterizedType(mock));
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback() throws Throwable {
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(null);
        
        assertNotNull(returnsDeepStubsSerializationFallback.answer(null));
        assertNotNull(returnsDeepStubsSerializationFallback.writeReplace());
    }
    
    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(null);
        
        assertNotNull(deeplyStubbedAnswer.answer(null));
    }
}