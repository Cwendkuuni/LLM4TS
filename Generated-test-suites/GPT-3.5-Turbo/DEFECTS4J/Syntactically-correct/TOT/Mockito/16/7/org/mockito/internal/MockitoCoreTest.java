import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Before
    public void setup() {
        mockitoCore = mock(MockitoCore.class, Answers.RETURNS_DEEP_STUBS);
    }

    @Test
    public void testMock_ValidClass_ReturnsMockObject() {
        // Arrange
        Class<TestClass> classToMock = TestClass.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;

        // Act
        TestClass expectedResult = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);

        // Assert
        Mockito.verify(mockitoCore, times(1)).mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        assertNotNull(expectedResult);
    }

    @Test
    public void testMock_ValidClassAndSettings_ReturnsMockObject() {
        // Arrange
        Class<TestClass> classToMock = TestClass.class;
        MockSettings mockSettings = mock(MockSettings.class);

        // Act
        TestClass expectedResult = mockitoCore.mock(classToMock, mockSettings);

        // Assert
        Mockito.verify(mockitoCore, times(1)).mock(classToMock, mockSettings);
        assertNotNull(expectedResult);
    }

    @Test
    public void testStub_PullOngoingStubbing_ReturnsOngoingStubbing() {
        // Arrange
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockitoCore.stub()).thenReturn(stubbing);

        // Act
        IOngoingStubbing expectedResult = mockitoCore.stub();

        // Assert
        Mockito.verify(mockitoCore, times(1)).stub();
        assertNotNull(expectedResult);
        assertEquals(stubbing, expectedResult);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testStub_DeprecatedMethodCall_ReturnsDeprecatedOngoingStubbing() {
        // Arrange
        DeprecatedOngoingStubbing deprecatedOngoingStubbing = mock(DeprecatedOngoingStubbing.class);
        when(mockitoCore.stub()).thenReturn(deprecatedOngoingStubbing);

        // Act
        DeprecatedOngoingStubbing expectedResult = mockitoCore.stub(new TestClass());

        // Assert
        Mockito.verify(mockitoCore, times(1)).stub(Mockito.any(TestClass.class));
        assertNotNull(expectedResult);
        assertEquals(deprecatedOngoingStubbing, expectedResult);
    }

    @Test
    public void testWhen_MethodCall_ReturnsOngoingStubbing() {
        // Arrange
        OngoingStubbing ongoingStubbing = mock(OngoingStubbing.class);
        when(mockitoCore.stub()).thenReturn(ongoingStubbing);

        // Act
        OngoingStubbing expectedResult = mockitoCore.when(new TestClass());

        // Assert
        Mockito.verify(mockitoCore, times(1)).when(Mockito.any(TestClass.class));
        assertNotNull(expectedResult);
        assertEquals(ongoingStubbing, expectedResult);
    }

    @Test
    public void testVerify_ValidMockAndMode_ReturnsMock() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        TestClass expectedResult = mockitoCore.verify(mock, mode);

        // Assert
        Mockito.verify(mockitoCore, times(1)).verify(mock, mode);
        assertNotNull(expectedResult);
        assertEquals(mock, expectedResult);
    }

    @Test(expected = NullPointerException.class)
    public void testVerify_NullMock_ThrowsNullPointerException() {
        // Arrange
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        mockitoCore.verify(null, mode);

        // Assert
        fail("NullPointerException should be thrown");
    }

    @Test(expected = NotAMockException.class)
    public void testVerify_NotAMock_ThrowsNotAMockException() {
        // Arrange
        TestClass notAMock = new TestClass();
        VerificationMode mode = VerificationModeFactory.times(1);

        // Act
        mockitoCore.verify(notAMock, mode);

        // Assert
        fail("NotAMockException should be thrown");
    }

    @Test
    public void testReset_ValidMocks_ResetsMocks() {
        // Arrange
        TestClass mock1 = mock(TestClass.class);
        TestClass mock2 = mock(TestClass.class);
        TestClass mock3 = mock(TestClass.class);
        TestClass[] mocks = {mock1, mock2, mock3};

        // Act
        mockitoCore.reset(mocks);

        // Assert
        Mockito.verify(mockitoCore, times(1)).reset(mocks);
        Mockito.verify(mock1, times(1)).reset();
        Mockito.verify(mock2, times(1)).reset();
        Mockito.verify(mock3, times(1)).reset();
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractions_NotAMock_ThrowsNotAMockException() {
        // Arrange
        TestClass notAMock = new TestClass();

        // Act
        mockitoCore.verifyNoMoreInteractions(notAMock);

        // Assert
        fail("NotAMockException should be thrown");
    }

    @Test
    public void testInOrder_ValidMocks_ReturnsInOrderObject() {
        // Arrange
        TestClass mock1 = mock(TestClass.class);
        TestClass mock2 = mock(TestClass.class);
        TestClass mock3 = mock(TestClass.class);
        TestClass[] mocks = {mock1, mock2, mock3};

        // Act
        InOrder inOrder = mockitoCore.inOrder(mocks);

        // Assert
        Mockito.verify(mockitoCore, times(1)).inOrder(mocks);
        assertNotNull(inOrder);
    }

    @Test(expected = NullPointerException.class)
    public void testInOrder_NullMock_ThrowsNullPointerException() {
        // Arrange
        TestClass[] mocks = {null};

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        fail("NullPointerException should be thrown");
    }

    @Test(expected = NotAMockException.class)
    public void testInOrder_NotAMock_ThrowsNotAMockException() {
        // Arrange
        TestClass notAMock = new TestClass();
        TestClass[] mocks = {notAMock};

        // Act
        mockitoCore.inOrder(mocks);

        // Assert
        fail("NotAMockException should be thrown");
    }

    @Test
    public void testDoAnswer_ValidAnswer_ReturnsStubber() {
        // Arrange
        Answer answer = mock(Answer.class);
        Stubber stubber = mock(Stubber.class);
        when(mockitoCore.stub()).thenReturn(stubber);

        // Act
        Stubber expectedResult = mockitoCore.doAnswer(answer);

        // Assert
        Mockito.verify(mockitoCore, times(1)).doAnswer(answer);
        assertNotNull(expectedResult);
        assertEquals(stubber, expectedResult);
    }

    @Test
    public void testStubVoid_ValidMock_ReturnsMockHandler() {
        // Arrange
        TestClass mock = mock(TestClass.class);
        MockHandlerInterface handler = mock(MockHandlerInterface.class);
        when(mockitoCore.getMockHandler(mock)).thenReturn(handler);

        // Act
        VoidMethodStubbable expectedResult = mockitoCore.stubVoid(mock);

        // Assert
        Mockito.verify(mockitoCore, times(1)).stubVoid(mock);
        assertNotNull(expectedResult);
        assertEquals(handler, expectedResult.getHandler());
    }

    @Test
    public void testValidateMockitoUsage_ValidState_NoExceptionsThrown() {
        // Arrange

        // Act
        mockitoCore.validateMockitoUsage();

        // Assert
        Mockito.verify(mockitoCore, times(1)).validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation_ValidInvocation_ReturnsLastInvocation() {
        // Arrange
        OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
        Invocation invocation = mock(Invocation.class);
        List<Invocation> allInvocations = new ArrayList<>();
        allInvocations.add(mock(Invocation.class));
        allInvocations.add(mock(Invocation.class));
        allInvocations.add(invocation);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockitoCore.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        // Act
        Invocation expectedResult = mockitoCore.getLastInvocation();

        // Assert
        Mockito.verify(mockitoCore, times(1)).getLastInvocation();
        Mockito.verify(mockitoCore, times(1)).pullOngoingStubbing();
        assertNotNull(expectedResult);
        assertEquals(invocation, expectedResult);
    }

    private class TestClass {
        public void reset() {}
    }
}