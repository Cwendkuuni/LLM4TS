import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    @Test
    public void testHandle_WhenInvocationContainerImplHasAnswersForStubbing_ShouldSetMethodForStubbingAndReturnNull() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        InvocationContainerImpl invocationContainer = mockHandler.getInvocationContainer();
        Answer<Object> answer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        List<Answer> answers = Arrays.asList(answer);
        invocationContainer.setAnswersForStubbing(answers);

        Invocation invocation = new Invocation();
        invocation.setMock(new Object());

        assertNull(mockHandler.handle(invocation));
        assertNotNull(invocationContainer.getMethodForStubbing());
    }

    @Test
    public void testHandle_WhenVerificationModeIsNull_ShouldCheckIfVerificationWasStartedOnCorrectMockAndReturnNull() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        InvocationContainerImpl invocationContainer = mockHandler.getInvocationContainer();
        Answer<Object> answer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        List<Answer> answers = Arrays.asList(answer);
        invocationContainer.setAnswersForStubbing(answers);

        Invocation invocation = new Invocation();
        invocation.setMock(new Object());

        VerificationMode verificationMode = new MockAwareVerificationMode(invocation.getMock());
        ThreadSafeMockingProgress mockingProgress = (ThreadSafeMockingProgress) mockHandler.getMockingProgress();
        mockingProgress.pushVerificationMode(verificationMode);

        assertNull(mockHandler.handle(invocation));
        assertNotNull(invocationContainer.getMethodForStubbing());
    }

    @Test
    public void testHandle_WhenVerificationModeIsNotNull_ShouldVerifyAndReturnNull() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        InvocationContainerImpl invocationContainer = mockHandler.getInvocationContainer();
        Answer<Object> answer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        List<Answer> answers = Arrays.asList(answer);
        invocationContainer.setAnswersForStubbing(answers);

        Invocation invocation = new Invocation();
        invocation.setMock(new Object());

        VerificationMode verificationMode = new MockAwareVerificationMode(invocation.getMock());
        ThreadSafeMockingProgress mockingProgress = (ThreadSafeMockingProgress) mockHandler.getMockingProgress();
        mockingProgress.pushVerificationMode(verificationMode);

        VerificationDataImpl verificationData = new VerificationDataImpl(invocationContainer, Matchers.any(InvocationMatcher.class));
        verificationMode.verify(verificationData);

        assertNull(mockHandler.handle(invocation));
        assertNotNull(invocationContainer.getMethodForStubbing());
    }

    @Test
    public void testHandle_WhenStubbedInvocationExists_ShouldAnswerAndReturnResult() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        InvocationContainerImpl invocationContainer = mockHandler.getInvocationContainer();
        Answer<Object> answer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        List<Answer> answers = Arrays.asList(answer);
        invocationContainer.setAnswersForStubbing(answers);

        InvocationMatcher invocationMatcher = new InvocationMatcher();
        invocationMatcher.setInvocation(new Invocation());
        invocationMatcher.setMatchersBinder(mockHandler.getMatchersBinder());

        StubbedInvocationMatcher stubbedInvocation = new StubbedInvocationMatcher(invocationMatcher, answer);
        invocationContainer.addAnswer(stubbedInvocation);

        Invocation invocation = new Invocation();

        assertNull(mockHandler.handle(invocation));
        assertNotNull(invocationContainer.getMethodForStubbing());
        assertNotNull(invocationContainer.getInvocationForPotentialStubbing());
    }

    @Test
    public void testHandle_WhenStubbedInvocationDoesNotExist_ShouldAnswerWithDefaultAnswerAndReturnResult() throws Throwable {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setDefaultAnswer(AdditionalAnswers.returnsFirstArg());
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        InvocationContainerImpl invocationContainer = mockHandler.getInvocationContainer();
        Answer<Object> answer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        List<Answer> answers = Arrays.asList(answer);
        invocationContainer.setAnswersForStubbing(answers);

        Invocation invocation = new Invocation();
        invocation.setMethod(TestMethod.class.getMethod("testMethod", String.class));

        assertNotNull(mockHandler.handle(invocation));
        assertNotNull(invocationContainer.getMethodForStubbing());
        assertNotNull(invocationContainer.getInvocationForPotentialStubbing());
    }

    @Test
    public void testVoidMethodStubbable_ShouldCreateVoidMethodStubbableImpl() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        Object mock = new Object();
        VoidMethodStubbable<T> stubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(stubbable);
        assertTrue(stubbable instanceof VoidMethodStubbableImpl);
        assertEquals(mock, ((VoidMethodStubbableImpl<?>) stubbable).getMock());
        assertEquals(mockHandler.getInvocationContainer(), ((VoidMethodStubbableImpl<?>) stubbable).getInvocationContainer());
    }

    @Test
    public void testGetMockSettings_ShouldReturnMockSettingsImpl() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing_ShouldSetAnswersForStubbingInInvocationContainer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);

        List<Answer> answers = Arrays.asList(mock(Answer.class));
        mockHandler.setAnswersForStubbing(answers);

        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer_ShouldReturnInvocationContainerImpl() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = new MockHandler<>(mockSettings);
        InvocationContainerImpl invocationContainer = mockHandler.getInvocationContainer();

        assertEquals(invocationContainer, mockHandler.getInvocationContainer());
    }

    private static class TestMethod {
        public void testMethod(String arg) {}
    }

}