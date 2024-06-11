import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.naming.MockNameImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.IOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
    }

    @Test
    public void testMockWithClassToMockAndMockSettings() {
        // Arrange 
        Class<CustomClass> classToMock = CustomClass.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);

        // Act
        CustomClass mock = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        assertNotNull(mock);
    }

    @Test
    public void testMockWithClassToMockMockSettingsAndShouldResetOngoingStubbing() {
        // Arrange 
        Class<CustomClass> classToMock = CustomClass.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;

        // Act
        CustomClass mock = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        // Assert
        assertNotNull(mock);
    }

    @Test
    public void testStub() {
        // Arrange

        // Act
        IOngoingStubbing stubbing = mockitoCore.stub();

        // Assert
        assertNotNull(stubbing);
    }

    @Test
    public void testStubWithMethodCall() {
        // Arrange
        CustomClass methodCall = Mockito.mock(CustomClass.class);

        // Act
        DeprecatedOngoingStubbing<CustomClass> stubbing = mockitoCore.stub(methodCall);

        // Assert
        assertNotNull(stubbing);
    }

    @Test
    public void testWhen() {
        // Arrange
        CustomClass methodCall = Mockito.mock(CustomClass.class);

        // Act
        OngoingStubbing<CustomClass> stubbing = mockitoCore.when(methodCall);

        // Assert
        assertNotNull(stubbing);
    }

    @Test
    public void testVerify() {
        // Arrange
        CustomClass mock = Mockito.mock(CustomClass.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);

        // Act
        CustomClass verifiedMock = mockitoCore.verify(mock, mode);

        // Assert
        assertNotNull(verifiedMock);
    }

    @Test
    public void testReset() {
        // Arrange
        CustomClass mock1 = Mockito.mock(CustomClass.class);
        CustomClass mock2 = Mockito.mock(CustomClass.class);

        // Act
        mockitoCore.reset(mock1, mock2);

        // Assert
        verify(mock1).reset();
        verify(mock2).reset();
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        // Arrange
        CustomClass mock1 = Mockito.mock(CustomClass.class);
        CustomClass mock2 = Mockito.mock(CustomClass.class);

        // Act
        mockitoCore.verifyNoMoreInteractions(mock1, mock2);

        // Assert
        verify(mock1).verifyNoMoreInteractions();
        verify(mock2).verifyNoMoreInteractions();
    }

    @Test
    public void testInOrder() {
        // Arrange
        CustomClass mock1 = Mockito.mock(CustomClass.class);
        CustomClass mock2 = Mockito.mock(CustomClass.class);

        // Act
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        // Assert
        assertNotNull(inOrder);
    }

    @Test
    public void testDoAnswer() {
        // Arrange
        Answer answer = Mockito.mock(Answer.class);

        // Act
        Stubber stubber = mockitoCore.doAnswer(answer);

        // Assert
        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid() {
        // Arrange
        CustomClass mock = Mockito.mock(CustomClass.class);

        // Act
        VoidMethodStubbable<CustomClass> voidMethodStubbable = mockitoCore.stubVoid(mock);

        // Assert
        assertNotNull(voidMethodStubbable);
    }

    @Test
    public void testValidateMockitoUsage() {
        // Arrange

        // Act
        mockitoCore.validateMockitoUsage();

        // Assert - No exception should be thrown
    }

    @Test
    public void testGetLastInvocation() {
        // Arrange

        // Act
        Invocation lastInvocation = mockitoCore.getLastInvocation();

        // Assert
        assertNotNull(lastInvocation);
    }

    private static class CustomClass {
        public void reset() {}

        public void verifyNoMoreInteractions() {}
    }
}