import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.exceptions.NotAMockException;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Test
    public void testMockCreationWithValidArguments() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock = mockitoCore.mock(String.class, mockSettings, false);
        Assert.assertNotNull(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testMockCreationWithNullClass() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        mockitoCore.mock(null, mockSettings, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMockCreationWithInvalidClass() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        mockitoCore.mock(int.class, mockSettings, false);
    }

    @Test
    public void testMockCreationWithResetOngoingStubbingSetToFalse() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock1 = mockitoCore.mock(String.class, mockSettings, false);
        Object mock2 = mockitoCore.mock(String.class, mockSettings, false);
        Assert.assertNotSame(mock1, mock2);
    }

    @Test
    public void testMockCreationWithResetOngoingStubbingSetToTrue() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock1 = mockitoCore.mock(String.class, mockSettings, true);
        Object mock2 = mockitoCore.mock(String.class, mockSettings, true);
        Assert.assertSame(mock1, mock2);
    }

    @Test
    public void testStubbing() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock = mockitoCore.mock(String.class, mockSettings, false);
        Stubber stubber = mockitoCore.stub(mock);
        stubber.thenReturn("Hello");
        String result = (String) mockitoCore.verify(mock).toString();
        Assert.assertEquals("Hello", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testStubbingWhenMockingProgressIsNotReset() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock = mockitoCore.mock(String.class, mockSettings, false);
        mockitoCore.stubbingStarted();
        Stubber stubber = mockitoCore.stub(mock);
        stubber.thenReturn("Hello");
        mockitoCore.verify(mock).toString();
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNullMock() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        mockitoCore.verify(null, VerificationModeFactory.mode().times(1));
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNonMockObject() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object nonMockObject = new Object();
        mockitoCore.verify(nonMockObject, VerificationModeFactory.mode().times(1));
    }

    @Test
    public void testReset() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock1 = mockitoCore.mock(String.class, mockSettings, false);
        Object mock2 = mockitoCore.mock(String.class, mockSettings, false);
        Stubber stubber = mockitoCore.stub(mock1);
        stubber.thenReturn("Hello");
        Assert.assertEquals("Hello", mockitoCore.verify(mock1).toString());
        mockitoCore.reset(mock1, mock2);
        Assert.assertNull(mockitoCore.verify(mock1).toString());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifyNoMoreInteractionsWithNullMocks() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock1 = mockitoCore.mock(String.class, mockSettings, false);
        Object mock2 = mockitoCore.mock(String.class, mockSettings, false);
        Stubber stubber1 = mockitoCore.stub(mock1);
        stubber1.thenReturn("Hello");
        Stubber stubber2 = mockitoCore.stub(mock2);
        stubber2.thenReturn("World");
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsWithNonMockObject() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object nonMockObject = new Object();
        mockitoCore.verifyNoMoreInteractions(nonMockObject);
    }

    @Test
    public void testInOrder() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock1 = mockitoCore.mock(String.class, mockSettings, false);
        Object mock2 = mockitoCore.mock(String.class, mockSettings, false);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        Stubber stubber1 = mockitoCore.stub(mock1);
        stubber1.thenReturn("Hello");
        Stubber stubber2 = mockitoCore.stub(mock2);
        stubber2.thenReturn("World");
        inOrder.verify(mock1).someMethod();
        inOrder.verify(mock2).someMethod();
    }

    @Test(expected = NullPointerException.class)
    public void testInOrderWithNullMocks() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        mockitoCore.inOrder(null);
    }

    @Test
    public void testAnswer() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock = mockitoCore.mock(String.class, mockSettings, false);
        Answer<String> answer = invocation -> "Answer";
        Stubber stubber = mockitoCore.doAnswer(answer);
        String result = (String) mockitoCore.verify(mock).someMethod();
        Assert.assertEquals("Answer", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLastInvocationWithNullMock() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        mockitoCore.getLastInvocation();
    }

    @Test(expected = NotAMockException.class)
    public void testGetLastInvocationWithNonMockObject() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object nonMockObject = new Object();
        mockitoCore.getLastInvocation();
    }

    @Test
    public void testValidation() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        mockitoCore.validateMockitoUsage();
    }

    @Test(expected = IllegalStateException.class)
    public void testStubbingWithDeprecatedMethod() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = mockitoCoreClass.newInstance();
        Object mock = mockitoCore.mock(String.class, mockSettings, false);
        mockitoCore.stub(mock);
        Object result = mockitoCore.verify(mock).toString();
    }

}