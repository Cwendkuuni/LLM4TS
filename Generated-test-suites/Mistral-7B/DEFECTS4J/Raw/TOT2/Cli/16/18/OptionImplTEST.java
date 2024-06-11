import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionImpl;

public class OptionImplTest {
    @Test
    public void testToString() {
        Option option = new OptionImpl(1, true);
        String expectedUsage = "1:<arg>";
        String actualUsage = option.toString();
        Assert.assertEquals(expectedUsage, actualUsage);
    }
}