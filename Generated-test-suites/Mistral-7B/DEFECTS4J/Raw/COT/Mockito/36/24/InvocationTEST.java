import org.junit.Test;
import org.junit. Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString");
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));

        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(args, invocation.getArguments());
        Assert.assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", String[].class);
        Object[] args = new Object[]{Arrays.asList("a", "b"), null};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));

        Object[] expected = new Object[]{"a", "b"};
        Assert.assertArrayEquals(expected, invocation.getArguments());
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNull() {
        Invocation invocation1 = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "toString"), new Object[0], 1, Mockito.mock(RealMethod.class));
        Invocation invocation2 = null;
        invocation1.equals(invocation2);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsDifferentClass() {
        Invocation invocation1 = new Invocation(Mockito.mock(String.class), Mockito.method(String.class, "toString"), new Object[0], 1, Mockito.mock(RealMethod.class));
        Invocation invocation2 = new Invocation(Mockito.mock(Integer.class), Mockito.method(Integer.class, "toString"), new Object[0], 1, Mockito.mock(RealMethod.class));
        invocation1.equals(invocation2);
    }

    @Test
    public void testHashCode() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString");
        Object[] args = new Object[0];
        Invocation invocation1 = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));
        Object[] args2 = new Object[]{};
        Invocation invocation2 = new Invocation(mock, mockitoMethod, args2, 2, Mockito.mock(RealMethod.class));

        Assert.assertNotEquals(invocation1.hashCode(), invocation2.hashCode());
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString");
        Object[] args = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));

        String expected = mock.getClass().getName() + "." + mockitoMethod.getName();
        Answer answer = Mockito.answer(new Object(){
            @Override
            public String toString() {
                return "answer";
            }
        });
        Mockito.when(mock.toString()).thenAnswer(answer);

        String result = invocation.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", String[].class);
        Object[] args = new Object[]{Arrays.asList("a", "b"), null};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));

        String expected = mock.getClass().getName() + "." + mockitoMethod.getName() + "(" +
                "[a, b], [])";
        Answer answer = Mockito.answer(new Object(){
            @Override
            public String toString() {
                return "answer";
            }
        });
        Mockito.when(mock.toString()).thenAnswer(answer);

        String result = invocation.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        Assert.assertTrue(Invocation.isToString(invocation));
        MockitoMethod toStringMethod = Mockito.method(String.class, "length");
        Invocation invocation2 = new Invocation(mock, toStringMethod, new Object[0], 2, Mockito.mock(RealMethod.class));

        Assert.assertFalse(Invocation.isToString(invocation2));
    }

    @Test(expected = AssertionError.class)
    public void testIsToStringNull() {
        Invocation invocation = null;
        Assert.assertFalse(Invocation.isToString(invocation));
    }

    @Test(expected = AssertionError.class)
    public void testIsToStringDifferentClass() {
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new Invocation(mock, Mockito.method(String.class, "toString"), new Object[0], 1, Mockito.mock(RealMethod.class));
        Object intMock = Mockito.mock(Integer.class);

        Assert.assertFalse(Invocation.isToString(invocation));
        Assert.assertFalse(Invocation.isToString(new Invocation(intMock, Mockito.method(Integer.class, "toString"), new Object[0], 2, Mockito.mock(RealMethod.class))));
    }

    @Test
    public void testIsValidException() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", Exception.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        Throwable throwable = new Exception("test");
        Assert.assertFalse(invocation.isValidException(throwable));

        Throwable throwable2 = new RuntimeException("test");
        Assert.assertFalse(invocation.isValidException(throwable2));

        Throwable throwable3 = Mockito. ArgumentMatchers.any(Exception.class);
        Mockito.when(mockitoMethod.invoke(mock, new Object[0])).thenThrow(throwable3);
        Assert.assertTrue(invocation.isValidException(throwable3));
    }

    @Test(expected = AssertionError.class)
    public void testIsValidExceptionNull() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", Exception.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        Assert.assertFalse(invocation.isValidException(null));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        Class intType = Integer.TYPE;
        Assert.assertTrue(invocation.isValidReturnType(intType));

        Class doubleType = Double.TYPE;
        Assert.assertTrue(invocation.isValidReturnType(doubleType));

        Class StringType = String.class;
        Assert.assertTrue(invocation.isValidReturnType(StringType));

        Class<List<String>> listType = List.class.cast(Collections.class.cast(Collections.singletonList("list")).getClass().getComponentType());
        Assert.assertTrue(invocation.isValidReturnType(listType));

        Class<Object[]> objectArrayType = Object[].class;
        Assert.assertTrue(invocation.isValidReturnType(objectArrayType));

        Class<String[]> stringArrayType = String[].class;
        Assert.assertTrue(invocation.isValidReturnType(stringArrayType));

        Class<Integer[]> intArrayType = Integer[].class;
        Assert.assertFalse(invocation.isValidReturnType(intArrayType));

        Class<List<Integer>> intListType = List.class.cast(Collections.class.cast(Collections.singletonList(1)).getClass());
        Assert.assertFalse(invocation.isValidReturnType(intListType));
    }

    @Test(expected = AssertionError.class)
    public void testIsValidReturnTypeNull() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        Assert.assertFalse(invocation.isValidReturnType(null));
    }

    @Test
    public void testIsVoid() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        Assert.assertFalse(invocation.isVoid());

        MockitoMethod mockitoMethodVoid = Mockito.method(Void.TYPE, "toString");
        Invocation invocationVoid = new Invocation(mock, mockitoMethodVoid, new Object[0], 2, Mockito.mock(RealMethod.class));

        Assert.assertTrue(invocationVoid.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        String expected = "int";
        Assert.assertEquals(expected, invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        String expected = "length";
        Assert.assertEquals(expected, invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        Assert.assertFalse(invocation.returnsPrimitive());

        MockitoMethod mockitoMethodPrimitive = Mockito.method(boolean.class, "booleanValue");
        Invocation invocationPrimitive = new Invocation(Mockito.mock(Boolean.class), mockitoMethodPrimitive, new Object[0], 2, Mockito.mock(RealMethod.class));

        Assert.assertTrue(invocationPrimitive.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        Location expected = new Location();
        Assert.assertEquals(expected, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", String[].class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{Arrays.asList("a", "b")}, 1, Mockito.mock(RealMethod.class));

        Assert.assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        Assert.assertNotNull(invocation.callRealMethod());
    }

    @Test(expected = AssertionError.class)
    public void testCallRealMethodNull() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));

        invocation.callRealMethod();
    }
}