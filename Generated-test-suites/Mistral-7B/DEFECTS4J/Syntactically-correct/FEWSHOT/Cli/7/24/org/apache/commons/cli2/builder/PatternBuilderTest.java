package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilderCreateOption() {
        PatternBuilder pb = new PatternBuilder();
        pb.createOption('@', false, '@');
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option.getLongOpt().startsWith("@"));

        pb.reset();

        pb.createOption('%', false, '%');
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.getLongOpt().startsWith("%"));

        pb.reset();

        pb.createOption('/', false, '/');
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.getLongOpt().startsWith("/"));

        pb.reset();

        pb.createOption('+', false, '+');
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.getLongOpt().startsWith("+"));

        pb.reset();

        pb.createOption('#', false, '#');
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.getLongOpt().startsWith("#"));

        pb.reset();

        pb.createOption('<', false, '<');
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.getLongOpt().startsWith("<"));

        pb.reset();

        pb.createOption('>', false, '>');
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.getLongOpt().startsWith(">"));

        pb.reset();

        pb.createOption('*', false, '*');
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.getLongOpt().startsWith("*"));

        pb.reset();

        pb.createOption(' ', false, ' ');
        option = pb.create();
        assertNull(option);
    }

    @Test
    public void testPatternBuilderWithPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("-a:<file> -b:@MyClass -c:+ --d:#2022-01-01 -e:%d -f:/url --g:<dir>");
        Option option = pb.create();
        assertNotNull(option);

        Option optionA = null;
        for (Object o : option.getOptions()) {
            if (o instanceof Option) {
                optionA = (Option)o;
                break;
            }
        }

        assertNotNull(optionA);
        assertTrue(optionA.getLongOpt().equals("a"));
        assertTrue(optionA.getArgName().equals("file"));
        assertNotNull(optionA.getValidator());
        assertTrue(optionA.getValidator() instanceof FileValidator);

        Option optionB = null;
        for (Object o : option.getOptions()) {
            if (o instanceof Option) {
                Option opt = (Option)o;
                if (opt.getLongOpt().equals("b")) {
                    optionB = opt;
                    break;
                }
            }
        }

        assertNotNull(optionB);
        assertTrue(optionB.getLongOpt().equals("b"));
        assertTrue(optionB.getArgument().isOptional());
        assertTrue(optionB.getArgument().getArgumentName().equals("MyClass"));
        assertNotNull(optionB.getValidator());
        assertTrue(optionB.getValidator() instanceof ClassValidator);
        assertTrue(optionB.getValidator().getInstance().isInstance(String.class));

        Option optionC = null;
        for (Object o : option.getOptions()) {
            if (o instanceof Option) {
                Option opt = (Option)o;
                if (opt.getLongOpt().equals("c")) {
                    optionC = opt;
                    break;
                }
            }
        }

        assertNotNull(optionC);
        assertTrue(optionC.getLongOpt().equals("c"));
        assertTrue(optionC.getArgument().isOptional());
        assertTrue(optionC.getArgument().getArgumentName().equals(""));
        assertNotNull(optionC.getValidator());
        assertTrue(optionC.getValidator() instanceof NumberValidator);

        Option optionD = null;
        for (Object o : option.getOptions()) {
            if (o instanceof Option) {
                Option opt = (Option)o;
                if (opt.getLongOpt().equals("d")) {
                    optionD = opt;
                    break;
                }
            }
        }

        assertNotNull(optionD);
        assertTrue(optionD.getLongOpt().equals("d"));
        assertTrue(optionD.getArgument().isOptional());
        assertNull(optionD.getArgument().getArgumentName());
        assertNotNull(optionD.getValidator());
        assertTrue(optionD.getValidator() instanceof DateValidator);
        LocalDate date = optionD.getValidator().validate("2022-01-01");
        assertTrue(date != null);

        Option optionE = null;
        for (Object o : option.getOptions()) {
            if (o instanceof Option) {
                Option opt = (Option)o;
                if (opt.getLongOpt().equals("e")) {
                    optionE = opt;
                    break;
                }
            }
        }

        assertNotNull(optionE);
        assertTrue(optionE.getLongOpt().equals("e"));
        assertTrue(optionE.getArgument().isOptional());
        assertTrue(optionE.getArgument().getArgumentName().equals("d"));
        assertNotNull(optionE.getValidator());
        assertTrue(optionE.getValidator() instanceof NumberValidator);

        Option optionF = null;
        for (Object o : option.getOptions()) {
            if (o instanceof Option) {
                Option opt = (Option)o;
                if (opt.getLongOpt().equals("f")) {
                    optionF = opt;
                    break;
                }
            }
        }

        assertNotNull(optionF);
        assertTrue(optionF.getLongOpt().equals("f"));
        assertTrue(optionF.getArgument().isOptional());
        assertNull(optionF.getArgument().getArgumentName());
        assertNotNull(optionF.getValidator());
        assertTrue(optionF.getValidator() instanceof UrlValidator);

        Option optionG = null;
        for (Object o : option.getOptions()) {
            if (o instanceof Option) {
                Option opt = (Option)o;
                if (opt.getLongOpt().equals("g")) {
                    optionG = opt;
                    break;
                }
            }
        }

        assertNotNull(optionG);
        assertTrue(optionG.getLongOpt().equals("g"));
        assertTrue(optionG.getArgument().isOptional());
        assertTrue(optionG.getArgument().getArgumentName().equals("dir"));
        assertNotNull(optionG.getValidator());
        assertTrue(optionG.getValidator() instanceof FileValidator);
    }

    @Test
    public void testPatternBuilderWithPatternEmpty() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("");
        Option option = pb.create();
        assertTrue(option.getOptions().isEmpty());
    }

    @Test
    public void testPatternBuilderWithPatternNoOptions() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("-");
        Option option = pb.create();
        assertTrue(option.getOptions().isEmpty());
    }
}