import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_invokeEqualsMethod_shouldReturnDelegateResult() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals(returnsDeepStubs.delegate.returnValueFor(Object.class), result);
    }

    @Test
    public void testAnswer_invokeMockitoCoreMethod_shouldReturnDelegateResult() throws Throwable {
        // Arrange
        MockitoCore mockitoCore = mock(MockitoCore.class);
        returnsDeepStubs.mockitoCore = mockitoCore;
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals(returnsDeepStubs.delegate.returnValueFor(Object.class), result);
    }

    @Test
    public void testGetMock_invocationMatchesStubbedInvocationMatcher_shouldReturnStubbedAnswerResult() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Answer<Object> stubbedAnswer = mock(Answer.class);
        Object expectedResult = new Object();

        when(invocation.getMock()).thenReturn(new Object());
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(invocation.matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(expectedResult);

        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetMock_invocationDoesNotMatchStubbedInvocationMatcher_shouldReturnRecordedDeepStubMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMock()).thenReturn(new Object());
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());

        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ReturnsDeepStubs);
    }

    @Test
    public void testCreateNewDeepStubMock_noExtraInterfaces_shouldCreateMockWithReturnType() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = Object.class;
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[0]);

        // Act
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testCreateNewDeepStubMock_withExtraInterfaces_shouldCreateMockWithReturnTypeAndExtraInterfaces() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = Object.class;
        Class<?>[] extraInterfaces = new Class[]{Comparable.class};
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(extraInterfaces);

        // Act
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
        assertTrue(result instanceof Comparable);
    }

    @Test
    public void testWithSettingsUsing_noExtraInterfaces_shouldReturnMockSettingsWithDeepStubsAnswer() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = Object.class;
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[0]);
        MockSettings mockSettings = mock(MockSettings.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[0]);
        when(mockSettings.defaultAnswer(any(Answer.class))).thenReturn(mockSettings);
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenReturn(new ReturnsDeepStubs());

        // Act
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertSame(mockSettings, result);
        verify(mockSettings).defaultAnswer(any(Answer.class));
    }

    @Test
    public void testWithSettingsUsing_withExtraInterfaces_shouldReturnMockSettingsWithExtraInterfacesAndDeepStubsAnswer() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = Object.class;
        Class<?>[] extraInterfaces = new Class[]{Comparable.class};
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(extraInterfaces);
        MockSettings mockSettings = mock(MockSettings.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(extraInterfaces);
        when(mockSettings.defaultAnswer(any(Answer.class))).thenReturn(mockSettings);
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenReturn(new ReturnsDeepStubs());

        // Act
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertSame(mockSettings, result);
        verify(mockSettings).defaultAnswer(any(Answer.class));
        verify(mockSettings).extraInterfaces(extraInterfaces);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_shouldReturnReturnsDeepStubsAnswerWithCorrectGenericMetadata() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object mock = new Object();

        // Act
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertSame(returnTypeGenericMetadata, result.actualParameterizedType(mock));
    }

    @Test
    public void testRecordDeepStubMock_shouldAddAnswerToContainerAndReturnMock() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Answer<Object> answer = mock(Answer.class);

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

        // Assert
        assertSame(mock, result);
        verify(container).addAnswer(answer, false);
    }

    @Test
    public void testActualParameterizedType_shouldReturnGenericMetadataSupportUsingCreationSettings() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(mockitoCore.mockSettings(mock)).thenReturn(mockSettings);
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(GenericMetadataSupport.inferFrom(Object.class)).thenReturn(genericMetadata);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        assertSame(genericMetadata, result);
        verify(mockitoCore).mockSettings(mock);
        verify(GenericMetadataSupport).inferFrom(Object.class);
    }

    @Test
    public void testAnswer_typicalUseCase_shouldReturnMockitoDelegateReturnValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(new Object());
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertEquals(returnsDeepStubs.delegate.returnValueFor(Object.class), result);
    }

    @Test
    public void testSerialVersionUID_shouldHaveCorrectValue() {
        // Arrange
        long expected = -7105341425736035847L;
        long actual = ReturnsDeepStubs.serialVersionUID;

        // Assert
        assertEquals(expected, actual);
    }
}