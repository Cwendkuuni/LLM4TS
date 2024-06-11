import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;

public class InvocationTest {

    @Test
    public void testInvocationWithNoArguments() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "toString");
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        Mockito.verify(mock).toString();
        Assert.assertEquals(invocation, Mockito.verify(mock).toString());
    }

    @Test
    public void testInvocationWithOneArgument() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length", String.class);
        String argument = "test";
        Invocation invocation = new Invocation(mock, method, new Object[]{argument}, 1, null);

        Mockito.verify(mock).length(ArgumentMatchers.eq(argument));
        Assert.assertEquals(invocation, Mockito.verify(mock).length(ArgumentMatchers.eq(argument)));
    }

    @Test
    public void testInvocationWithVarArgs() {
        Object mock = Mockito.mock(Collection.class);
        MockitoMethod method = Mockito.method(Collection.class, "addAll", Collection.class);
        Collection<String> collection = new ArrayList<>();
        collection.add("test1");
        collection.add("test2");
        Invocation invocation = new Invocation(mock, method, new Object[]{collection}, 1, null);

        Mockito.verify(mock).addAll(ArgumentMatchers.contains(collection.toArray()));
        Assert.assertEquals(invocation, Mockito.verify(mock).addAll(ArgumentMatchers.contains(collection.toArray())));
    }

    @Test(expected = RuntimeException.class)
    public void testInvocationWithThrowable() {
        Object mock = Mockito.mock(ExceptionHandler.class);
        MockitoMethod method = Mockito.method(ExceptionHandler.class, "handleException", Throwable.class);
        Throwable throwable = new RuntimeException("test exception");
        Invocation invocation = new Invocation(mock, method, new Object[]{throwable}, 1, null);

        Mockito.doThrow(throwable).when(mock).handleException(ArgumentMatchers.any(Throwable.class));
        Mockito.verify(mock).handleException(ArgumentMatchers.eq(throwable));
    }

    @Test
    public void testInvocationWithNull() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "concat", String.class);
        String argument = null;
        Invocation invocation = new Invocation(mock, method, new Object[]{argument}, 1, null);

        Mockito.verify(mock).concat((String) ArgumentMatchers.isNull());
        Assert.assertEquals(invocation, Mockito.verify(mock).concat((String) ArgumentMatchers.isNull()));
    }

    @Test
    public void testInvocationWithPrimitive() {
        Object mock = Mockito.mock(Math.class);
        MockitoMethod method = Mockito.method(Math.class, "add", int.class, int.class);
        int argument1 = 5;
        int argument2 = 10;
        Invocation invocation = new Invocation(mock, method, new Object[]{argument1, argument2}, 1, null);

        Mockito.verify(mock).add(argument1, argument2);
        Assert.assertEquals(invocation, Mockito.verify(mock).add(argument1, argument2));
    }

    @Test
    public void testInvocationWithVoidMethod() {
        Object mock = Mockito.mock(Logger.class);
        MockitoMethod method = Mockito.method(Logger.class, "log", String.class, org.apache.log4j.Level.class);
        String message = "test message";
        org.apache.log4j.Level level = org.apache.log4j.Level.INFO;
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        Mockito.verify(mock).log(level, message);
        Assert.assertEquals(invocation, Mockito.verify(mock).log(level, message));
    }

    @Test
    public void testInvocationWithStaticMethod() {
        Object mock = Mockito.mock(Math.class);
        MockitoMethod method = Mockito.staticMethod(Math.class, "abs", int.class);
        int argument = 5;
        Invocation invocation = new Invocation(mock, method, new Object[]{argument}, 1, null);

        Mockito.when(mock.getClass()).thenReturn(Math.class);
        Mockito.verify(mock).abs(argument);
        Assert.assertEquals(invocation, Mockito.verify(mock).abs(argument));
    }

    @Test
    public void testInvocationWithMethodWithAnnotations() {
        Object mock = Mockito.mock(Logger.class);
        MockitoMethod method = Mockito.method(Logger.class, "log", String.class, org.apache.log4j.Level.class);
        method.addAnnotations(new Annotation[]{neworg.mockito.internal.util.annotations.AnnotatedElement(new org.mockito.internal.util.annotations.AnnotationInfo("@MyAnnotation", new Class[]{Logger.class, String.class, org.apache.log4j.Level.class}, new Object[]{mock, "test message", org.apache.log4j.Level.INFO}))});
        String message = "test message";
        org.apache.log4j.Level level = org.apache.log4j.Level.INFO;
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);

        Mockito.verify(mock).log(level, message);
        Assert.assertEquals(invocation, Mockito.verify(mock).log(level, message));
    }
}