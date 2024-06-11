import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {
    
    private ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchersAndArgumentsLengthDifferent_returnsEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new TestMatcher());
        Object[] arguments = new Object[0];
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsContainsExtraTypeInformationAndMatches_returnEmptyArray() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformationMatcher());
        Object[] arguments = new Object[1];
        arguments[0] = new Object();
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result.length);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_containsContainsExtraTypeInformationAndNotMatches_returnIndex() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformationMatcher());
        Object[] arguments = new Object[1];
        arguments[0] = new TestObject();
        
        Integer[] result = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertEquals(0, result[0].intValue());
    }

    @Test
    public void testSafelyMatches_matcherThrowsException_returnsFalse() {
        TestMatcher matcher = new TestMatcher();

        boolean result = argumentMatchingTool.safelyMatches(matcher, new Object());

        assertFalse(result);
    }

    @Test
    public void testSafelyMatches_matcherReturnsTrue_returnsTrue() {
        TestMatcher matcher = new TestMatcher();

        boolean result = argumentMatchingTool.safelyMatches(matcher, new TestObject());

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_matcherToStringEqualsArgToString_returnsTrue() {
        TestMatcher matcher = new TestMatcher();
        Object arg = new TestObject();

        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_matcherToStringNotEqualsArgToString_returnsFalse() {
        TestMatcher matcher = new TestMatcher();
        Object arg = new Object();

        boolean result = argumentMatchingTool.toStringEquals(matcher, arg);

        assertFalse(result);
    }

    private static class TestMatcher implements Matcher {

        @Override
        public boolean matches(Object o) {
            return o instanceof TestObject;
        }

        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {
            
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
            
        }

        @Override
        public void describeTo(Description description) {
            
        }
    }
    
    private static class ContainsExtraTypeInformationMatcher extends ContainsExtraTypeInformation {
        @Override
        public boolean matches(Object o) {
            return o instanceof TestObject;
        }
        
        @Override
        public void describeMismatch(Object item, Description mismatchDescription) {
            
        }

        @Override
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
            
        }

        @Override
        public void describeTo(Description description) {
            
        }
    }

    private static class TestObject {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestObject;
        }

        @Override
        public String toString() {
            return "TestObject";
        }
    }
}