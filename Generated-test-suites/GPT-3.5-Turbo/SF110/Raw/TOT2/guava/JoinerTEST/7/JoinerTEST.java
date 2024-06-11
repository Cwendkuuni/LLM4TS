import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner newJoiner = joiner.useForNull("NA");
        assertNotNull(newJoiner);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner newJoiner = joiner.skipNulls();
        assertNotNull(newJoiner);
    }

    // Additional test cases for other methods can be added here

}