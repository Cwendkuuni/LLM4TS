import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.invocation.MethodTest;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MethodTest().staticMethod(String.class, "methodWithExceptions", String.class, new Class[] {Exception1.class, Exception2.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedExceptionTypes = new Class[] {Exception1.class, Exception2.class};
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }
}