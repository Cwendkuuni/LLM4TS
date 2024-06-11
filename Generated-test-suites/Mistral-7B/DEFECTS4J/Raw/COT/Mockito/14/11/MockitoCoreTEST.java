import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockUtil;
import org.mockito.internal.stubbing.answers.CallsRealMethod;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.api.Stubbing.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class MockitoCoreTest {

    @Mock
    private Object mock;

    @Captor
    private ArgumentCaptor<Object> argumentCaptor;

    @Captor
    private ArgumentCaptor<VerificationMode> verificationModeCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test for mock() method
    @Test
    public void testMock() {
        Class<?> mockClass = Mockito.class;
        MockSettings mockSettings = new MockSettingsImpl();
        Object mockInstance = new MockitoCore().mock(mockClass, mockSettings);
        assertNotNull(mockInstance);
        verify(mockSettings).verifyNoAnnotationsPresent(mockClass);
    }

    // Test for stub() method
    @Test
    public void testStub() {
        Object mock = Mockito.mock(Object.class);
        IOngoingStubbing stubbing = newMockitoCore().stub();
        verify(stubbing).isInstanceOf(IOngoingStubbing.class);

        AtomicReference<Integer> result = new AtomicReference<>(0);
        when(mock).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                result.set(invocationOnMock.getArgument(0).hashCode());
                return null;
            }
        });

        int value = mock.hashCode();
        assertEquals(value, result.get());
    }

    // Test for when() method
    @Test
    public void testWhen() {
        Object mock = Mockito.mock(Object.class);

        AtomicReference<Integer> result = new AtomicReference<>(0);
        when(mock).thenAnswer(new CallsRealMethod<>());

        int value = 10;
        mock.hashCode();
        assertEquals(value, result.get());
    }

    // Test for verify() method
    @Test(expected = AssertionError.class)
    public void testVerifyNullMock() {
        newMockitoCore().verify(null, eq(VerificationMode.times(1)));
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNotAMock() {
        new MockitoCore().verify(new Object(), eq(VerificationMode.times(1)));
    }

    // Test for verifyNoMoreInteractions() method
    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock = Mockito.mock(Object.class);

        newMockitoCore().verifyNoMoreInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNullMock() {
        newMockitoCore().verifyNoMoreInteractions(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        newMockitoCore().verifyNoMoreInteractions(new Object());
    }

    // Test for verifyNoMoreInteractionsInOrder() method
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);

        InOrder inOrder = inOrder(mock1, mock2);

        newMockitoCore().verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    // Test for reset() method
    @Test
    public void testReset() {
        Object mock = Mockito.mock(Object.class);

        newMockitoCore().reset(mock);

        verify(mockUtil).resetMock(mock);
    }

    @Test
    public void testResetMultipleMocks() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);

        newMockitoCore().reset(mock1, mock2);

        verify(mockUtil).resetMock(mock1);
        verify(mockUtil).resetMock(mock2);
    }

    // Test for verify method with exceptions
    @Test(expected = NotAMockException.class)
    public void testVerifyException() throws NotAMockException, InvocationTargetException, IllegalAccessException {
        Object mock = new Object();

        newMockitoCore().verify(mock, Arguments.not(eq(Mockito.mock(Object.class))));
    }

    // Test for verifyNoMoreInteractions method with exceptions
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsException() throws NotAMockException {
        Object mock = new Object();

        newMockitoCore().verifyNoMoreInteractions(mock);
    }

    // Test for verifyNoMoreInteractionsInOrder method with exceptions
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsInOrderException() throws NotAMockException {
        Object mock = new Object();
        InOrder inOrder = Mockito.inOrder(mock);

        newMockitoCore().verifyNoMoreInteractionsInOrder(Arrays.asList(mock), inOrder);
    }

    // Test for inOrder() method
    @Test
    public void testInOrder() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);

        InOrder inOrder = newMockitoCore().inOrder(mock1, mock2);

        verify(mockitoCore).inOrder(mock1, mock2);
    }

    // Test for doAnswer() method
    @Test
    public void testDoAnswer() {
        Object mock = Mockito.mock(Object.class);

        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] arguments = invocationOnMock.getArguments();
                ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

                String argument = stringArgumentCaptor.getValue(arguments);
                System.out.println("Received argument: " + argument);

                return null;
            }
        };

        newMockitoCore().doAnswer(answer).when(mock).someMethod(anyString());

        mock.someMethod("Test");
    }

    // Test for stubVoid() method
    @Test
    public void testStubVoid() {
        Object mock = Mockito.mock(VoidMethodStubbable.class);

        VoidMethodStubbable<Object> voidStub = newMockitoCore().stubVoid(mock);

        verify(mockUtil).voidMethodStubbable(mock);
    }

    // Test for validateMockitoUsage() method
    @Test
    public void testValidateMockitoUsage() {
        newMockitoCore().validateMockitoUsage();
    }
}