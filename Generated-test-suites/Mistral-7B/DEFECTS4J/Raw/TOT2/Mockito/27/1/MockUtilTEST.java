import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.InvocationOnMock;
import org.mockito.exceptions.misusing.NotAMockException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertNotNull;
import static org.mockito.Mockito.assertNotSame;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockSettings settings = new MockSettings();
        Class<?> mockClass = Mockito.class;
        Object spiedInstance = new Object();

        MockUtil mockUtil = new MockUtil();
        T mock = mockUtil.createMock(mockClass, settings);

        verify(mock).answer(any(InvocationOnMock.class)); // Basic functionality check

        assertNotNull(mock); // Null check
        assertNotSame(spiedInstance, mock); // Check if a new mock is created
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null, new MockSettings());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(new Object() {}, new MockSettings());
    }
}