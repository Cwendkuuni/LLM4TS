import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_successfulMatch_returnStubbedAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(GenericMetadataSupport.class));
        when(invocation.getMethod()).thenReturn(GenericMetadataSupport.class.getMethod("rawType"));

        Object actual = returnsDeepStubs.answer(invocation);

        assertNotNull(actual);
        assertEquals(mock(GenericMetadataSupport.class), actual);
    }

    @Test
    public void testAnswer_unmockableType_returnEmptyValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(GenericMetadataSupport.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("getClass"));

        Object actual = returnsDeepStubs.answer(invocation);

        assertNull(actual);
    }

    @Test
    public void testGetMock_successfulMatch_returnStubbedInvocationMatcherAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(invocation.getMock()).thenReturn(mock(GenericMetadataSupport.class));
        when(mockHandler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(mockHandler.getInvocationContainer().getInvocationForStubbing()).thenReturn(invocation);
        when(mockHandler.getInvocationContainer().getStubbedInvocations()).thenReturn(singletonList(mock(StubbedInvocationMatcher.class)));

        Object actual = returnsDeepStubs.getMock(invocation, mock(GenericMetadataSupport.class));

        assertNotNull(actual);
        assertEquals(mock(StubbedInvocationMatcher.class).answer(invocation), actual);
    }

    @Test
    public void testGetMock_noMatch_createNewDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(invocation.getMock()).thenReturn(mock(GenericMetadataSupport.class));
        when(mockHandler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(mockHandler.getInvocationContainer().getInvocationForStubbing()).thenReturn(invocation);
        when(mockHandler.getInvocationContainer().getStubbedInvocations()).thenReturn(new ArrayList<>());
        when(mockitoCore.isTypeMockable(any())).thenReturn(true);
        when(mockitoCore.mock(any(), any())).thenReturn(mock(InvocationContainerImpl.class));

        Object actual = returnsDeepStubs.getMock(invocation, mock(GenericMetadataSupport.class));

        assertNotNull(actual);
        assertEquals(mock(InvocationContainerImpl.class), actual);
    }

    @Test
    public void testCreateNewDeepStubMock_noExtraInterfaces_createMockWithDefaultSettings() {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);

        Object actual = returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport);

        assertNotNull(actual);
        assertEquals(mockitosCore.mock(Object.class, withSettings()), actual);
    }

    @Test
    public void testCreateNewDeepStubMock_withExtraInterfaces_createMockWithExtraInterfaces() {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});

        Object actual = returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport);

        assertNotNull(actual);
        assertEquals(mockitosCore.mock(Object.class, withSettings().extraInterfaces(Serializable.class)), actual);
    }

    @Test
    public void testWithSettingsUsing_noExtraInterfaces_returnSettingsWithDefaultAnswer() {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[]{});

        MockSettings actual = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);

        assertNotNull(actual);
        assertEquals(withSettings().defaultAnswer(returnsDeepStubsAnswerUsing(genericMetadataSupport)), actual);
    }

    @Test
    public void testWithSettingsUsing_withExtraInterfaces_returnSettingsWithExtraInterfaces() {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});

        MockSettings actual = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);

        assertNotNull(actual);
        assertEquals(withSettings().extraInterfaces(Serializable.class).defaultAnswer(returnsDeepStubsAnswerUsing(genericMetadataSupport)), actual);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_returnSelfAnswerWithReturnTypeGenericMetadata() {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        ReturnsDeepStubs actual = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);

        assertNotNull(actual);
        assertEquals(genericMetadataSupport, actual.actualParameterizedType(null));
    }

    @Test
    public void testRecordDeepStubMock_addAnswerToContainerAndReturnMock() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        Object actual = returnsDeepStubs.recordDeepStubMock(mock, container);

        assertNotNull(actual);
        verify(container, times(1)).addAnswer(any(), eq(false));
        assertEquals(mock, actual);
    }


}