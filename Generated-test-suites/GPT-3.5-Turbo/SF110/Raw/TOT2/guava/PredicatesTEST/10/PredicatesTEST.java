import static org.junit.Assert.*;
import org.junit.Test;
import java.util.regex.Pattern;
import java.util.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
        assertTrue(alwaysTrue.apply(10));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<String> alwaysFalse = Predicates.alwaysFalse();
        assertFalse(alwaysFalse.apply("Test"));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> isNull = Predicates.isNull();
        assertTrue(isNull.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<Object> notNull = Predicates.notNull();
        assertFalse(notNull.apply(null));
    }

    @Test
    public void testNot() {
        Predicate<Integer> greaterThanFive = num -> num > 5;
        Predicate<Integer> notGreaterThanFive = Predicates.not(greaterThanFive);
        assertTrue(notGreaterThanFive.apply(3));
    }

    @Test
    public void testAnd() {
        Predicate<Integer> divisibleByTwo = num -> num % 2 == 0;
        Predicate<Integer> greaterThanFive = num -> num > 5;
        Predicate<Integer> combinedPredicate = Predicates.and(divisibleByTwo, greaterThanFive);
        assertFalse(combinedPredicate.apply(4));
    }

    @Test
    public void testOr() {
        Predicate<Integer> divisibleByTwo = num -> num % 2 == 0;
        Predicate<Integer> greaterThanFive = num -> num > 5;
        Predicate<Integer> combinedPredicate = Predicates.or(divisibleByTwo, greaterThanFive);
        assertTrue(combinedPredicate.apply(6));
    }

    @Test
    public void testEqualTo() {
        Predicate<String> equalToApple = Predicates.equalTo("apple");
        assertTrue(equalToApple.apply("apple"));
    }

    @Test
    public void testInstanceOf() {
        Predicate<Object> instanceOfInteger = Predicates.instanceOf(Integer.class);
        assertTrue(instanceOfInteger.apply(10));
    }

    @Test
    public void testAssignableFrom() {
        Predicate<Class<?>> assignableFromClassNumber = Predicates.assignableFrom(Number.class);
        assertTrue(assignableFromClassNumber.apply(Double.class));
    }

    @Test
    public void testIn() {
        Collection<Integer> numberCollection = Arrays.asList(1, 2, 3, 4, 5);
        Predicate<Integer> inCollection = Predicates.in(numberCollection);
        assertTrue(inCollection.apply(3));
    }

    @Test
    public void testCompose() {
        Predicate<Integer> greaterThanThree = num -> num > 3;
        Function<Integer, Integer> squareFunction = num -> num * num;
        Predicate<Integer> composedPredicate = Predicates.compose(greaterThanThree, squareFunction);
        assertTrue(composedPredicate.apply(5));
    }

    @Test
    public void testContainsPatternFromString() {
        Predicate<CharSequence> containsPattern = Predicates.containsPattern("^[a-zA-Z]*$");
        assertTrue(containsPattern.apply("Test"));
    }

    @Test
    public void testContains() {
        Predicate<CharSequence> containsPattern = Predicates.contains(Pattern.compile("\\d"));
        assertTrue(containsPattern.apply("Test123"));
    }

}