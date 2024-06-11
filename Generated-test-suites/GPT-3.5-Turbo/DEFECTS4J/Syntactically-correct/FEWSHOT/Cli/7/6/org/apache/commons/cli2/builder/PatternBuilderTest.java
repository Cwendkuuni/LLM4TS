import org.apache.commons.cli2.builder.*;
import org.apache.commons.cli2.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertNull(option);

        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        pb = new PatternBuilder(new GroupBuilder(), obuilder, new ArgumentBuilder());
        pb.reset();
        option = pb.create();
        assertNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder pb = new PatternBuilder();
        pb.reset();
        
        groupBuilder.reset();
        assertEquals(0, options.size());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("");
        Option option = pb.create();
        assertNull(option);

        pb = new PatternBuilder();
        pb.withPattern("a");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNull(((DefaultOption)option).getArgument());

        pb.reset();
        pb.withPattern("!");
        option = pb.create();
        assertNotNull(option);
        assertEquals("", option.getShortName());
        assertTrue(option.isRequired());
        assertNull(((DefaultOption)option).getArgument());

        pb.reset();
        pb.withPattern("!a");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertTrue(option.isRequired());
        assertNull(((DefaultOption)option).getArgument());

        pb.reset();
        pb.withPattern(":");
        option = pb.create();
        assertNotNull(option);
        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("class", ((DefaultOption)option).getArgument().getType());
        assertTrue(((DefaultOption)option).getArgument().isOptional());

        pb.reset();
        pb.withPattern("@");
        option = pb.create();
        assertNotNull(option);
        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("class", ((DefaultOption)option).getArgument().getType());
        assertFalse(((DefaultOption)option).getArgument().isOptional());

        pb.reset();
        pb.withPattern("%");
        option = pb.create();
        assertNotNull(option);
        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("number", ((DefaultOption)option).getArgument().getType());
        assertTrue(((DefaultOption)option).getArgument().isOptional());

        pb.reset();
        pb.withPattern("#");
        option = pb.create();
        assertNotNull(option);
        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("date", ((DefaultOption)option).getArgument().getType());
        assertTrue(((DefaultOption)option).getArgument().isOptional());

        pb.reset();
        pb.withPattern("<");
        option = pb.create();
        assertNotNull(option);
        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("file", ((DefaultOption)option).getArgument().getType());
        assertTrue(((DefaultOption)option).getArgument().isOptional());

        pb.reset();
        pb.withPattern(">");
        option = pb.create();
        assertNotNull(option);
        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("file", ((DefaultOption)option).getArgument().getType());
        assertTrue(((DefaultOption)option).getArgument().isOptional());

        pb.reset();
        pb.withPattern("*");
        option = pb.create();
        assertNotNull(option);
        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("file", ((DefaultOption)option).getArgument().getType());
        assertTrue(((DefaultOption)option).getArgument().isOptional());

        pb.reset();
        pb.withPattern("/");
        option = pb.create();
        assertNotNull(option);
        assertEquals("", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("url", ((DefaultOption)option).getArgument().getType());
        assertTrue(((DefaultOption)option).getArgument().isOptional());

        pb.reset();
        pb.withPattern("ab:c/");
        option = pb.create();
        assertNotNull(option);
        assertEquals("a", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("class", ((DefaultOption)option).getArgument().getType());
        assertFalse(((DefaultOption)option).getArgument().isOptional());

        option = ((GroupOption)option).getOption(0);
        assertEquals("b", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("", ((DefaultOption)option).getArgument().getType());
        assertFalse(((DefaultOption)option).getArgument().isOptional());

        option = ((GroupOption)option).getOption(0);
        assertEquals("c", option.getShortName());
        assertFalse(option.isRequired());
        assertNotNull(((DefaultOption)option).getArgument());
        assertEquals("url", ((DefaultOption)option).getArgument().getType());
        assertTrue(((DefaultOption)option).getArgument().isOptional());
    }
}