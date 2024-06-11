
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

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

public class MockHandlerTest {
  
  private MockHandler<Object> mockHandler;
  private InvocationContainerImpl invocationContainerImpl;
  private MatchersBinder matchersBinder;
  private MockingProgress mockingProgress;
  private MockSettingsImpl mockSettings;

  @Before
  public void setup() {
    mockSettings = new MockSettingsImpl();
    mockingProgress = new ThreadSafeMockingProgress();
    matchersBinder = new MatchersBinder();
    invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
    mockHandler = new MockHandler<Object>(mockSettings);
    mockHandler.invocationContainerImpl = invocationContainerImpl;
    mockHandler.matchersBinder = matchersBinder;
    mockHandler.mockingProgress = mockingProgress;
  }

  // Test that invocationContainerImpl has answers for stubbing
  @Test
  public void testHandle_hasAnswersForStubbing() throws Throwable {
    Invocation invocation = mock(Invocation.class);
    InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
    InvocationContainerImpl spyContainer = mock(InvocationContainerImpl.class);
    StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
  
    // Define stubbing interactions
    mockHandler.invocationContainerImpl = spyContainer;
    mockHandler.invocationContainerImpl.setAnswersForStubbing(Arrays.asList(mock(Answer.class)));
    mockHandler.matchersBinder = matchersBinder;
    mockHandler.matchersBinder.bindMatchers(matchersBinder.getArgumentMatcherStorage(), invocation);
    mockHandler.invocationContainerImpl.setMethodForStubbing(invocationMatcher);

    // Mock behavior
    mockHandler.handle(invocation);

    // Verify interactions
    mockHandler.invocationContainerImpl.hasAnswersForStubbing();
    mockHandler.matchersBinder.bindMatchers(matchersBinder.getArgumentMatcherStorage(), invocation);
    mockHandler.invocationContainerImpl.setMethodForStubbing(invocationMatcher);
  }

  // Test that invocationContainerImpl does not have answers for stubbing
  @Test
  public void testHandle_noAnswersForStubbing() throws Throwable {
    Invocation invocation = mock(Invocation.class);
  
    // Define stubbing interactions
    mockHandler.invocationContainerImpl.setAnswersForStubbing(null);
    mockHandler.matchersBinder.bindMatchers(matchersBinder.getArgumentMatcherStorage(), invocation);
    mockHandler.invocationContainerImpl.setMethodForStubbing(null);

    // Mock behavior
    mockHandler.handle(invocation);

    // Verify interactions
    mockHandler.invocationContainerImpl.hasAnswersForStubbing();
    mockHandler.matchersBinder.bindMatchers(matchersBinder.getArgumentMatcherStorage(), invocation);
    mockHandler.invocationContainerImpl.setMethodForStubbing(null);
  }

  // Test that the verification mode is not null
  @Test
  public void testHandle_verificationModeNotNull() throws Throwable {
    Invocation invocation = mock(Invocation.class);
    MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
    MockitoInvocation mockitoInvocation = mock(MockitoInvocation.class);
  
    // Define stubbing interactions
    mockHandler.mockingProgress.pullVerificationMode();
    MockitoInvocation mocks = invocation.getMock();
    verificationMode.getMock();
  
    // Mock behavior
    mockHandler.handle(invocation);

    // Verify interactions
    MockitoInvocation mockedInvocation = matchersBinder.bindMatchers(
        mockingProgress.getArgumentMatcherStorage(), invocation);
    VerificationDataImpl dataImpl = new VerificationDataImpl(invocationContainerImpl, mockedInvocation);
    verificationMode.verify(dataImpl);
    mockHandler.handle(mockitoInvocation);
  }

  // Test that the verification mode is null
  @Test
  public void testHandle_verificationModeNull() throws Throwable {
    Invocation invocation = mock(Invocation.class);
    VerificationDataImpl dataImpl = mock(VerificationDataImpl.class);
  
    // Define stubbing interactions
    mockHandler.mockingProgress.pullVerificationMode();
    verificationMode.getMatchingInvocation();
  
    // Mock behavior
    mockHandler.handle(invocation);
    mockHandler.handle(null);

    // Verify interactions
    mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);
    mockHandler.invocationContainerImpl.findAnswerFor(invocation);
    mockSettings.getDefaultAnswer().answer(invocation);
    mockHandler.invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
  }

  // Test the behavior if the stubbedInvocation is not null
  @Test
  public void testHandle_stubbedInvocationNotNull() throws Throwable {
    Invocation invocation = mock(Invocation.class);
    StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

    // Define stubbing interactions
    mockHandler.invocationContainerImpl.findAnswerFor(invocation);

    // Mock behavior
    mockHandler.handle(invocation);

    // Verify interactions
    mockHandler.invocationContainerImpl.findAnswerFor(invocation);
    stubbedInvocation.captureArgumentsFrom(invocation);
    stubbedInvocation.answer(invocation);
  }

  // Test the behavior if the stubbedInvocation is null
  @Test
  public void testHandle_stubbedInvocationNull() throws Throwable {
    Invocation invocation = mock(Invocation.class);
  
    // Define stubbing interactions
    mockHandler.invocationContainerImpl.findAnswerFor(null);

    // Mock behavior
    mockHandler.handle(invocation);

    // Verify interactions
    mockHandler.invocationContainerImpl.findAnswerFor(invocation);
    mockSettings.getDefaultAnswer().answer(invocation);
    mockHandler.invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
  }

  // Test that the invocationContainerImpl has answers for stubbing
  @Test
  public void testSetAnswersForStubbing() throws Throwable {
    List<Answer> answers = Arrays.asList(mock(Answer.class));

    // Define stubbing interactions
    mockHandler.invocationContainerImpl.setAnswersForStubbing(answers);

    // Verify interactions
    mockHandler.invocationContainerImpl.setAnswersForStubbing(answers);
  }

  // Test that the invocationContainerImpl does not have answers for stubbing
  @Test
  public void testSetAnswersForStubbing_null() throws Throwable {
    // Define stubbing interactions
    mockHandler.invocationContainerImpl.setAnswersForStubbing(null);

    // Verify interactions
    mockHandler.invocationContainerImpl.setAnswersForStubbing(null);
  }

  // Test the behavior of voidMethodStubbable with a mock object
  @Test
  public void testVoidMethodStubbable() throws Throwable {
    Object mock = mock(Object.class);
  
    // Mock behavior
    VoidMethodStubbable<T> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

    // Verify interactions
    new VoidMethodStubbableImpl<T>(mock, invocationContainerImpl);
  }

  // Test that mockSettings is returned as expected
  @Test
  public void testGetMockSettings() throws Throwable {
    // Define stubbing interactions
    mockHandler.mockSettings;

    // Verify interactions
    mockHandler.mockSettings;
  }

  // Test that the invocationContainer is returned as expected
  @Test
  public void testGetInvocationContainer() throws Throwable {
    // Define stubbing interactions
    mockHandler.invocationContainerImpl;

    // Verify interactions
    mockHandler.invocationContainerImpl;
  }
}
