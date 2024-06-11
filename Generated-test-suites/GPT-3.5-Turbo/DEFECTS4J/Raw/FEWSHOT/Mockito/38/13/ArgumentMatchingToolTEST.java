package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeNotEqualArgumentsLength() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(Matcher.class));

        Object[] arguments = new Object[]{};
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndSafelyMatchesAndToStringEqualsAndNotTypeMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.toString()).thenReturn("Mockito");

        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(matcher);

        Object[] arguments = new Object[]{"Mockito"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndSafelyMatchesAndToStringEqualsAndTypeMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.toString()).thenReturn("Mockito");
        when(matcher.typeMatches("Mockito")).thenReturn(true);

        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(matcher);

        Object[] arguments = new Object[]{"Mockito"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndNotSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.toString()).thenReturn("Mockito");
        when(matcher.matches("Mockito")).thenReturn(false);

        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(matcher);

        Object[] arguments = new Object[]{"Mockito"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndNotToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.toString()).thenReturn("Mockito");

        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(matcher);

        Object[] arguments = new Object[]{"Other"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_otherMatcherType() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher matcher = mock(Matcher.class);
        when(matcher.toString()).thenReturn("Mockito");

        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(matcher);

        Object[] arguments = new Object[]{"Mockito"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, result.length);
    }
}