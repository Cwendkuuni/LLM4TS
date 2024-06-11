import org.junit.Test;
import org.mockito.MockitoCore;
import static org.junit.Assert.*;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<String> stringClass = String.class;
        MockSettings mockSettings = new MockSettings();

        String mock = mockitoCore.mock(stringClass, mockSettings);

        assertNotNull(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testMockWithNullClass() {
        MockitoCore mockitoCore = new MockitoCore();
        MockSettings mockSettings = new MockSettings();

        mockitoCore.mock(null, mockSettings);
    }

    @Test(expected = NullPointerException.class)
    public void testMockWithNullSettings() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<String> stringClass = String.class;

        mockitoCore.mock(stringClass, null);
    }

    @Test
    public void testMockWithAlreadyMockedObject() {
        MockitoCore mockitoCore = new MockitoCore();
        String mock = mockitoCore.mock(String.class, new MockSettings());
        String sameMock = mockitoCore.mock(String.class, new MockSettings());

        assertEquals(mock, sameMock);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMockWithUnsupportedClass() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<?> unsupportedClass = int[].class;

        mockitoCore.mock(unsupportedClass, new MockSettings());
    }
}