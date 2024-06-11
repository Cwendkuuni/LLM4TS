import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockUtil;

public class MockUtilTest {

    MockUtil mockUtil = new MockUtil();

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        Object mock = ClassImposterizer.INSTANCE.imposterise(new Object(), Object.class, new Class<?>[]{});

        assertThrows(Exception.class, () -> mockUtil.resetMock(mock));
    }

    @Test
    public void testGetMockHandler() {
        Object mock = ClassImposterizer.INSTANCE.imposterise(new Object(), Object.class, new Class<?>[]{});

        assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(mock));
    }

    @Test
    public void testIsMock() {
        Object mock = ClassImposterizer.INSTANCE.imposterise(new Object(), Object.class, new Class<?>[]{});

        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        Object mock = ClassImposterizer.INSTANCE.imposterise(new Object(), Object.class, new Class<?>[]{});

        assertThrows(NotAMockException.class, () -> mockUtil.getMockName(mock));
    }
}