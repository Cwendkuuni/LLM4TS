package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    // ... (Previous tests)

    @Test
    public void testToStringWithOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("test1", "test1", true, "Test Option 1");
        Option option2 = new Option("test2", "test2", true, "Test Option 2");
        group.addOption(option1);
        group.addOption(option2);
        assertEquals("[--test1 Test Option 1, --test2 Test Option 2]", group.toString());
    }

}