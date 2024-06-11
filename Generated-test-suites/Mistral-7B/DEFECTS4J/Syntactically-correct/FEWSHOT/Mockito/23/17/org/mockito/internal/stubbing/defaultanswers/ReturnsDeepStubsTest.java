package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mockito.Answers;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.mock.MockSettings;
import org.mockito.internal.stubbing.AnswerInvocation;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

	private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
	private static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

	@Test
	public void testAnswer() throws Exception {
		MockitoCore mockitoCore = spy(new MockitoCore());
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
			@Override
			protected MockitoCore mockitoCore = mockitoCore;
		};

		Class<?> rawType = Integer.class;
		GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
		when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
		when(returnTypeGenericMetadata.resolveGenericReturnType(any())).thenReturn(returnTypeGenericMetadata);

		Answer<Object> answer = returnsDeepStubs;

		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMock()).thenReturn(mock(Map.class));
		when(invocation.getMethod()).thenReturn(getMethod(Map.class, "entrySet"));

		Object result = answer.answer(invocation);
		assertSame(mock(Map.class), result);
	}

	@Test
	public void testGetMock() throws Exception {
		MockitoCore mockitoCore = spy(new MockitoCore());
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
			@Override
			protected MockitoCore mockitoCore = mockitoCore;
		};

		Class<?> rawType = Integer.class;
		GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
		when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
		when(returnTypeGenericMetadata.resolveGenericReturnType(any())).thenReturn(returnTypeGenericMetadata);

		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMock()).thenReturn(mock(Map.class));
		when(invocation.getMethod()).thenReturn(getMethod(Map.class, "entrySet"));

		InvocationContainer invocationContainer = mock(InvocationContainerImpl.class);
		when(invocationContainer.getStubbedInvocations()).thenReturn(Arrays.asList(createStubbedInvocationMatcher(invocation)));
		when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

		Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
		assertSame(invocationContainer, result);
	}

	@Test
	public void testCreateNewDeepStubMock() throws Exception {
		MockitoCore mockitoCore = spy(new MockitoCore());
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
			@Override
			protected MockitoCore mockitoCore = mockitoCore;
		};

		GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
		when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
		when(returnTypeGenericMetadata.resolveGenericReturnType(any())).thenReturn(returnTypeGenericMetadata);

		Object mock = mock(Map.class, withSettings().defaultAnswer(returnsDeepStubs));
		assertEquals(mockitoCore, returnsDeepStubs.mockitoCore);
		assertEquals(returnTypeGenericMetadata, returnsDeepStubs.actualParameterizedType(mock));
	}

	@Test
	public void testWithSettingsUsing() throws Exception {
		MockitoCore mockitoCore = spy(new MockitoCore());
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
			@Override
			protected MockitoCore mockitoCore = mockitoCore;
		};

		Class<?> rawType = Map.class;
		MockSettings mockSettings = mock(MockSettings.class);
		when(mockSettings.getTypeToMock()).thenReturn(rawType);
		when(mockSettings.getDefaultAnswer()).thenReturn(Answers.RETURNS_DEEP_STUBS);

		GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
		when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
		when(returnTypeGenericMetadata.resolveGenericReturnType(any())).thenReturn(returnTypeGenericMetadata);

		ReturnsDeepStubs deepStubs = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
		assertEquals(returnTypeGenericMetadata, deepStubs.actualParameterizedType(mockitoCore.mock(rawType())));
	}

	@Test
	public void testRecordDeepStubMock() throws Exception {
		MockitoCore mockitoCore = spy(new MockitoCore());
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
			@Override
			protected MockitoCore mockitoCore = mockitoCore;
		};

		Class<?> rawType = Map.class;
		MockSettings mockSettings = mock(MockSettings.class);
		when(mockSettings.getTypeToMock()).thenReturn(rawType);
		when(mockSettings.getDefaultAnswer()).thenReturn(Answers.RETURNS_DEEP_STUBS);

		InvocationContainer invocationContainer = mock(InvocationContainerImpl.class);
		when(invocationContainer.addAnswer(any(), anyBoolean())).thenAnswer(invocation -> invocation.getArguments()[0]);

		Object mock = mockitoCore.mock(rawType, withSettings(mockSettings));
		assertEquals(mockitoCore, returnsDeepStubs.mockitoCore);
		assertEquals(mock, returnsDeepStubs.recordDeepStubMock(mock, invocationContainer));
	}

	@Test
	public void testAnswerWithStubbedInvocation() throws Exception {
		MockitoCore mockitoCore = spy(new MockitoCore());
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
			@Override
			protected MockitoCore mockitoCore = mockitoCore;
		};

		Class<?> rawType = Map.class;
		MockSettings mockSettings = mock(MockSettings.class);
		when(mockSettings.getTypeToMock()).thenReturn(rawType);
		when(mockSettings.getDefaultAnswer()).thenReturn(Answers.RETURNS_DEEP_STUBS);

		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMock()).thenReturn(mockitoCore.mock(rawType(), withSettings(mockSettings)));
		when(invocation.getMethod()).thenReturn(getMethod(rawType(), "toString"));

		InvocationContainer invocationContainer = mock(InvocationContainerImpl.class);
		when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);
		when(invocationContainer.getStubbedInvocations()).thenReturn(Collections.singletonList(createStubbedInvocationMatcher(invocation)));

		ReturnsDeepStubs deepStubs = returnsDeepStubs.withSettingsUsing(mock(GenericMetadataSupport.class));
		Answer<Object> answer = deepStubs;

		Object result = answer.answer(invocation);
		assertSame(invocationContainer, result);
	}

	@Test
	public void testAnswerWithNullInvocationContainer() throws Exception {
		MockitoCore mockitoCore = spy(new MockitoCore());
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
			@Override
			protected MockitoCore mockitoCore = mockitoCore;
		};
		Answer<Object> answer = returnsDeepStubs;

		assertNull(answer.answer(null));
	}

	private InvocationMatcher createStubbedInvocationMatcher(InvocationOnMock invocation) {
		return new StubbedInvocationMatcher(invocation, new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArguments()[0];
			}
		});
	}

	private Method getMethod(Class<?> clazz, String methodName) throws SecurityException, NoSuchMethodException {
		return clazz.getMethod(methodName);
	}
}