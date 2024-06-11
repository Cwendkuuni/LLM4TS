package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandlerInvocation;
import org.mockito.invocation.MockHandlerInvocationWithMethod;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockHandler;
import org.mockito.mock.MockName;
import org.mockito.mock.handler.MockHandlerFactory;
import org.mockito.mock.handler.MockHandlerImpl;
import org.mockito.mock.handler.MockHandlerProxy;
import org.mockito.mock.handler.MockHandlerWrapper;
import org.mockito.plugin.MockMaker;
import org.mockito.stubbing.AlwaysReturn;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationAfterDelay;
import org.mockito.verification.VerificationContinuousMode;
import org.mockito.verification.VerificationEvent;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("unchecked")
public class MockHandlerTest {

    @Test
    public void mock_handler_should_delegate_to_mock_stub_strategy() throws Throwable {
        // given
        MockHandlerImpl mockHandler = createHandler();
        Invocation invocation = createInvocation();
        MockCreationSettings settings = createSettings();
        Stubber<?> stubber = createStubber();
        stubber.when(invocation.callRealMethod()).then(new AlwaysReturn(""));
        MockHandlerFactory strategy = createFactory();
        MockHandlerImpl expected = createHandler();
        Stubber returnStubber = createStubber();
        returnStubber.when(new MockHandlerInvocationWithMethod(invocation)).then(expected);
        strategy.when(mockHandler.invoke(invocation)).thenReturn(returnStubber);
        MockHandlerInvocationWithMethod result = new MockHandlerInvocationWithMethod(invocation, mockHandler);
        // when
        MockHandlerImpl actual = MockHandlerImpl.create(settings).getMockHandler();
        // then
        assertEquals(expected, actual);
        assertEquals(new MockHandlerProxy(result), MockHandlerProxy.create(result));
        assertEquals(new MockHandlerWrapper(mockHandler), MockHandlerWrapper.create(mockHandler));
        assertEquals(mockHandler, mockHandler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mock_handler_should_not_support_verify_no_more_interactions() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        VerificationMode mode = createVerificationMode();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        stubber.when(new VerificationEvent(invocation)).then(null);
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        handler.invoke(invocation);
        // when
        MockHandlerImpl actualHandler = MockHandlerImpl.create(null).getMockHandler();
        // then
    }

    @Test
    public void mock_handler_should_delegate_to_mockito_real_method_invocato() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        stubber.when(invocation.callRealMethod()).then(new AlwaysReturn(""));
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        Object[] args = new Object[0];
        // when
        handler.realMethodInvocation(invocation, args, MockitoRealMethodInvoker.INSTANCE);
        // then
        assertEquals(new MockHandlerProxy(MockHandlerInvocation.create(invocation, args)), MockHandlerProxy.create(MockHandlerInvocation.create(invocation, args)));
    }

    @Test
    public void mock_handler_should_delegate_to_mockito_stubbed_method_invoker() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        when(invocation.getMethod().getReturnType()).thenReturn("");
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        String[] args = new String[0];
        // when
        handler.stubbedMethodInvocation(invocation, args, MockitoStubbedMethodInvoker.INSTANCE);
        // then
        assertEquals(new MockHandlerProxy(MockHandlerInvocationWithMethod.create(invocation, args)), MockHandlerProxy.create(MockHandlerInvocationWithMethod.create(invocation, args)));
    }

    @Test
    public void mock_handler_should_delegate_to_mockito_stubbed_when_then_handler() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        stubber.when(invocation.callRealMethod()).then(new AlwaysReturn(""));
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        // when
        handler.stubWhenThenInvocation(invocation);
        // then
        assertEquals(new MockHandlerProxy(MockHandlerInvocation.create(invocation)), MockHandlerProxy.create(MockHandlerInvocation.create(invocation)));
    }

    @Test
    public void mock_handler_should_delegate_to_mockito_stubbed_method_invokator_on_verify() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        stubber.when(invocation.callRealMethod()).then(new AlwaysReturn(""));
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        // when
        handler.stubbedWhenThenInvocation(invocation);
        // then
        assertEquals(new MockHandlerProxy(MockHandlerInvocation.create(invocation)), MockHandlerProxy.create(MockHandlerInvocation.create(invocation)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mock_handler_should_not_support_to_string() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        VerificationMode mode = createVerificationMode();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        stubber.when(invocation.callRealMethod()).then(new AlwaysReturn(""));
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        String actualResult = "";
        // when
        String expectedResult = handler.realMethodInvocation(null, null, null);
        // then
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mock_handler_should_not_support_equals() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        VerificationMode mode = createVerificationMode();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        stubber.when(invocation.callRealMethod()).then(new AlwaysReturn(""));
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        boolean result = true;
        // when
        boolean expectedResult = handler.realMethodInvocation(null, null, null);
        // then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void mock_handler_should_delegate_to_on_runner_event_handlers() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        stubber.when(invocation.callRealMethod()).then(new AlwaysReturn(""));
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        // when
        handler.handleMethodsToStub(invocation);
        // then
        assertEquals(new MockHandlerProxy(MockHandlerInvocation.create(invocation)), MockHandlerProxy.create(MockHandlerInvocation.create(invocation)));
    }

    @Test
    public void mock_handler_should_delegate_to_verification_after_delay_handlers() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        stubber.when(invocation.callRealMethod()).then(new AlwaysReturn(""));
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        VerificationAfterDelay mode = createVerificationAfterDelay();
        // when
        handler.getVerificationAfterDelayHandler().verificationStarted(mode, handler);
        // then
        assertEquals(new MockHandlerProxy(MockHandlerInvocation.create(invocation)), MockHandlerProxy.create(MockHandlerInvocation.create(invocation)));
    }

    @Test
    public void mock_handler_should_delegate_to_verification_continuous_mode_handlers() throws Throwable {
        // given
        MockHandlerImpl handler = createHandler();
        Invocation invocation = createInvocation();
        MockHandlerFactory factory = createFactory();
        MockHandlerImpl expectedHandler = createHandler();
        Stubber stubber = createStubber();
        stubber.when(invocation.callRealMethod()).then(new AlwaysReturn(""));
        factory.when(handler.invoke(invocation)).thenReturn(stubber);
        java.lang.Iterable<VerificationEvent> events = createIterable();
        // when
        handler.handleVerificationFor(invocation, events);
        // then
        assertEquals(new MockHandlerProxy(MockHandlerInvocation.create(invocation)), MockHandlerProxy.create(MockHandlerInvocation.create(invocation)));
    }

    private MockHandlerImpl createHandler() {
        return new MockHandlerImpl();
    }

    private Invocation createInvocation() {
        return mock(Invocation.class);
    }

    private MockCreationSettings createSettings() {
        MockCreationSettings settings = mock(MockCreationSettings.class);
        when(settings.getInvocationFactory()).thenReturn(new MockHandlerInvocationFactory(), new MockHandlerInvocationFactory());
        MockName mockName = createMockName();
        when(settings.getMockName()).thenReturn(mockName);
        when(settings.getMockName().toString()).thenReturn("");
        when(settings.getMockName().toString()).thenReturn("");
        when(settings.getSerializationEngine()).thenReturn(null);
        when(settings.getStubbingLookupListeners()).thenReturn(null);
        when(settings.isSerializable()).thenReturn(true, true);
        return settings;
    }

    private MockName createMockName() {
        MockName mockName = mock(MockName.class);
        MockName actualMockName = null;
        when(mockName.appendTo(anyString())).thenReturn(actualMockName);
        when(mockName.appendTo(anyString())).thenReturn(actualMockName);
        return mockName;
    }

    private Stubber<?> createStubber() {
        return mock(Stubber.class);
    }

    private MockHandlerFactory createFactory() {
        return mock(MockHandlerFactory.class);
    }

    private VerificationMode createVerificationMode() {
        return mock(VerificationMode.class);
    }

    private VerificationEvent createVerificationEvent() {
        return mock(VerificationEvent.class);
    }

    private VerificationAfterDelay createVerificationAfterDelay() {
        return mock(VerificationAfterDelay.class);
    }

    private java.lang.Iterable<VerificationContinuousMode> createIterable() {
        return mock(java.lang.Iterable.class);
    }
}