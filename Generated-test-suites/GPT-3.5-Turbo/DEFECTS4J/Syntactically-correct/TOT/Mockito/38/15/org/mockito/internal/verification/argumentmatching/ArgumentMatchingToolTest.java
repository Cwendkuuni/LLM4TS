import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class ArgumentMatchingToolTest {
    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeDifferentFromArgumentsSize_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        Object[] arguments = new Object[3];

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, is(emptyArray()));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_safelyMatches_returnsEmptyArray() {
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        Object argument = new Object();
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{argument};
        Mockito.when(matcher.matches(argument)).thenReturn(true);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, is(emptyArray()));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_toStringEquals_returnsEmptyArray() {
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        Object argument = new Object();
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{argument};
        Mockito.when(matcher.matches(argument)).thenReturn(false);
        Mockito.when(matcher.toString()).thenReturn(argument.toString());

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, is(emptyArray()));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndTypeMatches_returnsEmptyArray() {
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        Object argument = new Object();
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{argument};
        Mockito.when(matcher.matches(argument)).thenReturn(false);
        Mockito.when(matcher.toString()).thenReturn("Some Matcher");
        Mockito.when(matcher.typeMatches(argument)).thenReturn(true);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, is(emptyArray()));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndTypeNotMatches_returnsArrayWithIndex() {
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        Object argument = new Object();
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{argument};
        Mockito.when(matcher.matches(argument)).thenReturn(false);
        Mockito.when(matcher.toString()).thenReturn("Some Matcher");
        Mockito.when(matcher.typeMatches(argument)).thenReturn(false);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, contains(0));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationToStringThrowsException_returnsArrayWithIndex() {
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        Object argument = new Object();
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{argument};
        Mockito.when(matcher.matches(argument)).thenReturn(false);
        Mockito.when(matcher.toString()).thenThrow(new RuntimeException());

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, contains(0));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationMatchesReturnsFalse_returnsArrayWithIndex() {
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        Object argument = new Object();
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{argument};
        Mockito.when(matcher.matches(argument)).thenReturn(false);
        Mockito.when(matcher.toString()).thenReturn("Some Matcher");
        Mockito.when(matcher.typeMatches(argument)).thenReturn(true);

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, contains(0));
    }
}