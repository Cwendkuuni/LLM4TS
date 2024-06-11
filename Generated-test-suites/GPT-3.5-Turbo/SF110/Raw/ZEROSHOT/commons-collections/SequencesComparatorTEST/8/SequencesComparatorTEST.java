import org.junit.*;
import java.util.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript() {
        SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        EditScript<Integer> script = comparator.getScript();
        Assert.assertEquals(0, script.getCommands().size());

        comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6));
        script = comparator.getScript();
        Assert.assertEquals(6, script.getCommands().size());
        Assert.assertTrue(script.getCommands().get(0) instanceof DeleteCommand);
        Assert.assertTrue(script.getCommands().get(1) instanceof DeleteCommand);
        Assert.assertTrue(script.getCommands().get(2) instanceof DeleteCommand);
        Assert.assertTrue(script.getCommands().get(3) instanceof InsertCommand);
        Assert.assertTrue(script.getCommands().get(4) instanceof InsertCommand);
        Assert.assertTrue(script.getCommands().get(5) instanceof InsertCommand);

        comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 3));
        script = comparator.getScript();
        Assert.assertEquals(3, script.getCommands().size());
        Assert.assertTrue(script.getCommands().get(0) instanceof KeepCommand);
        Assert.assertTrue(script.getCommands().get(1) instanceof InsertCommand);
        Assert.assertTrue(script.getCommands().get(2) instanceof KeepCommand);

        comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2));
        script = comparator.getScript();
        Assert.assertEquals(1, script.getCommands().size());
        Assert.assertTrue(script.getCommands().get(0) instanceof DeleteCommand);

        comparator = new SequencesComparator<>(Arrays.asList(1, 2), Arrays.asList(1, 2, 3));
        script = comparator.getScript();
        Assert.assertEquals(1, script.getCommands().size());
        Assert.assertTrue(script.getCommands().get(0) instanceof InsertCommand);
    }

}