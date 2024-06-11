import static org.junit.Assert.*;
import org.junit.Test;

public class MockHandlerTest {

    @Test
    public void testConstructor() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> handler = new MockHandler<>(mockSettings);
        assertNotNull(handler);
    }

    @Test
    public void testHandle() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> handler = new MockHandler<>(mockSettings);
        assertNotNull(handler.handle(null));
    }

    @Test
    public void testVoidMethodStubbable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> handler = new MockHandler<>(mockSettings);
        assertNotNull(handler.voidMethodStubbable(null));
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> handler = new MockHandler<>(mockSettings);
        assertEquals(mockSettings, handler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> handler = new MockHandler<>(mockSettings);
        handler.setAnswersForStubbing(null);
    }

    @Test
    public void testGetInvocationContainer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> handler = new MockHandler<>(mockSettings);
        assertNotNull(handler.getInvocationContainer());
    }

}