import org.junit.Test;
import org.hamcrest.Matcher;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.containsExtraTypeInfo;
import static org.mockito.internal.util.ArgumentMatcherStorage.ARGMATCHER_STORAGE;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsDifferentSize_ReturnsEmptyArray() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                argThat(is("String 1")),
                argThat(is("String 2"))
        );
        Object[] arguments = new Object[] {"String 1"};

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertThat(result.length, is(0));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherDoesNotContainExtraTypeInformation_ReturnsEmptyArray() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                argThat(containsExtraTypeInfo(false)),
                argThat(containsExtraTypeInfo(false))
        );
        Object[] arguments = new Object[] {5, 10};

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertThat(result.length, is(0));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherSafelyMatchesArgument_ReturnsEmptyArray() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                argThat(is(5)),
                argThat(is(10))
        );
        Object[] arguments = new Object[] {5, 10};

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertThat(result.length, is(0));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherDoesNotToStringEqualArgument_ReturnsEmptyArray() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                argThat(is(5)),
                argThat(is("10"))
        );
        Object[] arguments = new Object[] {5, 10};

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertThat(result.length, is(0));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherDoesNotTypeMatchArgument_ReturnsIndexes() {
        // Arrange
        List<Matcher> matchers = Arrays.asList(
                argThat(is("String 1")),
                argThat(containsExtraTypeInfo(true))
        );
        Object[] arguments = new Object[] {5, "String 2"};

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertThat(result, arrayContaining(1));
    }
}