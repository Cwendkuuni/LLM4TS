import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;
    private List<Matcher> matchers;
    private Object[] arguments;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
        matchers = new ArrayList<>();
        arguments = new Object[]{};
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyArgsAndMatchers() {
        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentLength() {
        matchers.add(new ContainsExtraTypeInformation(Integer.class));
        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgs() {
        matchers.add(new ContainsExtraTypeInformation(Integer.class));
        matchers.add(new ContainsExtraTypeInformation(String.class));
        arguments = new Object[]{1, "test"};
        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciousArgs() {
        matchers.add(new ContainsExtraTypeInformation(Integer.class));
        matchers.add(new ContainsExtraTypeInformation(String.class));
        arguments = new Object[]{1, "1"};
        Integer[] expected = new Integer[]{1};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciousArgsWithTypeMismatch() {
        matchers.add(new ContainsExtraTypeInformation(Integer.class));
        matchers.add(new ContainsExtraTypeInformation(String.class));
        arguments = new Object[]{1, 1};
        Integer[] expected = new Integer[]{1};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciousArgsWithMatcherException() {
        matchers.add(new ContainsExtraTypeInformation(Integer.class));
        arguments = new Object[]{1};
        Integer[] expected = new Integer[]{0};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciousArgsWithToStringMismatch() {
        matchers.add(new ContainsExtraTypeInformation(Integer.class));
        matchers.add(new ContainsExtraTypeInformation(String.class));
        arguments = new Object[]{1, "test"};
        Integer[] expected = new Integer[]{1};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected, actual);
    }

}