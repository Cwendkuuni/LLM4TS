import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {
    
    private ArgumentMatchingTool argumentMatchingTool;
    
    @Before
    public void setup() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_lengthMismatch() {
        List<Matcher> matchers = List.of(any(), any(), any());
        Object[] arguments = new Object[] { 1, 2 };
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousArgs() {
        List<Matcher> matchers = List.of((Matcher) equalTo("mockito"), (Matcher) instanceOf(Integer.class));
        Object[] arguments = new Object[] { "mockito", 10 };
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsSuspiciousArg() {
        List<Matcher> matchers = List.of(
                (Matcher) new ContainsExtraTypeInformation("mockito", String.class),
                (Matcher) new ContainsExtraTypeInformation(10, Integer.class)
        );
        Object[] arguments = new Object[] { "mockito", 20 };
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(Integer.valueOf(1), result[0]);
    }
    
    @Test
    public void testSafelyMatches_matches() {
        Matcher matcher = equalTo("mockito");
        Object argument = "mockito";
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, argument);
        
        assertTrue(result);
    }
    
    @Test
    public void testSafelyMatches_doesNotMatch() {
        Matcher matcher = equalTo("mockito");
        Object argument = 10;
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, argument);
        
        assertFalse(result);
    }
    
    @Test
    public void testToStringEquals_equals() {
        Matcher matcher = equalTo("mockito");
        Object argument = "mockito";
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, argument);
        
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_notEquals() {
        Matcher matcher = equalTo("mockito");
        Object argument = "junit";
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, argument);
        
        assertFalse(result);
    }
}