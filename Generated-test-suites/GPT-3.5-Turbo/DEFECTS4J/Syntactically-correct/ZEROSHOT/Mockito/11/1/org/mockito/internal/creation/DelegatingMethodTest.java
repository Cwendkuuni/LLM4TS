
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private Method testMethod;

    @Before
    public void setUp() throws Exception {
        testMethod = DelegatingMethodTest.class.getDeclaredMethod("sampleTestMethod");
    }

    @Test
    public void testConstructor() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(testMethod, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertArrayEquals(testMethod.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(testMethod.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertArrayEquals(testMethod.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(testMethod.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(testMethod.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method abstractMethod = DelegatingMethodTest.class.getDeclaredMethod("sampleAbstractMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(abstractMethod);
        assertTrue(delegatingMethod.isAbstract());

        DelegatingMethod nonAbstractMethod = new DelegatingMethod(testMethod);
        assertFalse(nonAbstractMethod.isAbstract());
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        Method equalMethod = DelegatingMethodTest.class.getDeclaredMethod("sampleTestMethod");
        
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        DelegatingMethod equalDelegatingMethod = new DelegatingMethod(equalMethod);
        
        assertTrue(delegatingMethod.equals(equalDelegatingMethod));
        assertTrue(delegatingMethod.equals(testMethod));
    }

    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(1, delegatingMethod.hashCode());
    }

    // Sample methods for testing

    public void sampleTestMethod() {}

    public abstract void sampleAbstractMethod();
}