import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withEmptyMatchersAndArguments_returnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingSizeOfMatchersAndArguments_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList((Matcher) Matchers.anything());
        Object[] arguments = new Object[]{};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withCompatibleMatcherAndArgument_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList((Matcher) Matchers.equalTo(1));
        Object[] arguments = new Object[]{1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingTypesAndMatchingToString_returnsArrayWithIndex() {
        List<Matcher> matchers = Arrays.asList((Matcher) Matchers.equalTo("1"));
        Object[] arguments = new Object[]{1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingTypesAndDifferentToString_returnsArrayWithIndex() {
        List<Matcher> matchers = Arrays.asList((Matcher) Matchers.startsWith("1"));
        Object[] arguments = new Object[]{1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingTypesWithExtraTypeInformation_returnsArrayWithIndex() {
        List<Matcher> matchers = Arrays.asList((Matcher) Matchers.startsWith("1"));
        Object[] arguments = new Object[]{1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withMismatchingTypesWithoutExtraTypeInformation_returnsEmptyArray() {
        List<Matcher> matchers = Arrays.asList((Matcher) Matchers.equalTo("1"));
        Object[] arguments = new Object[]{1};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_withCountIncompatibleTypeMatchers_returnsArrayWithIndexes() {
        List<Matcher> matchers = Arrays.asList(
                (Matcher) Matchers.equalTo(1),
                (Matcher) Matchers.contains(2, 3),
                (Matcher) Matchers.equalTo("four")
        );
        Object[] arguments = new Object[]{1, Arrays.asList(2, 3, 4), "five"};

        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals(1, result[0].intValue());
        assertEquals(2, result[1].intValue());
        assertEquals(2, result[2].intValue());
    }
}