import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class InvocationTest {

    @Test
    public void testInvocationConstructor() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.spy(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, realMethod);

        verify(mockitoMethod).getArguments();
        verify(mockitoMethod).isVarArgs();
        verify(mockitoMethod).getName();
        verify(realMethod).invoke(mock, args);
    }
}