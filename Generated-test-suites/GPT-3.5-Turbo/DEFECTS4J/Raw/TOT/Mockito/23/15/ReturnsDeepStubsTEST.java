
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_invokeMethodWithMock_thenReturnValueFromStubbedInvocation() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        MyMockClass mock = mock(MyMockClass.class, answer);

        // Stubbing
        when(mock.someMethod()).thenReturn("Hello");

        // Act
        String result = mock.someMethod();

        // Assert
        assertEquals("Hello", result);
    }

    @Test
    public void testAnswer_invokeMethodWithNonMock_thenReturnValueFromDelegate() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        MyMockClass mock = new MyMockClass();

        // Act
        String result = answer.answer(createInvocation(mock));

        // Assert
        assertEquals("Default", result);
    }

    @Test
    public void testAnswer_invokeMethodWithUnmockableType_thenReturnValueFromDelegate() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        MyUnmockableClass mock = new MyUnmockableClass();

        // Act
        String result = answer.answer(createInvocation(mock));

        // Assert
        assertEquals("Default", result);
    }

    @Test
    public void testAnswer_invokeMethodWithMatchingStubbedInvocation_thenReturnValueFromStub() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        MyMockClass mock = mock(MyMockClass.class, answer);

        // Stubbing
        when(mock.someMethod()).thenReturn("Hello");

        // Act
        String result = answer.answer(createInvocation(mock.someMethod()));

        // Assert
        assertEquals("Hello", result);
    }

    @Test
    public void testAnswer_invokeMethodWithNonMatchingStubbedInvocation_thenReturnNewDeepStubMock() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        MyMockClass mock = mock(MyMockClass.class, answer);

        // Stubbing
        when(mock.someMethod()).thenReturn("Hello");

        // Act
        MyMockClass result = (MyMockClass) answer.answer(createInvocation(mock.anotherMethod()));

        // Assert
        assertNotNull(result);
    }

    private static InvocationOnMock createInvocation(Object mock) throws Exception {
        return new InvocationBuilder().getMock(mock).getMethod("someMethod").toInvocation();
    }

    static class MyMockClass {
        public String someMethod() {
            return "Default";
        }

        public String anotherMethod() {
            return "Default";
        }
    }

    static class MyUnmockableClass {
        public final String someMethod() {
            return "Default";
        }
    }
}

