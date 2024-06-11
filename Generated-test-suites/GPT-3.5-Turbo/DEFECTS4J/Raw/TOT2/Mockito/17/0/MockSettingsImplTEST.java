import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        
        Assert.assertEquals(java.io.Serializable.class, result.getExtraInterfaces()[0]);
    }

    @Test
    public void testExtraInterfaces_ValidInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] interfaces = {Runnable.class, Comparable.class};
        MockSettings result = mockSettings.extraInterfaces(interfaces);
        
        Assert.assertEquals(interfaces, result.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NullInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    // Similar tests for other methods...

}