import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockitoCoreTest {

	@Test
	public void testMock_classToMockMockSettings_shouldResetOngoingStubbing() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Class<?> classToMock = String.class;
		MockSettings mockSettings = Mockito.mock(MockSettings.class);
		boolean shouldResetOngoingStubbing = true;
		
		// Act
		Object result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
		
		// Assert
		assertNotNull(result);
		assertTrue(Mockito.mockingDetails(result).isMock());
	}
	
	@Test
	public void testMock_classToMockMockSettings() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Class<?> classToMock = String.class;
		MockSettings mockSettings = Mockito.mock(MockSettings.class);
		
		// Act
		Object result = mockitoCore.mock(classToMock, mockSettings);
		
		// Assert
		assertNotNull(result);
		assertTrue(Mockito.mockingDetails(result).isMock());
	}
	
	@Test(expected = NotAMockException.class)
	public void testMock_nullMock() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Class<?> classToMock = null;
		MockSettings mockSettings = Mockito.mock(MockSettings.class);
		
		// Act
		Object result = mockitoCore.mock(classToMock, mockSettings);
		
		// Assert
		assertNull(result);
	}
	
	@Test
	public void testStub_noOngoingStubbing() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		
		// Act
		IOngoingStubbing<?> result = mockitoCore.stub();
		
		// Assert
		assertNull(result);
	}
	
	@Test
	public void testStub_oneOngoingStubbing() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		IOngoingStubbing<?> ongoingStubbing = Mockito.mock(IOngoingStubbing.class);
		MockitoCore.mockingProgress.setOngoingStubbing(ongoingStubbing);
		
		// Act
		IOngoingStubbing<?> result = mockitoCore.stub();
		
		// Assert
		assertNotNull(result);
	}
	
	@Test
	public void testStub_deprecatedMethodCall() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object methodCall = new Object();
		MockUtil mockUtil = Mockito.mock(MockUtil.class);
		MockitoCore.mockUtil = mockUtil;
		
		// Act
		DeprecatedOngoingStubbing<?> result = mockitoCore.stub(methodCall);
		
		// Assert
		assertNotNull(result);
	}
	
	@Test
	public void testWhen_methodCall() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object methodCall = new Object();
		MockUtil mockUtil = Mockito.mock(MockUtil.class);
		MockitoCore.mockUtil = mockUtil;
		
		// Act
		OngoingStubbing<?> result = mockitoCore.when(methodCall);
		
		// Assert
		assertNotNull(result);
	}
	
	@Test(expected = NotAMockException.class)
	public void testVerify_nullMock() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object mock = null;
		VerificationMode mode = Mockito.mock(VerificationMode.class);
		
		// Act
		Object result = mockitoCore.verify(mock, mode);
		
		// Assert
		assertNull(result);
	}
	
	@Test(expected = NotAMockException.class)
	public void testVerify_notAMockPassedToVerify() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object mock = new Object();
		VerificationMode mode = Mockito.mock(VerificationMode.class);
		
		// Act
		Object result = mockitoCore.verify(mock, mode);
		
		// Assert
		assertNull(result);
	}

	@Test
	public void testReset_withMocks() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = { new Object() };
		MockUtil mockUtil = Mockito.mock(MockUtil.class);
		MockitoCore.mockUtil = mockUtil;
		
		// Act
		mockitoCore.reset(mocks);
		
		// Assert
		Mockito.verify(mockUtil).resetMock(mocks[0]);
	}
	
	@Test(expected = NotAMockException.class)
	public void testVerifyNoMoreInteractions_nullMock() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = { null };
		
		// Act
		mockitoCore.verifyNoMoreInteractions(mocks);
		
		// Assert
	}
	
	@Test(expected = NotAMockException.class)
	public void testVerifyNoMoreInteractions_notAMockPassedToVerifyNoMoreInteractions() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = { new Object() };
		MockUtil mockUtil = Mockito.mock(MockUtil.class);
		MockitoCore.mockUtil = mockUtil;
		
		// Act
		mockitoCore.verifyNoMoreInteractions(mocks);
		
		// Assert
	}
	
	@Test(expected = NotAMockException.class)
	public void testVerifyNoMoreInteractions_multipleMocks() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = { new Object() };
		MockUtil mockUtil = Mockito.mock(MockUtil.class);
		MockitoCore.mockUtil = mockUtil;
		
		// Act
		mockitoCore.verifyNoMoreInteractions(mocks);
		
		// Assert
		Mockito.verify(mockUtil).getMockHandler(mocks[0]);
	}
	
	@Test(expected = AssertionError.class)
	public void testAssertMocksNotEmpty_noMocksPassed() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = {};
		Reporter reporter = Mockito.mock(Reporter.class);
		MockitoCore.reporter = reporter;
		
		// Act
		mockitoCore.verifyNoMoreInteractions(mocks);
		
		// Assert
		Mockito.verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
	}
	
	@Test(expected = AssertionError.class)
	public void testAssertMocksNotEmpty_nullMocksPassed() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = null;
		Reporter reporter = Mockito.mock(Reporter.class);
		MockitoCore.reporter = reporter;
		
		// Act
		mockitoCore.verifyNoMoreInteractions(mocks);
		
		// Assert
		Mockito.verify(reporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
	}
	
	@Test
	public void testInOrder_withMocks() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = { Mockito.mock(Object.class) };
		Reporter reporter = Mockito.mock(Reporter.class);
		MockitoCore.reporter = reporter;
		MockUtil mockUtil = Mockito.mock(MockUtil.class);
		MockitoCore.mockUtil = mockUtil;
		
		// Act
		InOrder result = mockitoCore.inOrder(mocks);
		
		// Assert
		assertNotNull(result);
	}
	
	@Test(expected = AssertionError.class)
	public void testInOrder_noMocksPassed() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = {};
		Reporter reporter = Mockito.mock(Reporter.class);
		MockitoCore.reporter = reporter;

		// Act
		mockitoCore.inOrder(mocks);

		// Assert
		Mockito.verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
	}
	
	@Test(expected = AssertionError.class)
	public void testInOrder_nullMocksPassed() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = null;
		Reporter reporter = Mockito.mock(Reporter.class);
		MockitoCore.reporter = reporter;
		
		// Act
		mockitoCore.inOrder(mocks);
		
		// Assert
		Mockito.verify(reporter).mocksHaveToBePassedWhenCreatingInOrder();
	}
	
	@Test(expected = AssertionError.class)
	public void testInOrder_notAMockPassed() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object[] mocks = { new Object() };
		Reporter reporter = Mockito.mock(Reporter.class);
		MockitoCore.reporter = reporter;
		MockUtil mockUtil = Mockito.mock(MockUtil.class);
		MockitoCore.mockUtil = mockUtil;
		
		// Act
		mockitoCore.inOrder(mocks);
		
		// Assert
		Mockito.verify(reporter).notAMockPassedWhenCreatingInOrder();
	}
	
	@Test
	public void testDoAnswer() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Answer<?> answer = Mockito.mock(Answer.class);
		MockUtil mockUtil = Mockito.mock(MockUtil.class);
		MockitoCore.mockUtil = mockUtil;
		
		// Act
		Stubber result = mockitoCore.doAnswer(answer);
		
		// Assert
		assertNotNull(result);
	}
	
	@Test
	public void testStubVoid() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		Object mock = Mockito.mock(Object.class);
		MockUtil mockUtil = Mockito.mock(MockUtil.class);
		MockitoCore.mockUtil = mockUtil;
		
		// Act
		VoidMethodStubbable<?> result = mockitoCore.stubVoid(mock);
		
		// Assert
		assertNotNull(result);
	}

	@Test
	public void testValidateMockitoUsage() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		
		// Act
		mockitoCore.validateMockitoUsage();
		
		// Assert
	}
	
	@Test
	public void testGetLastInvocation() {
		// Arrange
		MockitoCore mockitoCore = new MockitoCore();
		MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
		MockitoCore.mockingProgress = mockingProgress;
		OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
		Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
		List<Invocation> allInvocations = Mockito.mock(List.class);
		Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
		Invocation lastInvocation = Mockito.mock(Invocation.class);
		Mockito.when(allInvocations.get(Mockito.anyInt())).thenReturn(lastInvocation);
		
		// Act
		Invocation result = mockitoCore.getLastInvocation();
		
		// Assert
		assertNotNull(result);
		assertEquals(lastInvocation, result);
	}
}