import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    private ArgumentMatchingTool argumentMatchingTool;
    
    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersSizeDifferent_ReturnEmptyArray() {
        List<Matcher> matchers = Arrays.asList(mock(Matcher.class), mock(Matcher.class), mock(Matcher.class));
        Object[] arguments = new Object[2];
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NotContainsExtraTypeInformation_ReturnEmptyArray() {
        ContainsExtraTypeInformation matcher1 = mock(ContainsExtraTypeInformation.class);
        when(matcher1.typeMatches(any())).thenReturn(true);
        ContainsExtraTypeInformation matcher2 = mock(ContainsExtraTypeInformation.class);
        when(matcher2.typeMatches(any())).thenReturn(true);
        ContainsExtraTypeInformation matcher3 = mock(ContainsExtraTypeInformation.class);
        when(matcher3.typeMatches(any())).thenReturn(true);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        Object[] arguments = new Object[3];
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SafelyMatches_ReturnEmptyArray() {
        ContainsExtraTypeInformation matcher1 = mock(ContainsExtraTypeInformation.class);
        when(matcher1.typeMatches(any())).thenReturn(false);
        ContainsExtraTypeInformation matcher2 = mock(ContainsExtraTypeInformation.class);
        when(matcher2.typeMatches(any())).thenReturn(false);
        ContainsExtraTypeInformation matcher3 = mock(ContainsExtraTypeInformation.class);
        when(matcher3.typeMatches(any())).thenReturn(false);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        Object[] arguments = new Object[3];
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringEquals_ReturnEmptyArray() {
        ContainsExtraTypeInformation matcher1 = mock(ContainsExtraTypeInformation.class);
        when(matcher1.typeMatches(any())).thenReturn(false);
        when(StringDescription.toString(matcher1)).thenReturn("matcher1");
        ContainsExtraTypeInformation matcher2 = mock(ContainsExtraTypeInformation.class);
        when(matcher2.typeMatches(any())).thenReturn(false);
        when(StringDescription.toString(matcher2)).thenReturn("matcher2");
        ContainsExtraTypeInformation matcher3 = mock(ContainsExtraTypeInformation.class);
        when(matcher3.typeMatches(any())).thenReturn(false);
        when(StringDescription.toString(matcher3)).thenReturn("matcher3");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        Object[] arguments = new Object[3];
        arguments[0] = "matcher1";
        arguments[1] = "matcher2";
        arguments[2] = "matcher3";
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_TypeDoesNotMatch_ReturnArrayWithIndexes() {
        ContainsExtraTypeInformation matcher1 = mock(ContainsExtraTypeInformation.class);
        when(matcher1.typeMatches(any())).thenReturn(false);
        when(StringDescription.toString(matcher1)).thenReturn("matcher1");
        ContainsExtraTypeInformation matcher2 = mock(ContainsExtraTypeInformation.class);
        when(matcher2.typeMatches(any())).thenReturn(false);
        when(StringDescription.toString(matcher2)).thenReturn("matcher2");
        ContainsExtraTypeInformation matcher3 = mock(ContainsExtraTypeInformation.class);
        when(matcher3.typeMatches(any())).thenReturn(false);
        when(StringDescription.toString(matcher3)).thenReturn("matcher3");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2, matcher3);
        Object[] arguments = new Object[3];
        arguments[0] = "matcher1";
        arguments[1] = "matcher2";
        arguments[2] = "matcher3";
        when(matcher1.matches("matcher1")).thenReturn(true);
        when(matcher2.matches("matcher2")).thenReturn(false);
        when(matcher3.matches("matcher3")).thenReturn(true);
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(1, result[0].intValue());
    }
}