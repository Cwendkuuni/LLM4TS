package com.example.junit; // replace with your test package

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    // Test start
    

    private ReturnsDeepStubs answer;
    private Object mock;
    private InvocationOnMock invocation;
    private GenericMetadataSupport returnTypeGenericMetadata;

    @Before
    public void setup() {
        answer = new ReturnsDeepStubs();
        mock = Mockito.mock(Object.class);
        returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        given(returnTypeGenericMetadata.rawType()).willReturn(Object.class);
        given(returnTypeGenericMetadata.hasRawExtraInterfaces()).willReturn(false);
    }

    @Test
    public void testAnswerWithNoGenericReturnType() throws Throwable {
        given(answer.answer(any(InvocationOnMock.class))).willCallRealMethod();

        Object result = answer.answer(invocation);
        assertEquals(mock, result);
    }

    @Test
    public void testAnswerWithGenericReturnType() throws Throwable {
        given(returnTypeGenericMetadata.rawType()).willReturn(Map.class);
        given(returnTypeGenericMetadata.hasRawExtraInterfaces()).willReturn(false);
        given(answer.deepStub(any(InvocationOnMock.class), any(GenericMetadataSupport.class))).willCallRealMethod();

        Object mapMock = mock(Map.class);
        given(answer.newDeepStubMock(returnTypeGenericMetadata)).willReturn(mapMock);

        Object result = answer.answer(invocation);
        assertEquals(mapMock, result);

        verify(answer).deepStub(invocation, returnTypeGenericMetadata);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerWithUnsupportedType() throws Throwable {
        given(returnTypeGenericMetadata.rawType()).willReturn(IOException.class);
        given(answer.mockitoCore()).willCallRealMethod();

        answer.answer(invocation);

        verify(answer.delegate()).returnValueFor(IOException.class);
    }

    @Test
    public void testNewDeepStubMockWithGenericInterfaces() throws Throwable {
        given(returnTypeGenericMetadata.hasRawExtraInterfaces()).willReturn(true);
        given(returnTypeGenericMetadata.rawExtraInterfaces()).willReturn(new Class[]{Collection.class, Serializable.class});

        Object mapMock = mock(Map.class);
        Object collectionMock = mock(Collection.class);

        given(answer.newDeepStubMock(returnTypeGenericMetadata)).willReturn(mapMock);
        given(mapMock.getClass()).willReturn(Map.class);
        given(mapMock.iterator()).willReturn(Collections.singletonList(collectionMock).iterator());

        Object result = answer.answer(invocation);
        assertEquals(collectionMock, result);

        verify(answer).newDeepStubMock(returnTypeGenericMetadata);
    }

    @Test
    public void testNewDeepStubMockWithEmptyList() throws Throwable {
        given(returnTypeGenericMetadata.hasRawExtraInterfaces()).willReturn(false);

        Object listMock = mock(Collections.class);
        given(listMock.emptyList()).willReturn(Collections.emptyList());

        given(answer.newDeepStubMock(returnTypeGenericMetadata)).willReturn(listMock);
        given(listMock.getClass()).willReturn(List.class);

        Object result = answer.answer(invocation);
        assertEquals(Collections.emptyList(), result);

        verify(answer).newDeepStubMock(returnTypeGenericMetadata);
    }

    @Test(expected = IllegalStateException.class)
    public void testRecordDeepStubAnswerWithNullMock() throws Throwable {
        given(answer.mockitoCore()).willCallRealMethod();

        answer.recordDeepStubAnswer(null, any(InvocationContainerImpl.class));
    }

    
}