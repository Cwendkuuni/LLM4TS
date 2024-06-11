import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {
	
	private Invocation invocation;
	private List<Matcher> matchers;
	private InvocationMatcher invocationMatcher;

	@Before
	public void setUp() {
		invocation = mock(Invocation.class);
		matchers = new ArrayList<Matcher>();
		invocationMatcher = new InvocationMatcher(invocation, matchers);
	}

	@Test
	public void testConstructor_withMatchersNotEmpty() {
		Matcher matcher1 = mock(Matcher.class);
		Matcher matcher2 = mock(Matcher.class);
		matchers.add(matcher1);
		matchers.add(matcher2);

		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

		assertSame(invocation, invocationMatcher.getInvocation());
		assertSame(matchers, invocationMatcher.getMatchers());
	}

	@Test
	public void testConstructor_withMatchersEmpty() {
		List<Matcher> emptyMatchers = new ArrayList<Matcher>();

		InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, emptyMatchers);

		assertSame(invocation, invocationMatcher.getInvocation());
		assertNotSame(emptyMatchers, invocationMatcher.getMatchers());
	}

	@Test
	public void testGetMethod() {
		Method method = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method);

		Method actualMethod = invocationMatcher.getMethod();

		assertSame(method, actualMethod);
	}

	@Test
	public void testGetInvocation() {
		Invocation actualInvocation = invocationMatcher.getInvocation();

		assertSame(invocation, actualInvocation);
	}

	@Test
	public void testGetMatchers() {
		List<Matcher> actualMatchers = invocationMatcher.getMatchers();

		assertSame(matchers, actualMatchers);
	}

	@Test
	public void testToString() {
		when(invocation.toString(matchers, new PrintSettings())).thenReturn("Expected Invocation String");

		String toString = invocationMatcher.toString();

		assertEquals("Expected Invocation String", toString);
	}

	@Test
	public void testMatches_withSameMockSameMethodArgumentsMatch() {
		Invocation actualInvocation = mock(Invocation.class);
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(actualInvocation.getMock()).thenReturn(invocation.getMock());
		when(invocation.getMethod()).thenReturn(mock(Method.class));
		when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
		when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);

		boolean matches = invocationMatcher.matches(actualInvocation);

		assertTrue(matches);
	}

	@Test
	public void testMatches_withDifferentMock() {
		Invocation actualInvocation = mock(Invocation.class);
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(actualInvocation.getMock()).thenReturn(mock(Object.class));

		boolean matches = invocationMatcher.matches(actualInvocation);

		assertFalse(matches);
	}

	@Test
	public void testMatches_withDifferentMethod() {
		Invocation actualInvocation = mock(Invocation.class);
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(actualInvocation.getMock()).thenReturn(invocation.getMock());
		when(invocation.getMethod()).thenReturn(mock(Method.class));
		Method differentMethod = mock(Method.class);
		when(actualInvocation.getMethod()).thenReturn(differentMethod);
		when(hasSameMethod(differentMethod)).thenReturn(false);

		boolean matches = invocationMatcher.matches(actualInvocation);

		assertFalse(matches);
	}

	@Test
	public void testMatches_withArgumentsNotMatching() {
		Invocation actualInvocation = mock(Invocation.class);
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(actualInvocation.getMock()).thenReturn(invocation.getMock());
		when(invocation.getMethod()).thenReturn(mock(Method.class));
		when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
		when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(false);

		boolean matches = invocationMatcher.matches(actualInvocation);

		assertFalse(matches);
	}

	@Test
	public void testHasSimilarMethod_withDifferentMethodNames() {
		Invocation candidate = mock(Invocation.class);
		Method method = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method);
		when(candidate.getMethod()).thenReturn(mock(Method.class));
		when(candidate.isVerified()).thenReturn(true);
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(candidate.getMock()).thenReturn(invocation.getMock());

		boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

		assertFalse(hasSimilarMethod);
	}

	@Test
	public void testHasSimilarMethod_withVerifiedCandidate() {
		Invocation candidate = mock(Invocation.class);
		Method method = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method);
		when(candidate.getMethod()).thenReturn(method);
		when(candidate.isVerified()).thenReturn(true);
		when(candidate.getMock()).thenReturn(invocation.getMock());
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(new ArgumentsComparator().argumentsMatch(invocationMatcher, candidate.getArguments())).thenReturn(true);

		boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

		assertFalse(hasSimilarMethod);
	}

	@Test
	public void testHasSimilarMethod_withDifferentMock() {
		Invocation candidate = mock(Invocation.class);
		when(invocation.getMethod()).thenReturn(mock(Method.class));
		when(candidate.getMethod()).thenReturn(invocation.getMethod());
		when(candidate.isVerified()).thenReturn(false);
		when(candidate.getMock()).thenReturn(mock(Object.class));
		when(invocation.getMock()).thenReturn(mock(Object.class));

		boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

		assertFalse(hasSimilarMethod);
	}

	@Test
	public void testHasSimilarMethod_withDifferentMethodButSameArguments() {
		Invocation candidate = mock(Invocation.class);
		Method method1 = mock(Method.class);
		Method method2 = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method1);
		when(candidate.getMethod()).thenReturn(method2);
		when(candidate.isVerified()).thenReturn(false);
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(candidate.getMock()).thenReturn(invocation.getMock());
		when(hasSameMethod(candidate)).thenReturn(false);
		when(safelyArgumentsMatch(candidate.getArguments())).thenReturn(true);

		boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

		assertFalse(hasSimilarMethod);
	}

	@Test
	public void testHasSimilarMethod_withSameMethodDifferentArguments() {
		Invocation candidate = mock(Invocation.class);
		Method method = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method);
		when(candidate.getMethod()).thenReturn(method);
		when(candidate.isVerified()).thenReturn(false);
		when(invocation.getMock()).thenReturn(mock(Object.class));
		when(candidate.getMock()).thenReturn(invocation.getMock());
		when(hasSameMethod(candidate)).thenReturn(true);

		boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

		assertTrue(hasSimilarMethod);
	}

	@Test
	public void testHasSameMethod_withSameMethod() {
		Method method = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method);
		when(invocation.getMethod()).thenReturn(method);

		boolean hasSameMethod = invocationMatcher.hasSameMethod(invocation);

		assertTrue(hasSameMethod);
	}

	@Test
	public void testHasSameMethod_withDifferentMethod() {
		Method method1 = mock(Method.class);
		Method method2 = mock(Method.class);
		when(invocation.getMethod()).thenReturn(method1);
		when(invocation.getMethod()).thenReturn(method2);

		boolean hasSameMethod = invocationMatcher.hasSameMethod(invocation);

		assertFalse(hasSameMethod);
	}

	@Test
	public void testGetLocation() {
		Location location = mock(Location.class);
		when(invocation.getLocation()).thenReturn(location);

		Location actualLocation = invocationMatcher.getLocation();

		assertSame(location, actualLocation);
	}

	@Test
	public void testToString_withPrintSettings() {
		PrintSettings printSettings = mock(PrintSettings.class);
		when(invocation.toString(matchers, printSettings)).thenReturn("Expected Invocation String");

		String toString = invocationMatcher.toString(printSettings);

		assertEquals("Expected Invocation String", toString);
	}

	@Test
	public void testCaptureArgumentsFrom_withMatchersCapturingArguments() {
		Matcher matcher1 = mock(Matcher.class);
		Matcher matcher2 = mock(Matcher.class);
		matchers.addAll(Arrays.asList(matcher1, matcher2));
		Invocation i = mock(Invocation.class);
		Object[] arguments = { "Argument1", "Argument2" };
		when(i.getArguments()).thenReturn(arguments);
		when(matcher1 instanceof CapturesArguments).thenReturn(true);
		when(matcher2 instanceof CapturesArguments).thenReturn(true);

		invocationMatcher.captureArgumentsFrom(i);

		verify((CapturesArguments)matcher1).captureFrom("Argument1");
		verify((CapturesArguments)matcher2).captureFrom("Argument2");
	}

}