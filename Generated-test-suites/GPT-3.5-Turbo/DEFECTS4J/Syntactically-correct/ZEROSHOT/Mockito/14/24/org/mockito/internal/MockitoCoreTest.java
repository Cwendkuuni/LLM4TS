import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {

	private MockitoCore mockitoCore;
	private Reporter mockReporter;
	private MockUtil mockMockUtil;
	private MockingProgress mockMockingProgress;

	@Before
	public void setUp() {
		mockReporter = mock(Reporter.class);
		mockMockUtil = mock(MockUtil.class);
		mockMockingProgress = mock(MockingProgress.class);
		mockitoCore = new MockitoCore();
		mockitoCore.reporter = mockReporter;
		mockitoCore.mockUtil = mockMockUtil;
		mockitoCore.mockingProgress = mockMockingProgress;
	}

	@Test
	public void testMock() {
		Class<T> classToMock = T.class;
		MockSettings mockSettings = mock(MockSettings.class);

		T resultMock = mockitoCore.mock(classToMock, mockSettings);

		verify(mockMockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
		verify(mockMockingProgress).mockingStarted(resultMock, classToMock, mockSettings);
		assertEquals(resultMock, mockitoCore.mock(classToMock, mockSettings));
	}

	@Test
	public void testStub() {
		IOngoingStubbing expectedStubbing = mock(IOngoingStubbing.class);
		when(mockMockingProgress.pullOngoingStubbing()).thenReturn(expectedStubbing);

		IOngoingStubbing stubbing = mockitoCore.stub();

		verify(mockMockingProgress).pullOngoingStubbing();
		verify(mockMockingProgress).reset();
		verify(mockReporter, times(0)).missingMethodInvocation();
		assertEquals(expectedStubbing, stubbing);
	}

	@Test
	public void testStub_NullStubbing() {
		when(mockMockingProgress.pullOngoingStubbing()).thenReturn(null);

		IOngoingStubbing stubbing = mockitoCore.stub();

		verify(mockMockingProgress).pullOngoingStubbing();
		verify(mockMockingProgress).reset();
		verify(mockReporter).missingMethodInvocation();
		assertNull(stubbing);
	}

	@Test
	public void testStub_T() {
		T methodCall = mock(T.class);

		DeprecatedOngoingStubbing<T> expectedStubbing = mock(DeprecatedOngoingStubbing.class);
		when(mockMockingProgress.stubbingStarted()).thenReturn(expectedStubbing);

		DeprecatedOngoingStubbing<T> stubbing = mockitoCore.stub(methodCall);

		verify(mockMockingProgress).stubbingStarted();
		assertEquals(expectedStubbing, stubbing);
	}

	@Test
	public void testWhen_T() {
		T methodCall = mock(T.class);

		OngoingStubbing<T> expectedStubbing = mock(OngoingStubbing.class);
		when(mockMockingProgress.stubbingStarted()).thenReturn(expectedStubbing);

		OngoingStubbing<T> stubbing = mockitoCore.when(methodCall);

		verify(mockMockingProgress).stubbingStarted();
		assertEquals(expectedStubbing, stubbing);
	}

	@Test
	public void testVerify() {
		T mock = mock(T.class);
		VerificationMode mode = mock(VerificationMode.class);

		T resultMock = mockitoCore.verify(mock, mode);

		verify(mockReporter, times(0)).nullPassedToVerify();
		verify(mockReporter, times(0)).notAMockPassedToVerify();
		verify(mockMockingProgress).verificationStarted(mode);
		assertEquals(mock, resultMock);
	}

	@Test
	public void testVerify_WithNullMock() {
		T mock = null;
		VerificationMode mode = mock(VerificationMode.class);

		mockitoCore.verify(mock, mode);

		verify(mockReporter).nullPassedToVerify();
		verify(mockReporter, times(0)).notAMockPassedToVerify();
		verify(mockMockingProgress, times(0)).verificationStarted(mode);
	}

	@Test
	public void testVerify_WithNotAMock() {
		T mock = mock(T.class);
		VerificationMode mode = mock(VerificationMode.class);
		when(mockMockUtil.isMock(mock)).thenReturn(false);

		mockitoCore.verify(mock, mode);

		verify(mockReporter, times(0)).nullPassedToVerify();
		verify(mockReporter).notAMockPassedToVerify();
		verify(mockMockingProgress, times(0)).verificationStarted(mode);
	}

	@Test
	public void testReset() {
		T[] mocks = { mock(T.class) };

		mockitoCore.reset(mocks);

		verify(mockMockingProgress).validateState();
		verify(mockMockingProgress).reset();
		verify(mockMockingProgress).resetOngoingStubbing();
		verify(mockMockUtil).resetMock(mocks[0]);
	}

	@Test
	public void testVerifyNoMoreInteractions() {
		T mock1 = mock(T.class);
		T mock2 = mock(T.class);
		T mock3 = mock(T.class);
		Object[] mocks = { mock1, mock2, mock3 };

		mockitoCore.verifyNoMoreInteractions(mocks);

		verify(mockMockingProgress).validateState();
		verify(mockReporter, times(0)).nullPassedToVerifyNoMoreInteractions();
		verify(mockMockUtil).getMockHandler(mock1);
		verify(mockMockUtil).getMockHandler(mock2);
		verify(mockMockUtil).getMockHandler(mock3);
		verify(mockReporter).notAMockPassedToVerifyNoMoreInteractions();
	}

	@Test
	public void testVerifyNoMoreInteractions_WithNullMock() {
		T mock1 = null;
		T mock2 = null;
		T mock3 = null;
		Object[] mocks = { mock1, mock2, mock3 };

		mockitoCore.verifyNoMoreInteractions(mocks);

		verify(mockMockingProgress).validateState();
		verify(mockReporter, times(3)).nullPassedToVerifyNoMoreInteractions();
		verify(mockMockUtil, times(0)).getMockHandler(mock1);
		verify(mockMockUtil, times(0)).getMockHandler(mock2);
		verify(mockMockUtil, times(0)).getMockHandler(mock3);
		verify(mockReporter, times(0)).notAMockPassedToVerifyNoMoreInteractions();
	}

	@Test
	public void testVerifyNoMoreInteractions_WithNotAMock() {
		T mock1 = mock(T.class);
		T mock2 = mock(T.class);
		T mock3 = mock(T.class);
		Object[] mocks = { mock1, mock2, mock3 };
		when(mockMockUtil.isMock(mock2)).thenReturn(false);

		mockitoCore.verifyNoMoreInteractions(mocks);

		verify(mockMockingProgress).validateState();
		verify(mockReporter, times(0)).nullPassedToVerifyNoMoreInteractions();
		verify(mockMockUtil).getMockHandler(mock1);
		verify(mockMockUtil).getMockHandler(mock2);
		verify(mockMockUtil, times(0)).getMockHandler(mock3);
		verify(mockReporter).notAMockPassedToVerifyNoMoreInteractions();
	}

	@Test
	public void testVerifyNoMoreInteractionsInOrder() {
		List<Object> mocks = Arrays.asList(mock(T.class), mock(T.class), mock(T.class));
		InOrderContext inOrderContext = mock(InOrderContext.class);

		mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

		verify(mockMockingProgress).validateState();
		verify(mockReporter, times(0)).mocksHaveToBePassedToVerifyNoMoreInteractions();
		verify(mockReporter, times(0)).nullPassedWhenCreatingInOrder();
		verify(mockMockUtil, times(3)).getMockHandler(any());
	}

	@Test
	public void testVerifyNoMoreInteractionsInOrder_EmptyMocks() {
		List<Object> mocks = Arrays.asList();
		InOrderContext inOrderContext = mock(InOrderContext.class);

		mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

		verify(mockMockingProgress).validateState();
		verify(mockReporter).mocksHaveToBePassedToVerifyNoMoreInteractions();
		verify(mockReporter, times(0)).nullPassedWhenCreatingInOrder();
		verify(mockMockUtil, times(0)).getMockHandler(any());
	}

	@Test
	public void testInOrder() {
		T mock1 = mock(T.class);
		T mock2 = mock(T.class);
		T mock3 = mock(T.class);
		Object[] mocks = { mock1, mock2, mock3 };

		InOrder inOrder = mockitoCore.inOrder(mocks);

		verify(mockReporter, times(0)).mocksHaveToBePassedWhenCreatingInOrder();
		verify(mockReporter, times(0)).nullPassedWhenCreatingInOrder();
		verify(mockReporter, times(0)).notAMockPassedWhenCreatingInOrder();
		assertNotNull(inOrder);
	}

	@Test
	public void testInOrder_EmptyMocks() {
		Object[] mocks = {};

		mockitoCore.inOrder(mocks);

		verify(mockReporter).mocksHaveToBePassedWhenCreatingInOrder();
		verify(mockReporter, times(0)).nullPassedWhenCreatingInOrder();
		verify(mockReporter, times(0)).notAMockPassedWhenCreatingInOrder();
	}

	@Test
	public void testInOrder_WithNullMock() {
		T mock1 = mock(T.class);
		T mock2 = null;
		T mock3 = mock(T.class);
		Object[] mocks = { mock1, mock2, mock3 };

		mockitoCore.inOrder(mocks);

		verify(mockReporter).nullPassedWhenCreatingInOrder();
		verify(mockReporter, times(0)).notAMockPassedWhenCreatingInOrder();
		verify(mockReporter, times(0)).mocksHaveToBePassedWhenCreatingInOrder();
	}

	@Test
	public void testInOrder_WithNotAMock() {
		T mock1 = mock(T.class);
		T mock2 = mock(T.class);
		T mock3 = mock(T.class);
		Object[] mocks = { mock1, mock2, mock3 };
		when(mockMockUtil.isMock(mock2)).thenReturn(false);

		mockitoCore.inOrder(mocks);

		verify(mockReporter).notAMockPassedWhenCreatingInOrder();
		verify(mockReporter, times(0)).nullPassedWhenCreatingInOrder();
		verify(mockReporter, times(0)).mocksHaveToBePassedWhenCreatingInOrder();
	}

	@Test
	public void testDoAnswer() {
		Answer answer = mock(Answer.class);
		Stubber stubber = mock(Stubber.class);
		when(mockMockingProgress.stubbingStarted()).thenReturn(stubber);

		Stubber resultStubber = mockitoCore.doAnswer(answer);

		verify(mockMockingProgress).stubbingStarted();
		verify(mockMockingProgress).resetOngoingStubbing();
		assertEquals(stubber, resultStubber);
	}

	@Test
	public void testStubVoid() {
		T mock = mock(T.class);

		VoidMethodStubbable<T> stubbable = mockitoCore.stubVoid(mock);

		verify(mockMockingProgress).stubbingStarted();
		assertEquals(mockitoCore.mockUtil.getMockHandler(mock).voidMethodStubbable(mock), stubbable);
	}

	@Test
	public void testValidateMockitoUsage() {
		mockitoCore.validateMockitoUsage();

		verify(mockMockingProgress).validateState();
	}

	@Test
	public void testGetLastInvocation() {
		OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
		List<Invocation> registeredInvocations = Arrays.asList(new Invocation(), new Invocation());
		when(ongoingStubbing.getRegisteredInvocations()).thenReturn(registeredInvocations);
		when(mockMockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
		Invocation expectedInvocation = registeredInvocations.get(registeredInvocations.size() - 1);

		Invocation lastInvocation = mockitoCore.getLastInvocation();

		verify(mockMockingProgress).pullOngoingStubbing();
		assertEquals(expectedInvocation, lastInvocation);
	}

	private class T {
		// test class
	}
}