package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.AnswerWithMock;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerWithDeepStub;

public class ReturnsDeepStubsTest {

	@Test
	public void testGetMock() {
		Class<?> rawType = String.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(rawType);
		InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
		InvocationContainerImpl container = new InvocationContainerImpl(handler, rawType);

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object mock = returnsDeepStubs.getMock(mock(InvocationOnMock.class), returnTypeGenericMetadata);
		assertNotNull(mock);
		verify(handler).getInvocationContainer();
		verify(handler).addAnswer(argThat(invocation -> invocation.getMock() == mock));
	}

	@Test
	public void testGetMock_withStubbedInvocationMatcher() {
		Class<?> rawType = String.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(rawType);
		InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
		InvocationContainerImpl container = new InvocationContainerImpl(handler, rawType);

		StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
		when(stubbedInvocationMatcher.getInvocation()).thenReturn(new InvocationOnMock(null, null, null, null, null, null));
		container.addStubbedInvocation(stubbedInvocationMatcher);

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object mock = returnsDeepStubs.getMock(mock(InvocationOnMock.class), returnTypeGenericMetadata);
		assertNotNull(mock);
		verify(handler).getInvocationContainer();
		verify(stubbedInvocationMatcher).answer(argThat(invocation -> invocation.getMock() == mock));
	}

	@Test
	public void testAnswer() throws Throwable {
		Class<?> rawType = String.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(rawType);
		InvocationOnMock invocation = mock(InvocationOnMock.class);

		InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
		InvocationContainerImpl container = new InvocationContainerImpl(handler, rawType);

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Answer<Object> answer = new AnswerWithMock<Object>() {
			@Override
			protected Object mock() {
				return mock(String.class);
			}
		};
		AnswerWithDeepStub answerWithDeepStub = returnsDeepStubs.answer(invocation);

		Object mock = answerWithDeepStub.answer(invocation);
		assertEquals(answer.mock().getClass(), mock.getClass());
		verify(handler).getInvocationContainer();
		verify(container).addAnswer(answer);
	}

	@Test
	public void testAnswer_withRecordDeepStubMock() throws Throwable {
		Class<?> rawType = String.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(rawType);
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		InvocationContainerImpl container = new InvocationContainerImpl(null, rawType);

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object mock = returnsDeepStubs.recordDeepStubMock(mock(Object.class), container);
		assertEquals(mock.getClass(), String.class);
		verify(container).addAnswer(argThat(answer -> answer instanceof AnswerWithMock));
	}

	@Test
	public void testAnswer_withRecordDeepStubMock_whenMockIsAlreadyStubbed() throws Throwable {
		Class<?> rawType = String.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(rawType);
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		InvocationContainerImpl container = new InvocationContainerImpl(null, rawType);
		Object mock = mock(String.class);
		container.addAnswer(new AnswerWithMock<Object>() {
			@Override
			protected Object mock() {
				return mock;
			}
		});

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object deepStubMock = returnsDeepStubs.recordDeepStubMock(mock, container);
		assertEquals(mock, deepStubMock);
		verifyNoMoreInteractions(container);
	}

	@Test
	public void testGetMock_whenInvocationDoesNotMatchAnyStubbedInvocation() throws Throwable {
		Class<?> rawType = String.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(rawType);
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		InvocationContainerImpl container = new InvocationContainerImpl(null, rawType);

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object mock = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
		assertNull(mock);
		verifyNoMoreInteractions(container);
	}

	@Test
	public void testAnswer_withRecordDeepStubMock_whenInvocationDoesNotMatchAnyStubbedInvocation() throws Throwable {
		Class<?> rawType = String.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(rawType);
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		InvocationContainerImpl container = new InvocationContainerImpl(null, rawType);

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object deepStubMock = returnsDeepStubs.recordDeepStubMock(null, container);
		assertNull(deepStubMock);
		verifyNoMoreInteractions(container);
	}

	@Test
	public void testAnswer_whenReturnTypeIsNotMockable() throws Throwable {
		Class<?> rawType = Object[].class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(rawType);

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object answer = returnsDeepStubs.answer(mock(InvocationOnMock.class));
		assertEquals(new ReturnsEmptyValues().returnValueFor(rawType), answer);
	}

	@Test
	public void testAnswer_whenReturnTypeIsAnInterface() throws Throwable {
		Class<?> rawType = List.class;
		Class<? extends List> listType = List.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(listType);

		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Arrays.class.getMethod("asList", Object[].class));

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object answer = returnsDeepStubs.answer(invocation);
		assertEquals(mock(List.class), answer);
		verify(invocation.getMock()).getClass();
		verify(invocation.getMethod()).getReturnType();
	}

	@Test
	public void testAnswer_whenReturnTypeHasGenericTypeArguments() throws Throwable {
		Class<?> rawType = Map.class;
		Class<? extends Map> mapType = Map.class;
		Class<?> keyType = String.class;
		Class<?> valueType = Integer.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(mapType).addTypeArguments(keyType, valueType);

		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Arrays.class.getMethod("asMap", Object[].class, Object[].class));

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object answer = returnsDeepStubs.answer(invocation);
		assertEquals(mock(Map.class, keyType, valueType), answer);
		verify(invocation.getMock()).getClass();
		verify(invocation.getMethod()).getReturnType();
	}

	@Test
	public void testAnswer_whenReturnTypeHasGenericTypeArgumentsAndIsAnInterface() throws Throwable {
		Class<?> rawType = GenericsNest.class;
		Class<? extends GenericsNest> genericsNestType = GenericsNest.class;
		Class<?> keyType = String.class;
		Class<?> valueType = Integer[].class;
		Class<?> numberType = Number.class;
		Class<?> comparableType = Comparable.class;
		Class<?> cloneableType = Cloneable.class;
		GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(genericsNestType)
				.addTypeArguments(Map.class, keyType, Set.class, numberType, Comparable.class, Cloneable.class);

		InvocationOnMock invocation = mock(InvocationOnMock.class);
		when(invocation.getMethod()).thenReturn(Arrays.class.getMethod("asMap", Object[].class, Object[].class));

		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		Object answer = returnsDeepStubs.answer(invocation);
		assertEquals(mock(GenericsNest.class, Map.class, keyType, Set.class, numberType, Comparable.class, Cloneable.class), answer);
		verify(invocation.getMock()).getClass();
		verify(invocation.getMethod()).getReturnType();
	}
}