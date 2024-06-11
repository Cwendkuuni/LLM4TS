import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertNotNull;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMockWithValidArguments() {
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String spiedInstance = "Hello World!";

        String mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);

        verify(mock).toString();
        LenientCopyTool copyTool = new LenientCopyTool();
        copyTool.copyToMock(spiedInstance, mock);
        verify(mock).equals(spiedInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithInvalidClass() {
        MockUtil mockUtil = new MockUtil();
        Class<Object> objectClass = Object.class;

        mockUtil.createMock(objectClass, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullSpiedInstance() {
        MockUtil mockUtil = new MockUtil();
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        mockUtil.createMock(stringClass, settings);
    }
}