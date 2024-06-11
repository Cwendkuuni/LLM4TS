import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noMatchersOrArguments_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList();
        Object[] arguments = new Object[]{};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeNotEqualToArgumentsSize_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(any(), any());
        Object[] arguments = new Object[]{1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherIsNotContainsExtraTypeInformation_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(any(), any());
        Object[] arguments = new Object[]{1, 2};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherSafelyMatches_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(any(), any());
        Object[] arguments = new Object[]{1, 2};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherToStringEquals_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(any(), any());
        Object[] arguments = new Object[]{1, 2};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherTypeMatches_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(any(), any());
        Object[] arguments = new Object[]{1, 2};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherDoesNotSafelyMatches_returnsArrayWithIndexes() {
        List<Matcher> matchers = Arrays.asList(any(), any());
        Object[] arguments = new Object[]{1, 2};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(2)));
        assertThat(result[0], is(equalTo(0)));
        assertThat(result[1], is(equalTo(1)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherToStringDoesNotEqual_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList(any(), any());
        Object[] arguments = new Object[]{1, 2};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

}