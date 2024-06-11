import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder builder;

    @Before
    public void setUp() {
        builder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = builder.create();
        assertNull(option);

        builder.withPattern("a");
        option = builder.create();
        assertEquals("a", option.getShortName());

        builder.reset();
        builder.withPattern("!a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());

        builder.reset();
        builder.withPattern("@a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator) option.getArgument().getValidator()).isInstance());

        builder.reset();
        builder.withPattern(":a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.getArgument().getValidator() == null);

        builder.reset();
        builder.withPattern("%a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);

        builder.reset();
        builder.withPattern("#a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof DateValidator);

        builder.reset();
        builder.withPattern("<a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertTrue(((FileValidator) option.getArgument().getValidator()).isFile());

        builder.reset();
        builder.withPattern(">a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertFalse(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertFalse(((FileValidator) option.getArgument().getValidator()).isFile());

        builder.reset();
        builder.withPattern("*a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertFalse(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertTrue(((FileValidator) option.getArgument().getValidator()).isFile());

        builder.reset();
        builder.withPattern("/a");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
    }

    @Test
    public void testReset() {
        builder.withPattern("a");
        Option option = builder.create();
        assertEquals("a", option.getShortName());

        builder.reset();
        option = builder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        builder.withPattern("ab");
        Option option = builder.create();
        assertEquals("a", option.getShortName());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());

        builder.reset();
        builder.withPattern("a!b");
        option = builder.create();
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());

        builder.reset();
        builder.withPattern("a@b");
        option = builder.create();
        assertEquals("a", option.getShortName());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);

        builder.reset();
        builder.withPattern("a:b");
        option = builder.create();
        assertEquals("a", option.getShortName());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());
        assertTrue(option.getArgument().getValidator() == null);

        builder.reset();
        builder.withPattern("a%b");
        option = builder.create();
        assertEquals("a", option.getShortName());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);

        builder.reset();
        builder.withPattern("a#b");
        option = builder.create();
        assertEquals("a", option.getShortName());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof DateValidator);

        builder.reset();
        builder.withPattern("a<b");
        option = builder.create();
        assertEquals("a", option.getShortName());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertTrue(((FileValidator) option.getArgument().getValidator()).isFile());

        builder.reset();
        builder.withPattern("a>b");
        option = builder.create();
        assertEquals("a", option.getShortName());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertFalse(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertFalse(((FileValidator) option.getArgument().getValidator()).isFile());

        builder.reset();
        builder.withPattern("a*b");
        option = builder.create();
        assertEquals("a", option.getShortName());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertFalse(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertTrue(((FileValidator) option.getArgument().getValidator()).isFile());

        builder.reset();
        builder.withPattern("a/b");
        option = builder.create();
        assertEquals("a", option.getShortName());
        option = (Option) option.getOptions().get(0);
        assertEquals("b", option.getShortName());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
    }

    @Test
    public void testValidator() {
        assertNull(PatternBuilder.validator('a'));
        assertTrue(PatternBuilder.validator('@') instanceof ClassValidator);
        assertTrue(PatternBuilder.validator('+') instanceof ClassValidator);
        assertNull(PatternBuilder.validator(':'));
        assertTrue(PatternBuilder.validator('%') instanceof NumberValidator);
        assertTrue(PatternBuilder.validator('#') instanceof DateValidator);
        assertTrue(PatternBuilder.validator('<') instanceof FileValidator);
        assertTrue(PatternBuilder.validator('>') instanceof FileValidator);
        assertTrue(PatternBuilder.validator('*') instanceof FileValidator);
        assertTrue(PatternBuilder.validator('/') instanceof UrlValidator);
    }
}