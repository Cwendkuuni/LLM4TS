package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;
    
    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }
    
    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object answer = returnsDeepStubs.answer(invocation);
        assertNotNull(answer);
    }
    
    @Test
    public void testDeepStub() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(invocation.getMock()).thenReturn(new Object());
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        
        Object deepStub = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        assertNotNull(deepStub);
    }
    
    @Test
    public void testNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        
        Object deepStubMock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        assertNotNull(deepStubMock);
    }
    
    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        assertNotNull(mockSettings);
        assertTrue(mockSettings.isSerializable());
        assertEquals(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata), mockSettings.getDefaultAnswer());
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        Mockito.when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] { Serializable.class });
        
        ReturnsDeepStubs returnsDeepStubsAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        assertNotNull(returnsDeepStubsAnswer);
        assertTrue(returnsDeepStubsAnswer instanceof Serializable);
    }
    
    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        
        Object recordedMock = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        
        assertNotNull(recordedMock);
        assertEquals(mock, recordedMock);
    }
    
    @Test
    public void testActualParameterizedType() {
        Object mock = Mockito.mock(Object.class);
        CreationSettings mockSettings = Mockito.mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
        
        Mockito.when(new MockUtil().getMockHandler(mock)).thenReturn(new MockHandler());
        Mockito.when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        Mockito.when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadataSupport);
        
        GenericMetadataSupport actualParameterizedType = returnsDeepStubs.actualParameterizedType(mock);
        
        assertNotNull(actualParameterizedType);
        assertEquals(genericMetadataSupport, actualParameterizedType);
    }
    
    private class MockHandler implements InternalMockHandler<Object> {
        @Override
        public InvocationContainerImpl getInvocationContainer() {
            return null;
        }
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = 
                returnsDeepStubs.new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
        
        assertNotNull(returnsDeepStubsSerializationFallback);
        assertEquals(returnTypeGenericMetadata, returnsDeepStubsSerializationFallback.returnTypeGenericMetadata);
    }
    
    @Test
    public void testActualParameterizedTypeInReturnsDeepStubsSerializationFallback() {
        Object mock = Mockito.mock(Object.class);
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
        
        Mockito.when(new MockUtil().getMockHandler(mock)).thenReturn(new ReturnsDeepStubsTest.MockHandler());
        
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = 
                returnsDeepStubs.new ReturnsDeepStubsSerializationFallback(genericMetadataSupport);
        
        GenericMetadataSupport actualParameterizedType = returnsDeepStubsSerializationFallback.actualParameterizedType(mock);
        
        assertNotNull(actualParameterizedType);
        assertEquals(genericMetadataSupport, actualParameterizedType);
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallbackWriteReplace() throws IOException {
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = 
                returnsDeepStubs.new ReturnsDeepStubsSerializationFallback(null);
        
        Object writeReplace = returnsDeepStubsSerializationFallback.writeReplace();
        
        assertEquals(Mockito.RETURNS_DEEP_STUBS, writeReplace);
    }
    
    @Test
    public void testDeeplyStubbedAnswer() {
        Object mock = Mockito.mock(Object.class);
        
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = 
                returnsDeepStubs.new DeeplyStubbedAnswer(mock);
        
        assertNotNull(deeplyStubbedAnswer);
        assertEquals(mock, deeplyStubbedAnswer.mock);
    }
    
    @Test
    public void testAnswerMethodOfDeeplyStubbedAnswer() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = 
                returnsDeepStubs.new DeeplyStubbedAnswer(mock);
        
        Object answer = deeplyStubbedAnswer.answer(invocation);
        
        assertEquals(mock, answer);
    }
    
    private static class MockitoCore {
        // TODO: Implement MockitoCore
    }
    
    private static class ReturnsEmptyValues implements Answer<Object>, Serializable {
        // TODO: Implement ReturnsEmptyValues
    }
}