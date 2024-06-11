
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.*;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[0];
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Integer[] expected = new Integer[0];
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_notEnoughMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        arguments[0] = "argument";
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Integer[] expected = new Integer[0];
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherNotSafelyMatches() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[1];
        arguments[0] = "argument";
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Integer[] expected = new Integer[0];
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_toStringNotEquals() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[1];
        arguments[0] = "different";
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Integer[] expected = new Integer[0];
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_typeDoesNotMatch() {
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = new ContainsExtraTypeInformation();
        matchers.add(matcher);
        Object[] arguments = new Object[1];
        arguments[0] = 123;
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Integer[] expected = new Integer[]{0};
        assertArrayEquals(expected, result);
    }

    @After
    public void tearDown() {
        argumentMatchingTool = null;
    }
}