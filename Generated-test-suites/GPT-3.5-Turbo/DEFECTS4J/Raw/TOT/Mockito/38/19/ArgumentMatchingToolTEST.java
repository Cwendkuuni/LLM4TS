import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
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
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersSizeIsNotEqualToArgumentsLength_ReturnsEmptyArray() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class));
        Object[] arguments = new Object[2];

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenAllMatchersAreNotContainsExtraTypeInformation_ReturnsEmptyArray() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(eq(1));
        matchers.add(eq("test"));
        Object[] arguments = new Object[]{1, "test"};

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenContainsExtraTypeInformationMatcherDoesNotMatch_ReturnsArrayWithIndexes() {
        // Arrange
        ContainsExtraTypeInformation matcher1 = mock(ContainsExtraTypeInformation.class);
        ContainsExtraTypeInformation matcher2 = mock(ContainsExtraTypeInformation.class);
        when(matcher1.typeMatches(1)).thenReturn(true);
        when(matcher2.typeMatches("test")).thenReturn(false);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        Object[] arguments = new Object[]{1, "test"};

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(1, result.length);
        assertEquals(1, (int)result[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenContainsExtraTypeInformationMatcherMatches_ReturnsEmptyArray() {
        // Arrange
        ContainsExtraTypeInformation matcher1 = mock(ContainsExtraTypeInformation.class);
        ContainsExtraTypeInformation matcher2 = mock(ContainsExtraTypeInformation.class);
        when(matcher1.typeMatches(1)).thenReturn(true);
        when(matcher2.typeMatches("test")).thenReturn(true);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        Object[] arguments = new Object[]{1, "test"};

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testSafelyMatches_WhenMatcherMatches_ReturnsTrue() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        Object arg = 1;
        when(matcher.matches(arg)).thenReturn(true);

        // Act
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_WhenMatcherThrowsException_ReturnsFalse() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        Object arg = 1;
        when(matcher.matches(arg)).thenThrow(new RuntimeException());

        // Act
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testToStringEquals_WhenMatcherToStringIsEqualToArgumentToString_ReturnsTrue() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        Object arg = 1;
        when(StringDescription.toString(matcher)).thenReturn("1");

        // Act
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testToStringEquals_WhenMatcherToStringIsNotEqualToArgumentToString_ReturnsFalse() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        Object arg = 1;
        when(StringDescription.toString(matcher)).thenReturn("2");

        // Act
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);

        // Assert
        assertFalse(result);
    }
}