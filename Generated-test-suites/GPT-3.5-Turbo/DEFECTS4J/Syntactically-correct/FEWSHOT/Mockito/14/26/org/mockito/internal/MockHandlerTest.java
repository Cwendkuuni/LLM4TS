package org.mockito.internal;


import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;


@SuppressWarnings("unchecked")

public class MockHandlerTest {
	
	@Test
	public void testHandle() throws Throwable {
		MockSettingsImpl mockSettings = new MockSettingsImpl();
		MockHandler mockHandler = new MockHandler(mockSettings);
		
		Invocation invocation = new Invocation();
		Object result = mockHandler.handle(invocation);
		
		// Add assertions here
		
	}
	
	@Test
	public void testVoidMethodStubbable() {
		MockSettingsImpl mockSettings = new MockSettingsImpl();
		MockHandler mockHandler = new MockHandler(mockSettings);
		Object mock = new Object();
		
		VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
		
		// Add assertions here
		
	}
	
	@Test
	public void testGetMockSettings() {
		MockSettingsImpl mockSettings = new MockSettingsImpl();
		MockHandler mockHandler = new MockHandler(mockSettings);
		
		MockSettingsImpl result = mockHandler.getMockSettings();
		
		// Add assertions here
		
	}
	
	@Test
	public void testSetAnswersForStubbing() {
		MockHandler mockHandler = new MockHandler();
		List answers = new ArrayList<>();
		
		mockHandler.setAnswersForStubbing(answers);
		
		// Add assertions here
		
	}
	
	@Test
	public void testGetInvocationContainer() {
		MockSettingsImpl mockSettings = new MockSettingsImpl();
		MockHandler mockHandler = new MockHandler(mockSettings);
		
		InvocationContainer result = mockHandler.getInvocationContainer();
		
		// Add assertions here
		
	}
	
}