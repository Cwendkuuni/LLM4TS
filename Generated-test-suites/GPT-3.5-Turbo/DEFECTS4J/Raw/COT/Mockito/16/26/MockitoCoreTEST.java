import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.InOrder;
import org.mockito.stubbing.*;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Test
    public void mock_ShouldCreateMockWithGivenClassAndMockSettings() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettingsImpl();

        // Act
        T result = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(Mockito.mockingDetails(result).isMock());
    }

    @Test
    public void mock_ShouldCreateMockWithGivenClassMockSettingsAndResetOngoingStubbing() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = true;

        // Act
        T result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(Mockito.mockingDetails(result).isMock());
    }

    @Test
    public void stub_ShouldReturnOngoingStubbingFromMockingProgressIfAvailable() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing<Integer> expectedStubbing = Mockito.mock(OngoingStubbing.class);
        Mockito.mockingProgress().pushOngoingStubbing(expectedStubbing);

        // Act
        OngoingStubbing<Integer> result = mockitoCore.stub();

        // Assert
        Assert.assertEquals(expectedStubbing, result);
    }

    @Test(expected = Reporter.class)
    public void stub_ShouldResetMockingProgressAndThrowExceptionWhenNoOngoingStubbingAvailable() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Mockito.mockingProgress().resetOngoingStubbing();

        // Act
        mockitoCore.stub();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void stub_ShouldResetMockingProgressAndReturnDeprecatedOngoingStubbingFromStub() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing<Integer> expectedStubbing = Mockito.mock(OngoingStubbing.class);
        Mockito.mockingProgress().pushOngoingStubbing(expectedStubbing);

        // Act
        DeprecatedOngoingStubbing<Integer> result = mockitoCore.stub(10);

        // Assert
        Assert.assertEquals(expectedStubbing, result);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void when_ShouldResetMockingProgressAndReturnOngoingStubbingFromStub() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing<Integer> expectedStubbing = Mockito.mock(OngoingStubbing.class);
        Mockito.mockingProgress().pushOngoingStubbing(expectedStubbing);

        // Act
        OngoingStubbing<Integer> result = mockitoCore.when(10);

        // Assert
        Assert.assertEquals(expectedStubbing, result);
    }

    @Test
    public void verify_ShouldThrowExceptionWhenMockIsNull() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mock = null;
        VerificationMode mode = Mockito.times(1);

        try {
            // Act
            mockitoCore.verify(mock, mode);
            Assert.fail("Exception not thrown");
        } catch (Reporter reporter) {
            // Assert
            Assert.assertEquals("nullPassedToVerify", reporter.getMessage());
        }
    }

    @Test
    public void verify_ShouldThrowExceptionWhenObjectIsNotAMock() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mock = new Integer(10);
        VerificationMode mode = Mockito.times(1);

        try {
            // Act
            mockitoCore.verify(mock, mode);
            Assert.fail("Exception not thrown");
        } catch (Reporter reporter) {
            // Assert
            Assert.assertEquals("notAMockPassedToVerify", reporter.getMessage());
        }
    }

    @Test
    public void verify_ShouldStartVerificationAndReturnMock() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mock = Mockito.mock(Integer.class);
        VerificationMode mode = Mockito.times(1);

        // Act
        Integer result = mockitoCore.verify(mock, mode);

        // Assert
        Assert.assertEquals(mock, result);
    }

    @Test
    public void reset_ShouldResetMockingProgressAndResetOngoingStubbing() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mock1 = Mockito.mock(Integer.class);
        Integer mock2 = Mockito.mock(Integer.class);

        // Act
        mockitoCore.reset(mock1, mock2);

        // Assert
        Assert.assertFalse(Mockito.mockingDetails(mock1).isMocked());
        Assert.assertFalse(Mockito.mockingDetails(mock2).isMocked());
    }

    @Test
    public void verifyNoMoreInteractions_ShouldThrowExceptionWhenMockIsNull() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mock = null;

        try {
            // Act
            mockitoCore.verifyNoMoreInteractions(mock);
            Assert.fail("Exception not thrown");
        } catch (Reporter reporter) {
            // Assert
            Assert.assertEquals("nullPassedToVerifyNoMoreInteractions", reporter.getMessage());
        }
    }

    @Test
    public void verifyNoMoreInteractions_ShouldThrowExceptionWhenNotAMockIsPassed() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mock = new Integer(10);

        try {
            // Act
            mockitoCore.verifyNoMoreInteractions(mock);
            Assert.fail("Exception not thrown");
        } catch (Reporter reporter) {
            // Assert
            Assert.assertEquals("notAMockPassedToVerifyNoMoreInteractions", reporter.getMessage());
        }
    }

    @Test
    public void verifyNoMoreInteractions_ShouldValidateStateAndVerifyNoMoreInteractionsForAllMocks() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mock1 = Mockito.mock(Integer.class);
        Integer mock2 = Mockito.mock(Integer.class);

        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);

        // Assert
        Mockito.verifyNoMoreInteractions(mock1, mock2);
    }

    @Test
    public void inOrder_ShouldThrowExceptionWhenMocksArrayIsNull() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = null;

        try {
            // Act
            mockitoCore.inOrder(mocks);
            Assert.fail("Exception not thrown");
        } catch (Reporter reporter) {
            // Assert
            Assert.assertEquals("mocksHaveToBePassedWhenCreatingInOrder", reporter.getMessage());
        }
    }

    @Test
    public void inOrder_ShouldThrowExceptionWhenNullIsPassedInMocksArray() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = {Mockito.mock(Integer.class), null};

        try {
            // Act
            mockitoCore.inOrder(mocks);
            Assert.fail("Exception not thrown");
        } catch (Reporter reporter) {
            // Assert
            Assert.assertEquals("nullPassedWhenCreatingInOrder", reporter.getMessage());
        }
    }

    @Test
    public void inOrder_ShouldThrowExceptionWhenNotAMockIsPassedInMocksArray() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = {Mockito.mock(Integer.class), new Integer(10)};

        try {
            // Act
            mockitoCore.inOrder(mocks);
            Assert.fail("Exception not thrown");
        } catch (Reporter reporter) {
            // Assert
            Assert.assertEquals("notAMockPassedWhenCreatingInOrder", reporter.getMessage());
        }
    }

    @Test
    public void inOrder_ShouldCreateNewInOrderImplWithMocksList() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = {Mockito.mock(Integer.class), Mockito.mock(String.class)};

        // Act
        InOrder result = mockitoCore.inOrder(mocks);

        // Assert
        Assert.assertEquals(InOrderImpl.class, result.getClass());
    }

    @Test
    public void doAnswer_ShouldResetMockingProgressAndReturnStubberImplWithGivenAnswer() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Answer answer = Mockito.mock(Answer.class);

        // Act
        Stubber result = mockitoCore.doAnswer(answer);

        // Assert
        Assert.assertEquals(StubberImpl.class, result.getClass());
    }

    @Test
    public void stubVoid_ShouldReturnVoidMethodStubbableFromMockHandlerInterface() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Integer mock = Mockito.mock(Integer.class);

        // Act
        VoidMethodStubbable<Integer> result = mockitoCore.stubVoid(mock);

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void validateMockitoUsage_ShouldValidateState() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();

        // Act
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void getLastInvocation_ShouldReturnLastInvocationFromOngoingStubbing() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        Invocation expectedInvocation = Mockito.mock(Invocation.class);
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(Arrays.asList(expectedInvocation));
        Mockito.mockingProgress().pushOngoingStubbing(ongoingStubbing);

        // Act
        Invocation result = mockitoCore.getLastInvocation();

        // Assert
        Assert.assertEquals(expectedInvocation, result);
    }

}