import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option1 = new DefaultOptionBuilder()
                .withShortName("a")
                .withRequired(true)
                .create();
        Option option2 = new DefaultOptionBuilder()
                .withShortName("b")
                .withRequired(false)
                .create();
        patternBuilder.gbuilder.withOption(option1);
        patternBuilder.gbuilder.withOption(option2);
        
        Option expectedOption = patternBuilder.gbuilder.create();
        Option actualOption = patternBuilder.create();
        
        assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.gbuilder.withOption(new DefaultOptionBuilder()
                .withShortName("a")
                .withRequired(true)
                .create());
        patternBuilder.reset();
        
        assertTrue(patternBuilder.options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("ab:cd");
        
        Option expectedOption = new DefaultOptionBuilder()
                .withShortName("a")
                .withArgument(new ArgumentBuilder()
                        .withValidator(null)
                        .withMinimum(1)
                        .withMaximum(1)
                        .create())
                .withRequired(false)
                .create();
        
        Option actualOption = patternBuilder.options.iterator().next();
        
        assertEquals(expectedOption, actualOption);
    }
}