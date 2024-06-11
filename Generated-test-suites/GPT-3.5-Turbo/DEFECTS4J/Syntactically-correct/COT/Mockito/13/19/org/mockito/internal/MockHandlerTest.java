import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockHandlerTest {
    
    private MockHandler<Object> mockHandler;
    private MockHandlerInterface<Object> mockHandlerInterface;
    private MockSettingsImpl mockSettings;
    
    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
        mockHandlerInterface = mock(MockHandlerInterface.class);
    }
    
    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Object result = mockHandler.handle(invocation);
        assertNull(result);
        verify(mockSettings).getDefaultAnswer();
        verify(mockHandlerInterface).getMockSettings();
    }
    
    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
        assertTrue(stubbable instanceof VoidMethodStubbableImpl);
        verify(mockHandler.getInvocationContainer()).resetInvocationForPotentialStubbing(any());
    }
    
    @Test
    public void testGetMockSettings() {
        MockSettingsImpl settings = mockHandler.getMockSettings();
        assertSame(settings, mockSettings);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        mockHandler.setAnswersForStubbing(answers);
        verify(mockHandler.getInvocationContainer()).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        InvocationContainer container = mockHandler.getInvocationContainer();
        assertSame(container, mockHandler.invocationContainerImpl);
    }
}