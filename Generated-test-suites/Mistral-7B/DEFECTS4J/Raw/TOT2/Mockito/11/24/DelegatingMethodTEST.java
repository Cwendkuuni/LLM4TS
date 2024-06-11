import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        Method testMethod = new Method("testMethod", new Class[]{}, new Class[]{});
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        String name = delegatingMethod.getName();
        Assert.assertEquals("testMethod", name);
    }
}