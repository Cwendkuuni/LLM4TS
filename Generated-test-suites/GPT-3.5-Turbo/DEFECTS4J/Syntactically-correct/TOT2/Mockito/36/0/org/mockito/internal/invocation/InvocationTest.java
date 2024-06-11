import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mockInstance = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mockInstance, method, args, 1, realMethod);
        
        assertEquals(mockInstance, invocation.getMock());
    }

    @Test
    public void testEquals() {
        Object mockInstance1 = mock(Object.class);
        MockitoMethod method1 = mock(MockitoMethod.class);
        Object[] args1 = new Object[]{"arg1", "arg2"};
        RealMethod realMethod1 = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mockInstance1, method1, args1, 1, realMethod1);

        Object mockInstance2 = mock(Object.class);
        MockitoMethod method2 = mock(MockitoMethod.class);
        Object[] args2 = new Object[]{"arg1", "arg2"};
        RealMethod realMethod2 = mock(RealMethod.class);
        
        Invocation invocation2 = new Invocation(mockInstance2, method2, args2, 2, realMethod2);
        
        assertFalse(invocation1.equals(invocation2));
    }

    // Add more test cases for each public method in the class...

}