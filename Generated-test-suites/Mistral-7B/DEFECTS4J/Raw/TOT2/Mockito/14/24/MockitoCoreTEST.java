import org.junit.Test;
import org.mockito.Mockito;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<String> stringClass = String.class;
        MockSettings mockSettings = new MockSettings();
        String mock = Mockito.mock(stringClass, mockSettings);
        assert(mock != null);
        // typical usage
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockNullClass() {
        Mockito.mock(null, new MockSettings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockNullSettings() {
        Mockito.mock(String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockEmptyClass() {
        Mockito.mock(new Class[0], new MockSettings());
    }

    @Test(expected = NotAMockException.class)
    public void testMockNonMock() {
        Object nonMock = new Object();
        Mockito.mock(nonMock, new MockSettings());
    }
}