import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.hamcrest.Matcher;
import java.util.List;
import java.util.LinkedList;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;

    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }

    // Test getSuspiciouslyNotMatchingArgsIndexes when matchers size is not equal to arguments length
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_InvalidInput() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[3];
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }

    // Test getSuspiciouslyNotMatchingArgsIndexes when all matchers match the arguments
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_AllMatch() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[2];
        arguments[0] = "ABC";
        arguments[1] = "DEF";

        // Stubbing matchers to always return true
        when(matchers.get(0).matches(arguments[0])).thenReturn(true);
        when(matchers.get(1).matches(arguments[1])).thenReturn(true);

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(0, result.length);
    }

    // Test getSuspiciouslyNotMatchingArgsIndexes when a matcher does not match the argument
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NotMatch() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        matchers.add(matcher);
        Object[] arguments = new Object[1];
        arguments[0] = "ABC";

        // Stubbing matchers to return false
        when(matcher.matches(arguments[0])).thenReturn(false);

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }

    // Test getSuspiciouslyNotMatchingArgsIndexes when a matcher is of type ContainsExtraTypeInformation and typeMatches returns false
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformation_NotTypeMatch() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        matchers.add(matcher);
        Object[] arguments = new Object[1];
        arguments[0] = "ABC";

        // Stubbing matchers to return true for matches and false for typeMatches
        when(matcher.matches(arguments[0])).thenReturn(true);
        when(matcher.typeMatches(arguments[0])).thenReturn(false);

        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Assert
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }

    // Test safelyMatches when matcher matches throws an exception
    @Test
    public void testSafelyMatches_MatcherException() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        Object arg = "ABC";

        // Stubbing matcher to throw an exception
        when(matcher.matches(arg)).thenThrow(new RuntimeException());

        // Act
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);

        // Assert
        assertFalse(result);
    }

    // Test safelyMatches when matcher matches does not throw an exception
    @Test
    public void testSafelyMatches_NoMatcherException() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        Object arg = "ABC";

        // Stubbing matcher to return true
        when(matcher.matches(arg)).thenReturn(true);

        // Act
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);

        // Assert
        assertTrue(result);
    }

    // Test toStringEquals when matcher toString is equal to argument toString
    @Test
    public void testToStringEquals_EqualToString() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        Object arg = "ABC";

        // Stubbing matcher toString to return "ABC"
        when(matcher.toString()).thenReturn("ABC");

        // Act
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);

        // Assert
        assertTrue(result);
    }

    // Test toStringEquals when matcher toString is not equal to argument toString
    @Test
    public void testToStringEquals_NotEqualToString() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        Object arg = "ABC";

        // Stubbing matcher toString to return "XYZ"
        when(matcher.toString()).thenReturn("XYZ");

        // Act
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);

        // Assert
        assertFalse(result);
    }

}