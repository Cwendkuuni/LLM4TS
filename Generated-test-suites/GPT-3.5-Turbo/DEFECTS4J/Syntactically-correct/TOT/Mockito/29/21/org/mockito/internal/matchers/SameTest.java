import org.junit.Test;
import org.hamcrest.MatcherAssert;

public class SameTest {

    @Test
    public void testMatches_withSameObjects() {
        Same same = new Same("hello");
        boolean result = same.matches("hello");
        MatcherAssert.assertThat(result, is(true));
    }

    @Test
    public void testMatches_withDifferentObjects() {
        Same same = new Same("hello");
        boolean result = same.matches("world");
        MatcherAssert.assertThat(result, is(false));
    }

    @Test
    public void testMatches_withNullObject() {
        Same same = new Same(null);
        boolean result = same.matches(null);
        MatcherAssert.assertThat(result, is(true));
    }

    @Test
    public void testMatches_withDifferentType() {
        Same same = new Same("hello");
        boolean result = same.matches(123);
        MatcherAssert.assertThat(result, is(false));
    }

    @Test
    public void testDescribeTo_withString() {
        Same same = new Same("hello");
        Description description = new StringDescription();
        same.describeTo(description);
        MatcherAssert.assertThat(description.toString(), is("same(\"hello\")"));
    }

    @Test
    public void testDescribeTo_withChar() {
        Same same = new Same('a');
        Description description = new StringDescription();
        same.describeTo(description);
        MatcherAssert.assertThat(description.toString(), is("same('a')"));
    }

    @Test
    public void testDescribeTo_withNull() {
        Same same = new Same(null);
        Description description = new StringDescription();
        same.describeTo(description);
        MatcherAssert.assertThat(description.toString(), is("same(null)"));
    }
}