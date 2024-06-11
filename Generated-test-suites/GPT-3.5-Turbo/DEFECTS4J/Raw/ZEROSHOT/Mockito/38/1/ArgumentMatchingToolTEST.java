import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool.*;
import org.junit.Test;
import org.hamcrest.Matcher;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeNotEqualToArgumentsLength_returnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[2];

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainsContainsExtraTypeInformation_returnsSuspiciousIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[2];
        arguments[0] = "string 1";
        arguments[1] = 10;

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(1, result.length);
        assertEquals(1, (int) result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainsContainsExtraTypeInformationButMatches_returnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[2];
        arguments[0] = "string 1";
        arguments[1] = "string 2";

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersContainsContainsExtraTypeInformationButTypesMatch_returnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());

        Object[] arguments = new Object[2];
        arguments[0] = "string 1";
        arguments[1] = "10";

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testSafelyMatches_matcherThrowsException_returnsFalse() {
        Matcher matcher = mock(Matcher.class);
        Object arg = "string";

        when(matcher.matches(arg)).thenThrow(Exception.class);

        boolean result = safelyMatches(matcher, arg);

        assertFalse(result);
    }

    @Test
    public void testSafelyMatches_matcherMatches_returnsTrue() {
        Matcher matcher = mock(Matcher.class);
        Object arg = "string";

        when(matcher.matches(arg)).thenReturn(true);

        boolean result = safelyMatches(matcher, arg);

        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_matcherDoesNotMatch_returnsFalse() {
        Matcher matcher = mock(Matcher.class);
        Object arg = "string";

        when(matcher.matches(arg)).thenReturn(false);

        boolean result = safelyMatches(matcher, arg);

        assertFalse(result);
    }

    @Test
    public void testToStringEquals_matcherToStringEqualsArgToString_returnsTrue() {
        Matcher matcher = mock(Matcher.class);
        Object arg = "string";

        when(StringDescription.toString(matcher)).thenReturn("string");

        boolean result = toStringEquals(matcher, arg);

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_matcherToStringNotEqualsArgToString_returnsFalse() {
        Matcher matcher = mock(Matcher.class);
        Object arg = "string";

        when(StringDescription.toString(matcher)).thenReturn("stream");

        boolean result = toStringEquals(matcher, arg);

        assertFalse(result);
    }

    @Test
    public void testToStringEquals_matcherToStringNull_returnsFalse() {
        Matcher matcher = mock(Matcher.class);
        Object arg = "string";

        when(StringDescription.toString(matcher)).thenReturn(null);

        boolean result = toStringEquals(matcher, arg);

        assertFalse(result);
    }

}