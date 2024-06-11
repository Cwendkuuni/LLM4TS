import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        GroupBuilder gbuilder = new GroupBuilder();
        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        ArgumentBuilder abuilder = new ArgumentBuilder();

        PatternBuilder pb = new PatternBuilder(gbuilder, obuilder, abuilder);

        // Create options
        Option option1 = pb.obuilder.withShortName("a").create();
        Option option2 = pb.obuilder.withShortName("b").create();
        Option option3 = pb.obuilder.withShortName("c").create();

        // Add options to pattern builder
        pb.gbuilder.withOption(option1)
                   .withOption(option2)
                   .withOption(option3);

        // Test create method
        Option createdOption = pb.create();

        // Check if created option is not null
        assertNotNull(createdOption);

        // Check if created option matches the added options
        assertTrue(createdOption.getOptions().contains(option1));
        assertTrue(createdOption.getOptions().contains(option2));
        assertTrue(createdOption.getOptions().contains(option3));
    }

    @Test
    public void testReset() {
        PatternBuilder pb = new PatternBuilder();
        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();

        // Create options
        Option option1 = obuilder.withShortName("a").create();
        Option option2 = obuilder.withShortName("b").create();
        Option option3 = obuilder.withShortName("c").create();

        // Add options to pattern builder
        pb.options.add(option1);
        pb.options.add(option2);
        pb.options.add(option3);

        // Reset pattern builder
        pb.reset();

        // Check if options have been removed
        assertTrue(pb.options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder pb = new PatternBuilder();

        // Test withPattern method
        pb.withPattern("!@:a*");

        // Check if options have been created correctly
        assertEquals(pb.options.size(), 1);
        Option option = (Option) pb.options.iterator().next();
        assertEquals(option.getShortName(), "a");
        assertTrue(option.isRequired());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().isOptional());
        assertTrue(option.getArgument().isMultiple());
    }

    @Test
    public void testValidator() {
        PatternBuilder pb = new PatternBuilder();

        // Test validator method
        Validator validator1 = pb.validator('@');
        Validator validator2 = pb.validator(':');
        Validator validator3 = pb.validator('%');
        Validator validator4 = pb.validator('#');
        Validator validator5 = pb.validator('<');
        Validator validator6 = pb.validator('>');
        Validator validator7 = pb.validator('*');
        Validator validator8 = pb.validator('/');

        // Check if correct validators are returned
        assertTrue(validator1 instanceof ClassValidator);
        assertTrue(validator2 == null);
        assertTrue(validator3 instanceof NumberValidator);
        assertTrue(validator4 instanceof DateValidator);
        assertTrue(validator5 instanceof FileValidator);
        assertTrue(validator6 instanceof FileValidator);
        assertTrue(validator7 instanceof FileValidator);
        assertTrue(validator8 instanceof UrlValidator);
    }
}