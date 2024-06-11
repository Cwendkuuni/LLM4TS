import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoCore;
import org.mockito.MockSettings;
import org.mockito.MockUtil;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationContainerImpl;
import org.mockito.internal.invocation.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.InvocationHandler;
import org.mockito.internal.stubbing.StubbingContext;
import org.mockito.internal.util.ReflectionUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private static final Class<ReturnsDeepStubs> CLASS_UNDER_TEST = ReturnsDeepStubs.class;

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object result = doAnswer(answer -> mock).answer(eq(any(InvocationOnMock.class)));
        assertSame(mock, result);
    }

    @Test
    public void testGetMock() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object result = new ReturnsDeepStubs().getMock(mock(InvocationOnMock.class), returnTypeGenericMetadata);
        assertSame(mock, result);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object result = new ReturnsDeepStubs().createNewDeepStubMock(returnTypeGenericMetadata);
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);
        Object result = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);
        assertEquals(mockSettings, result.getDefaultAnswer());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object result = new ReturnsDeepStubs().returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertEquals(new ReturnsDeepStubs(), result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        doAnswer(answer -> mock).answers(invocationContainerImpl);
        Object result = new ReturnsDeepStubs().recordDeepStubMock(mock, invocationContainerImpl);
        assertSame(mock, result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        Object result = new ReturnsDeepStubs().actualParameterizedType(mock);
        assertSame(mock.getClass(), result.getClass());
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerWithNullInvocationOnMock() {
        new ReturnsDeepStubs().answer(null);
    }

    @Test
    public void testAnswerWithThrowable() throws Throwable {
        doThrow(new RuntimeException()).when(mock(Object.class)).toString();
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object result = doAnswer(answer -> {
            throw new RuntimeException();
        }).answer(eq(any(InvocationOnMock.class)));
        fail("Expected a NullPointerException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMockWithEmptyOrNullReturnTypeGenericMetadata() {
        new ReturnsDeepStubs().getMock(mock(InvocationOnMock.class), null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMockWithMockitoCoreIsTypeMockableReturnsFalse() throws Throwable {
        doThrow(new IllegalStateException()).when(mock(MockitoCore.class)).mockType(any(Class.class));
        Object result = new ReturnsDeepStubs().getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));
        fail("Expected an IllegalStateException");
    }

    @Test(expected = NullPointerException.class)
    public void testGetMockWithNullInvocationContainerImpl() {
        new ReturnsDeepStubs().getMock(mock(InvocationOnMock.class), null);
    }

    @Test(expected = AssertionError.class)
    public void testStubbedInvocationMatcherDoesNotMatchAnyInvocation() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.getInvocationForStubbing()).thenReturn(invocation);
        doAnswer(answer -> {
            fail("Expected stubbed invocation matcher to not match any invocation");
            return null;
        }).when(invocationContainerImpl).getAnswer();
        new ReturnsDeepStubs().getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));
    }

    @Test(expected = RuntimeException.class)
    public void testRecordDeepStubMockWithException() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        doAnswer(answer -> {
            throw new RuntimeException();
        }).answers(invocationContainerImpl);
        new ReturnsDeepStubs().recordDeepStubMock(mock, invocationContainerImpl);
        fail("Expected a RuntimeException");
    }

    @Test(expected = IllegalStateException.class)
    public void testActualParameterizedTypeWithNull() {
        new ReturnsDeepStubs().actualParameterizedType(null);
    }
}