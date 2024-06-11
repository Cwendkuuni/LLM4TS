import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        Method mockMethod = mock(Method.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        String expectedName = "testMethod";
        when(mockMethod.getName()).thenReturn(expectedName);

        String actualName = delegatingMethod.getName();

        verify(mockMethod).getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testIsAbstract() {
        Method mockMethod = mock(Method.class);
        when(mockMethod.isAbstract()).thenReturn(true);

        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        assertTrue(delegatingMethod.isAbstract());

        when(mockMethod.isAbstract()).thenReturn(false);

        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = Mockito.spy(new Method("testMethod", new Class[0]) {});
        Method method2 = new Method("testMethod", new Class[0]) {};
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = method1;
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertTrue(delegatingMethod1.equals(delegatingMethod3));

        assertTrue(method1.equals(method3));
        assertTrue(delegatingMethod1.equals(new DelegatingMethod(method1)));
    }
}