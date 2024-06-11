import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withNoMatchersAndNoArguments() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        Object[] arguments = new Object[0];
        Integer[] expected = new Integer[0];

        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, actual.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withNoMatchersAndArguments() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        Object[] arguments = new Object[]{1, 2, 3};
        Integer[] expected = new Integer[0];

        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, actual.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMatchersAndNoArguments() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(anyInt());
        matchers.add(anyString());
        matchers.add(anyBoolean());
        Object[] arguments = new Object[0];
        Integer[] expected = new Integer[0];

        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, actual.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMatchersAndArguments() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(anyInt());
        matchers.add(anyString());
        matchers.add(anyBoolean());
        Object[] arguments = new Object[]{1, "test", true};
        Integer[] expected = new Integer[0];

        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, actual.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMatchersAndArgumentsNotMatching() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(anyInt());
        matchers.add(anyString());
        matchers.add(anyBoolean());
        Object[] arguments = new Object[]{1, 2, 3};
        Integer[] expected = new Integer[]{1, 2};

        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, actual.length);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMatchersAndArgumentsNotMatchingWithTypeMismatch() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(containsExtraTypeInformation(anyInt(), Integer.class));
        matchers.add(containsExtraTypeInformation(anyString(), String.class));
        matchers.add(containsExtraTypeInformation(anyBoolean(), Boolean.class));
        Object[] arguments = new Object[]{1, "test", true};
        Integer[] expected = new Integer[]{1, 2};

        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(expected.length, actual.length);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    private Matcher<Object> containsExtraTypeInformation(Matcher matcher, Class<?> type) {
        return new ContainsExtraTypeInformation(matcher, type);
    }
}