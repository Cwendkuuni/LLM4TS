package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.withPattern("a:").create();
        assertNotNull(option);
        assertTrue(option.getShortName().equals("a"));
        assertTrue(option.isRequired());
        assertTrue(option.getArgument() == null);

        Option optionWithArg = builder.withPattern("a:s").create();
        assertNotNull(optionWithArg);
        assertTrue(optionWithArg.getShortName().equals("a"));
        assertTrue(optionWithArg.isRequired());
        assertNotNull(optionWithArg.getArgument());

        Option optionWithClassArg = builder.withPattern("c:C").create();
        assertNotNull(optionWithClassArg);
        assertTrue(optionWithClassArg.getShortName().equals("c"));
        assertTrue(optionWithClassArg.isRequired());
        assertTrue(optionWithClassArg.getArgument() instanceof Argument);
        assertTrue(optionWithClassArg.getValueType() == ClassValidator.class);

        Option optionWithNumberArg = builder.withPattern("n:").create();
        assertNotNull(optionWithNumberArg);
        assertTrue(optionWithNumberArg.getShortName().equals("n"));
        assertTrue(optionWithNumberArg.isRequired());
        assertTrue(optionWithNumberArg.getArgument() instanceof Argument);
        assertTrue(optionWithNumberArg.getValueType() == NumberValidator.class);

        Option optionWithDateArg = builder.withPattern("d:").create();
        assertNotNull(optionWithDateArg);
        assertTrue(optionWithDateArg.getShortName().equals("d"));
        assertTrue(optionWithDateArg.isRequired());
        assertTrue(optionWithDateArg.getArgument() instanceof Argument);
        assertTrue(optionWithDateArg.getValueType() == DateValidator.class);

        Option optionWithFileArg = builder.withPattern("f:").create();
        assertNotNull(optionWithFileArg);
        assertTrue(optionWithFileArg.getShortName().equals("f"));
        assertTrue(optionWithFileArg.isRequired());
        assertTrue(optionWithFileArg.getArgument() instanceof Argument);
        assertTrue(optionWithFileArg.getValueType() == FileValidator.class);

        Option optionWithExistingFileArg = builder.withPattern("ef:").create();
        assertNotNull(optionWithExistingFileArg);
        assertTrue(optionWithExistingFileArg.getShortName().equals("ef"));
        assertTrue(optionWithExistingFileArg.isRequired());
        assertTrue(optionWithExistingFileArg.getArgument() instanceof Argument);
        assertTrue(optionWithExistingFileArg.getValueType() == FileValidator.class);
        assertTrue(optionWithExistingFileArg.isExisting());

        Option optionWithUrlArg = builder.withPattern("u:").create();
        assertNotNull(optionWithUrlArg);
        assertTrue(optionWithUrlArg.getShortName().equals("u"));
        assertTrue(optionWithUrlArg.isRequired());
        assertTrue(optionWithUrlArg.getArgument() instanceof Argument);
        assertTrue(optionWithUrlArg.getValueType() == UrlValidator.class);
    }
}