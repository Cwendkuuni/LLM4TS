import org.hamcrest.Matchers;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_allMatchersMatchingArguments() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcherWithToString("1"));
        matchers.add(matcherWithToString("2"));

        Object[] arguments = {"1", "2"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, IsEmptyArray());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_allMatchersNotMatchingArguments() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcherWithToString("1"));
        matchers.add(matcherWithToString("2"));

        Object[] arguments = {"3", "4"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, arrayContaining(0, 1));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_someMatchersMatchingAndSomeNotMatchingArguments() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcherWithToString("1"));
        matchers.add(matcherWithToString("2"));

        Object[] arguments = {"1", "3"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, arrayContaining(1));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeDifferentFromArgumentsSize() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcherWithToString("1"));

        Object[] arguments = {"1", "2"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, IsEmptyArray());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformation() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation(String.class));
        matchers.add(matcherWithToString("2"));

        Object[] arguments = {"1", "2"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, arrayContaining(0));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationWithMatchingType() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation(String.class));
        matchers.add(matcherWithToString("2"));

        Object[] arguments = {new Integer(1), "2"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, arrayContaining(0));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationWithNotMatchingType() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation(String.class));
        matchers.add(matcherWithToString("2"));

        Object[] arguments = {new Integer(1), "2"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, arrayContaining(0));
    }

    private Matcher matcherWithToString(String value) {
        return argThat(new HamcrestToStringMatcher(value));
    }
}


class HamcrestToStringMatcher extends ArgumentMatcher<Object> {

    private String expectedToString;

    public HamcrestToStringMatcher(String expectedToString) {
        this.expectedToString = expectedToString;
    }

    @Override
    public boolean matches(Object argument) {
        return expectedToString.equals(argument.toString());
    }
}