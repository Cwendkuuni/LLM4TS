import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import static org.junit.Assert.assertNotNull;

public class OptionBuilderTest {

    @Test
    public void testCreateOption() {
        Option option = OptionBuilder.create('-a');
        assertNotNull(option);
    }
}