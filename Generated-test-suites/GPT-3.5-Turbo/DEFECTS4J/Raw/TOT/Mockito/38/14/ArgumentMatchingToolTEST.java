import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsSizeMismatch_ReturnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        Object[] arguments = new Object[0];

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, emptyArray());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationAndMatches_ReturnsEmptyArray() {
        Matcher mockMatcher = mock(Matcher.class);
        ContainsExtraTypeInformation mockExtraTypeMatcher = mock(ContainsExtraTypeInformation.class);
        List<Matcher> matchers = Arrays.asList(mockExtraTypeMatcher);
        Object[] arguments = new Object[1];

        when(mockExtraTypeMatcher.typeMatches(any())).thenReturn(true);
        when(mockMatcher.matches(any())).thenReturn(true);
        when(mockMatcher.toString()).thenReturn("mockObject");
        when(mockMatcher.toString()).thenReturn("mockObject");

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, emptyArray());
        verify(mockExtraTypeMatcher, times(1)).typeMatches(any());
        verify(mockMatcher, times(1)).matches(any());
        verify(mockMatcher, times(1)).toString();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationAndNotMatches_ReturnsIndexes() {
        Matcher mockMatcher = mock(Matcher.class);
        ContainsExtraTypeInformation mockExtraTypeMatcher = mock(ContainsExtraTypeInformation.class);
        List<Matcher> matchers = Arrays.asList(mockExtraTypeMatcher);
        Object[] arguments = new Object[1];

        when(mockExtraTypeMatcher.typeMatches(any())).thenReturn(false);
        when(mockMatcher.matches(any())).thenReturn(false);
        when(mockMatcher.toString()).thenReturn("mockObject");
        when(mockMatcher.toString()).thenReturn("mockObject");

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, arrayWithSize(1));
        assertThat(result, arrayContaining(0));
        verify(mockExtraTypeMatcher, times(1)).typeMatches(any());
        verify(mockMatcher, times(1)).matches(any());
        verify(mockMatcher, times(1)).toString();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationAndMatchesButToStringNotEqual_ReturnsEmptyArray() {
        Matcher mockMatcher = mock(Matcher.class);
        ContainsExtraTypeInformation mockExtraTypeMatcher = mock(ContainsExtraTypeInformation.class);
        List<Matcher> matchers = Arrays.asList(mockExtraTypeMatcher);
        Object[] arguments = new Object[1];

        when(mockExtraTypeMatcher.typeMatches(any())).thenReturn(true);
        when(mockMatcher.matches(any())).thenReturn(true);
        when(mockMatcher.toString()).thenReturn("mockObject");
        when(mockMatcher.toString()).thenReturn("anotherObject");

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, emptyArray());
        verify(mockExtraTypeMatcher, times(1)).typeMatches(any());
        verify(mockMatcher, times(1)).matches(any());
        verify(mockMatcher, times(1)).toString();
    }

    @Test
    public void testSafelyMatches_ExceptionThrown_ReturnsFalse() {
        Matcher mockMatcher = mock(Matcher.class);
        Object argument = mock(Object.class);

        when(mockMatcher.matches(any())).thenThrow(new RuntimeException());

        boolean result = argumentMatchingTool.safelyMatches(mockMatcher, argument);

        assertThat(result, is(false));
        verify(mockMatcher, times(1)).matches(any());
    }

    @Test
    public void testSafelyMatches_NoException_ReturnsTrue() {
        Matcher mockMatcher = mock(Matcher.class);
        Object argument = mock(Object.class);

        when(mockMatcher.matches(any())).thenReturn(true);

        boolean result = argumentMatchingTool.safelyMatches(mockMatcher, argument);

        assertThat(result, is(true));
        verify(mockMatcher, times(1)).matches(any());
    }

    @Test
    public void testToStringEquals_Equals_ReturnsTrue() {
        Matcher mockMatcher = mock(Matcher.class);
        Object argument = mock(Object.class);

        when(mockMatcher.toString()).thenReturn("mockObject");
        when(argument.toString()).thenReturn("mockObject");

        boolean result = argumentMatchingTool.toStringEquals(mockMatcher, argument);

        assertThat(result, is(true));
        verify(mockMatcher, times(1)).toString();
        verify(argument, times(1)).toString();
    }

    @Test
    public void testToStringEquals_NotEquals_ReturnsFalse() {
        Matcher mockMatcher = mock(Matcher.class);
        Object argument = mock(Object.class);

        when(mockMatcher.toString()).thenReturn("mockObject");
        when(argument.toString()).thenReturn("anotherObject");

        boolean result = argumentMatchingTool.toStringEquals(mockMatcher, argument);

        assertThat(result, is(false));
        verify(mockMatcher, times(1)).toString();
        verify(argument, times(1)).toString();
    }

}