package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

	@Test
	public void testAnswerWithNotNullValue() {
		ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
		Object mock = mock(String.class);
		when(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(mock);

		assertNotNull(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class)));
	}

	@Test
	public void testAnswerWithNullValueAndMockableType() {
		ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
		String mock = mock(String.class);

		Class<?> stringClass = String.class;
		Object smartNull = ReturnsSmartNulls.ClassImposterizer.INSTANCE.imposterise(new ReturnsSmartNulls.ThrowingInterceptor(null), stringClass);

		assertNull(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class)));
		verify(smartNull).toString();
	}

	@Test(expected = IllegalStateException.class)
	public void testAnswerWithNullValueAndNonMockableType() {
		ReturnsSmartNulls returnsSmartNulls = mock(ReturnsSmartNulls.class);
		final Class<?> finalClass = int.class;

		assertNull(returnsSmartNulls.answer(ArgumentMatchers.any(InvocationOnMock.class)));
	}

	@Test
	public void testThrowingInterceptorToStringMethod() {
		ReturnsSmartNulls returnsSmartNulls = spy(new ReturnsSmartNulls());
		InvocationOnMock invocation = mock(InvocationOnMock.class);
		Method method = mock(Method.class);
		Object[] args = new Object[0];
		MethodProxy proxy = mock(MethodProxy.class);

		String expected = "SmartNull returned by unstubbed add(java.lang.Integer, java.lang.Integer) method on mock";
		String result = returnsSmartNulls.new ThrowingInterceptor(invocation).intercept(null, method, args, proxy).toString();

		assertEquals(expected, result);
	}
}