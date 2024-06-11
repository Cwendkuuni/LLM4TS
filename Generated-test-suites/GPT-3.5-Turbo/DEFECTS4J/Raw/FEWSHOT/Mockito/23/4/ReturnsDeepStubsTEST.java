import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        assertNull(returnsDeepStubs.answer(invocation));
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        object mock = mock(Object.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(new MockUtil().getMockHandler(any())).thenReturn(new InternalMockHandler<Object>(mock));
        assertNull(returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata));
    }

    @Test
    public void testGetMockStubbedInvocationMatcher() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        object mock = mock(Object.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(new MockUtil().getMockHandler(any())).thenReturn(new InternalMockHandler<Object>(mock));
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(mock(StubbedInvocationMatcher.class)));
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        assertNull(returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata));
    }

    @Test
    public void testCreateNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        assertEquals(mock(Object.class), returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Serializable.class});
        assertEquals(mock(MockSettings.class), returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        assertEquals(mock(ReturnsDeepStubs.class), returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        assertNull(returnsDeepStubs.recordDeepStubMock(mock, container));
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        assertEquals(mock(GenericMetadataSupport.class), returnsDeepStubs.actualParameterizedType(mock));
    }
}