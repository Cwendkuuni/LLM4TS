import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchSizeNotEqual_ThenReturnEmptyArray() {
        // Prepare
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2, 3 };
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersContainExtraTypeInformation_AndNotMatches_AndToStringEquals_AndTypeNotMatches_ThenReturnIndexes() {
        // Prepare
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2 };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        ContainsExtraTypeInformation matcher1 = (ContainsExtraTypeInformation) matchers.get(0);
        when(matcher1.typeMatches(arguments[0])).thenReturn(false);
        when(matcher1.matches(arguments[0])).thenReturn(false);
        when(StringDescription.toString(matcher1)).thenReturn(arguments[0].toString());
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(1, indexes.length);
        assertEquals(0, indexes[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersContainExtraTypeInformation_AndNotMatches_AndToStringEquals_AndTypeMatches_ThenReturnEmptyArray() {
        // Prepare
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2 };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        ContainsExtraTypeInformation matcher1 = (ContainsExtraTypeInformation) matchers.get(0);
        when(matcher1.typeMatches(arguments[0])).thenReturn(true);
        when(matcher1.matches(arguments[0])).thenReturn(false);
        when(StringDescription.toString(matcher1)).thenReturn(arguments[0].toString());
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersContainExtraTypeInformation_AndMatches_ThenReturnEmptyArray() {
        // Prepare
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2 };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        ContainsExtraTypeInformation matcher1 = (ContainsExtraTypeInformation) matchers.get(0);
        when(matcher1.matches(arguments[0])).thenReturn(true);
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersContainExtraTypeInformation_AndMatches_AndToStringNotEquals_ThenReturnEmptyArray() {
        // Prepare
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(ContainsExtraTypeInformation.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2 };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        ContainsExtraTypeInformation matcher1 = (ContainsExtraTypeInformation) matchers.get(0);
        when(matcher1.matches(arguments[0])).thenReturn(true);
        when(StringDescription.toString(matcher1)).thenReturn(arguments[0].toString() + " extra info");
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersDoesNotContainExtraTypeInformation_AndNotMatches_ThenReturnEmptyArray() {
        // Prepare
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2 };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        when(matchers.get(0).matches(arguments[0])).thenReturn(false);
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersDoesNotContainExtraTypeInformation_AndMatches_ThenReturnEmptyArray() {
        // Prepare
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2 };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        when(matchers.get(0).matches(arguments[0])).thenReturn(true);
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersDoesNotContainExtraTypeInformation_AndNotMatches_AndToStringEquals_ThenReturnEmptyArray() {
        // Prepare
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2 };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        when(matchers.get(0).matches(arguments[0])).thenReturn(false);
        when(StringDescription.toString(matchers.get(0))).thenReturn(arguments[0].toString());
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersDoesNotContainExtraTypeInformation_AndNotMatches_AndToStringNotEquals_ThenReturnEmptyArray() {
        // Prepare
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2 };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        when(matchers.get(0).matches(arguments[0])).thenReturn(false);
        when(StringDescription.toString(matchers.get(0))).thenReturn(arguments[0].toString() + " extra info");
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(0, indexes.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_WhenMatchersDoesNotContainExtraTypeInformation_AndMatches_AndToStringNotEquals_ThenReturnEmptyArray() {
        // Prepare
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        Object[] arguments = new Object[] { 1, 2 };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        when(matchers.get(0).matches(arguments[0])).thenReturn(true);
        when(StringDescription.toString(matchers.get(0))).thenReturn(arguments[0].toString() + " extra info");
        
        // Execute
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        // Verify
        assertEquals(0, indexes.length);
    }
    
    @Test
    public void testSafelyMatches_WhenMatcherThrowsException_ThenReturnFalse() {
        // Prepare
        Matcher matcher = mock(Matcher.class);
        Object arg = new Object();
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        doThrow(Exception.class).when(matcher).matches(arg);
        
        // Execute
        boolean result = tool.safelyMatches(matcher, arg);
        
        // Verify
        assertFalse(result);
    }
    
    @Test
    public void testSafelyMatches_WhenMatcherDoesNotThrowException_ThenReturnResultOfMatches() {
        // Prepare
        Matcher matcher = mock(Matcher.class);
        Object arg = new Object();
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        when(matcher.matches(arg)).thenReturn(true);
        
        // Execute
        boolean result = tool.safelyMatches(matcher, arg);
        
        // Verify
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_WhenToStringEquals_ThenReturnTrue() {
        // Prepare
        Matcher matcher = mock(Matcher.class);
        Object arg = new Object();
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        when(StringDescription.toString(matcher)).thenReturn(arg.toString());
        
        // Execute
        boolean result = tool.toStringEquals(matcher, arg);
        
        // Verify
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_WhenToStringNotEquals_ThenReturnFalse() {
        // Prepare
        Matcher matcher = mock(Matcher.class);
        Object arg = new Object();
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        when(StringDescription.toString(matcher)).thenReturn(arg.toString() + " extra info");
        
        // Execute
        boolean result = tool.toStringEquals(matcher, arg);
        
        // Verify
        assertFalse(result);
    }

}