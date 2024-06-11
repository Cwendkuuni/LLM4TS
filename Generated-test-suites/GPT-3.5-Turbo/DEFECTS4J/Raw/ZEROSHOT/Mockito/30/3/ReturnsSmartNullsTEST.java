import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = new InvocationOnMock();
        rs.answer(invocation);
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = new InvocationOnMock();
        ReturnsSmartNulls.ThrowingInterceptor ti = rs.new ThrowingInterceptor(invocation);
        Object obj = new Object();
        Method method = obj.getClass().getMethod("toString");
        Object[] args = {};
        MethodProxy proxy = new MethodProxy();

        try {
            ti.intercept(obj, method, args, proxy);
        } catch (Throwable e) {
            assertEquals(e.getClass(), Reporter.class);
        }
    }

    @Test
    public void testFormatMethodCall() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = new InvocationOnMock();
        invocation.setArguments(new Object[]{"arg1", "arg2", "arg3"});
        invocation.setMethod(new Object().getClass().getMethod("testMethod", String.class, String.class, String.class));
        ReturnsSmartNulls.ThrowingInterceptor ti = rs.new ThrowingInterceptor(invocation);

        assertEquals(ti.formatMethodCall(), "testMethod(arg1, arg2, arg3)");
    }

    @Test
    public void testReturnsSmartNulls() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = new InvocationOnMock();
        Class<?> type = new Object().getClass();

        rs.answer(invocation);
        rs.answer(invocation);
        ClassImposterizer ci = new ClassImposterizer();
        ci.canImposterise(type);
        ci.imposterise(rs.new ThrowingInterceptor(invocation), type);
    }

}