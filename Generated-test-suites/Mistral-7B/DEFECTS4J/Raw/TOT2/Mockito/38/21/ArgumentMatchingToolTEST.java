import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ContainsExtraTypeInformation("expectedString", String.class));
        matchers.add(new ContainsExtraTypeInformation(1, Integer.TYPE));
        Object[] arguments = new Object[]{"expectedString", new Integer(1)};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[]{1}, result);
    }

    @Test(expected = AssertionError.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SizeMismatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Assert.assertArrayEquals(new Integer[0], tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherNotInstanceOfContainsExtraTypeInformation() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new StringMatcher("expectedString"));
        Object[] arguments = new Object[]{"expectedString"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], result);
    }

    @Test(expected = Throwable.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ErrorInSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ContainsExtraTypeInformation("expectedString", String.class));
        Object[] arguments = new Object[]{"expectedString"};

        Matcher m = mock(Matcher.class);
        doThrow(new RuntimeException()).when(m).matches((Object) argThat(isA(String.class)));

        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test(expected = AssertionError.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ErrorInToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ContainsExtraTypeInformation("expectedString", String.class));
        Object[] arguments = new Object[]{1};

        Assert.assertArrayEquals(new Integer[0], tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
    }

}