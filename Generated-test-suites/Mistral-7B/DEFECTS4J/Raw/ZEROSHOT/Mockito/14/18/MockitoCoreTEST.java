import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.MockitoCore;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    @Rule
    public TestRule mockitoRule = MockitoAnnotations.openMocks(this);

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @org.junit.Test
    public void testMock() {
        Class<String> stringClass = String.class;
        String mock = mockitoCore.mock(stringClass, new MockSettingsImpl());
        verify(mock).toString();
    }

    @org.junit.Test
    public void testStub() {
        Object mock = mock(Object.class);
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("Test");
        when(mock).thenReturn("Test");
        String result = (String) mock;
        verify(mockitoCore.getLastInvocation()).getArgument(0);
        verify(mock).toString();
        verify(mock).equals("Test");
        verify(mock).equals("Test");
        String returnedValue = result;
        verify(mockitoCore).reset(mock);
        verifyZeroInteractions(mock);
    }

    @org.junit.Test
    public void testStubDeprecated() {
        Object mock = mock(Object.class);
        DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock);
        stubbing.thenReturn("Test");
        verify(mockitoCore).resetOngoingStubbing();
        Object returnedValue = stubbing.getMock();
        verify(mockitoCore).reset(mock);
        verifyZeroInteractions(mock);
        verify(returnedValue).toString();
        verify(returnedValue).equals("Test");
    }

    @org.junit.Test
    public void testWhen() {
        Object mock = mock(Object.class);
        OngoingStubbing<Object> stubbing = mockitoCore.when(mock);
        stubbing.thenReturn("Test");
        Object returnedValue = stubbing.getMock();
        verify(mockitoCore).resetOngoingStubbing();
        verifyZeroInteractions(mock);
        verify(returnedValue).toString();
        verify(returnedValue).equals("Test");
    }

    @org.junit.Test
    public void testVerify() {
        Object mock = mock(Object.class);
        try {
            mockitoCore.verify(mock, Mockito.eq("Test"));
        } catch (MockitoException e) {
            String errorMessage = e.getMessage();
            if (!errorMessage.contains("not the same reference")) {
                throw e;
            }
        }
        verifyNoMoreInteractions(mock);
        verifyZeroInteractions(mock);
    }

    @org.junit.Test
    public void testReset() {
        Object mock = mock(Object.class);
        mockitoCore.reset(mock);
        verifyZeroInteractions(mock);
    }

    @org.junit.Test
    public void testVerifyNoMoreInteractions() {
        Object mock = mock(Object.class);
        verifyNoMoreInteractions(mock);
    }

    @org.junit.Test
    public void testVerifyNoMoreInteractionsOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = inOrder(mock1, mock2);
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @org.junit.Test
    public void testVerifyNoMoreInteractionsWithError() {
        Object mock = mock(Object.class);
        try {
            mockitoCore.verifyNoMoreInteractions(mock, Mockito.eq(mockitoCore));
        } catch (MockitoException e) {
            String errorMessage = e.getMessage();
            if (!errorMessage.contains("not the same reference")) {
                throw e;
            }
        }
        verifyZeroInteractions(mock);
    }

    @org.junit.Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).toString();
        inOrder.verify(mock2).toString();
    }

    @org.junit.Test
    public void testDoAnswer() {
        Object mock = mock(Object.class);
        Answer<Object> answer = invocation -> "Test";
        Stubber stubber = mockitoCore.doAnswer(answer);
        Object returnedValue = stubber.getMock();
        verifyZeroInteractions(mock);
        verify(mockitoCore).resetOngoingStubbing();
        verify(returnedValue).toString();
        verify(returnedValue).equals("Test");
    }

    @org.junit.Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        VoidMethodStubbable<Object> stubbable = mockitoCore.stubVoid(mock);
        stubbable.doNothing();
        verifyZeroInteractions(mock);
        verify(mockitoCore).resetOngoingStubbing();
    }

    @org.junit.Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @org.junit.Test
    public void testGetLastInvocation() {
        Object mock = mock(Object.class);
        Invocation onMock = mock(InvocationOnMock.class);
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        OngoingStubbing<Object> stubbing = mockitoCore.when(mock).thenAnswer(invocation -> onMock);
        stubbing.withArguments(ArgumentMatchers.any());
        Object[] arguments = new Object[1];
        stubbing.thenAnswer(invocation -> {
            arguments[0] = invocation.getArgument(0);
            return null;
        });
        when(mock).thenReturn("Test");
        mockitoCore.verify(mock).toString();
        Object lastInvocationArgument = mockitoCore.getLastInvocation().getArgument(0);
        verify(onMock).callRealMethod();
        verify(mock).toString();
        verify(mock).equals("Test");
        verify(mock).equals("Test");
        verify(lastInvocationArgument).toString();
        verify(lastInvocationArgument).equals("Test");
        verify(arguments[0]).toString();
        verify(arguments[0]).equals("Test");
        verifyZeroInteractions(mockitoCore);
    }

    @org.junit.Test
    public void testAnswer() {
        Object mock = mock(Object.class);
        Answer<Object> answer = invocation -> "Test";
        Stubber stubber = mockitoCore.doAnswer(answer);
        Object returnedValue = stubber.getMock();
        verifyZeroInteractions(mock);
        verify(mockitoCore).resetOngoingStubbing();
        verify(returnedValue).toString();
        verify(returnedValue).equals("Test");
    }

    @org.junit.Test
    public void testAnswerWithArgument() {
        Object mock = mock(Object.class);
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        Answer<Object> answer = invocation -> argumentCaptor.getValue(invocation).toString();
        Stubber stubber = mockitoCore.doAnswer(answer);
        Object returnedValue = stubber.getMock();
        verifyZeroInteractions(mock);
        verify(mockitoCore).resetOngoingStubbing();
        Object testObject = new Object();
        stubber.thenAnswer(invocation -> testObject);
        Object argument = mockitoCore.getLastInvocation().getArgument(0);
        verify(argument).toString();
        verify(argument).equals(testObject);
        verifyZeroInteractions(mockitoCore);
    }

    @org.junit.Test
    public void testAnswerWithReturnValue() {
        Object mock = mock(Object.class);
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        Answer<Object> answer = invocation -> testObject();
        Stubber stubber = mockitoCore.doAnswer(answer);
        Object returnedValue = stubber.getMock();
        Object testObject = new Object();
        verifyZeroInteractions(mock);
        verify(mockitoCore).resetOngoingStubbing();
        Object argument = mockitoCore.getLastInvocation().getArgument(0);
        verify(argument).toString();
        verify(argument).equals(testObject);
        verifyZeroInteractions(mockitoCore);
    }

    private Object testObject() {
        return new Object();
    }

    @org.junit.Test
    public void testAnswerWithCapture() {
        Object mock = mock(Object.class);
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        Answer<Object> answer = invocation -> {
            Object argument = argumentCaptor.getValue(invocation);
            AtomicReference<Object> capture = new AtomicReference<>(null);
            capture.set(argument);
            return argument;
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
        Object returnedValue = stubber.getMock();
        Object argument = mockitoCore.getLastInvocation().getArgument(0);
        Object capturedValue = mockitoCore.getLastCapturedArgument();
        verifyZeroInteractions(mock);
        verify(mockitoCore).resetOngoingStubbing();
        verify(argument).toString();
        verify(argument).equals(argument);
        verify(capturedValue).toString();
        verify(capturedValue).equals(argument);
        verifyZeroInteractions(mockitoCore);
    }

    @org.junit.Test
    public void testAnswerWithMethodCall() {
        Object mock = mock(Object.class);
        Answer<Object> answer = invocation -> invocation.callRealMethod();
        Stubber stubber = mockitoCore.doAnswer(answer);
        Object returnedValue = stubber.getMock();
        verifyZeroInteractions(mock);
        verify(mockitoCore).resetOngoingStubbing();
        verify(returnedValue).toString();
        verify(returnedValue).equals("Test");
        verifyZeroInteractions(mockitoCore);
    }

    @org.junit.Test
    public void testAnswerWithCallRealMethodAndArguments() {
        Object mock = mock(Object.class);
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        Answer<Object> answer = invocation -> invocation.callRealMethodWith(argumentCaptor.capture("argument"));
        Stubber stubber = mockitoCore.doAnswer(answer);
        Object returnedValue = stubber.getMock();
        Object argument = mockitoCore.getLastCapturedArgument();
        verifyZeroInteractions(mock);
        verify(mockitoCore).resetOngoingStubbing();
        verify(argument).toString();
        verify(argument).equals("argument");
        verifyZeroInteractions(mockitoCore);
    }
}