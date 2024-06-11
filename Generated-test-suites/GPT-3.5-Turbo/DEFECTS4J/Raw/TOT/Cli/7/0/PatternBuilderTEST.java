import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli2.Option;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate_withSingleOption() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
    }

    @Test
    public void testCreate_withMultipleOptions() {
        patternBuilder.withPattern("a:b:c");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertEquals("b", option.getChildren().get(0).getShortName());
        assertEquals("c", option.getChildren().get(0).getChildren().get(0).getShortName());
    }

    @Test
    public void testReset_afterCreate() {
        patternBuilder.withPattern("a");
        Option option1 = patternBuilder.create();
        patternBuilder.reset();
        patternBuilder.withPattern("b");
        Option option2 = patternBuilder.create();
        assertEquals("b", option2.getShortName());
    }

    @Test
    public void testWithPattern_withRequiredOption() {
        patternBuilder.withPattern("!a");
        Option option = patternBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPattern_withMultipleOptionsAndType() {
        patternBuilder.withPattern("a@b:c%*");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertEquals("b", option.getChildren().get(0).getShortName());
        assertEquals("c", option.getChildren().get(0).getChildren().get(0).getShortName());
        assertEquals("Number", option.getChildren().get(0).getChildren().get(0).getArgument().getTypeName());
        assertEquals("FileValidator", option.getChildren().get(0).getChildren().get(1).getArgument().getValidator().getClass().getSimpleName());
        assertTrue(option.getChildren().get(0).getChildren().get(0).getArgument().isRequired());
    }

    @Test
    public void testWithPattern_withNullOrInvalidOption() {
        patternBuilder.withPattern(":");
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern_withInvalidValidator() {
        patternBuilder.withPattern("a^");
        Option option = patternBuilder.create();
        assertNull(option);
    }

}