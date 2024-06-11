import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_WithMatchingType_ReturnsMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(mock(StubbedInvocationMatcher.class)));

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testAnswer_WithNonMockableType_ReturnsEmptyValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testGetMock_WithMatchingInvocation_ReturnsAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        List<StubbedInvocationMatcher> stubbedInvocations = new ArrayList<>();
        stubbedInvocations.add(mock(StubbedInvocationMatcher.class));

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(stubbedInvocations);
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        when(container.getInvocationForStubbing().matches(any(InvocationOnMock.class))).thenReturn(true);

        Object result = returnsDeepStubs.getMock(invocation);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testGetMock_WithoutMatchingInvocation_ReturnsDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());

        Object result = returnsDeepStubs.getMock(invocation);

        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testRecordDeepStubMock_AddsAnswerToContainer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(true);

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        assertNotNull(result);
        assertTrue(result instanceof String);
        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());
    }

    @Test
    public void testActualParameterizedType_ReturnsGenericMetadataSupport() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(genericMetadataSupport.inferFrom(any())).thenReturn(genericMetadataSupport);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        assertNotNull(result);
        assertEquals(genericMetadataSupport, result);
        verify(mockUtil, times(1)).getMockHandler(mock);
    }
}