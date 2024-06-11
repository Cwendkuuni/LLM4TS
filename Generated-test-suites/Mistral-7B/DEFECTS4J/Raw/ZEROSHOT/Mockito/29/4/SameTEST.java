import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Object actual = wanted;

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testDifferentTypes() {
        Integer wanted = 1;
        String actual = "one";

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testSameString() {
        String wanted = "hello";
        String actual = "hello";

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testSameCharacter() {
        Character wanted = 'a';
        Character actual = 'a';

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testMockitoUsage() {
        Object wanted = Mockito.mock(Object.class);
        Object actual = wanted;

        ArgumentMatcher<Object> matcher = new Same(wanted);

        Mockito.verify(wanted).equals(actual);
        Mockito.verify(wanted).matches(matcher);
    }

    @Test(expected = IllegalStateException.class)
    public void testSerializable() {
        Object wanted = new Object();

        // Serialize and deserialize
        byte[] serialized = Mockito.serialize(new Same(wanted));
        Object deserialized = Mockito.deserialize(serialized);

        ArgumentMatcher<Object> matcher = (ArgumentMatcher<Object>) deserialized;

        Object actual = new Object();

        matcher.matches(actual); // This will throw an IllegalStateException
    }
}