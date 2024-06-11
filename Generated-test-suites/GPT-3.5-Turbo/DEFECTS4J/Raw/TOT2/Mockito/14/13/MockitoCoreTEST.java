import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.mock(Object.class, MockSettings.withSettings()));
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNull(mockitoCore.stub());
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNull(mockitoCore.when("test"));
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.verify(mock(Object.class), times(1)));
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.reset(new Object());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractions(new Object());
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(new Object()), InOrderContext.class);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.inOrder(new Object()));
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.doAnswer(answer));
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.stubVoid(new Object()));
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNotNull(mockitoCore.getLastInvocation());
    }

}