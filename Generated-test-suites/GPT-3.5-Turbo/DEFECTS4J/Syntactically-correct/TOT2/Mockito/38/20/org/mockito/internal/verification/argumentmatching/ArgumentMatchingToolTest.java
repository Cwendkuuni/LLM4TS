import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(eq(1));
        matchers.add(any());
        matchers.add(any(Integer.class));

        Object[] arguments = new Object[]{1, "string", 3};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Integer[] expected = new Integer[]{1, 2}; // Indexes 1 and 2 should be suspicious
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspicious() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(eq(1));
        matchers.add(any());
        matchers.add(any(Integer.class));

        Object[] arguments = new Object[]{1, 2, 3};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Integer[] expected = new Integer[0]; // No suspicious indexes
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{1, 2, 3};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Integer[] expected = new Integer[0]; // No matchers, no suspicious indexes
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentLengths() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(eq(1));

        Object[] arguments = new Object[]{1, 2};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Integer[] expected = new Integer[0]; // Different lengths, no suspicious indexes
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ThrowsException() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[]{1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Integer[] expected = new Integer[]{0}; // Index 0 should be suspicious
        assertArrayEquals(expected, result);
    }
}