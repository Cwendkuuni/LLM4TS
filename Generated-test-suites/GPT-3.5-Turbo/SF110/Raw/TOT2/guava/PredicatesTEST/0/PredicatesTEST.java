import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;
import java.util.*;

public class PredicatesTest {
    
    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply("test"));
    }
    
    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicates.alwaysFalse().apply("test"));
    }
    
    @Test
    public void testIsNull() {
        assertTrue(Predicates.isNull().apply(null));
        assertFalse(Predicates.isNull().apply("test"));
    }
    
    @Test
    public void testNotNull() {
        assertFalse(Predicates.notNull().apply(null));
        assertTrue(Predicates.notNull().apply("test"));
    }
    
    @Test
    public void testNot() {
        Predicate<String> predicate = s -> s.length() > 5;
        Predicate<String> notPredicate = Predicates.not(predicate);
        assertTrue(notPredicate.apply("test"));
        assertFalse(notPredicate.apply("testing"));
    }
    
    @Test
    public void testAndIterable() {
        List<Predicate<String>> components = Arrays.asList(s -> s.length() > 3, s -> s.contains("test"));
        Predicate<String> andPredicate = Predicates.and(components);
        assertTrue(andPredicate.apply("testing"));
        assertFalse(andPredicate.apply("test"));
    }
    
    @Test
    public void testAndVarArgs() {
        Predicate<String> andPredicate = Predicates.and(s -> s.length() > 3, s -> s.contains("test"));
        assertTrue(andPredicate.apply("testing"));
        assertFalse(andPredicate.apply("test"));
    }
    
    @Test
    public void testAndTwoPredicates() {
        Predicate<String> andPredicate = Predicates.and(s -> s.length() > 3, s -> s.contains("test"));
        assertTrue(andPredicate.apply("testing"));
        assertFalse(andPredicate.apply("test"));
    }
    
    @Test
    public void testOrIterable() {
        List<Predicate<String>> components = Arrays.asList(s -> s.length() > 3, s -> s.contains("test"));
        Predicate<String> orPredicate = Predicates.or(components);
        assertTrue(orPredicate.apply("testing"));
        assertTrue(orPredicate.apply("test"));
        assertFalse(orPredicate.apply("t"));
    }
    
    @Test
    public void testOrVarArgs() {
        Predicate<String> orPredicate = Predicates.or(s -> s.length() > 3, s -> s.contains("test"));
        assertTrue(orPredicate.apply("testing"));
        assertTrue(orPredicate.apply("test"));
        assertFalse(orPredicate.apply("t"));
    }
    
    @Test
    public void testOrTwoPredicates() {
        Predicate<String> orPredicate = Predicates.or(s -> s.length() > 3, s -> s.contains("test"));
        assertTrue(orPredicate.apply("testing"));
        assertTrue(orPredicate.apply("test"));
        assertFalse(orPredicate.apply("t"));
    }
    
    @Test
    public void testEqualTo() {
        Predicate<String> equalToPredicate = Predicates.equalTo("test");
        assertTrue(equalToPredicate.apply("test"));
        assertFalse(equalToPredicate.apply("testing"));
    }
    
    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfPredicate = Predicates.instanceOf(String.class);
        assertTrue(instanceOfPredicate.apply("test"));
        assertFalse(instanceOfPredicate.apply(1));
    }
    
    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromPredicate = Predicates.assignableFrom(String.class);
        assertTrue(assignableFromPredicate.apply(String.class));
        assertFalse(assignableFromPredicate.apply(Integer.class));
    }
    
    @Test
    public void testIn() {
        Set<String> set = new HashSet<>(Arrays.asList("test1", "test2", "test3"));
        Predicate<String> inPredicate = Predicates.in(set);
        assertTrue(inPredicate.apply("test1"));
        assertFalse(inPredicate.apply("test4"));
    }
    
    @Test
    public void testCompose() {
        Predicate<String> lengthPredicate = s -> s.length() > 3;
        Function<String, Integer> lengthFunction = String::length;
        Predicate<String> composedPredicate = Predicates.compose(lengthPredicate, lengthFunction);
        assertTrue(composedPredicate.apply("testing"));
        assertFalse(composedPredicate.apply("test"));
    }
    
    @Test
    public void testContainsPattern() {
        Predicate<CharSequence> containsPatternPredicate = Predicates.containsPattern("test");
        assertTrue(containsPatternPredicate.apply("testing"));
        assertFalse(containsPatternPredicate.apply("example"));
    }
    
    @Test
    public void testContains() {
        Predicate<CharSequence> containsPredicate = Predicates.contains(Pattern.compile("test"));
        assertTrue(containsPredicate.apply("testing"));
        assertFalse(containsPredicate.apply("example"));
    }
    
    @Test
    public void testAsStringWithTwoPredicates() {
        Predicate<String> andPredicate = Predicates.and(s -> s.length() > 3, s -> s.length() < 10);
        assertTrue(andPredicate.apply("testing"));
        assertFalse(andPredicate.apply("test"));
    }
}