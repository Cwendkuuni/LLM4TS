import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_WhenReturnTypeNotMockable_ReturnsEmptyValue() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Mocking a non-mockable return type
        when(invocation.getMock()).thenReturn("nonMockableType");

        Object result = returnsDeepStubs.answer(invocation);

        // Verify that it returns empty value for non-mockable types
        assertEquals(null, result);
    }

    @Test
    public void testAnswer_WhenInvocationMatches_ReturnsStubbedInvocation() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        Object result = returnsDeepStubs.answer(invocation);

        // Verify that it returns stubbed invocation if matches
        assertNotNull(result);
    }

    @Test
    public void testAnswer_WhenInvocationDoesNotMatch_ReturnsDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("equals", Object.class));

        Object result = returnsDeepStubs.answer(invocation);

        // Verify that it returns deep stub mock if no match found
        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock_ReturnsMockObject() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Method method = Object.class.getMethod("hashCode");
        when(invocation.getMethod()).thenReturn(method);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(null);

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Verify that it returns a mock object
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType_ReturnsGenericMetadataSupport() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings creationSettings = mock(CreationSettings.class);
        when(creationSettings.getTypeToMock()).thenReturn(Object.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(creationSettings);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Verify that it returns GenericMetadataSupport
        assertNotNull(result);
    }
}