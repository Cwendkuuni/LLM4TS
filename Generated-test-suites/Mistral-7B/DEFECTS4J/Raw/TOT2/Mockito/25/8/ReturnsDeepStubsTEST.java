import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    private Answer<Object> returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswerMethod() {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        Object result = returnsDeepStubs.answer(invocation);
        assertEquals(mock, result);
    }

    @Test
    public void testGetMockMethod() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        Object result = returnsDeepStubs.getMock(invocation);
        assertEquals(mock, result);
    }

    @Test
    public void testRecordDeepStubMockMethod() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(new Method("test"));

        returnsDeepStubs.recordDeepStubMock(invocation, new InvocationContainerImpl());

        verify(new InvocationContainerImpl()).addAnswer(Mockito.eq(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mock;
            }
        }), Mockito.eq(false));
    }

    @Test
    public void testActualParameterizedTypeMethod() {
        Object mock = Mockito.mock(Object.class);

        GenericMetadataSupport result = new ReturnsDeepStubs().actualParameterizedType(mock);

        assertEquals(mock.getClass(), result.rawType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerMethodWhenTypeIsNotMockable() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(new Method("test", new Class[]{int.class}));

        returnsDeepStubs.answer(invocation);
    }

    @Test
    public void testGetMockMethodWhenInvocationIsNotFound() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        assertNull(returnsDeepStubs.getMock(invocation));
    }
}