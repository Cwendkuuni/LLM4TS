import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.mockito.internal.invocation.Invocation;
import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

	@Test
	public void testGetMethod() {
		Method method = SomeClass.class.getMethod();
		assertNotNull(method);
	}

	@Test
	public void testGetInvocation() {
		Invocation invocation = new Invocation();
		InvocationMatcher invMatcher = new InvocationMatcher(invocation);
		assertEquals(invocation, invMatcher.getInvocation());
	}

	@Test
	public void testGetMatchers() {
		List<Matcher> matchers = Collections.emptyList();
		Invocation invocation = new Invocation();
		InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);
		assertEquals(matchers, invMatcher.getMatchers());
	}

	@Test
	public void testToString() {
		Invocation invocation = new Invocation();
		InvocationMatcher invMatcher = new InvocationMatcher(invocation);
		String expected = invocation.toString(invMatcher.getMatchers(), new PrintSettings());
		assertEquals(expected, invMatcher.toString());
	}

	@Test
	public void testMatches() {
		Invocation invocation = new Invocation();
		InvocationMatcher invMatcher = new InvocationMatcher(invocation);
		assertTrue(invMatcher.matches(invocation));
	}

	@Test
	public void testHasSimilarMethod() {
		Invocation invocation = new Invocation();
		Invocation otherInvocation = new Invocation();
		InvocationMatcher invMatcher = new InvocationMatcher(invocation);

		assertTrue(invMatcher.hasSimilarMethod(otherInvocation));
	}

	@Test
	public void testHasSameMethod() {
		Invocation invocation = new Invocation();
		InvocationMatcher invMatcher = new InvocationMatcher(invocation);

		assertTrue(invMatcher.hasSameMethod(invocation));
	}

	@Test
	public void testGetLocation() {
		Invocation invocation = new Invocation();
		InvocationMatcher invMatcher = new InvocationMatcher(invocation);
		assertNotNull(invMatcher.getLocation());
	}

	@Test
	public void testToStringWithPrintSettings() {
		Invocation invocation = new Invocation();
		InvocationMatcher invMatcher = new InvocationMatcher(invocation);
		String expected = invocation.toString(invMatcher.getMatchers(), new PrintSettings());
		assertEquals(expected, invMatcher.toString(new PrintSettings()));
	}

	@Test
	public void testCaptureArgumentsFrom() {
		Invocation invocation = new Invocation();
		InvocationMatcher invMatcher = new InvocationMatcher(invocation);

		Invocation otherInvocation = new Invocation();
		invMatcher.captureArgumentsFrom(otherInvocation);

		// Assert that no error occurs
		assertTrue(true);
	}

	@Test
	public void testCreateFrom() {
		List<Invocation> invocations = Collections.emptyList();
		List<InvocationMatcher> invMatchersList = InvocationMatcher.createFrom(invocations);
		assertEquals(invocations.size(), invMatchersList.size());
	}

}