import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        ClassToMock classToMock = mock(ClassToMock.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(classToMock, settings);

        verify(classToMock).initiateMockName(classToMock);
        verify(settings).initiateMockName(classToMock);
        verify(classToMock).getExtraInterfaces();
        verify(classToMock).getSpiedInstance();
        verify(classToMock).getAncillaryTypes();
        verify(classToMock).imposterize(classToMock, ancillaryTypes);
        verify(classToMock).copyToMock(spiedInstance, mock);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);

        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        verify(mock).setCallback(0, newFilter);
        verify(mock).getCallback(0);
    }

    @Test
    public void testGetMockHandler() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler(mock);

        verify(mock).getClass();
        verify(mock).isEnhanced();
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();

        mockUtil.isMock(mock);

        verify(mock).getClass();
        verify(mock).isEnhanced();
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockName(mock);

        verify(mock).getMockSettings();
    }
}