import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAreDifferentTypesButToStringRepresentationIsTheSame_thenIndexesAreReturned() {
        // given
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(createMatcher("StringMatcher"));
        matchers.add(createMatcher("IntegerMatcher"));
        Object[] arguments = new Object[]{"testString", 1};

        // when
        Integer[] suspiciousIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // then
        assertThat(suspiciousIndexes, is(instanceOf(Integer[].class)));
        assertThat(suspiciousIndexes.length, is(equalTo(1)));
        assertThat(suspiciousIndexes[0], is(equalTo(1)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAreMatching_thenNoIndexesAreReturned() {
        // given
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(createMatcher("StringMatcher"));
        matchers.add(createMatcher("StringMatcher"));
        Object[] arguments = new Object[]{"testString", "testString"};

        // when
        Integer[] suspiciousIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // then
        assertThat(suspiciousIndexes, is(instanceOf(Integer[].class)));
        assertThat(suspiciousIndexes.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAreNotSame_thenNoIndexesAreReturned() {
        // given
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers1 = new ArrayList<>();
        matchers1.add(createMatcher("StringMatcher"));
        Object[] arguments1 = new Object[]{"testString"};

        List<Matcher> matchers2 = new ArrayList<>();
        matchers2.add(createMatcher("IntegerMatcher"));
        Object[] arguments2 = new Object[]{1};

        // when
        Integer[] suspiciousIndexes1 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers1, arguments1);
        Integer[] suspiciousIndexes2 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);

        // then
        assertThat(suspiciousIndexes1, is(instanceOf(Integer[].class)));
        assertThat(suspiciousIndexes1.length, is(equalTo(0)));
        assertThat(suspiciousIndexes2, is(instanceOf(Integer[].class)));
        assertThat(suspiciousIndexes2.length, is(equalTo(0)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersAreSameButTypesAreDifferent_thenIndexOfDifferentTypeIsReturned() {
        // given
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(createMatcher("StringMatcher"));
        Object[] arguments = new Object[]{"testString", 1};

        // when
        Integer[] suspiciousIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // then
        assertThat(suspiciousIndexes, is(instanceOf(Integer[].class)));
        assertThat(suspiciousIndexes.length, is(equalTo(1)));
        assertThat(suspiciousIndexes[0], is(equalTo(1)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenMatchersContainExtraTypeInformation_thenIndexOfExtraTypeIsReturned() {
        // given
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(createMatcherWithExtraTypeInformation("StringMatcher<String>", "StringMatcher"));
        Object[] arguments = new Object[]{"testString", 1};

        // when
        Integer[] suspiciousIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // then
        assertThat(suspiciousIndexes, is(instanceOf(Integer[].class)));
        assertThat(suspiciousIndexes.length, is(equalTo(1)));
        assertThat(suspiciousIndexes[0], is(equalTo(1)));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsAreNull_thenIndexesAreNotReturned() {
        // given
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(createMatcher("StringMatcher"));
        Object[] arguments = new Object[]{null};

        // when
        Integer[] suspiciousIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // then
        assertThat(suspiciousIndexes, is(instanceOf(Integer[].class)));
        assertThat(suspiciousIndexes.length, is(equalTo(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenArgumentsCountIsDifferentThanMatchersCount_thenIllegalArgumentExceptionIsThrown() {
        // given
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{"testString"};

        // when
        argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // then
        // exception is thrown
    }

    private Matcher createMatcher(String matcherName) {
        return mock(Matcher.class);
    }

    private Matcher createMatcherWithExtraTypeInformation(String description, String matcherName) {
        Matcher matcher = mock(Matcher.class);
        StringDescription stringDescription = mock(StringDescription.class);
        when(matcher.description()).thenReturn(stringDescription);
        when(stringDescription.toString()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return description;
            }
        });
        when(matcher.matches(anyObject())).thenReturn(true);
        when(matcher instanceof ContainsExtraTypeInformation).thenReturn(true);
        when(((ContainsExtraTypeInformation) matcher).typeMatches(anyObject())).thenReturn(true);
        return matcher;
    }
}