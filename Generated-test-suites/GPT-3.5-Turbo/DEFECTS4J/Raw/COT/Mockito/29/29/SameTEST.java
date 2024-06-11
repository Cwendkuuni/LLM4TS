
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

import static org.junit.Assert.*;

public class SameTest {

    private Same same;

    @Test
    public void testMatches_SameObject_ReturnsTrue() {
        Object obj = new Object();
        same = new Same(obj);

        assertTrue(same.matches(obj));
    }

    @Test
    public void testMatches_DifferentObject_ReturnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        same = new Same(obj1);

        assertFalse(same.matches(obj2));
    }

    @Test
    public void testDescribeTo_StringArgument_AppendsQuoting() {
        String str = "test";
        same = new Same(str);

        Description description = new StringDescription();
        same.describeTo(description);

        assertThat(description.toString(), CoreMatchers.containsString("\"test\""));
    }

    @Test
    public void testDescribeTo_CharacterArgument_AppendsQuoting() {
        Character ch = 'c';
        same = new Same(ch);

        Description description = new StringDescription();
        same.describeTo(description);

        assertThat(description.toString(), CoreMatchers.containsString("'c'"));
    }

    @Test
    public void testDescribeTo_NonStringNonCharacterArgument_DoesNotAppendQuoting() {
        Integer num = 123;
        same = new Same(num);

        Description description = new StringDescription();
        same.describeTo(description);

        assertThat(description.toString(), CoreMatchers.containsString("123"));
        assertThat(description.toString(), CoreMatchers.not(CoreMatchers.containsString("\"")));
        assertThat(description.toString(), CoreMatchers.not(CoreMatchers.containsString("'")));
    }
}
