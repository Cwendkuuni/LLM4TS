import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.GroupImpl;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GroupImplTest {

    @Test
    public void testGroupImplConstruction() throws Exception {
        List<Option> options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("groupOption")
                .withDescription("A group option")
                .isRequired()
                .create());
        options.add(OptionBuilder.withLongOpt("option1")
                .withDescription("An option")
                .create());
        options.add(OptionBuilder.withLongOpt("option2")
                .withDescription("Another option")
                .create());

        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 2);

        assertNotNull(group.getName());
        assertEquals("Group description", group.getDescription());
        assertEquals(options, group.getOptions());
        assertEquals(1, group.getMinimum());
        assertEquals(2, group.getMaximum());
    }
}