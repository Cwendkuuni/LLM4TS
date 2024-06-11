
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.Arrays;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeDifferentFromArgumentsSize_returnsEmptyArray() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(
                mock(Matcher.class),
                mock(Matcher.class)
        );
        Object[] arguments = new Object[3];

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAreNotContainsExtraTypeInformation_returnsEmptyArray() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(
                mock(Matcher.class),
                mock(Matcher.class)
        );
        Object[] arguments = new Object[] {
                "argument1",
                2
        };

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersTypeMatches_returnsEmptyArray() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(
                mock(ContainsExtraTypeInformation.class)
        );
        Object[] arguments = new Object[] {
                2
        };

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSafelyMatches_returnsEmptyArray() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(
                mock(ContainsExtraTypeInformation.class)
        );
        Object[] arguments = new Object[1];
        when(((ContainsExtraTypeInformation)matchers.get(0)).typeMatches(arguments[0])).thenReturn(true);
        when(matchers.get(0).matches(arguments[0])).thenReturn(true);

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherToStringNotEqualsArgumentToString_returnsEmptyArray() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher<String> matcher = mock(Matcher.class);
        Object[] arguments = new Object[] {
                2
        };
        when(matcher.toString()).thenReturn("matcherToString");

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                Arrays.asList(matcher), arguments);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherToStringEqualsArgumentToString_returnsArrayWithIndex0() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher<String> matcher = mock(Matcher.class);
        Object[] arguments = new Object[] {
                "matcherToString"
        };
        when(matcher.toString()).thenReturn("matcherToString");

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                Arrays.asList(matcher), arguments);

        // Assert
        assertEquals(1, result.length);
        assertEquals(0, (int)result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherSafelyMatchesFalse_returnsArrayWithIndex0() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        Matcher<String> matcher = mock(Matcher.class);
        Object[] arguments = new Object[] {
                "matcherToString"
        };
        when(matcher.toString()).thenReturn("matcherToString");
        when(matcher.matches(arguments[0])).thenReturn(false);

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                Arrays.asList(matcher), arguments);

        // Assert
        assertEquals(1, result.length);
        assertEquals(0, (int)result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherTypeMatchesFalse_returnsArrayWithIndex0() {
        // Arrange
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        Object[] arguments = new Object[] {
                "matcherToString"
        };
        when(matcher.toString()).thenReturn("matcherToString");
        when(matcher.typeMatches(arguments[0])).thenReturn(false);

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(
                Arrays.asList(matcher), arguments);

        // Assert
        assertEquals(1, result.length);
        assertEquals(0, (int)result[0]);
    }
}
