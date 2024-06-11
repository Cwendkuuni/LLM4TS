import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MockHandlerTest {

    MockHandler mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler();
    }

    @Test
    public void testHandle() {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.handle(invocation)).thenReturn(null);
        
        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandler mockHandler = new MockHandler();
        List<Answer> answers = new ArrayList<>();
        mockHandler.setAnswersForStubbing(answers);
        
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mockHandler);
        
        assertNotNull(stubbable);
    }

    @Test
    public void testGetMockSettings() {
        MockHandler mockHandler = new MockHandler();
        MockSettingsImpl settings = mockHandler.getMockSettings();
        
        assertNotNull(settings);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        
        mockHandler.setAnswersForStubbing(answers);
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }
}