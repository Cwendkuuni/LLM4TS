import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {
    
    private ArgumentMatchingTool argumentMatchingTool;
    
    @Before
    public void setUp() {
        argumentMatchingTool = new ArgumentMatchingTool();
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersSizeDifferentFromArgumentsLength_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[1];
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherIsInstanceOfContainsExtraTypeInformationTypeMatchesReturnsFalse_ReturnsIndex() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(1, result.length);
        assertEquals(0, result[0].intValue());
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatcherIsNotInstanceOfContainsExtraTypeInformation_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new Matcher() {
            
            @Override
            public void describeTo(org.hamcrest.Description arg0) {
                
            }
            
            @Override
            public boolean matches(Object arg0) {
                return false;
            }
        });
        Object[] arguments = new Object[1];
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SafelyMatchesThrowsException_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringEqualsReturnsFalse_ReturnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation());
        Object[] arguments = new Object[1];
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testSafelyMatches_ExceptionThrown_ReturnsFalse() {
        Matcher matcher = new Matcher() {
            
            @Override
            public void describeTo(org.hamcrest.Description arg0) {
                
            }
            
            @Override
            public boolean matches(Object arg0) {
                throw new NullPointerException();
            }
        };
        Object arg = new Object();
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);
        
        assertFalse(result);
    }
    
    @Test
    public void testSafelyMatches_MatcherMatches_ReturnsTrue() {
        Matcher matcher = new Matcher() {
            
            @Override
            public void describeTo(org.hamcrest.Description arg0) {
                
            }
            
            @Override
            public boolean matches(Object arg0) {
                return true;
            }
        };
        Object arg = new Object();
        
        boolean result = argumentMatchingTool.safelyMatches(matcher, arg);
        
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_ToStringEquals_ReturnsTrue() {
        Matcher matcher = new Matcher() {
            
            @Override
            public void describeTo(org.hamcrest.Description arg0) {
                
            }
            
            @Override
            public boolean matches(Object arg0) {
                return false;
            }
            
            @Override
            public String toString() {
                return "Mockito";
            }
        };
        Object arg = "Mockito";
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);
        
        assertTrue(result);
    }
    
    @Test
    public void testToStringEquals_ToStringNotEquals_ReturnsFalse() {
        Matcher matcher = new Matcher() {
            
            @Override
            public void describeTo(org.hamcrest.Description arg0) {
                
            }
            
            @Override
            public boolean matches(Object arg0) {
                return false;
            }
            
            @Override
            public String toString() {
                return "Mockito";
            }
        };
        Object arg = "Other";
        
        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);
        
        assertFalse(result);
    }
}