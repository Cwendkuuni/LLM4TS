import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    @Test
    public void answer_shouldReturnExpectedValueWhenInvocationMatches() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(new HashMap<Integer, Set<String>>());
        Mockito.when(invocation.getMethod()).thenReturn(Map.class.getMethod("entrySet"));

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Set);
    }

    @Test
    public void answer_shouldReturnReturnValueForNonMockableType_WhenInvocationDoesNotMatch() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(new HashSet<Integer>());
        Mockito.when(invocation.getMethod()).thenReturn(Set.class.getMethod("size"));

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void getMock_shouldReturnMatchingStubbedInvocation_WhenInvocationMatches() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
        Mockito.when(invocation.getMock()).thenReturn(new HashMap<Integer, Set<String>>());
        Mockito.when(handler.getInvocationContainer()).thenReturn(container);
        Mockito.when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        Mockito.when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        Mockito.when(stubbedInvocationMatcher.answer(invocation)).thenReturn("mockedValue");

        // Act
        Object result = answer.getMock(invocation);

        // Assert
        assertEquals("mockedValue", result);
    }

    @Test
    public void getMock_shouldReturnDeepStubMock_WhenInvocationDoesNotMatch() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        Mockito.when(invocation.getMock()).thenReturn(new HashMap<Integer, Set<String>>());
        Mockito.when(handler.getInvocationContainer()).thenReturn(container);
        Mockito.when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        Mockito.when(invocation.getMethod()).thenReturn(Map.Entry.class.getMethod("getValue"));

        // Act
        Object result = answer.getMock(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Map.Entry);
    }

    @Test
    public void recordDeepStubMock_shouldReturnMockWithAnswer_WhenInvocationIsMadeOnMock() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        Mockito.when(invocation.getMethod()).thenReturn(Map.Entry.class.getMethod("getKey"));
        Mockito.when(invocation.getMock()).thenReturn(new HashMap<Integer, Set<String>>());
        Mockito.when(container.addAnswer(Mockito.any(Answer.class), Mockito.anyBoolean())).thenReturn(true);
        Mockito.when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(Mockito.mock(InternalMockHandler.class));
        Mockito.doAnswer(invocationOnMock -> "mockedValue").when(new MockUtil().getMockHandler(invocation.getMock())).addAnswer(Mockito.any(Answer.class), Mockito.anyBoolean());

        // Act
        Object result = answer.recordDeepStubMock(invocation, container);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Map.Entry);
        assertEquals("mockedValue", result.toString());
    }

    @Test
    public void actualParameterizedType_shouldReturnCorrectGenericMetadataSupport() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        Object mock = Mockito.mock(Object.class);
        CreationSettings mockSettings = Mockito.mock(CreationSettings.class);
        Mockito.when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(new HashMap<Integer, Set<String>>().getClass());

        // Act
        GenericMetadataSupport result = answer.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        assertEquals(new HashMap<Integer, Set<String>>().getClass(), result.rawType());
    }

}