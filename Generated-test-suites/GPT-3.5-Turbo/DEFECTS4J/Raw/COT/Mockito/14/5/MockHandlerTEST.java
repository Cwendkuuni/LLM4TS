import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationContainer;
import org.mockito.internal.invocation.InvocationContainerImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_withAnswer_returnsAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Answer<Object> answer = mock(Answer.class);

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(answer);

        Object result = mockHandler.handle(invocation);

        assertEquals(answer, result);
    }

    @Test
    public void testHandle_withoutAnswer_returnsDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Answer<Object> defaultAnswer = mock(Answer.class);

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);

        Object result = mockHandler.handle(invocation);

        assertEquals(defaultAnswer.answer(invocation), result);
    }

    @Test
    public void testVoidMethodStubbable_returnsVoidMethodStubbableImpl() {
        Object mock = new Object();
        VoidMethodStubbable<Void> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertEquals(VoidMethodStubbableImpl.class, voidMethodStubbable.getClass());
    }

    @Test
    public void testSetAnswersForStubbing_setsAnswers() {
        List<Answer> answers = new ArrayList<>();
        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }
}