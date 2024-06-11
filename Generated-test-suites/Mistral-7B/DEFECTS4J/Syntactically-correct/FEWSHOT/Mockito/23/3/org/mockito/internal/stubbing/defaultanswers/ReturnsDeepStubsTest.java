package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.MockSettings;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationContainerImpl;
import org.mockito.internal.stubbing.AnswerWithValue;
import org.mockito.internal.stubbing.InvocationMatcher;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerImpl;

public class ReturnsDeepStubsTest {

	@Test
	public void testAnswerWithEmptyValue() {
		ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
		assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);
	}

	@Test
	public void testAnswerWithMatchingInvocation() throws Exception {
		MockSettings mockSettings = mockSettings().defaultAnswer(new AnswerImpl<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return "mock answer";
			}
		});
		Object mock = mock(Object.class, mockSettings);
		InvocationContainerImpl container = (InvocationContainerImpl) Mockito.getMockHandler(mock).getInvocationContainer();
		stub(mock).method(eq("method"), any());
		assertEquals("mock answer", container.getMock().method("method").invoke(mock, "input"));
	}

	@Test
	public void testAnswerWithDeepStub() throws Exception {
		MockitoCore mockitoCore = new MockitoCore();
		InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock(Object.class).getMock());
		InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

		MockSettings mockSettings = withSettingsUsing(new GenericMetadataSupport() {
			@Override
			public Type getGenericSuperclass() {
				return Map.class;
			}

			@Override
			public Type[] getActualTypeArguments() {
				return new Type[] { String.class, Set.class };
			}

			@Override
			public Type[] getRawTypeArguments() {
				return new Type[] { Map.class, Set.class };
			}

			@Override
			public Type getDeclaringClass() {
				return GenericsNest.class;
			}

			@Override
			public List<Class<?>> getRawExtraInterfaces() {
				return Collections.singletonList(Number.class);
			}
		});
		Answer<Object> answer = new ReturnsDeepStubs();
		mockitoCore.mock(Map.class, withSettingsUsing(answer));

		assertEquals(answer.getMock(new InvocationOnMock(mock, new Method(null, "method", new Class[0]))), answer.getMock(new InvocationOnMock(mock, new Method(null, "method", new Class[0]))));
	}

	@Test
	public void testAnswerWithDeepStub_whenInvocationMatchesStubbedInvocation() throws Exception {
		MockitoCore mockitoCore = new MockitoCore();
		Object mock = mock(Object.class, new ReturnsDeepStubs());
		InvocationContainerImpl container = (InvocationContainerImpl) Mockito.getMockHandler(mock).getInvocationContainer();
		stub(mock).method(eq("method"), any());

		Object stubbedMock = mockitoCore.mock(Object.class, new ReturnsDeepStubs());
		container.addAnswer(new AnswerWithValue<Object>(stubbedMock), false);

		assertEquals(stubbedMock, container.getMock().method("method").invoke(mock, "input"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAnswerWithIllegalArgument() throws Exception {
		MockitoCore mockitoCore = new MockitoCore();
		mockitoCore.mock(Object.class, new ReturnsDeepStubs());
	}

	private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
		MockSettings mockSettings = returnTypeGenericMetadata.rawExtraInterfaces().length > 0 ?
				withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()) : withSettings();

		return mockSettings
				.defaultAnswer(new ReturnsDeepStubs().returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
	}

	private ReturnsDeepStubs returnsDeepStubsAnswerUsing(final GenericMetadataSupport returnTypeGenericMetadata) {
		return new ReturnsDeepStubs() {
			@Override
			protected GenericMetadataSupport actualParameterizedType(Object mock) {
				return returnTypeGenericMetadata;
			}
		};
	}
}