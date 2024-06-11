
import org.junit.*;
import org.mockito.*;
import org.mockito.internal.InternalMockHandler;
import org.mockito.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

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
    public void testAnswer_invocationMatches_ReturnsMatchingStubResult() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        doReturn(true).when(container.getInvocationForStubbing()).matches(invocation);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("stubbed value");

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals("stubbed value", result);
    }

    @Test
    public void testAnswer_invocationDoesNotMatch_NotMockableReturnType_ReturnsEmptyValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(invocation.getMock()).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock)).thenReturn(mockHandler);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(new MockCreationValidator().isTypeMockable(Object.class)).thenReturn(false);
        when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        when(delegate.returnValueFor(Object.class)).thenReturn("empty value");
        when(returnsDeepStubs.returnEmptyValues()).thenReturn(delegate);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals("empty value", result);
    }

    @Test
    public void testAnswer_invocationDoesNotMatch_MockableReturnType_ReturnsMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(invocation.getMock()).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock)).thenReturn(mockHandler);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(new MockCreationValidator().isTypeMockable(Object.class)).thenReturn(true);
        when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testGetMock_verifiesInvocationForVerification_ReturnsMatchedInvocationResult() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        List<StubbedInvocationMatcher> stubbedInvocations = new ArrayList<>();
        when(container.getStubbedInvocations()).thenReturn(stubbedInvocations);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.matches(invocation)).thenReturn(true);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        stubbedInvocations.add(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("matched invocation result");

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertEquals("matched invocation result", result);
    }

    @Test
    public void testGetMock_noMatchingInvocationForVerification_ReturnsRecordDeepStubMockResult() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        List<StubbedInvocationMatcher> stubbedInvocations = new ArrayList<>();
        when(container.getStubbedInvocations()).thenReturn(stubbedInvocations);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.matches(invocation)).thenReturn(false);

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testRecordDeepStubMock_returnsMockObjectWithMockAnswerAndContainerAddAnswerCalled() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocation.getMock()).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mock(CreationSettings.class));
        when(new MockUtil().getMockHandler(mock).getMockSettings().getTypeToMock()).thenReturn(Object.class);
        Answer<Object> answer = mock(Answer.class);
        when(answer.answer(invocation)).thenReturn("mock object");
        doCallRealMethod().when(container).addAnswer(answer, false);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mock(CreationSettings.class));
        when(new MockUtil().getMockHandler(mock).getMockSettings().getTypeToMock()).thenReturn(Object.class);
        when(container.addAnswer(answer, false)).thenReturn("mock object");

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
        verify(container).addAnswer(answer, false);
    }

    @Test
    public void testActualParameterizedType_returnsGenericMetadataSupportFromMockSettingsTypeToMock() throws Throwable {
        // Arrange
        Object mock = mock(InvocationOnMock.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadata);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        assertEquals(genericMetadata, result);
    }
}

