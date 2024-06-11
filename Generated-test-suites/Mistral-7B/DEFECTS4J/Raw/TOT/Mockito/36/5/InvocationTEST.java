import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class InvocationTest {

    private MyMock mock;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        mock = mock(MyMock.class);
    }

    @Test
    public void testMethodWithNoArguments() {
        MyClass myClass = mock(MyClass.class);
        when(myClass.method1()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                invocation.markVerified();
                return 42;
            }
        });

        MyClass myClassSpy = spy(MyClass.class);

        when(myClassSpy.method1()).thenCallRealMethod();

        int result = myClass.method1();
        int spyResult = myClassSpy.method1();

        verify(myClass).method1();
        verifyZeroInteractions(myClassSpy);

        verify(myClass).method1();
        verify(myClassSpy).method1();

        verify(myClass, never()).method2();

        verifyNoMoreInteractions(myClass);
        verifyNoMoreInteractions(myClassSpy);

        assertEquals(42, result);
        assertEquals(42, spyResult);
    }

    @Test
    public void testMethodWithOneArgument() {
        MyClass myClass = mock(MyClass.class);
        when(myClass.method2(ArgumentMatchers.integer(1))).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                invocation.markVerified();
                int arg = invocation.getArguments()[0];
                return String.valueOf(arg);
            }
        });

        String result = myClass.method2(1);

        verify(myClass).method2(ArgumentMatchers.integer(1));
        verifyZeroInteractions(myClass);

        assertEquals("1", result);
    }

    @Test(expected = RuntimeException.class)
    public void testMethodWithException() {
        MyClass myClass = mock(MyClass.class);
        when(myClass.method3()).thenThrow(new RuntimeException("Test Exception"));

        try {
            myClass.method3();
        } catch (RuntimeException e) {
            // Do nothing
        }

        verify(myClass).method3();
        verifyZeroInteractions(myClass);

        throw new RuntimeException("Test Exception");
    }

    @Test
    public void testMethodWithVarArgs() {
        MyClass myClass = mock(MyClass.class);
        when(myClass.method4(ArgumentMatchers.array(Integer.class), ArgumentMatchers.array(Integer.class)))
                .thenAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        invocation.markVerified();
                        Object[] args = invocation.getArguments();
                        List<Integer> argList = Arrays.asList((Integer[]) args[0]);
                        List<Integer> argList2 = Arrays.asList((Integer[]) args[1]);
                        int sum = Collections.sum(argList) + Collections.sum(argList2);
                        System.out.println("Sum: " + sum);
                        return null;
                    }
                });

        myClass.method4(new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6});

        verify(myClass).method4(ArgumentMatchers.array(Integer.class), ArgumentMatchers.array(Integer.class));
        verifyZeroInteractions(myClass);

        verify(myClass, times(1)).method4(ArgumentMatchers.array(Integer.class),
                ArgumentMatchers.array(Integer.class));
    }

    @Test
    public void testMethodWithVoidReturn() {
        MyClass myClass = mock(MyClass.class);
        doNothing().when(myClass).method5();

        myClass.method5();

        verify(myClass).method5();
        verifyZeroInteractions(myClass);
    }

    private static class MyMock {}
    private static class MyClass {
        public int method1() {
            return 42;
        }

        public String method2(int arg) {
            return String.valueOf(arg);
        }

        public void method3() {
            throw new RuntimeException("Test Exception");
        }

        public void method4(Integer[] args1, Integer[] args2) {
            // Do nothing
        }

        public void method5() {
            // Do nothing
        }
    }
}