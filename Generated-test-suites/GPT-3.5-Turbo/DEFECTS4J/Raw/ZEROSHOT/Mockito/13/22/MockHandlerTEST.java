import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler mockHandler;
    @Mock
    private MockSettingsImpl mockSettings;
    @Mock
    private InvocationContainerImpl invocationContainerImpl;
    @Mock
    private MatchersBinder matchersBinder;
    @Mock
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockHandler = new MockHandler(mockSettings);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle_stubbingVoidWithMockitoStyle() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);

        assertNull(mockHandler.handle(invocation));

        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_verifyWithCorrectMock() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());

        assertNull(mockHandler.handle(invocation));

        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_verifyWithDifferentMock() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(mock(MockitoInvocationHandler.class));

        assertNull(mockHandler.handle(invocation));

        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockAwareVerificationMode, never()).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_handleStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("mockedReturnValue");
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn("defaultValue");

        assertEquals("mockedReturnValue", mockHandler.handle(invocation));

        verify(stubbedInvocation).captureArgumentsFrom(invocation);
    }

    @Test
    public void testHandle_handleUnstubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = null;

        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn("defaultValue");

        assertEquals("defaultValue", mockHandler.handle(invocation));

        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        VoidMethodStubbable expected = mock(VoidMethodStubbableImpl.class);

        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(invocationContainerImpl.stub(mock)).thenReturn(expected);

        VoidMethodStubbable actual = mockHandler.voidMethodStubbable(mock);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl expected = mock(MockSettingsImpl.class);

        assertEquals(expected, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainer expected = mock(InvocationContainerImpl.class);

        assertEquals(expected, mockHandler.getInvocationContainer());
    }

}