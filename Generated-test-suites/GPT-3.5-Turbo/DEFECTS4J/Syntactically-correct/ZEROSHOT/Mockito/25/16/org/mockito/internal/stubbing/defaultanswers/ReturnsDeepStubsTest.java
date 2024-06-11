package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

	@Test
	public void testAnswer() throws Throwable {
		// Setup
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		InvocationOnMock invocationMock = mock(InvocationOnMock.class);

		// Run
		Object result = returnsDeepStubs.answer(invocationMock);

		// Verify
		assertNotNull(result);
	}

	@Test
	public void testGetMock() throws Throwable {
		// Setup
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		InvocationOnMock invocationMock = mock(InvocationOnMock.class);

		// Run
		Object result = returnsDeepStubs.getMock(invocationMock);

		// Verify
		assertNotNull(result);
	}

	@Test
	public void testRecordDeepStubMock() throws Throwable {
		// Setup
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		InvocationOnMock invocationMock = mock(InvocationOnMock.class);
		InvocationContainerImpl containerMock = mock(InvocationContainerImpl.class);
		when(invocationMock.getMethod().getReturnType()).thenReturn(Object.class);
		when(invocationMock.getMethod()).thenReturn(Object.class.getMethod("toString"));
		when(containerMock.getInvocationForStubbing().matches(invocationMock)).thenReturn(true);
		when(containerMock.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{mock(StubbedInvocationMatcher.class)});
		when(containerMock.getInvocationForStubbing().matches(any())).thenReturn(true);

		// Run
		Object result = returnsDeepStubs.recordDeepStubMock(invocationMock, containerMock);

		// Verify
		assertNotNull(result);
	}

	@Test
	public void testActualParameterizedType() {
		// Setup
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		MockUtil mockUtilMock = mock(MockUtil.class);
		CreationSettings mockSettings = mock(CreationSettings.class);
		when(mockSettings.getTypeToMock()).thenReturn(Object.class);
		when(mockUtilMock.getMockHandler(any())).thenReturn(mock(InternalMockHandler.class)).thenReturn(mock(InternalMockHandler.class));
		when(mockUtilMock.getMockHandler(any()).getMockSettings()).thenReturn(mockSettings);

		// Run
		GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock(Object.class));

		// Verify
		assertNotNull(result);
	}

}