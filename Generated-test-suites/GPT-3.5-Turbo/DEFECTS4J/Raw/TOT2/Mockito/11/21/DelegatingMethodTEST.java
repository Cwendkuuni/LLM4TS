import org.junit.*;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private Method method;

    @Before
    public void setUp() {
        method = DelegatingMethod.class.getDeclaredMethod("getExceptionTypes");
    }

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals("getExceptionTypes", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(Modifier.isAbstract(method.getModifiers()), delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(1, delegatingMethod.hashCode());
    }
}