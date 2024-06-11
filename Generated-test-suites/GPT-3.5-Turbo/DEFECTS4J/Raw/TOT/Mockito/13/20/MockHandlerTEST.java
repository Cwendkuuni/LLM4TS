
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.*;
import org.mockito.internal.progress.*;
import org.mockito.internal.stubbing.*;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MockHandlerTest {

    private MockHandler mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testHandle_StubbingVoidsWithStubVoidStyle_ShouldSetMethodForStubbing() throws Throwable {
        MockitoInvocationHandler.InvocationContainer container = mock(MockitoInvocationHandler.InvocationContainer.class);
        when(container.hasAnswersForStubbing()).thenReturn(true);

        Invocation invocation = new InvocationBuilder().getMock(mockHandler).toInvocation();

        mockHandler.handle(invocation);

        verify(container).setMethodForStubbing(ArgumentMatchers.any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_WithVerificationModeAndCorrectMock_ShouldVerify() throws Throwable {
        MockitoInvocationHandler.InvocationContainer container = mock(MockitoInvocationHandler.InvocationContainer.class);
        MockitoInvocationHandler.VerificationMode verificationMode = mock(MockitoInvocationHandler.VerificationMode.class);
        Invocation invocation = new InvocationBuilder().getMock(mockHandler).toInvocation();

        when(container.hasAnswersForStubbing()).thenReturn(false);
        when(container.findAnswerFor(ArgumentMatchers.any(Invocation.class))).thenReturn(null);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockitoInvocationHandler.MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());

        mockHandler.handle(invocation);

        verify(verificationMode).verify(ArgumentMatchers.any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_WithVerificationModeAndDifferentMock_ShouldNotVerify() throws Throwable {
        MockitoInvocationHandler.InvocationContainer container = mock(MockitoInvocationHandler.InvocationContainer.class);
        MockitoInvocationHandler.VerificationMode verificationMode = mock(MockitoInvocationHandler.VerificationMode.class);
        Invocation invocation = new InvocationBuilder().getMock(mockHandler).toInvocation();
        Invocation differentMockInvocation = new InvocationBuilder().getMock().toInvocation();

        when(container.hasAnswersForStubbing()).thenReturn(false);
        when(container.findAnswerFor(ArgumentMatchers.any(Invocation.class))).thenReturn(null);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockitoInvocationHandler.MockAwareVerificationMode) verificationMode).getMock()).thenReturn(differentMockInvocation.getMock());

        mockHandler.handle(invocation);

        verify(verificationMode, never()).verify(ArgumentMatchers.any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_WithStubbedInvocation_ShouldReturnStubbedAnswer() throws Throwable {
        MockitoInvocationHandler.InvocationContainer container = mock(MockitoInvocationHandler.InvocationContainer.class);
        Invocation invocation = new InvocationBuilder().getMock(mockHandler).toInvocation();
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Answer stubbedAnswer = mock(Answer.class);
        Object expected = new Object();

        when(container.hasAnswersForStubbing()).thenReturn(false);
        when(container.findAnswerFor(ArgumentMatchers.any(Invocation.class))).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(ArgumentMatchers.any(Invocation.class))).thenReturn(expected);

        Object actual = mockHandler.handle(invocation);

        assertEquals(expected, actual);
    }

    @Test
    public void testHandle_WithNoStubbedInvocation_ShouldReturnDefaultAnswer() throws Throwable {
        MockitoInvocationHandler.InvocationContainer container = mock(MockitoInvocationHandler.InvocationContainer.class);
        Invocation invocation = new InvocationBuilder().getMock(mockHandler).toInvocation();
        Answer defaultAnswer = mock(Answer.class);
        Object expected = new Object();

        when(container.hasAnswersForStubbing()).thenReturn(false);
        when(container.findAnswerFor(ArgumentMatchers.any(Invocation.class))).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(ArgumentMatchers.any(Invocation.class))).thenReturn(expected);

        Object actual = mockHandler.handle(invocation);

        assertEquals(expected, actual);
    }

    @Test
    public void testVoidMethodStubbable_ShouldReturnVoidMethodStubbable() throws Throwable {
        Object mock = new Object();
        VoidMethodStubbable voidMethodStubbable = mock(VoidMethodStubbable.class);

        when(mockHandler.voidMethodStubbable(mock)).thenReturn(voidMethodStubbable);

        VoidMethodStubbable actual = mockHandler.voidMethodStubbable(mock);

        assertEquals(voidMethodStubbable, actual);
    }

    @Test
    public void testGetMockSettings_ShouldReturnMockSettings() {
        MockSettingsImpl expected = mock(MockSettingsImpl.class);

        when(mockHandler.getMockSettings()).thenReturn(expected);

        MockSettingsImpl actual = mockHandler.getMockSettings();

        assertEquals(expected, actual);
    }

    @Test
    public void testSetAnswersForStubbing_ShouldSetAnswersForStubbing() {
        List<Answer> expected = new ArrayList<>();

        mockHandler.setAnswersForStubbing(expected);

        assertEquals(expected, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }

    @Test
    public void testGetInvocationContainer_ShouldReturnInvocationContainer() {
        InvocationContainer expected = mock(InvocationContainer.class);

        when(mockHandler.getInvocationContainer()).thenReturn(expected);

        InvocationContainer actual = mockHandler.getInvocationContainer();

        assertEquals(expected, actual);
    }
}

