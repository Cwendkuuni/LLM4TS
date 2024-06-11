import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoCore;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockUtil;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private MockHandlerInterface mockHandler;
    private Object mock;
    private Object otherMock;

    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
        MockitoAnnotations.initMocks(this);
        mockHandler = mockUtil(Mockito.class, MockHandlerInterface.class);
        mock = mock(Object.class);
        otherMock = mock(Object.class);
    }

    @Test
    public void testMockCreation() {
        T mockInstance = mockitoCore.mock(Object.class);
        assert mockInstance instanceof Object;
        Mockito.verifyNoMoreInteractions(mockInstance);
    }

    @Test
    public void testMockCreationWithSettings() {
        MockSettings settings = new MockSettings();
        T mockInstance = mockitoCore.mock(Object.class, settings, false);
        assert mockInstance instanceof Object;
        Mockito.verifyNoMoreInteractions(mockInstance);
    }

    @Test
    public void testStubbing() {
        when(mock).thenReturn("hello");
        String result = (String) mockitoCore.verify(mock).someMethod();
        Mockito.verify(mock).someMethod();
        Mockito.verifyNoMoreInteractions(mock);
        assert result.equals("hello");
    }

    @Test
    public void testStubbingWithAnswer() {
        Answer<Integer> answer = invocation -> 42;
        when(mock).thenAnswer(answer);
        Integer result = (Integer) mockitoCore.verify(mock).someMethod();
        Mockito.verify(mock).someMethod();
        Mockito.verifyNoMoreInteractions(mock);
        assert result.equals(42);
    }

    @Test
    public void testVerification() {
        mockitoCore.verify(mock);
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testVerificationWithNullMock() {
        mockitoCore.verify(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerificationWithNotAMock() {
        mockitoCore.verify(new Object());
    }

    @Test
    public void testResetMocks() {
        when(mock).thenReturn("hello");
        String result = (String) mockitoCore.verify(mock).someMethod();
        assert result.equals("hello");

        mockitoCore.reset(mock);

        result = (String) mockitoCore.verify(mock).someMethod();
        Mockito.verifyNoMoreInteractions(mock);
        assert result == null;
    }

    @Test
    public void testResetAllMocks() {
        when(mock).thenReturn("hello");
        when(otherMock).thenReturn("world");

        String mockResult = (String) mockitoCore.verify(mock).someMethod();
        String otherMockResult = (String) mockitoCore.verify(otherMock).someMethod();
        assert mockResult.equals("hello");
        assert otherMockResult.equals("world");

        mockitoCore.reset(mock, otherMock);

        mockResult = (String) mockitoCore.verify(mock).someMethod();
        otherMockResult = (String) mockitoCore.verify(otherMock).someMethod();
        Mockito.verifyNoMoreInteractions(mock);
        Mockito.verifyNoMoreInteractions(otherMock);
        assert mockResult == null;
        assert otherMockResult == null;
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        when(mock).thenReturn("hello");
        mockitoCore.verify(mock).someMethod();
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsWithNullMock() {
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsWithNotAMock() {
        mockitoCore.verifyNoMoreInteractions(new Object());
    }

    @Test
    public void testInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder order = mockitoCore.inOrder(mock1, mock2);

        mockitoCore.verify(mock1).someMethod();
        mockitoCore.verify(mock2).someMethod();
        order.verify(mock1).someMethod();
        order.verify(mock2).someMethod();
    }

    @Test(expected = AssertionError.class)
    public void testInOrderWithNullMock() {
        InOrder order = mockitoCore.inOrder(null);
    }

    @Test(expected = AssertionError.class)
    public void testInOrderWithNotAMock() {
        InOrder order = mockitoCore.inOrder(new Object());
    }

    @Test
    public void testStubVoid() {
        mockitoCore.stubVoid(mock).someVoidMethod();
        mockitoCore.verify(mock).someVoidMethod();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    private MockHandlerInterface mockUtil(Class<?> clazz, Class<?> interfaceType) {
        return MockUtil.mockUtil(clazz, interfaceType);
    }
}