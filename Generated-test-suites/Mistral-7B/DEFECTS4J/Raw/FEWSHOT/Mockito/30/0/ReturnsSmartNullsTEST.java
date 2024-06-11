package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.stubbing.answers.ReturnsMoreEmptyValues;
import org.mockito.internal.stubbing.answers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

	@Test
	public void testAnswerNull() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		Method method = getMethod(Object.class, "toString");
		when(target.toString()).thenAnswer(returnsSmartNulls);

		assertNull(target.toString());
		verify(target).toString();
	}

	@Test
	public void testAnswerSmartNull() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		Method method = getMethod(Object.class, "toString");
		when(target.toString()).thenAnswer(returnsSmartNulls);

		String result = (String) target.toString();
		assertNotNull(result);
		assertEquals("SmartNull returned by unstubbed toString method on mock", result);
		verify(target).toString();
	}

	@Test
	public void testDelegateAnswer() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		Method method = getMethod(Object.class, "toString");
		when(target.toString()).thenAnswer(new ReturnsMoreEmptyValues());

		String result = (String) target.toString();
		assertNull(result);
		verify(target).toString();
	}

	@Test
	public void testDelegateAnswerNull() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		Method method = getMethod(Object.class, "toString");
		when(target.toString()).thenAnswer(null);

		String result = (String) target.toString();
		assertNull(result);
		verify(target).toString();
	}

	@Test(expected = NullPointerException.class)
	public void testDelegateAnswerNpe() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		Method method = getMethod(Object.class, "toString");
		when(target.toString()).thenAnswer(new ReturnsMoreEmptyValues());

		target.toString();
	}

	@Test
	public void testDelegateAnswerSmartNull() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		Method method = getMethod(Object.class, "toString");
		when(target.toString()).thenAnswer(new ReturnsMoreEmptyValues() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return "empty string";
			}
		});

		String result = (String) returnsSmartNulls.answer(new InvocationOnMock(target, method, new Object[0], new Object[0]));
		assertNotNull(result);
		assertEquals("empty string", result);
	}

	@Test
	public void testImposterisedAnswer() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		Method method = getMethod(Object.class, "toString");

		Class<?> clazz = ClassImposterizer.class;
		Class<?> imposterisedClass = ClassImposterizer.INSTANCE.imposterise(ReturnsSmartNulls.class, clazz);
		Object imposterisedInstance = ClassImposterizer.INSTANCE.newInstance(imposterisedClass);
		Method imposterisedMethod = getMethod(imposterisedClass, "answer");

		Object[] arguments = new Object[] { new InvocationOnMock(target, method, new Object[0], new Object[0]) };
		Object result = imposterisedMethod.invoke(imposterisedInstance, arguments);

		assertNull(result);
		verify(target).toString();
	}

	@Test
	public void testImposterisedAnswerSmartNull() {
		ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
		Object target = mock(Object.class);
		Method method = getMethod(Object.class, "toString");

		Class<?> clazz = ClassImposterizer.class;
		Class<?> imposterisedClass = ClassImposterizer.INSTANCE.imposterise(ReturnsSmartNulls.class, clazz);
		Object imposterisedInstance = ClassImposterizer.INSTANCE.newInstance(imposterisedClass);
		Method imposterisedMethod = getMethod(imposterisedClass, "answer");

		Object[] arguments = new Object[] { new InvocationOnMock(target, method, new Object[0], new Object[0]) };
		Object result = imposterisedMethod.invoke(imposterisedInstance, arguments);

		assertNotNull(result);
		assertEquals("SmartNull returned by unstubbed toString method on mock", result.toString());
		verify(target).toString();
	}

	private Method getMethod(Class<?> clazz, String methodName) {
		try {
			return clazz.getMethod(methodName);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}