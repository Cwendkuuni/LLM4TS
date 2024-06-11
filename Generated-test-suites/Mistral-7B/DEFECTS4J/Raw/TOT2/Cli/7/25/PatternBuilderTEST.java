import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PatternBuilderTest {

    @Test
    public void testConstructor() {
        PatternBuilder builder = new PatternBuilder();
        assertEquals(new HashSet(), builder.getOptions());
    }

    @Test
    public void testReset() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("test");
        builder.reset();
        assertEquals(new HashSet(), builder.getOptions());
    }
}