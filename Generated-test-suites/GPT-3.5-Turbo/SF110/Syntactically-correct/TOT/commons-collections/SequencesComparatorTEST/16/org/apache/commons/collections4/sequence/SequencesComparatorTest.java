
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SequencesComparatorTest {

    private SequencesComparator<Integer> sequencesComparator;
    private List<Integer> sequence1;
    private List<Integer> sequence2;

    @Before
    public void setup() {
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();
        sequencesComparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test
    public void testGetScript_BothSequencesEmpty_ReturnsEmptyScript() {
        EditScript<Integer> script = sequencesComparator.getScript();
        Assert.assertEquals(0, script.getCommands().size());
    }

    @Test
    public void testGetScript_Sequence1Empty_Sequence2NotEmpty_ReturnsInsertCommands() {
        sequence2.add(1);
        sequence2.add(2);

        EditScript<Integer> script = sequencesComparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();

        Assert.assertEquals(2, commands.size());
        Assert.assertTrue(commands.get(0) instanceof InsertCommand);
        Assert.assertTrue(commands.get(1) instanceof InsertCommand);
        Assert.assertEquals(1, commands.get(0).getObject());
        Assert.assertEquals(2, commands.get(1).getObject());
    }

    @Test
    public void testGetScript_Sequence2Empty_Sequence1NotEmpty_ReturnsDeleteCommands() {
        sequence1.add(1);
        sequence1.add(2);

        EditScript<Integer> script = sequencesComparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();

        Assert.assertEquals(2, commands.size());
        Assert.assertTrue(commands.get(0) instanceof DeleteCommand);
        Assert.assertTrue(commands.get(1) instanceof DeleteCommand);
        Assert.assertEquals(1, commands.get(0).getObject());
        Assert.assertEquals(2, commands.get(1).getObject());
    }

    @Test
    public void testGetScript_BothSequencesHaveCommonElements_ReturnsKeepCommands() {
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);

        EditScript<Integer> script = sequencesComparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();

        Assert.assertEquals(3, commands.size());
        Assert.assertTrue(commands.get(0) instanceof KeepCommand);
        Assert.assertEquals(1, commands.get(0).getObject());
        Assert.assertTrue(commands.get(1) instanceof KeepCommand);
        Assert.assertEquals(2, commands.get(1).getObject());
        Assert.assertTrue(commands.get(2) instanceof KeepCommand);
        Assert.assertEquals(3, commands.get(2).getObject());
    }

    @Test
    public void testGetScript_Sequence1LongerThanSequence2_ReturnsDeleteCommands() {
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence1.add(4);

        sequence2.add(1);
        sequence2.add(2);

        EditScript<Integer> script = sequencesComparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();

        Assert.assertEquals(2, commands.size());
        Assert.assertTrue(commands.get(0) instanceof DeleteCommand);
        Assert.assertEquals(3, commands.get(0).getObject());
        Assert.assertTrue(commands.get(1) instanceof DeleteCommand);
        Assert.assertEquals(4, commands.get(1).getObject());
    }

    @Test
    public void testGetScript_Sequence2LongerThanSequence1_ReturnsInsertCommands() {
        sequence1.add(1);
        sequence1.add(2);

        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);

        EditScript<Integer> script = sequencesComparator.getScript();
        List<EditCommand<Integer>> commands = script.getCommands();

        Assert.assertEquals(2, commands.size());
        Assert.assertTrue(commands.get(0) instanceof InsertCommand);
        Assert.assertEquals(3, commands.get(0).getObject());
        Assert.assertTrue(commands.get(1) instanceof InsertCommand);
        Assert.assertEquals(4, commands.get(1).getObject());
    }
}

