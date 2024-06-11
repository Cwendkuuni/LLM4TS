import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;
    
    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersSizeNotEqualToArgumentsLength_returnsEmptyArray() {
        // Arrange
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] {1, 2, 3};
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndMatchesAndToStringEqualsAndTypeMatches_returnsCorrectIndexes() {
        // Arrange
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.typeMatches(any())).thenReturn(true);
        when(matcher.matches(any())).thenReturn(true);
        when(matcher.toString()).thenReturn("TestMatcher");
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        
        Object[] arguments = new Object[] {"Test String"};
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndDoesNotMatch_returnsCorrectIndexes() {
        // Arrange
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.typeMatches(any())).thenReturn(false);
        when(matcher.matches(any())).thenReturn(false);
        when(matcher.toString()).thenReturn("TestMatcher");
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        
        Object[] arguments = new Object[] {"Test String"};
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsExtraTypeInformationAndMatchesButToStringNotEquals_returnsCorrectIndexes() {
        // Arrange
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.typeMatches(any())).thenReturn(true);
        when(matcher.matches(any())).thenReturn(true);
        when(matcher.toString()).thenReturn("TestMatcher");
        
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        
        Object[] arguments = new Object[] {1};
        
        // Act
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testSafelyMatches_matcherThrowsException_returnsFalse() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        when(matcher.matches(any())).thenThrow(RuntimeException.class);
        
        // Act
        boolean result = argumentMatchingTool.safelyMatches(matcher, "Test");
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testSafelyMatches_matcherDoesNotThrowException_returnsTrue() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        when(matcher.matches(any())).thenReturn(true);
        
        // Act
        boolean result = argumentMatchingTool.safelyMatches(matcher, "Test");
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_matcherToStringEqualsArgumentToString_returnsTrue() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        when(matcher.toString()).thenReturn("TestMatcher");
        
        // Act
        boolean result = argumentMatchingTool.toStringEquals(matcher, "TestMatcher");
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_matcherToStringNotEqualsArgumentToString_returnsFalse() {
        // Arrange
        Matcher matcher = mock(Matcher.class);
        when(matcher.toString()).thenReturn("TestMatcher");
        
        // Act
        boolean result = argumentMatchingTool.toStringEquals(matcher, "Different");
        
        // Assert
        assertFalse(result);
    }
}