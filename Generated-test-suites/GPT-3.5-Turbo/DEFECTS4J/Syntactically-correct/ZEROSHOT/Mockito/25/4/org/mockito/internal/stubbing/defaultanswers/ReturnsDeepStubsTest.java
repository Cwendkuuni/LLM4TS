import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.StubbedInvocationMatcher;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_returnsDelegateValueForNonMockableType() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(Mockito.mock(String.class));
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));

        Object result = returnsDeepStubs.answer(invocation);

        assertSame(returnsDeepStubs.delegate.returnValueFor(String.class), result);
    }

    @Test
    public void testAnswer_returnsMockForMatchingInvocation() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(Mockito.mock(String.class));
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));

        Object result = returnsDeepStubs.answer(invocation);

        assertSame(invocation.getMock(), result);
    }

    @Test
    public void testGetMock_returnsMatchingMockForVerification() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(Mockito.mock(String.class));

        ReturnsDeepStubs returnsDeepStub = Mockito.mock(ReturnsDeepStubs.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(Mockito.mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(invocation.getMock()).getInvocationContainer())
                .thenReturn(Mockito.mock(InvocationContainerImpl.class));
        when(mockUtil.getMockHandler(invocation.getMock()).getInvocationContainer().getStubbedInvocations())
                .thenReturn(Mockito.mock(StubbedInvocationMatcher[].class));
        when(mockUtil.getMockHandler(invocation.getMock()).getInvocationContainer().getInvocationForStubbing())
                .thenReturn(Mockito.mock(InvocationOnMock.class));

        Object result = returnsDeepStub.getMock(invocation);

        assertSame(invocation.getMock(), result);
    }

    @Test
    public void testGetMock_returnsRecordedDeepStubMock() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(Mockito.mock(String.class));

        ReturnsDeepStubs returnsDeepStub = Mockito.mock(ReturnsDeepStubs.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(Mockito.mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(invocation.getMock()).getInvocationContainer())
                .thenReturn(Mockito.mock(InvocationContainerImpl.class));
        when(mockUtil.getMockHandler(invocation.getMock()).getInvocationContainer().getStubbedInvocations())
                .thenReturn(new StubbedInvocationMatcher[0]);

        Object result = returnsDeepStub.getMock(invocation);

        assertNotNull(result);
        assertSame(result, returnsDeepStub.recordDeepStubMock(invocation,
                mockUtil.getMockHandler(invocation.getMock()).getInvocationContainer()));
    }

    @Test
    public void testRecordDeepStubMock_addsAnswerToContainer() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));

        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        InternalMockHandler<Object> mockHandler = Mockito.mock(InternalMockHandler.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);

        ReturnsDeepStubs returnsDeepStub = new ReturnsDeepStubs();
        Object result = returnsDeepStub.recordDeepStubMock(invocation, container);

        verify(container, times(1)).addAnswer(any(Answer.class), eq(false));
        assertSame(result, container.getAnswerFor(invocation));
    }

    @Test
    public void testActualParameterizedType_infersFromMockSettings() {
        Object mock = Mockito.mock(Object.class);
        CreationSettings mockSettings = Mockito.mock(CreationSettings.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        ReturnsDeepStubs returnsDeepStub = new ReturnsDeepStubs();
        when(mockUtil.getMockHandler(mock)).thenReturn(Mockito.mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        GenericMetadataSupport genericMetadata = GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock());
        GenericMetadataSupport actualType = returnsDeepStub.actualParameterizedType(mock);

        assertSame(genericMetadata, actualType);
    }
}