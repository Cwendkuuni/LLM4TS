import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

	@Test
	public void testInvocationMatcher() {
		Invocation invocation = mock(Invocation.class);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
		assertEquals(invocation, invocationMatcher.getInvocation());
		assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
	}

	@Test
	public void testInvocationMatcherWithMatchers() {
		Invocation invocation = mock(Invocation.class);
		Matcher matcher = mock(Matcher.class);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));
		assertEquals(invocation, invocationMatcher.getInvocation());
		assertTrue(invocationMatcher.getMatchers().contains(matcher));
	}

	@Test
	public void testGetMethod() {
		Invocation invocation = mock(Invocation.class);
		Method method = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
		assertEquals(method, invocationMatcher.getMethod());
	}

	@Test
	public void testToString() {
		Invocation invocation = mock(Invocation.class);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
		String toStringResult = invocation.toString(Collections.emptyList(), new PrintSettings());
		when(invocation.toString(Collections.emptyList(), new PrintSettings())).thenReturn(toStringResult);
		assertEquals(toStringResult, invocationMatcher.toString());
	}

	@Test
	public void testMatches() {
		Invocation invocation = mock(Invocation.class);
		Invocation actual = mock(Invocation.class);
		ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(actual.getMock()).thenReturn(mock(Object.class));
		when(invocation.getMethod()).thenReturn(mock(Method.class));
		when(invocation.isVerified()).thenReturn(false);
		when(argumentsComparator.argumentsMatch(invocation, actual)).thenReturn(true);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
		assertTrue(invocationMatcher.matches(actual));
		verify(invocation).getMock();
		verify(actual).getMock();
		verify(invocation).getMethod();
		verify(invocation).isVerified();
		verify(argumentsComparator).argumentsMatch(invocation, actual);
	}

	@Test
	public void testSafelyArgumentsMatch() {
		Invocation invocation = mock(Invocation.class);
		Method method = mock(Method.class);
		Object[] actualArgs = new Object[1];
		ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
		when(invocation.getMethod()).thenReturn(method);
		when(argumentsComparator.argumentsMatch(invocation, actualArgs)).thenReturn(true);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
		assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
		verify(invocation).getMethod();
		verify(argumentsComparator).argumentsMatch(invocation, actualArgs);
	}

	@Test
	public void testHasSimilarMethod() {
		Invocation invocation = mock(Invocation.class);
		Invocation candidate = mock(Invocation.class);
		Method method = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.isVerified()).thenReturn(false);
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(candidate.getMethod()).thenReturn(method);
		when(candidate.isVerified()).thenReturn(false);
		when(candidate.getMock()).thenReturn(mock(Object.class));
		when(invocation.getArguments()).thenReturn(new Object[1]);
		when(candidate.getArguments()).thenReturn(new Object[1]);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
		assertTrue(invocationMatcher.hasSimilarMethod(candidate));
		verify(invocation).getMethod();
		verify(invocation).isVerified();
		verify(invocation).getMock();
		verify(candidate).getMethod();
		verify(candidate).isVerified();
		verify(candidate).getMock();
		verify(invocation).getArguments();
		verify(candidate).getArguments();
	}

	@Test
	public void testHasSameMethod() {
		Invocation invocation = mock(Invocation.class);
		Invocation candidate = mock(Invocation.class);
		Method method = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method);
		when(candidate.getMethod()).thenReturn(method);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
		assertTrue(invocationMatcher.hasSameMethod(candidate));
		verify(invocation).getMethod();
		verify(candidate).getMethod();
	}

	@Test
	public void testGetLocation() {
		Invocation invocation = mock(Invocation.class);
		Location location = mock(Location.class);
		when(invocation.getLocation()).thenReturn(location);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
		assertEquals(location, invocationMatcher.getLocation());
	}

	@Test
	public void testToStringPrintSettings() {
		Invocation invocation = mock(Invocation.class);
		PrintSettings printSettings = mock(PrintSettings.class);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
		String toStringPrintSettingsResult = invocation.toString(Collections.emptyList(), printSettings);
		when(invocation.toString(Collections.emptyList(), printSettings)).thenReturn(toStringPrintSettingsResult);
		assertEquals(toStringPrintSettingsResult, invocationMatcher.toString(printSettings));
	}

	@Test
	public void testCaptureArgumentsFrom() {
		Invocation invocation = mock(Invocation.class);
		Invocation i = mock(Invocation.class);
		Matcher matcher = mock(Matcher.class);
		CapturesArguments capturesArguments = mock(CapturesArguments.class);
		when(invocation.getArguments()).thenReturn(new Object[1]);
		when(invocation.getMethod()).thenReturn(mock(Method.class));
		when(matcher instanceof CapturesArguments).thenReturn(true);
		when(matcher.captureFrom(i.getArguments()[0])).thenReturn(true);
		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));
		invocationMatcher.captureArgumentsFrom(i);
		verify(invocation).getArguments();
		verify(invocation).getMethod();
		verify(matcher).captureFrom(i.getArguments()[0]);
	}

}