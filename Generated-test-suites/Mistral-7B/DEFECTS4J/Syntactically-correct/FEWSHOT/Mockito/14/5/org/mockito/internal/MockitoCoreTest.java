package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.onCall;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {

	private MockitoCore mockitoCore;
	private Reporter reporter;
	private MockUtil mockUtil;
	private MockingProgress mockingProgress;
	private Object mock;
	private IOngoingStubbing ongoingStubbing;
	private DeprecatedOngoingStubbing<Object> deprecatedOngoingStubbing;
	private OngoingStubbing<Object> ongoingStubbing;
	private VerificationDataImpl verificationData;
	private VerificationDataInOrder verificationDataInOrder;
	private InOrderContext inOrderContext;
	private List<Invocation> invocations;
	private Object[] mocks;
	private VerificationMode verificationMode;
	private StubberImpl stubberImpl;

	@Before
	public void setup() {
		mockitoCore = new MockitoCore();
		reporter = mockitoCore.reporter;
		mockUtil = mockitoCore.mockUtil;
		mockingProgress = mockitoCore.mockingProgress;
	}

	@Test
	public void testMock() {
		Class<Object> classToMock = Object.class;
		MockSettings mockSettings = new MockSettingsImpl();
		mock = mockitoCore.mock(classToMock, mockSettings);
		assertTrue(mockitoCore.isMock(mock));
	}

	@Test(expected = NullPointerException.class)
	public void testMockWithNullClass() {
		MockSettings mockSettings = new MockSettingsImpl();
		mockitoCore.mock(null, mockSettings);
	}

	@Test
	public void testMockWithCustomMockFactory() {
		Class<Object> classToMock = Object.class;
		MockSettings mockSettings = new MockSettingsImpl();
		classToMock = (Class<Object>) spy(classToMock);
		mock = mockitoCore.mock(classToMock, mockSettings);
		assertTrue(mockitoCore.isMock(mock));
	}

	@Test
	public void testStubbingStarted() {
		ongoingStubbing = mockitoCore.stub();
		assertTrue(mockingProgress.isStubbingStarted());
	}

	@Test
	public void testStubbingStarted_Null() {
		assertTrue(mockingProgress.isStubbingStarted());
	}

	@Test
	public void testStubbingStartedTwice() {
		mockingProgress.stubbingStarted();
		mockingProgress.stubbingStarted();
		assertTrue(mockingProgress.isStubbingStarted());
		assertTrue(mockingProgress.isStubbingStarted());
	}

	@Test
	public void testStubbingFinished() {
		ongoingStubbing = mockitoCore.stub();
		mockingProgress.stubbingFinished();
		assertFalse(mockingProgress.isStubbingStarted());
	}

	@Test
	public void testStubbingFinished_Null() {
		assertFalse(mockingProgress.isStubbingStarted());
	}

	@Test
	public void testStub() {
		mockingProgress.reset();
		ongoingStubbing = mockitoCore.stub();
		assertSame(ongoingStubbing, mockitoCore.stub());
	}

	@Test(expected = IllegalStateException.class)
	public void testStubWithoutStubbingStarted() {
		mockingProgress.reset();
		mockitoCore.stub();
	}

	@Test
	public void testDeprecatedStub() {
		mockingProgress.reset();
		deprecatedOngoingStubbing = mockitoCore.stub((Object) null);
		assertSame(ongoingStubbing, deprecatedOngoingStubbing);
	}

	@Test(expected = IllegalStateException.class)
	public void testDeprecatedStubWithoutStubbingStarted() {
		mockingProgress.reset();
		mockitoCore.stub((Object) null);
	}

	@Test
	public void testOngoingStubbing_When() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		ongoingStubbing = mockitoCore.when(mock);
		assertSame(ongoingStubbing, mockitoCore.when(mock));
	}

	@Test
	public void testOngoingStubbing_When_Null() {
		assertSame(mockitoCore.when(null), mockitoCore.when(null));
	}

	@Test
	public void testOngoingStubbing_When_NullMock() {
		assertSame(mockitoCore.when(null), mockitoCore.when((Object) null));
	}

	@Test
	public void testOngoingStubbing_When_NullMethodCall() {
		assertSame(mockitoCore.when(mock), mockitoCore.when(null));
	}

	@Test
	public void testVerify() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		verify(mock, VerificationModeFactory.verifyNoMoreInteractions());
	}

	@Test(expected = NullPointerException.class)
	public void testVerifyWithNullMock() {
		verify(null, VerificationModeFactory.verifyNoMoreInteractions());
	}

	@Test(expected = NotAMockException.class)
	public void testVerifyNotAMock() {
		mock = new Object();
		verify(mock, VerificationModeFactory.verifyNoMoreInteractions());
	}

	@Test
	public void testVerifyNoMoreInteractions() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		mockitoCore.when(mock).foo();
		verify(mock, VerificationModeFactory.verifyNoMoreInteractions());
	}

	@Test
	public void testVerifyNoMoreInteractions_Null() {
		verifyNoMoreInteractions(null);
	}

	@Test
	public void testVerifyNoMoreInteractions_EmptyArray() {
		verifyNoMoreInteractions();
	}

	@Test
	public void testVerifyNoMoreInteractions_EmptyList() {
		Object mock1 = mockitoCore.mock(String.class, new MockSettingsImpl());
		Object mock2 = mockitoCore.mock(String.class, new MockSettingsImpl());
		List<Object> mocks = Arrays.asList(mock1, mock2);
		verifyNoMoreInteractions(mocks.toArray());
	}

	@Test
	public void testVerifyNoMoreInteractionsInOrder() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		mockitoCore.when(mock).foo().thenReturn("foo");
		mockitoCore.when(mock).bar().thenReturn("bar");
		mockitoCore.when(mock).baz().thenReturn("baz");
		inOrderContext = inOrder(mock);
		verifyNoMoreInteractionsInOrder(Arrays.asList(mock), inOrderContext);
	}

	@Test
	public void testVerifyNoMoreInteractionsInOrder_Null() {
		verifyNoMoreInteractionsInOrder(null, inOrderContext);
	}

	@Test
	public void testVerifyNoMoreInteractionsInOrder_EmptyList() {
		verifyNoMoreInteractionsInOrder(Collections.emptyList(), inOrderContext);
	}

	@Test
	public void testVerifyNoMoreInteractionsInOrder_EmptyArray() {
		verifyNoMoreInteractionsInOrder(new Object[0], inOrderContext);
	}

	@Test
	public void testVerifyZeroInteractions() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		verifyZeroInteractions(mock);
		verifyZeroInteractions(mock);
	}

	@Test(expected = NullPointerException.class)
	public void testVerifyZeroInteractions_Null() {
		verifyZeroInteractions(null);
	}

	@Test
	public void testReset() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		mockitoCore.when(mock).foo().thenReturn("foo");
		reset(mock);
		verifyZeroInteractions(mock);
	}

	@Test(expected = NullPointerException.class)
	public void testReset_Null() {
		reset(null);
	}

	@Test
	public void testVerifyInteractions() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		mockitoCore.when(mock).foo().thenReturn("foo");
		verify(mock, VerificationModeFactory.verifyInteractions(VerificationModeFactory.atLeastOnce()));
	}

	@Test
	public void testVerifyInteractions_Null() {
		verify(mock, null);
	}

	@Test
	public void testVerifyInteractions_EmptyArray() {
		verify(mock, VerificationModeFactory.verifyInteractions(new VerificationMode[0]));
	}

	@Test
	public void testVerifyInteractions_EmptyList() {
		verifyInteractions(mock, Arrays.asList(VerificationModeFactory.atLeastOnce()));
	}

	@Test
	public void testInOrder() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		mockitoCore.when(mock).foo().thenReturn("foo");
		mockitoCore.when(mock).bar().thenReturn("bar");
		mockitoCore.when(mock).baz().thenReturn("baz");
		inOrderContext = inOrder(mock);
		assertEquals(mock, inOrderContext.inOrder().getIndex(0));
		assertEquals("foo", inOrderContext.inOrder().getInvocations().get(0).getMethod().getName());
		assertEquals("bar", inOrderContext.inOrder().getInvocations().get(1).getMethod().getName());
		assertEquals("baz", inOrderContext.inOrder().getInvocations().get(2).getMethod().getName());
	}

	@Test(expected = NullPointerException.class)
	public void testInOrder_Null() {
		inOrder(null);
	}

	@Test
	public void testInOrder_EmptyList() {
		inOrder(Collections.emptyList());
	}

	@Test
	public void testDoAnswer() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		Answer<String> answer = invocation -> invocation.getArgumentAt(0, String.class);
		mockitoCore.doAnswer(answer).when(mock).foo();
		assertEquals("foo", mockitoCore.verify(mock).foo().getArgumentAt(0));
	}

	@Test(expected = NullPointerException.class)
	public void testDoAnswer_Null() {
		doAnswer(null);
	}

	@Test
	public void testDoAnswer_EmptyList() {
		Answer<String> answer = invocation -> invocation.getArgumentAt(0, String.class);
		doAnswer(answer);
	}

	@Test
	public void testDoAnswer_EmptyArray() {
		Answer<String> answer = invocation -> invocation.getArgumentAt(0, String.class);
		doAnswer(answer);
	}

	@Test
	public void testVoidMethodStubbable() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		VoidMethodStubbable<String> voidMethodStubbable = mockitoCore.stubVoid(mock);
		verify(mock).foo();
		verifyNoMoreInteractions(mock);
		voidMethodStubbable.answerVoid();
		verify(mock).foo();
		verify(mock).foo();
		verifyNoMoreInteractions(mock);
	}

	@Test
	public void testValidateMockitoUsage() {
		mockitoCore.validateMockitoUsage();
	}

	@Test(expected = IllegalStateException.class)
	public void testValidateMockitoUsage_StubbingAlreadyStarted() {
		mockitoCore.stub();
		mockitoCore.validateMockitoUsage();
	}

	@Test(expected = IllegalStateException.class)
	public void testValidateMockitoUsage_VerificationAlreadyStarted() {
		mockitoCore.verify(mock, VerificationModeFactory.verifyNoMoreInteractions());
		mockitoCore.validateMockitoUsage();
	}

	@Test
	public void testValidateMockitoUsage_NoInteractions() {
		mockitoCore.validateMockitoUsage();
	}

	@Test
	public void testGetLastInvocation() {
		mock = mockitoCore.mock(String.class, new MockSettingsImpl());
		mockitoCore.when(mock).foo().thenReturn("foo");
		mockitoCore.when(mock).bar().thenReturn("bar");
		ongoingStubbing = mockitoCore.when(mock).baz();
		assertEquals("baz", ongoingStubbing.getMethod().getName());
		reset(mock);
		verifyZeroInteractions(mock);
		assertEquals(ongoingStubbing, mockitoCore.getLastInvocation());
	}

	@Test
	public void testGetLastInvocation_Null() {
		assertEquals(null, mockitoCore.getLastInvocation());
	}

	private void verifyInteractions(Object mock, List<VerificationMode> verificationModes) {
		VerificationDataInOrder data = new VerificationDataInOrderImpl(inOrderContext, Arrays.asList(mock), null);
		for (VerificationMode verificationMode : verificationModes) {
			try {
				VerificationModeFactory.verify(data, verificationMode);
			} catch (NotAMockException e) {
				reporter.notAMockPassedToVerify(verificationMode, data.getMockArray()[0]);
			}
		}
	}

}