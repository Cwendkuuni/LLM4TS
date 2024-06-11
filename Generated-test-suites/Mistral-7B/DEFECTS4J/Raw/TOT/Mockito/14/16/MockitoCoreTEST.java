import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractionsInOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private Object mock1;
    private Object mock2;

    @Before
    public void init() {
        mockitoCore = new MockitoCore();
        MockitoAnnotations.initMocks(this);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
    }

    @Test
    public void testMockCreation() {
        Object mock = mockitoCore.mock(Object.class, null);
        verify(mockitoCore).mockingStarted(mock, Object.class, null);
        verify(mockitoCore).mockingStarted();
    }

    @Test
    public void testStubbing() {
        Object mock = mockitoCore.mock(Object.class, null);
        when(mock).thenReturn("return value");
        verify(mock).someMethod();
        verify(mock).someMethod().equals("return value");
    }

    @Test
    public void testVerification() {
        Object mock = mockitoCore.mock(Object.class, null);
        verify(mock).someMethod();
        verifyNoMoreInteractions(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPassedToVerify() {
        mockitoCore.verify(null, Mockito.any(VerificationMode.class));
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testNotAMockPassedToVerify() {
        mockitoCore.verify(new Object(), Mockito.any(VerificationMode.class));
    }

    @Test
    public void testVerificationModeFactory() {
        Object mock = mockitoCore.mock(Object.class, null);
        VerificationMode verifyNoInteractions = Mockito.never();
        VerificationMode verifyOnce = Mockito.times(1);
        VerificationMode verifyTwice = Mockito.times(2);

        List<Object> mocks = Arrays.asList(mock1, mock2);

        // Verify no interactions
        mockitoCore.verifyNoMoreInteractions(mock);
        mockitoCore.verifyNoMoreInteractions(mocks);
        verifyNoMoreInteractions(mock);
        verifyNoMoreInteractions(mocks);
        verifyNoMoreInteractionsInOrder(mocks, inOrder(mock1, mock2));

        // Verify once
        verify(mock1).someMethod();
        verify(mock2).someMethod();
        verify(mock1).someMethod();
        verify(mock2).someMethod();

        // Verify twice
        verify(mock1).someMethod();
        verify(mock1).someMethod();
        verify(mock2).someMethod();
        verify(mock2).someMethod();

        // Verify never
        verifyNoInteractions(mock);
        verifyNoInteractions(mock);
        verifyNoInteractions(mocks);
        verifyNoInteractions(mocks);
        verifyNoInteractionsInOrder(mocks, inOrder(mock1, mock2));
        verifyNoInteractionsInOrder(mocks, inOrder(mock2, mock1));
    }

    @Test
    public void testAnswer() {
        Object mock = mockitoCore.mock(Object.class, null);
        Answer<String> answer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
        stubber.stub(mock).someMethod().thenReturn("return value");
        verify(mock).someMethod().equals("answer");
    }

    @Test
    public void testStubVoid() {
        Object mock = mockitoCore.mock(Object.class, null);
        mockitoCore.stubVoid(mock).someVoidMethod();
        verify(mock).someVoidMethod();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testReset() {
        Object mock = mockitoCore.mock(Object.class, null);
        mockitoCore.reset(mock);
        verify(mockitoCore).reset();
        verify(mockitoCore).resetOngoingStubbing();
        verify(mockitoCore).reset();
    }

    @Test
    public void testVerifyNoMoreInteractionsWithMocks() {
        Object mock1 = mockitoCore.mock(Object.class, null);
        Object mock2 = mockitoCore.mock(Object.class, null);
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPassedToVerifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testNotAMockPassedToVerifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractions(new Object());
    }

    @Test
    public void testInOrder() {
        Object mock1 = mockitoCore.mock(Object.class, null);
        Object mock2 = mockitoCore.mock(Object.class, null);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();
    }

    @Test(expected = NullPointerException.class)
    public void testNullPassedWhenCreatingInOrder() {
        mockitoCore.inOrder(null);
    }

    @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
    public void testNotAMockPassedWhenCreatingInOrder() {
        mockitoCore.inOrder(new Object());
    }
}