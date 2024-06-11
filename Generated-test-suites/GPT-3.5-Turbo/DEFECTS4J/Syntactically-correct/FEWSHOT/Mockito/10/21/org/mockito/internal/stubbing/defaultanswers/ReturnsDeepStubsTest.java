
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubsSpy = spy(ReturnsDeepStubs.class);
        
        // Create mock objects for testing
        Object mockInvocation = mock(InvocationOnMock.class);
        Object mockGenericTypeSupport = mock(GenericMetadataSupport.class);
        Object mockMockHandler = mock(InternalMockHandler.class);
        Object mockContainer = mock(InvocationContainerImpl.class);
        Object mockAnswer = mock(Answer.class);
        Object mockNewDeepStub = mock(Object.class);
        Object mockRecordDeepStub = mock(Object.class);
        Object mockActualParameterizedType = mock(GenericMetadataSupport.class);
        Object mockReturnsDeepStubsSerializationFallback = mock(ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback.class);
        Object mockDeeplyStubbedAnswer = mock(ReturnsDeepStubs.DeeplyStubbedAnswer.class);
        Object mockMockitoCore = mock(MockitoCore.class);
        Object mockDelegate = mock(ReturnsEmptyValues.class);
        
        when(invocation.getMock()).thenReturn(mockGenericTypeSupport);
        when(mockGenericTypeSupport.rawType()).thenReturn(mockClass);
        when(mockGenericTypeSupport.hasRawExtraInterfaces()).thenReturn(true);
        when(mockGenericTypeSupport.rawExtraInterfaces()).thenReturn(mockExtraInterfaces);
        
        when(mockitoCore().isTypeMockable(mockClass)).thenReturn(false);
        when(delegate().returnValueFor(mockClass)).thenReturn(mockReturnValue);
        
        when(returnsDeepStubsSpy.deepStub(invocation, returnTypeGenericMetadata)).thenReturn(mockReturnValue);
        
        when(containter.getStubbedInvocations()).thenReturn(mockInvocations);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(true);
        when(stubbedInvocation.answer(invocation)).thenReturn(mockAnswer);

        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(mockExtraInterfaces);
        when(withSettingsUsing(returnTypeGenericMetadata)).thenReturn(mockSettings);
        
        when(mockitoCore().mock(returnTypeGenericMetadata.rawType(), mockSettings)).thenReturn(mockNewDeepStub);
        
        when(newDeepStubMock(returnTypeGenericMetadata)).thenReturn(mockNewDeepStub);
        when(recordDeepStubAnswer(mockNewDeepStub, container)).thenReturn(mockRecordDeepStub);
        
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockSettings);
        
        when(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenReturn(mockReturnsDeepStubsSerializationFallback);
        
        when(actualParameterizedType(mock)).thenReturn(mockGenericTypeSupport);
        
        when(mockReturnsDeepStubsSerializationFallback.actualParameterizedType(mock)).thenReturn(mockGenericTypeSupport);
        
        when(mockDelegate.returnValueFor(mockClass)).thenReturn(mockReturnValue);
        
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = spy(new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata));
        
        when(mockitoCore()).thenReturn(mockMockitoCore);
        when(delegate()).thenReturn(mockDelegate);
        
        // Test method
        Object result = returnsDeepStubsSpy.answer(mockInvocation);
        
        // Assertions
        assertEquals(mockReturnValue, result);
        verify(mockInvocation).getMock();
        verify(mockGenericTypeSupport).rawType();
        verify(mockGenericTypeSupport).hasRawExtraInterfaces();
        verify(mockGenericTypeSupport).rawExtraInterfaces();
        
        verify(mockitoCore()).isTypeMockable(mockClass);
        verify(delegate()).returnValueFor(mockClass);
        
        verify(returnsDeepStubsSpy).deepStub(invocation, returnTypeGenericMetadata);
        
        verify(containter).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
        verify(stubbedInvocation).answer(invocation);
        
        verify(returnTypeGenericMetadata).hasRawExtraInterfaces();
        verify(returnTypeGenericMetadata).rawExtraInterfaces();
        verify(withSettingsUsing(returnTypeGenericMetadata)).serializable();
        verify(withSettingsUsing(returnTypeGenericMetadata)).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
        
        verify(mockitoCore()).mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata));
        
        verify(newDeepStubMock(returnTypeGenericMetadata)).thenReturn(mockNewDeepStub);
        verify(recordDeepStubAnswer(mockNewDeepStub, container)).thenReturn(mockRecordDeepStub);
        
        verify(mockSettings).serializable();
        verify(mockSettings).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
        
        verify(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).actualParameterizedType(mock);
        
        verify(mockReturnsDeepStubsSerializationFallback).actualParameterizedType(mock);
        
        verify(mockDelegate).returnValueFor(mockClass);
    }
    
}
