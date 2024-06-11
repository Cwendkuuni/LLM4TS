
package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option option = patternBuilder.create();
        assertNull(option);

        patternBuilder.reset();
        patternBuilder.withPattern("-f");

        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-f", option.getPreferredName());

        patternBuilder.reset();
        patternBuilder.withPattern("-f -a --bar @");

        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-f", option.getPreferredName());
        assertEquals("-f -a --bar @", option.toString());

        patternBuilder.reset();
        patternBuilder.withPattern("-f -a --bar @ !");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-f", option.getPreferredName());
        assertTrue(option.isRequired());

        patternBuilder.reset();
        patternBuilder.withPattern("-f -a --bar @ ! -v");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("--bar", option.getPreferredName());
        assertFalse(option.isRequired());
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option option = patternBuilder.create();
        assertNull(option);

        patternBuilder.reset();
        patternBuilder.withPattern("-f -a --bar @");

        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-f -a --bar @", option.toString());

        patternBuilder.reset();

        option = patternBuilder.create();
        assertNull(option);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();

        patternBuilder.withPattern("-f -a --bar @");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-f -a --bar @", option.toString());

        patternBuilder.reset();

        patternBuilder.withPattern("-f -* --bar @ : !");
        option = patternBuilder.create();
        assertNotNull(option);
        assertEquals("-f", option.getPreferredName());
        assertTrue(option.isRequired());
        assertEquals(Argument.LIST, option.getArgument().getArity());
        assertEquals(Collections.singletonList(": !"), option.getArgument().getValidators());
    }

}
