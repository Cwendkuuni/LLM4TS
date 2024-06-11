
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MockHandlerTest {

    @Test
    public void testHandle() throws Throwable {
        MockHandler<String> mockHandler = new MockHandler<>();

        Invocation invocation = new Invocation(mockHandler, null, "handle", Invocation.MethodType.ONE_ARG, "arg1");

        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandler<String> mockHandler = new MockHandler<>();

        VoidMethodStubbable<String> stubbable = mockHandler.voidMethodStubbable("mock");

        assertNotNull(stubbable);
    }

    @Test
    public void testGetMockSettings() {
        MockHandler<String> mockHandler = new MockHandler<>();

        MockSettingsImpl mockSettings = mockHandler.getMockSettings();

        assertNotNull(mockSettings);
    }

    @Test
    public void testSetAnswersForStubbing() {
        MockHandler<String> mockHandler = new MockHandler<>();

        List<Answer> answers = new ArrayList<>();
        answers.add(null);

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }
}
