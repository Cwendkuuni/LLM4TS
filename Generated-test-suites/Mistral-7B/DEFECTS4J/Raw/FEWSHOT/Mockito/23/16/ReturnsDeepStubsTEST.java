package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoCore;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.mock.MockHandler;
import org.mockito.internal.reflection.GenericMetadataSupport;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerWith;

public class ReturnsDeepStubsTest {

	@Test
	public void testAnswerWithDeepStubsInstance() {
		AnswerWith<Object> answerWithDeepStubsInstance = new AnswerWith<Object>() {
			@Override
			protected Answer<Object> answerInstance() {
				return new ReturnsDeepStubs();
			}
		};
		Object mock = mock(Object.class, answerWithDeepStubsInstance);

		assertEquals(mock, mock.hashCode());
		assertEquals(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)));
	}

	@Test
	public void testGetMockWithSimpleClass() {
		Object mock = mock(String.class, new ReturnsDeepStubs());
		assertEquals(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)));
	}

	@Test
	public void testGetMockWithInterface() {
		interface MyInterface extends Comparable<MyInterface> {
		}

		MyInterface mock = mock(MyInterface.class, new ReturnsDeepStubs());
		assertEquals(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)));
	}

	@Test
	public void testGetMockWithClassWithExtraInterfaces() {
		interface MyClassWithExtraInterfaces extends Comparable<MyClassWithExtraInterfaces>, Cloneable {
			Set<Number> getNumbers();
		}

		MyClassWithExtraInterfaces mock = mock(MyClassWithExtraInterfaces.class, new ReturnsDeepStubs());
		assertNotSame(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)).getNumbers());
	}

	@Test
	public void testGetMockWithGenericClass() {
		interface Generics<T> {
			T getT();
		}

		Generics<String> mock = mock(Generics.class, new ReturnsDeepStubs());
		assertEquals(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)));
		assertEquals("", mock.getT());
	}

	@Test
	public void testGetMockWithGenericClassWithExtraInterfaces() {
		interface GenericsWithExtraInterfaces<T> extends Comparable<GenericsWithExtraInterfaces<T>> {
			T getT();
			Map<T, Set<Number>> getMap();
		}

		GenericsWithExtraInterfaces<String> mock = mock(GenericsWithExtraInterfaces.class, new ReturnsDeepStubs());
		assertNotSame(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)).getMap());
		assertEquals("", mock.getT());
		assertEquals(Collections.emptyMap(), mock.getMap());
	}

	@Test
	public void testGetMockWithNestedGenericClass() {
		interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
		}

		GenericsNest<? extends Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
		assertNotSame(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)).entrySet().iterator()
				.next().getValue().iterator().next());
		assertNull(mock.entrySet().iterator().next().getValue().iterator().next());
	}

	@Test
	public void testGetMockWithInterfaceAndNestedGenericClass() {
		interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
			Number getNumber();
		}

		GenericsNest<? extends Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
		assertNotSame(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)).getNumber());
		assertNull(mock.getNumber());
		assertEquals(Collections.emptyMap(), mock.entrySet().iterator().next().getValue());
	}

	@Test
	public void testGetMockWithGenericClassWithNestedGenerics() {
		interface GenericsWithNestedGenerics<K extends Comparable<K> & Cloneable> extends Map<K, List<GenericsWithNestedGenerics<Number>>> {
		}

		GenericsWithNestedGenerics<String> mock = mock(GenericsWithNestedGenerics.class, new ReturnsDeepStubs());
		GenericsWithNestedGenerics<Integer> nestedMock = mock(GenericsWithNestedGenerics.class, new ReturnsDeepStubs());
		mock.put(mock, Arrays.asList(nestedMock));

		assertNotSame(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)).get(mock));
		assertEquals(Collections.singletonList(nestedMock), mock.get(mock));
		assertNotSame(nestedMock, nestedMock.getClass().cast(nestedMock.getClass().getMethod("deepStub").invoke(nestedMock))
				.get(mock));
		assertEquals(Collections.singletonList(nestedMock), nestedMock.get(mock));
	}

	@Test
	public void testGetMockWithInterfaceAndNestedGenericClassWithExtraInterfaces() {
		interface GenericsWithNestedInterfaces<K extends Comparable<K> & Cloneable> extends Map<K, List<GenericsWithNestedInterfaces<Number>>> {
			Number getNumber();
			Map<Number, Number> getMap();
		}

		GenericsWithNestedInterfaces<String> mock = mock(GenericsWithNestedInterfaces.class, new ReturnsDeepStubs());
		GenericsWithNestedInterfaces<Integer> nestedMock = mock(GenericsWithNestedInterfaces.class, new ReturnsDeepStubs());
		mock.put(mock, Arrays.asList(nestedMock));

		assertNotSame(mock, mock.getClass().cast(mock.getClass().getMethod("deepStub").invoke(mock)).get(mock));
		assertEquals(Collections.singletonList(nestedMock), mock.get(mock));
		assertNotSame(nestedMock, nestedMock.getClass().cast(nestedMock.getClass().getMethod("deepStub").invoke(nestedMock))
				.get(mock));
		assertEquals(Collections.singletonList(nestedMock), nestedMock.get(mock));
		assertEquals(Collections.emptyMap(), nestedMock.getMap());
	}

	@Test
	public void testDeepStubWithInvocationMatcher() {
		interface GenericsWithInvocationMatcher<K extends Comparable<K> & Cloneable> extends Map<K, List<GenericsWithInvocationMatcher<Number>>> {
			Number getNumber();
			Map<Number, Number> getMap();
			List<Number> getList();
		}

		GenericsWithInvocationMatcher<String> mock = mock(GenericsWithInvocationMatcher.class, new ReturnsDeepStubs());
		GenericsWithInvocationMatcher<Integer> nestedMock1 = mock(GenericsWithInvocationMatcher.class, new ReturnsDeepStubs());
		GenericsWithInvocationMatcher<Integer> nestedMock2 = mock(GenericsWithInvocationMatcher.class, new ReturnsDeepStubs());

		when(mock.getNumber()).thenReturn(1);
		when(mock.getMap()).thenReturn(Collections.singletonMap(1, nestedMock1));
		when(nestedMock1.getNumber()).thenReturn(2);
		when(nestedMock1.getList()).thenReturn(Arrays.asList(nestedMock2));
		when(nestedMock2.getNumber()).thenReturn(3);

		GenericsWithInvocationMatcher<String> deepStub = mockitoCore.mock(GenericsWithInvocationMatcher.class);
		InvocationContainerImpl container = (InvocationContainerImpl) mockitoCore.getMockHandler(mock).getInvocationContainer();
		StubbedInvocationMatcher stubbedInvocationMatcher = container.getStubbedInvocations()[0];

		assertEquals(mock, deepStub);

		assertEquals(1, deepStub.getNumber());
		assertEquals(Collections.singletonMap(1, nestedMock1), deepStub.getMap());
		assertEquals(Arrays.asList(nestedMock2), deepStub.getList());
		assertEquals(2, nestedMock1.getNumber());
		assertEquals(Collections.singletonList(nestedMock2), nestedMock1.getList());
		assertEquals(3, nestedMock2.getNumber());
	}

	@Test
	public void testDeepStubWithAnotherMockHandler() {
		interface GenericsWithAnotherMockHandler<K extends Comparable<K> & Cloneable> extends Map<K, List<GenericsWithAnotherMockHandler<Number>>> {
			Number getNumber();
			Map<Number, Number> getMap();
			List<Number> getList();
		}

		GenericsWithAnotherMockHandler<String> mock = mock(GenericsWithAnotherMockHandler.class, new ReturnsDeepStubs(), new MockSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
		GenericsWithAnotherMockHandler<Integer> nestedMock1 = mock(GenericsWithAnotherMockHandler.class, new ReturnsDeepStubs(), new MockSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
		GenericsWithAnotherMockHandler<Integer> nestedMock2 = mock(GenericsWithAnotherMockHandler.class, new ReturnsDeepStubs(), new MockSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

		when(mock.getNumber()).thenReturn(1);
		when(mock.getMap()).thenReturn(Collections.singletonMap(1, nestedMock1));
		when(nestedMock1.getNumber()).thenReturn(2);
		when(nestedMock1.getList()).thenReturn(Arrays.asList(nestedMock2));
		when(nestedMock2.getNumber()).thenReturn(3);

		GenericsWithAnotherMockHandler<String> deepStub = mockitoCore.mock(GenericsWithAnotherMockHandler.class, new MockSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
		InvocationContainerImpl container = (InvocationContainerImpl) mockitoCore.getMockHandler(mock).getInvocationContainer();
		MockHandler<GenericsWithAnotherMockHandler<Integer>> anotherMockHandler = mockitoCore.mock(GenericsWithAnotherMockHandler.class, new MockSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
		container.setMockHandler(GenericsWithAnotherMockHandler.class, anotherMockHandler);
		StubbedInvocationMatcher stubbedInvocationMatcher = container.getStubbedInvocations()[0];

		assertEquals(mock, deepStub);

		assertEquals(1, deepStub.getNumber());
		assertEquals(Collections.singletonMap(1, nestedMock1), deepStub.getMap());
		assertEquals(Arrays.asList(nestedMock2), deepStub.getList());
		assertEquals(2, nestedMock1.getNumber());
		assertEquals(Collections.singletonList(nestedMock2), nestedMock1.getList());
		assertEquals(3, nestedMock2.getNumber());
	}
}