package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertNotNull(option);

        pb.withPattern("a:");
        option = pb.create();
        assertTrue(option.hasArg());

        pb.withPattern("a:s");
        option = pb.create();
        assertTrue(option.hasArg());
        assertTrue(option.getArgType().isStringType());

        pb.withPattern("a:s@org.apache.commons.MyClass");
        option = pb.create();
        assertTrue(option.hasArg());
        assertTrue(option.getArgType().isClassType());
        assertEquals(MyClass.class, option.getArgType().getArgumentType());

        pb.withPattern("a:i");
        option = pb.create();
        assertTrue(option.hasArg());
        assertTrue(option.getArgType().isNumberType());

        pb.withPattern("a:i{1,3}");
        option = pb.create();
        assertTrue(option.hasArg());
        assertTrue(option.getArgType().isNumberType());
        assertTrue(option.getArgType().getBounds().isRange());
        assertEquals(1, option.getArgType().getBounds().getMinimum());
        assertEquals(3, option.getArgType().getBounds().getMaximum());

        pb.withPattern("a:d{yyyy-MM-dd}");
        option = pb.create();
        assertTrue(option.hasArg());
        assertTrue(option.getArgType().isDateType());

        pb.withPattern("a:f");
        option = pb.create();
        assertTrue(option.hasArg());
        assertTrue(option.getArgType().isFileType());

        pb.withPattern("a:g");
        option = pb.create();
        assertTrue(option.hasArg());
        assertTrue(option.getArgType().isFileType());
        assertTrue(option.getArgType().getFile().isExists());

        pb.withPattern("a:g!");
        option = pb.create();
        assertTrue(option.hasArg());
        assertTrue(option.getArgType().isFileType());
        assertTrue(option.getArgType().getFile().exists());

        pb.withPattern("a:u");
        option = pb.create();
        assertTrue(option.hasArg());
        assertTrue(option.getArgType().isUrlType());

        pb.withPattern("g(a:s,b:s)");
        Option groupOption = pb.create();
        assertTrue(groupOption.getOptions().size() == 2);

        Option optionA = null;
        for (Option option : groupOption.getOptions()) {
            if (option.getLongOpt().equals("a")) {
                optionA = option;
                break;
            }
        }
        assertTrue(optionA != null);
        assertTrue(optionA.hasArg());
        assertTrue(optionA.getArgType().isStringType());

        Option optionB = null;
        for (Option option : groupOption.getOptions()) {
            if (option.getLongOpt().equals("b")) {
                optionB = option;
                break;
            }
        }
        assertTrue(optionB != null);
        assertTrue(optionB.hasArg());
        assertTrue(optionB.getArgType().isStringType());
    }

    private static class MyClass {
    }
}