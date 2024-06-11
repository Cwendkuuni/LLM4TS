
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = Arrays.asList(null, null, null);
        Object[] arguments = new Object[3];

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMismatchedTypes() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(String.class), null, null);
        Object[] arguments = new Object[] { 1, null, null };

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals((Integer) 0, result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithToStringEquals() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(String.class), null, null);
        Object[] arguments = new Object[] { "1", null, null };

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithTypeMatches() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(String.class), null, null);
        Object[] arguments = new Object[] { "1", 1, null };

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals((Integer) 1, result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithThrowable() {
        List<Matcher> matchers = Arrays.asList(new ContainsExtraTypeInformation(String.class), null, null);
        Object[] arguments = new Object[] { "1", new Throwable(), null };

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals((Integer) 1, result[0]);
    }
    
}