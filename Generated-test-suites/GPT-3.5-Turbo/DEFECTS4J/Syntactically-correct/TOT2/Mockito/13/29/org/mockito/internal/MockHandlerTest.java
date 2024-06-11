import static org.junit.Assert.*;
import org.junit.Test;

public class MockHandlerTest {
    MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());

    @Test
    public void testHandle_InvokedMethodWithAnswersForStubbing_ReturnsNull() throws Throwable {
        Invocation invocation = new Invocation();
        mockHandler.setAnswersForStubbing(Arrays.asList(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "mock";
            }
        }));
        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testHandle_InvokedMethodWithoutAnswersForStubbing_ReturnsAnswer() throws Throwable {
        Invocation invocation = new Invocation();
        assertEquals("mock", mockHandler.handle(invocation));
    }

    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbableImplInstance() {
        VoidMethodStubbable<String> voidMethodStubbable = mockHandler.voidMethodStubbable("mock");
        assertNotNull(voidMethodStubbable);
        assertTrue(voidMethodStubbable instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbing() {
        List<Answer> answers = Arrays.asList(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "mock";
            }
        });
        mockHandler.setAnswersForStubbing(answers);
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }
}