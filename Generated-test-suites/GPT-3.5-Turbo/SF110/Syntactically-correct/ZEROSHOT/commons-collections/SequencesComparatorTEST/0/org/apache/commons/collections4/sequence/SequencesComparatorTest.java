import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SequencesComparatorTest {

    private SequencesComparator<Integer> comparator;
    private List<Integer> sequence1;
    private List<Integer> sequence2;

    @Before
    public void setUp() {
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();
    }

    @Test
    public void testGetScript() {
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence2.add(1);
        sequence2.add(4);
        sequence2.add(3);

        comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(3, script.size());
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
        assertEquals(new InsertCommand<>(4), script.getCommand(1));
        assertEquals(new KeepCommand<>(3), script.getCommand(2));
    }

    @Test
    public void testGetScriptWithEmptySequences() {
        comparator = new SequencesComparator<>(sequence1, sequence2);

        EditScript<Integer> script = comparator.getScript();

        assertEquals(0, script.size());
    }

    @Test
    public void testGetScriptWithEqualSequences() {
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);

        comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(3, script.size());
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
        assertEquals(new KeepCommand<>(2), script.getCommand(1));
        assertEquals(new KeepCommand<>(3), script.getCommand(2));
    }

    @Test
    public void testGetScriptWithDifferentSizes() {
        sequence1.add(1);
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);

        comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(3, script.size());
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
        assertEquals(new InsertCommand<>(2), script.getCommand(1));
        assertEquals(new InsertCommand<>(3), script.getCommand(2));
    }

    @Test
    public void testGetScriptWithNoMatchingElements() {
        sequence1.add(1);
        sequence2.add(2);

        comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(2, script.size());
        assertEquals(new InsertCommand<>(1), script.getCommand(0));
        assertEquals(new InsertCommand<>(2), script.getCommand(1));
    }

    @Test
    public void testGetScriptWithNoMatchingElementsAndDifferentSizes() {
        sequence1.add(1);
        sequence2.add(2);
        sequence2.add(3);

        comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(2, script.size());
        assertEquals(new InsertCommand<>(1), script.getCommand(0));
        assertEquals(new InsertCommand<>(2), script.getCommand(1));
    }

    @Test
    public void testGetScriptWithNullEquator() {
        sequence1.add(1);
        sequence2.add(1);

        comparator = new SequencesComparator<>(sequence1, sequence2, null);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(1, script.size());
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
    }

    @Test
    public void testGetScriptWithNullEquatorAndNullElements() {
        sequence1.add(null);
        sequence2.add(null);

        comparator = new SequencesComparator<>(sequence1, sequence2, null);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(1, script.size());
        assertEquals(new KeepCommand<>(null), script.getCommand(0));
    }

    @Test
    public void testGetScriptWithNullEquatorAndDifferentSizes() {
        sequence1.add(1);
        sequence1.add(2);
        sequence2.add(1);
        sequence2.add(3);

        comparator = new SequencesComparator<>(sequence1, sequence2, null);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(2, script.size());
        assertEquals(new KeepCommand<>(1), script.getCommand(0));
        assertEquals(new InsertCommand<>(3), script.getCommand(1));
    }

    @Test
    public void testGetScriptWithNullEquatorAndNoMatchingElements() {
        sequence1.add(1);
        sequence2.add(2);

        comparator = new SequencesComparator<>(sequence1, sequence2, null);
        EditScript<Integer> script = comparator.getScript();

        assertEquals(2, script.size());
        assertEquals(new InsertCommand<>(1), script.getCommand(0));
        assertEquals(new InsertCommand<>(2), script.getCommand(1));
    }
}