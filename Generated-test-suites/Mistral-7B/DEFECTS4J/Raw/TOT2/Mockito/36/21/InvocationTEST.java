import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString", new Class[0]);
        Object[] args = new Object[]{"test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));

        verify(mock).toString();
        verifyNoMoreInteractions(mock);
    }
}

// Edge cases and exception handling scenarios:
// 1. Null mock
// 2. Null mockitoMethod
// 3. Null args
// 4. Negative sequenceNumber
// 5. Null realMethod
// 6. RealMethod throws an exception