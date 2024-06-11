import static org.junit.Assert.*;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.*;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.any(Integer.class));
        Object[] arguments = { "test", 123 };

        Integer[] result = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertTrue(result.length == 1);
        assertEquals((int) result[0], 0);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_whenNoMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = { "test", 123 };

        Integer[] result = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertTrue(result.length == 0);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withExtraTypeInformation() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = { 123 };

        Integer[] result = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertTrue(result.length == 1);
        assertEquals((int) result[0], 0);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchedType() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(Matchers.any(Integer.class));
        Object[] arguments = { "test" };

        Integer[] result = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertTrue(result.length == 1);
        assertEquals((int) result[0], 0);
    }

    @Test
    public void testSafelyMatches() {
        Matcher matcher = Matchers.any(Integer.class);
        boolean result = safelyMatches(matcher, "test");
        assertFalse(result);
    }

    @Test
    public void testToStringEquals() {
        Matcher matcher = Matchers.any(Integer.class);
        boolean result = toStringEquals(matcher, "test");
        assertFalse(result);
    }

}