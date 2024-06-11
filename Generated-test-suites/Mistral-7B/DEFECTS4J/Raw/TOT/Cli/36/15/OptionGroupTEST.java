package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OptionGroupTest {

    @Test
    public void testOptionGroup() {
        OptionGroup group = new OptionGroup();

        Option optA = new Option("a", "group-a", true, "This is option A.");
        Option optB = new Option("b", "group-b", true, "This is option B.");
        Option optC = new Option("c", "group-c", true, "This is option C.");

        group.addOption(optA);
        group.addOption(optB);
        group.addOption(optC);

        try {
            List<String> args = Arrays.asList("--group-a", "--group-c");
            group.parse(args.toArray(new String[args.size()]));
            Assert.assertEquals(optA.getKey(), group.getSelected());
            Assert.assertTrue(group.isRequired());

            args = Collections.singletonList("--group-b");
            group.parse(args.toArray(new String[args.size()]));
            Assert.assertEquals(optB.getKey(), group.getSelected());
            Assert.assertFalse(group.isRequired());

            args = Arrays.asList("--group-a", "--group-b");
            Assert.assertTrue(new AlreadySelectedException(group, optA).getMessage().contains("Option 'a' is already selected."));

            args = Collections.emptyList();
            Assert.assertThrows(ParseException.class, () -> group.parse(args.toArray(new String[args.size()])));

        } catch (AlreadySelectedException e) {
            Assert.fail("Unexpected AlreadySelectedException: " + e.getMessage());
        }
    }
}