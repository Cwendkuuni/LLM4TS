import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_allMatchersAndArgsMatch_suspiciouslyNotMatchingArgsIndexesIsEmpty() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(equalTo("one"));
        matchers.add(equalTo(2));
        matchers.add(instanceOf(Object.class));

        Object[] arguments = new Object[] { "one", 2, new Object() };

        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgsSizeMismatch_suspiciouslyNotMatchingArgsIndexesIsEmpty() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(equalTo("one"));
        matchers.add(equalTo(2));

        Object[] arguments = new Object[] { "one", 2, new Object() };

        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciouslyNotMatchingArgs_suspiciouslyNotMatchingArgsIndexesIsCorrect() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(equalTo("one"));
        matchers.add(new ContainsExtraTypeInformation("2"));
        matchers.add(instanceOf(Object.class));

        Object[] arguments = new Object[] { "one", 2, new Object() };

        Integer[] expected = new Integer[] { 1 };
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciouslyNotMatchingArgs_suspiciouslyNotMatchingArgsIndexesIsCorrect2() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(equalTo("one"));
        matchers.add(new ContainsExtraTypeInformation("2"));
        matchers.add(instanceOf(Object.class));

        Object[] arguments = new Object[] { "one", "2", new Object() };

        Integer[] expected = new Integer[] { 1 };
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciouslyNotMatchingArgs_suspiciouslyNotMatchingArgsIndexesIsCorrect3() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(equalTo("one"));
        matchers.add(new ContainsExtraTypeInformation("2"));
        matchers.add(instanceOf(String.class));

        Object[] arguments = new Object[] { "one", 2, new Object() };

        Integer[] expected = new Integer[] { 1 };
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciouslyNotMatchingArgs_suspiciouslyNotMatchingArgsIndexesIsCorrect4() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(equalTo("one"));
        matchers.add(new ContainsExtraTypeInformation("2"));
        matchers.add(instanceOf(Integer.class));

        Object[] arguments = new Object[] { "one", "2", new Object() };

        Integer[] expected = new Integer[] { 1 };
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciouslyNotMatchingArgs_suspiciouslyNotMatchingArgsIndexesIsCorrect5() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(equalTo("one"));
        matchers.add(new ContainsExtraTypeInformation("2"));
        matchers.add(instanceOf(Integer.class));

        Object[] arguments = new Object[] { "one", 2, new Object() };

        Integer[] expected = new Integer[0];
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

}