package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class OptionBuilderTest {

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create('o');
        assertNotNull(option);
    }
}