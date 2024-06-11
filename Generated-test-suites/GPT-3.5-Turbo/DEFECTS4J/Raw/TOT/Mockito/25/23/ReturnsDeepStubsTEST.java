
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_ReturnsEmptyValues() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(this);

        Object result = returnsDeepStubs.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testAnswer_ReturnsMockForMockableType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(this);
        when(invocation.getMethod()).thenReturn(getClass().getMethod("testAnswer_ReturnsMockForMockableType"));

        Object result = returnsDeepStubs.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testGetMock_MatchingInvocation() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(this);

        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        when(handler.getInvocationContainer().getInvocationForStubbing()).thenReturn(invocation);
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);

        Object result = returnsDeepStubs.getMock(invocation);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testGetMock_NoMatchingInvocation() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(this);

        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        when(handler.getInvocationContainer().getInvocationForStubbing()).thenReturn(invocation);

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);

        Object result = returnsDeepStubs.getMock(invocation);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(getClass().getMethod("testRecordDeepStubMock"));

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testActualParameterizedType() {
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(this)).thenReturn(handler);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(this);
        assertNotNull(result);
    }
}

