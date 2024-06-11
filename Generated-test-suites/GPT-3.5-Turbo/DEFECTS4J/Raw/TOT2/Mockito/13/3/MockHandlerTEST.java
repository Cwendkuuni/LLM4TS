import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.stubbing.Answer;

public class MockHandlerTest {

    @Test
    public void testConstructor() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler mockHandler = new MockHandler(mockSettings);

        assertNotNull(mockHandler);
        assertNotNull(mockHandler.getInvocationContainer());
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testHandle_WithAnswersForStubbing() throws Throwable {
        MockHandler mockHandler = new MockHandler();
        Invocation invocation = mock(Invocation.class);

        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(true);

        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testHandle_WithoutAnswersForStubbing() throws Throwable {
        MockHandler mockHandler = new MockHandler();
        Invocation invocation = mock(Invocation.class);

        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(returnsFirstArg());

        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandler mockHandler = new MockHandler();
        Object mock = new Object();

        assertNotNull(mockHandler.voidMethodStubbable(mock));
    }

    @Test
    public void testSetAnswersForStubbing() {
        MockHandler mockHandler = new MockHandler();
        List<Answer> answers = Arrays.asList((Answer) returnsFirstArg());

        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testHandle_VerificationMode() throws Throwable {
        MockHandler mockHandler = new MockHandler();
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        when(mockHandler.getMockingProgress().pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode.verify(any())).thenReturn(null);

        assertNull(mockHandler.handle(invocation));
    }
}