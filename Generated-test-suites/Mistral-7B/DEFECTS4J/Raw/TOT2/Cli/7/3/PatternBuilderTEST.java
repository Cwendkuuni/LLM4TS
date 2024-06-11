import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
    private PatternBuilder builder;

    @Before
    public void setup() {
        builder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(builder.options.isEmpty());
    }

    @Test
    public void testReset() {
        Option option = builder.create();
        builder.reset();
        assertNull(option);
        assertTrue(builder.options.isEmpty());
    }

    @Test
    public void testWithPattern_Valid() {
        builder.withPattern("a:s");
        Option option = builder.create();
        assertNotNull(option);
        assertTrue(builder.options.size() == 1);
        Option actualOption = (Option) builder.options.iterator().next();
        assertNotNull(actualOption.getShortName());
        assertNotNull(actualOption.getLongName());
        assertNotNull(actualOption.getArgument());
        assertNotNull(actualOption.getValidator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithPattern_EmptyString() {
        builder.withPattern("");
        builder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithPattern_InvalidCharacter() {
        builder.withPattern("@a:s");
        builder.create();
    }

    @Test(expected = NullPointerException.class)
    public void testWithPattern_NullString() {
        builder.withPattern(null);
        builder.create();
    }
}