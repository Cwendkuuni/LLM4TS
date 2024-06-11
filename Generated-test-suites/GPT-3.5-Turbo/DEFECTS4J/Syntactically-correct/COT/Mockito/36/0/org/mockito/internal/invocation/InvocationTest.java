import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.invocation.realmethod.RealMethod;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = Mockito.mock(MockitoMethod.class);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        
        assertEquals(mock, invocation.getMock());
    }
    
    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = Mockito.mock(MockitoMethod.class);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        
        assertEquals(mockitoMethod, invocation.getMethod());
    }
    
    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = Mockito.mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        
        assertArrayEquals(args, invocation.getArguments());
    }
    
    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = Mockito.mock(MockitoMethod.class);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        
        assertFalse(invocation.isVerified());
    }
    
    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = Mockito.mock(MockitoMethod.class);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        
        assertEquals(new Integer(sequenceNumber), invocation.getSequenceNumber());
    }
    
    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = Mockito.mock(MockitoMethod.class);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        
        assertFalse(invocation.isVerifiedInOrder());
    }
    
    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod mockitoMethod1 = Mockito.mock(MockitoMethod.class);
        MockitoMethod mockitoMethod2 = Mockito.mock(MockitoMethod.class);
        Object[] args1 = new Object[] { "arg1", "arg2" };
        Object[] args2 = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        
        Invocation invocation1 = new Invocation(mock, mockitoMethod1, args1, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod1, args1, sequenceNumber, realMethod);
        Invocation invocation3 = new Invocation(mock, mockitoMethod2, args1, sequenceNumber, realMethod);
        Invocation invocation4 = new Invocation(mock, mockitoMethod1, args2, sequenceNumber, realMethod);
        
        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation4));
    }
    
}