import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private InvocationOnMock invocation;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        invocation = mock(InvocationOnMock.class);
    }

    @Test
    public void testAnswer_withNonMockableReturnType_shouldReturnDelegateReturnValue() throws Throwable {
        // Mock the InvocationOnMock
        Method method = TestClass.class.getMethod("nonMockableReturnTypeMethod");
        when(invocation.getMethod()).thenReturn(method);
        TestClass testObject = mock(TestClass.class);

        // Call the answer method
        Object result = returnsDeepStubs.answer(invocation);

        // Verify that delegate.returnValueFor is called
        verify(returnsDeepStubs.delegate).returnValueFor(TestClass.class);

        // Verify that the delegate's return value is returned
        assertEquals("DelegateReturnValue", result);
    }

    @Test
    public void testAnswer_withMockableReturnType_shouldReturnMock() throws Throwable {
        // Mock the InvocationOnMock
        Method method = TestClass.class.getMethod("mockableReturnTypeMethod");
        when(invocation.getMethod()).thenReturn(method);
        TestClass testObject = mock(TestClass.class);

        // Call the answer method
        Object result = returnsDeepStubs.answer(invocation);

        // Verify that Mockito.mock is called
        verifyStatic(Mockito.class);
        TestClass.mockableReturnTypeMethod();

        // Verify that the mock is returned
        assertEquals(testObject, result);
    }

    @Test
    public void testGetMock_withMatchingInvocationForVerification_shouldReturnMatchedStubAnswer() throws Throwable {
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        // Call the getMock method
        Object result = returnsDeepStubs.getMock(invocation);

        // Verify that the answer is obtained from the matched stub
        verify(stubbedInvocationMatcher).answer(invocation);

        // Verify that the answer is returned
        assertEquals("AnswerFromMatchedStub", result);
    }

    @Test
    public void testGetMock_withNoMatchingInvocationForVerification_shouldReturnDeepStubMock() throws Throwable {
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        TestClass testObject = mock(TestClass.class);
        when(invocation.getMock()).thenReturn(testObject);

        // Call the getMock method
        Object result = returnsDeepStubs.getMock(invocation);

        // Verify that Mockito.mock is called with the correct class
        verifyStatic(Mockito.class);
        TestClass.mock();

        // Verify that the mock is returned
        assertEquals(testObject, result);
    }

    @Test
    public void testRecordDeepStubMock_withReturnType_shouldAddAnswerToContainer() throws Throwable {
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(invocation.getMethod()).thenReturn(TestClass.class.getMethod("mockableReturnTypeMethod"));
        when(invocation.getMock()).thenReturn(mock(TestClass.class));

        // Call the recordDeepStubMock method
        returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Verify that the answer is added to the container
        verify(container).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void testActualParameterizedType_withMock_shouldReturnGenericMetadata() {
        TestClass testObject = mock(TestClass.class);

        // Call the actualParameterizedType method
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(testObject);

        // Verify that GenericMetadataSupport.inferFrom is called with the correct argument
        verifyStatic(GenericMetadataSupport.class);
        GenericMetadataSupport.inferFrom(TestClass.class);

        // Verify that the GenericMetadataSupport object is returned
        assertNotNull(result);
    }

    // Helper class for testing
    static class TestClass {
        public String nonMockableReturnTypeMethod() {
            return "DelegateReturnValue";
        }

        public static TestClass mockableReturnTypeMethod() {
            return mock(TestClass.class);
        }

        public static TestClass mock() {
            return mock(TestClass.class);
        }
    }

}