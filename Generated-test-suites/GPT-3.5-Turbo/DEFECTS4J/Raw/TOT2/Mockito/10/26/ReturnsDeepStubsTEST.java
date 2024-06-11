import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_WhenReturnTypeNotMockable_ReturnsDelegateValue() throws Throwable {
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        assertEquals("Delegate value should be returned for non-mockable type", returnsDeepStubs.delegate().returnValueFor(String.class), result);
    }

    @Test(expected = Exception.class)
    public void testAnswer_WhenExceptionThrown_ThrowsException() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        returnsDeepStubs.answer(invocation);
    }

    @Test
    public void testAnswer_WhenInvocationMatches_ReturnsAnswer() throws Throwable {
        // Add test for when invocation matches stubbed invocation
    }

    @Test
    public void testAnswer_WhenNoMatchForInvocation_RecordDeepStubAnswer() throws Throwable {
        // Add test for when there is no match for the invocation
    }

    @Test
    public void testActualParameterizedType_InferFromMockSettings_ReturnsGenericMetadata() {
        // Add test for actualParameterizedType method
    }

    @Test
    public void testNewDeepStubMock_WithGenericType_ReturnsMock() {
        // Add test for newDeepStubMock method with generic type
    }

    // Additional test cases for edge cases and error scenarios can be added here

}