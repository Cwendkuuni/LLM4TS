
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_withNonMockableReturnType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("NonMockableType");

        Object result = returnsDeepStubs.answer(invocation);

        assertEquals("NonMockableType", result);
    }

    @Test
    public void testAnswer_withMockableReturnType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(MockitoCore.class));

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof MockitoCore);
    }

    @Test
    public void testAnswer_withMatchingInvocationContainer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(MockitoCore.class));

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(true);

        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);

        ReflectionTestUtils.setField(returnsDeepStubs, "mockitoCore", mock(MockitoCore.class));
        ReflectionTestUtils.setField(returnsDeepStubs, "delegate", mock(ReturnsEmptyValues.class));
        ReflectionTestUtils.setField(returnsDeepStubs, "mockUtil", mockUtil);

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testAnswer_withNonMatchingInvocationContainer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(MockitoCore.class));

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(false);

        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);

        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);

        ReflectionTestUtils.setField(returnsDeepStubs, "mockitoCore", mock(MockitoCore.class));
        ReflectionTestUtils.setField(returnsDeepStubs, "delegate", mock(ReturnsEmptyValues.class));
        ReflectionTestUtils.setField(returnsDeepStubs, "mockUtil", mockUtil);

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testCreateNewDeepStubMock_withGenericMetadata() throws Throwable {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);

        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.mock(eq(Object.class), any())).thenReturn(mock(Object.class));

        ReflectionTestUtils.setField(returnsDeepStubs, "mockitoCore", mockitoCore);

        Object result = returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport);

        assertNotNull(result);
        assertTrue(result instanceof Object);

        verify(mockitoCore).mock(eq(Object.class), any());
    }

    @Test
    public void testWithSettingsUsing_withGenericMetadataAndExtraInterfaces() throws Throwable {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});

        ReflectionTestUtils.setField(returnsDeepStubs, "mockitoCore", mock(MockitoCore.class));

        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);

        assertNotNull(mockSettings);
        assertTrue(mockSettings instanceof MockSettings);
    }

    @Test
    public void testWithSettingsUsing_withGenericMetadataWithoutExtraInterfaces() throws Throwable {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[0]);

        ReflectionTestUtils.setField(returnsDeepStubs, "mockitoCore", mock(MockitoCore.class));

        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);

        assertNotNull(mockSettings);
        assertTrue(mockSettings instanceof MockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_withGenericMetadata() throws Throwable {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);

        assertNotNull(result);
        assertTrue(result instanceof ReturnsDeepStubs);
    }

    @Test
    public void testRecordDeepStubMock_withMockAndInvocationContainer() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.addAnswer(any(Answer.class), eq(false))).thenReturn(true);

        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

        assertNotNull(result);
        assertTrue(result instanceof Object);
        verify(container).addAnswer(any(Answer.class), eq(false));
    }
}
