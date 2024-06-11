import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

    @Test
    public void testNewInvocation() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod(), is(mockitoMethod));
        assertThat(invocation.getArguments(), is(arguments));
        assertThat(invocation.sequenceNumber, is(1));
        assertThat(invocation.verified, is(false));
        assertThat(invocation.verifiedInOrder, is(false));
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{new String("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(invocation.getSequenceNumber(), is(1));
    }

    @Test
    public void testGetMethodName() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{new String("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(invocation.getMethodName(), is("length"));
    }

    @Test
    public void testGetMockName() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{new String("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(invocation.new MockUtil().getMockName(mock), is("mock(String.class)"));
    }

    @Test
    public void testIsVerified() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{new String("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(invocation.isVerified(), is(false));
    }

    @Test
    public void testMarkVerified() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{new String("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        invocation.markVerified();

        assertThat(invocation.isVerified(), is(true));
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod1 = Mockito.method(String.class, "length");
        Object[] arguments1 = new Object[]{new String("test1")};
        Invocation invocation1 = new Invocation(mock, mockitoMethod1, arguments1, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        Object mock2 = Mockito.mock(String.class);
        MockitoMethod mockitoMethod2 = Mockito.method(String.class, "length");
        Object[] arguments2 = new Object[]{new String("test2")};
        Invocation invocation2 = new Invocation(mock, mockitoMethod2, arguments2, 2, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        assertThat(invocation1.isVerifiedInOrder(), is(true));
        assertThat(invocation2.isVerifiedInOrder(), is(true));
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{new String("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(invocation.toString(), is("mock(String.class).length()"));
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{new String("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"));
        assertThat(invocation.toString(matchers, new PrintSettings()), is("mock(String.class).length(\"test\")"));
    }

    @Test
    public void testToStringMultilineWithMatchers() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "substring", String.class, int.class, int.class);
        Object[] arguments = new Object[]{mock, 0, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(mock), ArgumentMatchers.eq(0), ArgumentMatchers.eq(3));
        assertThat(invocation.toString(matchers, new PrintSettings(true)), is("mock(String.class).substring(\n" +
                "    this,\n" +
                "    argThat(arg -> arg instanceof String),\n" +
                "    0,\n" +
                "    3\n" +
                ")"));
    }

    @Test
    public void testIsToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString");
        Object[] arguments = new Object[]{};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(Invocation.isToString(invocation), is(true));
    }

    @Test
    public void testIsValidException() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", Exception.class);
        Object[] arguments = new Object[]{new RuntimeException("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                throw new RuntimeException("test");
            }
        });

        assertThat(invocation.isValidException(new RuntimeException("test")), is(true));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{new String("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(invocation.isValidReturnType(int.class), is(true));
    }

    @Test
    public void testIsVoid() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "voidMethod");
        Object[] arguments = new Object[]{};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(invocation.isVoid(), is(false));
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{new String("test")};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return null;
            }
        });

        assertThat(invocation.printMethodReturnType(), is("int"));
    }
}