import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.mockito.internal.MockCreationSettings;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.junit.Test;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<String> classToMock = String.class;

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertEquals(classToMock, mock.getClass());
        verify(mockUtil, times(1)).creationValidator.validateType(classToMock);
    }
}