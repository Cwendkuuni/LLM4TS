import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubbing;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_validInvocation_returnsStubbedValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Object stubbedValue = new Object();
        
        when(invocation.getMock()).thenReturn(new Object());
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(stubbedValue);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals(stubbedValue, result);
    }

    @Test
    public void testAnswer_invalidInvocation_returnsDelegateValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object delegateValue = new Object();
        
        when(invocation.getMock()).thenReturn(new Object());
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(Collections.emptyList());
        when(new ReturnsEmptyValues().returnValueFor(String.class)).thenReturn(delegateValue);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals(delegateValue, result);
    }

    @Test
    public void testGetMock_validInvocation_returnsNewMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        when(invocation.getMock()).thenReturn(new Object());
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());

        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetMock_invalidInvocation_returnsStubbedValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Object stubbedValue = new Object();
        
        when(invocation.getMock()).thenReturn(new Object());
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(new Object());
        when(new MockUtil().getMockHandler(invocation.getMock()).getInvocationContainer().getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(stubbedValue);

        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Assert
        assertEquals(stubbedValue, result);
    }

    @Test
    public void testCreateNewDeepStubMock_validGenericMetadata_returnsNewMock() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);

        // Act
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing_validGenericMetadata_returnsMockSettings() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[] { Serializable.class });
        when(returnTypeGenericMetadata.rawExtraInterfaces().length).thenReturn(1);

        // Act
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertEquals(Serializable.class, result.getExtraInterfaces()[0]);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_validGenericMetadata_returnsReturnsDeepStubs() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);

        // Act
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock_validMock_addsAnswer() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.addAnswer(any(Answer.class), any(Boolean.class))).thenReturn(container);

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

        // Assert
        assertEquals(mock, result);
        verify(container).addAnswer(any(Answer.class), any(Boolean.class));
    }

    @Test
    public void testActualParameterizedType_validMock_returnsGenericMetadata() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);

        when(mockSettings.getTypeToMock()).thenReturn(List.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadata);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertEquals(genericMetadata, result);
    }

}