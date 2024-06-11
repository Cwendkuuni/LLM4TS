package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Options;

public class GnuParserTest {

    GnuParser parser = new GnuParser();

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"--option1", "value1", "-o2", "-Dproperty=value", "nonOption"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--option1", "value1", "-o2", "-D", "property=value", "nonOption"}, result);
    }
}
