import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockCreationSettings;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> stringClass = String.class;
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockUtil mockUtil = new MockUtil();

        String mock = mockUtil.createMock(stringClass, mockSettings);

        verify(mock).toString();
    }
}