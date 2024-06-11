import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SameTest {

    @Test
    public void testMatches_sameObject_returnsTrue() {
        Object obj = new Object();
        Same same = new Same(obj);

        boolean result = same.matches(obj);

        assertThat(result, is(true));
    }

    @Test
    public void testMatches_differentObject_returnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);

        boolean result = same.matches(obj2);

        assertThat(result, is(false));
    }

    @Test
    public void testDescribeTo_stringObject_appendsQuoting() {
        String str = "hello";
        Description description = new Description();
        Same same = new Same(str);

        same.describeTo(description);

        assertThat(description.toString(), is("same(\"hello\")"));
    }

    @Test
    public void testDescribeTo_characterObject_appendsQuoting() {
        Character ch = 'a';
        Description description = new Description();
        Same same = new Same(ch);

        same.describeTo(description);

        assertThat(description.toString(), is("same('a')"));
    }
}