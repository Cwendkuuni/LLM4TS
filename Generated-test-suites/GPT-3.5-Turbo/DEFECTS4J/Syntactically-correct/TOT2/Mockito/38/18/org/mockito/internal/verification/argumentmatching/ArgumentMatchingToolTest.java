import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(any(Integer.class));
        matchers.add(any(String.class));

        Object[] arguments = {1, "hello"};

        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentTypes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(any(Integer.class));
        matchers.add(any(String.class));

        Object[] arguments = {1L, "hello"};

        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, indexes.length);
        assertEquals(0, indexes[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatchers() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();

        Object[] arguments = {1, "hello"};

        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArguments() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(any(Integer.class));
        matchers.add(any(String.class));

        Object[] arguments = null;

        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedSizes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(any(Integer.class));

        Object[] arguments = {1, "hello"};

        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ExceptionHandling() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        List<Matcher> matchers = new LinkedList<>();
        matchers.add(null);

        Object[] arguments = {1, "hello"};

        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, indexes.length);
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        Matcher matcher = any(Integer.class);
        Object arg = 1;

        assertFalse(tool.toStringEquals(matcher, arg));
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();

        Matcher matcher = any(Integer.class);
        Object arg = 1;

        assertTrue(tool.safelyMatches(matcher, arg));
    }

}