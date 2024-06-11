import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Mock the actualParameterizedType method
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{});

        // Mock the createNewDeepStubMock method
        Object newMock = mock(Object.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("equals", Object.class));
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.mock(Object.class, withSettingsUsing(returnTypeGenericMetadata))).thenReturn(newMock);

        // Mock the recordDeepStubMock method
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(null);

        // Mock the getMock method
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(mockUtil.getMockHandler(any())).thenReturn(handler);

        // Mock the isTypeMockable method
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(false);

        // Mock the returnValueFor method
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        when(delegate.returnValueFor(Object.class)).thenReturn(null);

        // Set internal objects
        returnsDeepStubs.mockitoCore = mockitoCore;
        returnsDeepStubs.delegate = delegate;

        // Execute the method under test
        Object result = returnsDeepStubs.answer(invocation);

        // Verify the result
        assertNull(result);

        // Verify interactions
        verify(returnTypeGenericMetadata).rawType();
        verify(returnTypeGenericMetadata).rawExtraInterfaces();
        verify(invocation).getMethod();
        verify(mockitoCore).mock(Object.class, withSettingsUsing(returnTypeGenericMetadata));
        verify(container).addAnswer(any(Answer.class), anyBoolean());
        verify(mockUtil).getMockHandler(any());
        verify(mockitoCore).isTypeMockable(Object.class);
        verify(delegate).returnValueFor(Object.class);
    }

    @Test
    public void testGetMock() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Mock the getInvocationContainer method
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        // Mock the getMockHandler method
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(handler);

        // Set internal object
        returnsDeepStubs.mockUtil = mockUtil;

        // Execute the method under test
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Verify the result
        assertNull(result);

        // Verify interactions
        verify(handler).getInvocationContainer();
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
    }

    @Test
    public void testCreateNewDeepStubMock() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Mock the mock method
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object newMock = mock(Object.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockitoCore.mock(Object.class, withSettingsUsing(returnTypeGenericMetadata))).thenReturn(newMock);

        // Set internal object
        returnsDeepStubs.mockitoCore = mockitoCore;

        // Execute the method under test
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Verify the result
        assertEquals(newMock, result);

        // Verify interactions
        verify(returnTypeGenericMetadata).rawType();
        verify(mockitoCore).mock(Object.class, withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Mock the withSettings method
        MockSettings mockSettings = mock(MockSettings.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockSettings.extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())).thenReturn(mockSettings);
        when(withSettings()).thenReturn(mockSettings);

        // Mock the returnsDeepStubsAnswerUsing method
        ReturnsDeepStubs deepStubsAnswer = mock(ReturnsDeepStubs.class);
        when(deepStubsAnswer.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenReturn(deepStubsAnswer);

        // Execute the method under test
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Verify the result
        assertEquals(mockSettings, result);

        // Verify interactions
        verify(returnTypeGenericMetadata).rawExtraInterfaces();
        verify(returnTypeGenericMetadata).rawType();
        verify(mockSettings).extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces());
        verify(withSettings()).defaultAnswer(deepStubsAnswer);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Mock the actualParameterizedType method
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);

        // Execute the method under test
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Verify the result
        assertNotNull(result);

        // Verify interactions
        verify(returnTypeGenericMetadata).rawType();
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // Mock the addAnswer method
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(null);

        // Execute the method under test
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

        // Verify the result
        assertEquals(mock, result);

        // Verify interactions
        verify(container).addAnswer(any(Answer.class), anyBoolean());
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        // Mock the getMockHandler method
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);

        // Mock the getTypeToMock method
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        // Mock the inferFrom method
        when(genericMetadataSupport.inferFrom(Object.class)).thenReturn(genericMetadataSupport);

        // Execute the method under test
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Verify the result
        assertEquals(genericMetadataSupport, result);

        // Verify interactions
        verify(mockUtil).getMockHandler(mock);
        verify(handler).getMockSettings();
        verify(mockSettings).getTypeToMock();
        verify(genericMetadataSupport).inferFrom(Object.class);
    }
}