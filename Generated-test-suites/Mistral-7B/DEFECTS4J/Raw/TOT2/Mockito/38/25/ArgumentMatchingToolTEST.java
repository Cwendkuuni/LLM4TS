import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.instanceOf(String.class));
        matchers.add(Matchers.instanceOf(Integer.class));
        Object[] arguments = new Object[]{"test", 1};
        Integer[] expected = new Integer[0];
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    // Test matchers list size is not equal to arguments size
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherSizeNotEqualArgumentsSize() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.instanceOf(String.class));
        matchers.add(Matchers.instanceOf(Integer.class));
        Object[] arguments = new Object[]{"test", 1, new Object()};
        Integer[] expected = new Integer[]{2};
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    // Test matchers list contains null
    @Test(expected = NullPointerException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherContainsNull() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.instanceOf(String.class));
        matchers.add(null);
        Object[] arguments = new Object[]{"test", 1};
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    // Test arguments contain null
    @Test(expected = NullPointerException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ArgumentsContainsNull() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.instanceOf(String.class));
        Object[] arguments = new Object[]{"test", null};
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    // Test matcher throws an exception
    @Test(expected = RuntimeException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherThrowsException() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new RuntimeExceptionTestMatcher());
        Object[] arguments = new Object[]{"test", 1};
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    // Test toString() representation of matcher and argument are the same but types are different but they match
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringRepresentationSameButTypesDifferentAndMatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new StringMatcher());
        Object[] arguments = new Object[]{new String("test"), new Object()};
        Integer[] expected = new Integer[]{0};
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

}

class StringMatcher implements Matcher<String> {
    @Override
    public boolean matches(String argument) {
        return true;
    }

    @Override
    public void describeTo(StringDescription description) {
        description.appendText("a string");
    }
}

class RuntimeExceptionTestMatcher implements Matcher<Object> {
    @Override
    public boolean matches(Object argument) {
        throw new RuntimeException();
    }

    @Override
    public void describeTo(StringDescription description) {
        description.appendText("a runtime exception");
    }
}