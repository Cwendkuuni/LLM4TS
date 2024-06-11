package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option = patternBuilder.create();
        assertNull(option);

        patternBuilder.withPattern("-a");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-a", option.getShortName());

        patternBuilder.reset();
        patternBuilder.withPattern("--b");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("--b", option.getShortName());

        patternBuilder.reset();
        patternBuilder.withPattern("-c:<String>");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-c", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("<String>", option.getArgument().getName());

        patternBuilder.reset();
        patternBuilder.withPattern("--d:@<Class>");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("--d", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("<Class>", option.getArgument().getName());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator)option.getArgument().getValidator()).isInstance());

        patternBuilder.reset();
        patternBuilder.withPattern("-e:%");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-e", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("%", option.getArgument().getName());
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);

        patternBuilder.reset();
        patternBuilder.withPattern("--f:#");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("--f", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("#", option.getArgument().getName());
        assertTrue(option.getArgument().getValidator() instanceof DateValidator);

        patternBuilder.reset();
        patternBuilder.withPattern("-g:<File>+");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-g", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("<File>", option.getArgument().getName());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(-1, option.getArgument().getMaximum());

        patternBuilder.reset();
        patternBuilder.withPattern("--h:*");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("--h", option.getShortName());
        assertNotNull(option.getArgument());
        assertNull(option.getArgument().getName());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertEquals(0, option.getArgument().getMinimum());
        assertEquals(-1, option.getArgument().getMaximum());

        patternBuilder.reset();
        patternBuilder.withPattern("-i:/");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-i", option.getShortName());
        assertNotNull(option.getArgument());
        assertNull(option.getArgument().getName());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);

        patternBuilder.reset();
        patternBuilder.withPattern("!");
        option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("-a");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-a", option.getShortName());

        patternBuilder.reset();
        patternBuilder.withPattern("--b");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("--b", option.getShortName());

        patternBuilder.reset();
        patternBuilder.withPattern("-c:<String>");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-c", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("<String>", option.getArgument().getName());

        patternBuilder.reset();
        patternBuilder.withPattern("--d:@<Class>");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("--d", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("<Class>", option.getArgument().getName());

        patternBuilder.reset();
        patternBuilder.withPattern("-e:%");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-e", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("%", option.getArgument().getName());

        patternBuilder.reset();
        patternBuilder.withPattern("--f:#");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("--f", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("#", option.getArgument().getName());

        patternBuilder.reset();
        patternBuilder.withPattern("-g:<File>+");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-g", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals("<File>", option.getArgument().getName());

        patternBuilder.reset();
        patternBuilder.withPattern("--h:*");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("--h", option.getShortName());
        assertNotNull(option.getArgument());
        assertNull(option.getArgument().getName());

        patternBuilder.reset();
        patternBuilder.withPattern("-i:/");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-i", option.getShortName());
        assertNotNull(option.getArgument());
        assertNull(option.getArgument().getName());

        patternBuilder.reset();
        patternBuilder.withPattern("!");
        option = patternBuilder.create();
        assertNull(option);
    }
}