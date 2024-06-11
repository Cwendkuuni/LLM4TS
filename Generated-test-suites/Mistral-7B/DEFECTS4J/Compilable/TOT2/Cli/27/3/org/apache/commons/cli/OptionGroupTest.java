package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OptionGroupTest {

    @Test
    public void testOptionGroupConstructor() {
        OptionGroup optionGroup = new OptionGroup();
        assertNotNull(optionGroup);
    }
}