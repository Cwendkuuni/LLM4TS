import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class ReturnsDeepStubsTest {

    @Test
    public void testMethod1() {
        MyInterface mock = Mockito.mock(MyInterface.class, Answers.RETURNS_DEEP_STUBS);
        Object result = mock.method1();
        Object expected = new MyDeepStub(); // replace with a deep stub implementation of MyInterface
        assertEquals(expected, result);
    }
}

// MyInterface and MyDeepStub should be defined according to the actual implementation