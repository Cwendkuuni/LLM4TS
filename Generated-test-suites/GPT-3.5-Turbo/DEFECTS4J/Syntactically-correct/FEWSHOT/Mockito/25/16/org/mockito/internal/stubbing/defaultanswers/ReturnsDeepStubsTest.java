import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;
import org.junit.Test;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        Mockito.doReturn(delegate).when(returnsDeepStubs).getDelegate();

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        assertEquals(delegate, returnsDeepStubs.answer(invocation));
    }

    @Test
    public void testGetDelegate() throws Throwable {
        ReturnsEmptyValues delegate = returnsDeepStubs.getDelegate();
        assertEquals(ReturnsEmptyValues.class, delegate.getClass());
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(handler.getInvocationContainer()).thenReturn(container);
        when(invocation.getMock()).thenReturn(handler);
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        Object mock = returnsDeepStubs.getMock(invocation);

        assertEquals(mock, returnsDeepStubs.recordDeepStubMock(invocation, container));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMethod()).thenReturn(ReturnsDeepStubs.class.getDeclaredMethod("answer", InvocationOnMock.class));
        when(container.addAnswer(Mockito.any(Answer.class), Mockito.anyBoolean())).thenReturn(true);

        Object mock = returnsDeepStubs.recordDeepStubMock(invocation, container);

        assertEquals(mock, returnsDeepStubs.getMock(invocation));
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        Object mock = mock(Object.class);
        ReturnsDeepStubs returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);

        Mockito.doReturn(delegate).when(returnsDeepStubs).getDelegate();
        Mockito.doReturn(mock).when(returnsDeepStubs).getMock(mock);
        Mockito.doReturn(GenericMetadataSupport.inferFrom(Object.class)).when(returnsDeepStubs).actualParameterizedType(mock);

        assertEquals(GenericMetadataSupport.inferFrom(Object.class), returnsDeepStubs.actualParameterizedType(mock));
    }

}