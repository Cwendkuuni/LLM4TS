import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockSettingsImpl;
import org.mockito.MockitoAnnotations;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<String> stringClass = String.class;
        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(stringClass, settings);
        MockitoAnnotations.initMocks(this);
        Mockito.verify(mock).toString();
    }
}