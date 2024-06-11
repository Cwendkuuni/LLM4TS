import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.*;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NoMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[0];

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsDifferentSize() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SafeMatches() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new ContainsExtraTypeInformation()};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SafeMatchesWithDifferentToString() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{"other"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NotSafeMatches() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new Object()};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(1)));
        assertThat(result[0], is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NotSafeMatchesWithDifferentToString() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[]{new Object()};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result.length, is(equalTo(1)));
        assertThat(result[0], is(equalTo(0)));
    }

}