import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.defaultanswers.ClassImposterizer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

public class ReturnsSmartNullsTest {

    @Mock
    InvocationOnMock invocation;

    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        Object defaultReturnValue = delegate.answer(invocation);
        if (defaultReturnValue != null) {
            assertEquals(defaultReturnValue, returnsSmartNulls.answer(invocation));
        }

        Class<?> type = invocation.getMethod().getReturnType();
        if (ClassImposterizer.INSTANCE.canImposterise(type)) {
            MethodInterceptor interceptor = new ThrowingInterceptor(invocation);
            Object imposterized = ClassImposterizer.INSTANCE.imposterise(interceptor, type);
            assertEquals(imposterized, returnsSmartNulls.answer(invocation));
        }
    }

    private final class ThrowingInterceptor implements MethodInterceptor {
        private final InvocationOnMock invocation;
        private final Location location = new Location();

        private ThrowingInterceptor(InvocationOnMock invocation) {
            this.invocation = invocation;
        }

        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            if (new ObjectMethodsGuru().isToString(method)) {
                return "SmartNull returned by unstubbed " + formatMethodCall()  + " method on mock";
            }

            new Reporter().smartNullPointerException(location);
            return null;
        }

        private String formatMethodCall() {
            String args = Arrays.toString(invocation.getArguments());
            return invocation.getMethod().getName() + "(" + args.substring(1, args.length() - 1) + ")";
        }
    }
}