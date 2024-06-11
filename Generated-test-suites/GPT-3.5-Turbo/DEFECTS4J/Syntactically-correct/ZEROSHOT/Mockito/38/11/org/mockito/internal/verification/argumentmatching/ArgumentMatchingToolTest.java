import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

public class ArgumentMatchingToolTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsHaveDifferentSizes_ReturnsEmptyArray() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{1, 2, 3};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousArgs_ReturnsEmptyArray() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        
        Object[] arguments = new Object[]{1, "string", true};
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationAndSafelyMatches_ReturnsSuspiciousArgIndex() {
        // Arrange
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        Object[] arguments = new Object[]{1};
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationAndDoesNotSafelyMatches_ReturnsEmptyArray() {
        // Arrange
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.matches(any())).thenThrow(new RuntimeException());
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = new Object[]{1};
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationAndToStringEquals_ReturnsSuspiciousArgIndex() {
        // Arrange
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.toString()).thenReturn("1");
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = new Object[]{1};
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationAndDoesNotToStringEquals_ReturnsEmptyArray() {
        // Arrange
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.toString()).thenReturn("1");
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = new Object[]{2};
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationAndTypeDoesNotMatch_ReturnsSuspiciousArgIndex() {
        // Arrange
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.matches(any())).thenReturn(true);
        when(matcher.typeMatches(any())).thenReturn(false);
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = new Object[]{1};
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(0), result[0]);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ContainsExtraTypeInformationAndTypeMatches_ReturnsEmptyArray() {
        // Arrange
        ContainsExtraTypeInformation matcher = mock(ContainsExtraTypeInformation.class);
        when(matcher.matches(any())).thenReturn(true);
        when(matcher.typeMatches(any())).thenReturn(true);
        
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcher);
        Object[] arguments = new Object[]{1};
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        // Act
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Assert
        assertEquals(0, result.length);
    }
}