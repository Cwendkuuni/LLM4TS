import org.junit.Assert.*;
import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod", new Class[]{}, int.class));
        String name = delegatingMethod.getName();
        assertEquals("testMethod", name);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNameWithNullMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
    }

    @Test
    public void testGetNameWithEmptyMethodName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("", new Class[]{}, int.class));
        String name = delegatingMethod.getName();
        assertEquals("", name);
    }

    @Test
    public void testGetNameWithSpecialCharacters() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method("testMethod#withSpecialCharacters", new Class[]{}, int.class));
        String name = delegatingMethod.getName();
        assertEquals("testMethod#withSpecialCharacters", name);
    }

    @Test
    public void testGetNameWithNullMethodName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(null, new Class[]{}, int.class));
        String name = delegatingMethod.getName();
        assertNull(name);
    }
}