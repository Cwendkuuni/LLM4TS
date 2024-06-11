import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockingProgress mockingProgress;
    private MatchersBinder matchersBinder;
    private InvocationContainerImpl invocationContainerImpl;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
        mockingProgress = new ThreadSafeMockingProgress();
        matchersBinder = new MatchersBinder();
        invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void handle_StubbingVoidWithStubVoid() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        mockingProgress.getArgumentMatcherStorage().reset();
        when(mockingProgress.getArgumentMatcherStorage().reportMatcher(any())).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                mockeryProgress.getArgumentMatcherStorage().addArgumentMatcher((ArgumentMatcher) args[0]);
                return null;
            }
        });
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(mockingProgress.getArgumentMatcherStorage(), times(1)).reportMatcher(any());
        verify(invocationContainerImpl, times(1)).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void handle_VerifyOnDifferentMock() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        ((MockAwareVerificationMode) verificationMode).setMock(invocation.getMock());
        mockingProgress.pullVerificationMode();
        
        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertNull(result);
        verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(any());
        verify(mockingProgress, times(1)).pullVerificationMode();
    }
    
    @Test
    public void handle_StubbingAnswerNotFound() throws Throwable {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        Object defaultAnswerResult = new Object();
        mockHandler.getMockSettings().setDefaultAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                return defaultAnswerResult;
            }
        });

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        assertEquals(defaultAnswerResult, result);
        verify(invocationContainerImpl, times(1)).findAnswerFor(invocation);
        verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(invocationMatcher);
    }
    
    @Test
    public void voidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        // Arrange
        Object mock = new Object();

        // Act
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        // Assert
        assertNotNull(stubbable);
        assertTrue(stubbable instanceof VoidMethodStubbableImpl);
    }

    @Test
    public void setAnswersForStubbing_CallsInvocationContainerImpl() {
        // Arrange
        List<Answer> answers = Arrays.asList(mock(Answer.class), mock(Answer.class));

        // Act
        mockHandler.setAnswersForStubbing(answers);

        // Assert
        verify(invocationContainerImpl, times(1)).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_ReturnsInvocationContainerImpl() {
        // Arrange

        // Act
        InvocationContainer container = mockHandler.getInvocationContainer();

        // Assert
        assertNotNull(container);
        assertTrue(container instanceof InvocationContainerImpl);
    }

}