import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatchers;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });

        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(new Object[0], invocation.getArguments());
        Assert.assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testEquals() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args1 = new Object[0];
        Object[] args2 = new Object[0];
        Invocation invocation1 = new Invocation(mock, mockitoMethod, args1, 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Invocation invocation2 = new Invocation(mock, mockitoMethod, args2, 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });

        Assert.assertTrue(invocation1.equals(invocation2));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        invocation.hashCode();
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] arguments = new Object[]{"arg1"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        PrintSettings printSettings = new PrintSettings();
        String print = invocation.toString(printSettings);
        Assert.assertFalse(print.isEmpty());
    }

    @Test
    public void testIsToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Assert.assertTrue(Invocation.isToString(invocation));

        mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Assert.assertFalse(Invocation.isToString(invocation));
    }

    @Test
    public void testGetLocation() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Assert.assertEquals(new Location(), invocation.getLocation());
    }

    @Test
    public void testIsVerified() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Assert.assertFalse(invocation.isVerified());

        invocation.markVerified();
        Assert.assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[0], 2, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });

        invocation1.markVerified();
        invocation1.markVerifiedInOrder();

        Assert.assertFalse(invocation2.isVerifiedInOrder());
    }

    @Test(expected = RuntimeException.class)
    public void testIsVerifiedInOrderWhenNotVerified() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        invocation.markVerifiedInOrder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpandVarArgsWithNullVarArgs() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args = new Object[]{null};
        Invocation.expandVarArgs(true, args);
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Object[] args1 = new Object[]{"arg1"};
        Object[] args2 = new Object[]{"arg1", "arg2"};
        Object[] expandedArgs1 = Invocation.expandVarArgs(true, args1);
        Object[] expandedArgs2 = Invocation.expandVarArgs(true, args2);

        Assert.assertArrayEquals(new Object[]{args1}, new Object[] {args1, args2});
        Assert.assertArrayEquals(expandedArgs1, new Object[]{args1});
        Assert.assertArrayEquals(expandedArgs2, new Object[]{args1, args2});
    }

    @Test
    public void testGetMethodName() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Assert.assertEquals("length", invocation.getMethodName());
    }

    @Test
    public void testIsVoid() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Assert.assertFalse(invocation.isVoid());

        mockitoMethod = Mockito.method(Void.TYPE, "voidMethod", new Class[0]);
        invocation = new Invocation(Mockito.mock(String.class), mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Assert.assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Assert.assertEquals("int", invocation.printMethodReturnType());
    }

    @Test
    public void testValidException() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws IOException {
                return null;
            }
        });
        Assert.assertTrue(invocation.isValidException(new IOException()));

        Assert.assertFalse(invocation.isValidException(new NullPointerException()));
    }

    @Test
    public void testValidReturnType() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return null;
            }
        });
        Assert.assertTrue(invocation.isValidReturnType(int.class));
        Assert.assertTrue(invocation.isValidReturnType(Integer.class));
        Assert.assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testCallRealMethod() throws Exception {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Exception {
                return receiver.length();
            }
        });
        Assert.assertEquals(5, invocation.callRealMethod());
    }
}