import org.junit.Assert;
import org.junit.Test;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testInitialState() {
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Assert.assertNotNull(propertyAndSetterInjection);
    }
}