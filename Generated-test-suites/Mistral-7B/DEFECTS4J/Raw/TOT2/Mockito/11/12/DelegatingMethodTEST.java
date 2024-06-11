import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        Method method = new Method(DelegatingMethod.class, "getName", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expected = "getName";
        String actual = delegatingMethod.getName();
        assertEquals(expected, actual);
    }

    // Edge case testing for getName() method
    @Test(expected = AssertionError.class)
    public void testGetNameWithNullMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
    }
}