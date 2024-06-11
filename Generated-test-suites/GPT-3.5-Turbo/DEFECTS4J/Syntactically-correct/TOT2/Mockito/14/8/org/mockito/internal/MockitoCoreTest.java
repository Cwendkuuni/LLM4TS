import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        // Test basic functionality of mock method
        MockitoCore core = new MockitoCore();
        Object mockObject = core.mock(Object.class, null);
        assertNotNull(mockObject);
    }

    @Test
    public void testStub() {
        // Test basic functionality of stub method
        MockitoCore core = new MockitoCore();
        IOngoingStubbing stubbing = core.stub();
        assertNull(stubbing);
    }

    @Test
    public void testWhen() {
        // Test basic functionality of when method
        MockitoCore core = new MockitoCore();
        OngoingStubbing stubbing = core.when(mock(Object.class));
        assertNull(stubbing);
    }
    
    // Add more test cases for the remaining methods in MockitoCore class

}