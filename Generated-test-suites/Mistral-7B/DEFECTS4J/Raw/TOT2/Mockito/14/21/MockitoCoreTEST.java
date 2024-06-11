import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<String> stringClass = String.class;
        MockSettings mockSettings = new MockSettings();
        String mock = Mockito.mock(stringClass, mockSettings);
        verify(mock).equals("someString");
    }
}