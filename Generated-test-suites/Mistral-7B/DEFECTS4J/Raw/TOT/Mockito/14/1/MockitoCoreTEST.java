import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.exceptions. base.MockitoException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    @Mock
    private MockHandlerInterface mockHandler;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore() {
            @Override
            public MockHandlerInterface getMockHandler(Object mock) {
                return mockHandler;
            }
        };
    }

    @Test
    public void testMockCreation() {
        String mock = mockitoCore.mock(String.class, new MockSettingsImpl());
        verify(mockitoCore).mockingStarted(mock, String.class, new MockSettingsImpl());
        verify(mockHandler).getInvocationContainer().setMock(mock);
    }

    @Test
    public void testStubbing() {
        String mock = mockitoCore.mock(String.class, new MockSettingsImpl());
        doAnswer(invocation -> "Result").when(mockitoCore).stub(mock, any(String.class));
        String result = mockitoCore.stub().stub(mock, "test");
        verify(mockitoCore).mockingProgress().stubbingStarted();
        verify(mockitoCore).stub().getRegisteredInvocations().get(0).getArgument(0).equals("test");
        verify(mockitoCore).stub().getRegisteredInvocations().get(0).getMethod().getName().equals("stub");
        verify(mockitoCore).stub().getRegisteredInvocations().get(0).getMock().equals(mock);
        verify(mockitoCore).stub().getRegisteredInvocations().get(0).getTarget().equals(this);
        verify(mockitoCore).stub().getRegisteredInvocations().get(0).getAnswer().equals(answer(i -> "Result"));
        verify(mockitoCore).stub().getRegisteredInvocations().get(0).getInvocation().getMethod().getName().equals("stub");
        verify(mockitoCore).stub().getRegisteredInvocations().get(0).getInvocation().getArguments()[0].equals("test");
        verify(mockitoCore).stub().getRegisteredInvocations().get(0).getInvocation().getTarget().equals(this);
        verify(mockitoCore).stub().getRegisteredInvocations().get(0).getInvocation().getAnswer().equals(answer(i -> "Result"));
        verify(mockitoCore).stub().getRegisteredInvocations().size() == 1;
        verify(mock).equals(mockitoCore.stub().getRegisteredInvocations().get(0).getMock());
        reset(mockitoCore.stub());
        verify(mockitoCore).reset();

        verifyNoMoreInteractions(mock);
        verify(mockitoCore).verifyNoMoreInteractions(mock);
    }

    @Test(expected = MockitoException.class)
    public void testStubbingWithNullMock() {
        mockitoCore.stub().stub(null, "test");
    }

    @Test
    public void testVerification() {
        String mock = mockitoCore.mock(String.class, new MockSettingsImpl());
        doReturn("ReturnValue").when(mockitoCore).stub(mock, any(String.class));
        when(mock).thenReturn("AnotherReturnValue");
        String result = mockitoCore.verify(mock, VerificationMode.returns(anyString()));
        verify(mockitoCore).mockingProgress().verificationStarted(VerificationMode.returns(anyString()));
        verify(mockitoCore).verify(mock, VerificationMode.returns(anyString())).equals("ReturnValue");
        verify(mockitoCore).verify(mock, VerificationMode.returns(anyString())).atLeastOnce();
        verify(mockitoCore).verify(mock, VerificationMode.returns(anyString())).never();
        verify(mockitoCore).verify(mock, VerificationMode.never()).equals("AnotherReturnValue");
        verify(mockitoCore).verify(mock, VerificationMode.never()).equals("AnotherValue");
        verify(mockitoCore).verify(mock, VerificationMode.never()).toString().equals("MockitoCore@1b6d3546");
        verify(mockitoCore).verify(mock, VerificationMode.never()).hashCode();

        verifyNoMoreInteractions(mock);
        verify(mockitoCore).verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerificationWithNullMock() {
        verifyNoMoreInteractions(null);
    }

    @Test
    public void testVerificationWithNotAMock() {
        Object notAMock = new Object();
        verify(notAMock, VerificationMode.never());
    }

    @Test(expected = MockitoException.class)
    public void testVerificationWithNullVerificationMode() {
        mockitoCore.verify(mock, null);
    }

    @Test
    public void testReset() {
        String mock = mockitoCore.mock(String.class, new MockSettingsImpl());
        doReturn("ReturnValue").when(mockitoCore).stub(mock, any(String.class));
        String result = mockitoCore.stub().stub(mock, "test");
        mockitoCore.reset();
        verifyNoMoreInteractions(mock);
        verify(mockitoCore).reset();
        verify(mockitoCore).resetOngoingStubbing();
        reset(mockitoCore.stub());
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        String mock = mockitoCore.mock(String.class, new MockSettingsImpl());
        doReturn("ReturnValue").when(mockitoCore).stub(mock, any(String.class));
        String result = mockitoCore.stub().stub(mock, "test");
        mockitoCore.verifyNoMoreInteractions(mock);
        verify(mockitoCore).verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        String mock1 = mockitoCore.mock(String.class, new MockSettingsImpl());
        String mock2 = mockitoCore.mock(String.class, new MockSettingsImpl());

        ArgumentCaptor<String> argCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argCaptor2 = ArgumentCaptor.forClass(String.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        doReturn("ReturnValue1").when(mockitoCore).stub(mock1, argCaptor1.capture());
        doReturn("ReturnValue2").when(mockitoCore).stub(mock2, argCaptor2.capture());

        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);

        verify(mockitoCore).verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder).inOrder();
        verify(mockitoCore).verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder).inOrder(mock1, mock2);
        verify(mockitoCore).verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder).inOrder(mock2, mock1);

        verify(mock1).equals(argCaptor1.getValue());
        verify(mock2).equals(argCaptor2.getValue());

        InOrder inOrder2 = mockitoCore.inOrder(mock1, mock2);

        verify(mockitoCore).verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder2).inOrder();
        verify(mockitoCore).verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder2).inOrder(mock1, mock2);
        verify(mockitoCore).verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder2).inOrder(mock2, mock1);

        verify(mock1).equals(inOrder2.getMock(0));
        verify(mock2).equals(inOrder2.getMock(1));
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        mockitoCore.verifyNoMoreInteractionsInOrder(null, new InOrderImpl(Arrays.asList(mock)));
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullInOrder() {
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock), null);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullMocks() {
        mockitoCore.verifyNoMoreInteractionsInOrder(null, null);
    }

    private Answer<String> answer(final int i) {
        return invocation -> "Result " + i;
    }
}