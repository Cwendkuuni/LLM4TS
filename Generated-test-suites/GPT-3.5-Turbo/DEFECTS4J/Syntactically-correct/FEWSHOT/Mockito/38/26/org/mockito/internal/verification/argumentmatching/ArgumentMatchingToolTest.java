package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {
	
	private ArgumentMatchingTool tool;
	
	@Before
	public void setup() {
		tool = new ArgumentMatchingTool();
	}
	
	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_noMatchers() {
		List<Matcher> matchers = new LinkedList<>();
		Object[] arguments = new Object[]{1, 2, 3};
		
		Integer[] expected = new Integer[0];
		Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchers() {
		List<Matcher> matchers = new LinkedList<>();
		Object[] arguments = new Object[0];
		
		Integer[] expected = new Integer[0];
		Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeDifferentFromArgumentsSize() {
		List<Matcher> matchers = new LinkedList<>();
		matchers.add(mock(Matcher.class));
		
		Object[] arguments = new Object[0];
		
		Integer[] expected = new Integer[0];
		Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_allMatchersMatching() {
		List<Matcher> matchers = new LinkedList<>();
		matchers.add(mock(Matcher.class));
		matchers.add(mock(Matcher.class));
		matchers.add(mock(Matcher.class));
		
		Object[] arguments = new Object[]{1, 2, 3};
		
		Integer[] expected = new Integer[0];
		Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_oneMatcherNotMatchingByType() {
		List<Matcher> matchers = new LinkedList<>();
		matchers.add(mock(Matcher.class));
		matchers.add(createMatcher(false));
		matchers.add(mock(Matcher.class));
		
		Object[] arguments = new Object[]{1, 2, 3};
		
		Integer[] expected = new Integer[]{1};
		Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_allMatchersNotMatchingByType() {
		List<Matcher> matchers = new LinkedList<>();
		matchers.add(createMatcher(false));
		matchers.add(createMatcher(false));
		matchers.add(createMatcher(false));
		
		Object[] arguments = new Object[]{1, 2, 3};
		
		Integer[] expected = new Integer[]{0, 1, 2};
		Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_oneMatcherMatchingByType() {
		List<Matcher> matchers = new LinkedList<>();
		matchers.add(mock(Matcher.class));
		matchers.add(createMatcher(true));
		matchers.add(mock(Matcher.class));
		
		Object[] arguments = new Object[]{1, 2, 3};
		
		Integer[] expected = new Integer[0];
		Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testGetSuspiciouslyNotMatchingArgsIndexes_allMatchersMatchingByType() {
		List<Matcher> matchers = new LinkedList<>();
		matchers.add(createMatcher(true));
		matchers.add(createMatcher(true));
		matchers.add(createMatcher(true));
		
		Object[] arguments = new Object[]{1, 2, 3};
		
		Integer[] expected = new Integer[0];
		Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
		
		assertArrayEquals(expected, actual);
	}
	
	private Matcher createMatcher(boolean typeMatches) {
		Matcher matcher = mock(Matcher.class);
		when(matcher instanceof ContainsExtraTypeInformation).thenReturn(true);
		when(matcher.matches(Mockito.any())).thenReturn(false);
		when(((ContainsExtraTypeInformation) matcher).typeMatches(Mockito.any())).thenReturn(typeMatches);
		StringDescription description = new StringDescription();
		when(matcher.toString()).thenReturn(description.toString());
		
		return matcher;
	}
}