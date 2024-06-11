package org.mockito.internal.verification.argumentmatching;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeLessThanArgumentsLength() {
		ArgumentMatchingTool tool = new ArgumentMatchingTool();
		List<Matcher> matchers = new LinkedList<>();
		matchers.add(mock(Matcher.class));
		Object[] arguments = { 1, 2 };

		Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

		assertArrayEquals(new Integer[0], result);
	}

	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciousMatchingArgsIndexes() {
		ArgumentMatchingTool tool = new ArgumentMatchingTool();
		List<Matcher> matchers = new LinkedList<>();
		matchers.add(new ContainsExtraTypeInformation(Integer.class));
		matchers.add(instanceOf(String.class));
		Object[] arguments = { 1, 2 };

		Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

		assertEquals(1, result.length);
		assertEquals(0, (int) result[0]);
	}

	@Test
	public void testSafelyMatches_matcherMatchesArgument() {
		ArgumentMatchingTool tool = new ArgumentMatchingTool();
		Matcher matcher = mock(Matcher.class);
		Object arg = mock(Object.class);
		when(matcher.matches(arg)).thenReturn(true);

		boolean result = tool.safelyMatches(matcher, arg);

		assertTrue(result);
	}

	@Test
	public void testSafelyMatches_matcherThrowsException() {
		ArgumentMatchingTool tool = new ArgumentMatchingTool();
		Matcher matcher = mock(Matcher.class);
		Object arg = mock(Object.class);
		when(matcher.matches(arg)).thenThrow(new RuntimeException());

		boolean result = tool.safelyMatches(matcher, arg);

		assertFalse(result);
	}

	@Test
	public void testToStringEquals_sameToString() {
		ArgumentMatchingTool tool = new ArgumentMatchingTool();
		Matcher matcher = mock(Matcher.class);
		Object arg = mock(Object.class);
		when(matcher.toString()).thenReturn("matcher");
		when(arg.toString()).thenReturn("matcher");

		boolean result = tool.toStringEquals(matcher, arg);

		assertTrue(result);
	}

	@Test
	public void testToStringEquals_differentToString() {
		ArgumentMatchingTool tool = new ArgumentMatchingTool();
		Matcher matcher = mock(Matcher.class);
		Object arg = mock(Object.class);
		when(matcher.toString()).thenReturn("matcher");
		when(arg.toString()).thenReturn("argument");

		boolean result = tool.toStringEquals(matcher, arg);

		assertFalse(result);
	}
}