import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Predicates;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply(new Object()));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicates.alwaysFalse().apply(new Object()));
    }

    @Test
    public void testIsNull() {
        assertTrue(Predicates.isNull().apply(null));
    }

    @Test
    public void testNotNull() {
        assertFalse(Predicates.notNull().apply(null));
    }

    @Test
    public void testNot() {
        assertFalse(Predicates.not(Predicates.alwaysTrue()).apply(new Object()));
    }

    @Test
    public void testAnd() {
        assertFalse(Predicates.and(Predicates.alwaysTrue(), Predicates.alwaysFalse()).apply(new Object()));
    }

    @Test
    public void testOr() {
        assertTrue(Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysFalse()).apply(new Object()));
    }

    @Test
    public void testEqualTo() {
        assertTrue(Predicates.equalTo(5).apply(5));
    }

    @Test
    public void testInstanceOf() {
        assertTrue(Predicates.instanceOf(String.class).apply("test"));
    }

    @Test
    public void testAssignableFrom() {
        assertTrue(Predicates.assignableFrom(Number.class).apply(Integer.class));
    }

    @Test
    public void testIn() {
        assertTrue(Predicates.in(Set.of(1, 2, 3)).apply(2));
    }

    @Test
    public void testCompose() {
        assertTrue(Predicates.compose(Predicates.notNull(), Object::toString).apply(new Object()));
    }

    @Test
    public void testContainsPattern() {
        assertTrue(Predicates.containsPattern("test").apply("test string"));
    }

    @Test
    public void testContains() {
        assertTrue(Predicates.contains(Pattern.compile("test")).apply("test string"));
    }

}