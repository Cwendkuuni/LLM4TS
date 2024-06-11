import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<Object>();
    }

    /* Test handle() method when invocationContainerImpl hasAnswersForStubbing() is false */

    @Test
    public void testHandle_withoutAnswersForStubbing_invocationContainerImplHasAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        
        Object result = mockHandler.handle(invocation);

        assertEquals(null, result);
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(mockingProgress, times(1)).getArgumentMatcherStorage();
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(invocationContainerImpl, times(1)).setInvocationForPotentialStubbing(any());
        verify(invocationContainerImpl, never()).findAnswerFor(any());
    }

    @Test
    public void testHandle_withoutAnswersForStubbing_invocationContainerImplHasNoAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        Answer<Object> defaultAnswer = mock(Answer.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);

        Object result = mockHandler.handle(invocation);

        assertEquals(defaultAnswer.answer(invocation), result);
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(mockingProgress, times(1)).getArgumentMatcherStorage();
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(any());
        verify(invocationContainerImpl, times(1)).findAnswerFor(invocation);
        verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(any());
    }

    /* Test handle() method when invocationContainerImpl hasAnswersForStubbing() is true */

    @Test
    public void testHandle_withAnswersForStubbing_stubbedInvocationFound() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));

        Object result = mockHandler.handle(invocation);

        assertEquals(null, result);
        verify(mockingProgress, never()).pullVerificationMode();
        verify(mockingProgress, never()).getArgumentMatcherStorage();
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(invocationContainerImpl, never()).setInvocationForPotentialStubbing(any());
        verify(invocationContainerImpl, times(1)).findAnswerFor(invocation);
        verify(invocationContainerImpl, never()).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void testHandle_withAnswersForStubbing_stubbedInvocationNotFound() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        
        Object result = mockHandler.handle(invocation);

        assertEquals(null, result);
        verify(mockingProgress, never()).pullVerificationMode();
        verify(mockingProgress, never()).getArgumentMatcherStorage();
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(invocationContainerImpl, times(1)).setMethodForStubbing(invocationMatcher);
    }

    /* Test voidMethodStubbable() method */

    @Test
    public void testVoidMethodStubbable() {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        mockHandler.setAnswersForStubbing(answers);

        VoidMethodStubbable<Void> voidMethodStubbable = mockHandler.voidMethodStubbable(mock(Void.class));

        assertNotNull(voidMethodStubbable);
        assertEquals(mockHandler.getInvocationContainer(), voidMethodStubbable.getInvocationContainer());
    }

    /* Test getMockSettings() method */

    @Test
    public void testGetMockSettings() {
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    /* Test setAnswersForStubbing() method */

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        
        mockHandler.setAnswersForStubbing(answers);
        
        ArgumentCaptor<List<Answer>> captor = ArgumentCaptor.forClass(List.class);
        verify(invocationContainerImpl, times(1)).setAnswersForStubbing(captor.capture());
        
        assertEquals(answers, captor.getValue());
    }

    /* Test getInvocationContainer() method */

    @Test
    public void testGetInvocationContainer() {
        assertEquals(invocationContainerImpl, mockHandler.getInvocationContainer());
    }

}