import org.junit.Test;
import org.junit.Assert;
import java.util.regex.*;
import java.util.*;
import java.io.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Assert.assertTrue(Predicates.alwaysTrue().apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        Assert.assertFalse(Predicates.alwaysFalse().apply(new Object()));
    }

    @Test
    public void testIsNull() {
        Assert.assertTrue(Predicates.isNull().apply(null));
    }

    @Test
    public void testNotNull() {
        Assert.assertFalse(Predicates.notNull().apply(null));
    }

    @Test
    public void testNot() {
        Predicate<Object> predicate = Predicates.notNull();
        Assert.assertTrue(Predicates.not(predicate).apply(null));
    }

    @Test
    public void testAnd() {
        Predicate<Object> predicate1 = Predicates.notNull();
        Predicate<Object> predicate2 = Predicates.notNull();
        Assert.assertTrue(Predicates.and(predicate1, predicate2).apply(new Object()));
    }

    @Test
    public void testOr() {
        Predicate<Object> predicate1 = Predicates.notNull();
        Predicate<Object> predicate2 = Predicates.notNull();
        Assert.assertTrue(Predicates.or(predicate1, predicate2).apply(new Object()));
    }

    @Test
    public void testEqualTo() {
        Assert.assertTrue(Predicates.equalTo("test").apply("test"));
    }

    @Test
    public void testInstanceOf() {
        Assert.assertTrue(Predicates.instanceOf(String.class).apply("test"));
    }

    @Test
    public void testAssignableFrom() {
        Assert.assertTrue(Predicates.assignableFrom(Object.class).apply(String.class));
    }

    @Test
    public void testIn() {
        List<String> list = new ArrayList<String>();
        list.add("test");
        Assert.assertTrue(Predicates.in(list).apply("test"));
    }

    @Test
    public void testCompose() {
        Predicate<String> predicate = Predicates.equalTo("test");
        Function<String, Integer> function = Integer::parseInt;
        Assert.assertTrue(Predicates.compose(predicate, function).apply("test"));
    }

    @Test
    public void testContainsPattern() {
        Assert.assertTrue(Predicates.containsPattern("test").apply("test"));
    }

    @Test
    public void testContains() {
        Pattern pattern = Pattern.compile("test");
        Assert.assertTrue(Predicates.contains(pattern).apply("test"));
    }
}