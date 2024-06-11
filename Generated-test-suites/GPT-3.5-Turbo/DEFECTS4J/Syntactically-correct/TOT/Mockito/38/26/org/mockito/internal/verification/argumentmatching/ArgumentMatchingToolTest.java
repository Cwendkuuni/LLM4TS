import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithEqualSize() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(org.mockito.Mockito.anyInt());
        matchers.add(org.mockito.Mockito.anyString());

        Object[] arguments = new Object[]{2, "test"};

        Integer[] expected = new Integer[]{};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithDifferentSize() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(org.mockito.Mockito.anyInt());
        matchers.add(org.mockito.Mockito.anyString());

        Object[] arguments = new Object[]{2, "test", true};

        Integer[] expected = new Integer[]{};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithInvalidTypeMatching() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation(Integer.class));
        matchers.add(org.mockito.Mockito.anyString());

        Object[] arguments = new Object[]{"test", 2};

        Integer[] expected = new Integer[]{0};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithNullArgument() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(org.mockito.Mockito.anyInt());

        Object[] arguments = new Object[]{null};

        Integer[] expected = new Integer[]{0};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithMatcherError() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(org.mockito.Mockito.anyInt());

        Object[] arguments = new Object[]{2};

        Integer[] expected = new Integer[]{};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithToStringEquals() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(org.mockito.Mockito.anyString());

        Object[] arguments = new Object[]{"test"};

        Integer[] expected = new Integer[]{};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WithToStringNotEquals() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(org.mockito.Mockito.endsWith("abc"));

        Object[] arguments = new Object[]{"test"};

        Integer[] expected = new Integer[]{0};
        Integer[] actual = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }
}