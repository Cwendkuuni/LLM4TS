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
        assertTrue(option.getArgName().isEmpty());
        assertTrue(option.isRequired());
        assertTrue(option.getValidators().isEmpty());

        Option optionWithArg = builder.withPattern("a:s").create();
        assertNotNull(optionWithArg);
        assertTrue(optionWithArg.getShortName().equals("a"));
        assertTrue(optionWithArg.getArgName().equals("s"));
        assertTrue(optionWithArg.isRequired());
        assertTrue(optionWithArg.getValidators().isEmpty());

        Option optionWithArgAndValidator = builder.withPattern("a:s{NumberValidator}").create();
        assertNotNull(optionWithArgAndValidator);
        assertTrue(optionWithArgAndValidator.getShortName().equals("a"));
        assertTrue(optionWithArgAndValidator.getArgName().equals("s"));
        assertTrue(optionWithArgAndValidator.isRequired());
        assertTrue(optionWithArgAndValidator.getValidators().size() > 0);
        assertTrue(optionWithArgAndValidator.getValidators().get(0) instanceof NumberValidator);

        Option optionWithGroup = builder.withPattern("g{YouShouldDocumentThisPattern}").create();
        assertNotNull(optionWithGroup);
        assertTrue(optionWithGroup.getShortName().isEmpty());
        assertTrue(optionWithGroup.getArgName().isEmpty());
        assertTrue(optionWithGroup.isRequired());
        assertTrue(optionWithGroup instanceof GroupOption);
    }
}