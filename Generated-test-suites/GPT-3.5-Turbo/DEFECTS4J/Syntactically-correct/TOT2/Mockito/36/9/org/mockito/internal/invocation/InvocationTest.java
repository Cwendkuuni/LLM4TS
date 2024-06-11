import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.stub();
        Object[] args = { "arg1", "arg2" };
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.stub();
        Object[] args = { "arg1", "arg2" };
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertEquals(method, invocation.getMethod());
    }

    // Add more test cases for other methods in the Invocation class

}