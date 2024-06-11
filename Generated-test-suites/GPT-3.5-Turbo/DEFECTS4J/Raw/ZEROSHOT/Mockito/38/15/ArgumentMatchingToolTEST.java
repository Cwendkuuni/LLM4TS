import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {
    private ArgumentMatchingTool tool;

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatchersAndArguments_ReturnsEmptyArray() {
        tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedMatchersAndArguments_ReturnsEmptyArray() {
        tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(isEqualTo(1));
        Object[] arguments = new Object[]{1, 2};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchingMatchersAndArguments_ReturnsEmptyArray() {
        tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(isEqualTo(1));
        Object[] arguments = new Object[]{1};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatchersAndArguments_ReturnsArrayWithIndexes() {
        tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(isEqualTo(1));
        Object[] arguments = new Object[]{"1"};

        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertEquals(1, result.length);
        Assert.assertEquals(Integer.valueOf(0), result[0]);
    }

    private Matcher isEqualTo(int val) {
        return new Matcher() {
            @Override
            public boolean matches(Object item) {
                return item.equals(val);
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {
                mismatchDescription.appendText("not equal to " + val);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("equal to " + val);
            }
        };
    }
}