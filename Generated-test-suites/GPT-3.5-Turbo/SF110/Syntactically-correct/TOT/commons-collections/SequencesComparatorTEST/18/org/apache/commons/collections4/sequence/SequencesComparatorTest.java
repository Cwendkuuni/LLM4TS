package org.apache.commons.collections4.sequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        // Typical use case
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        Assert.assertEquals(sequence1, comparator.sequence1);
        Assert.assertEquals(sequence2, comparator.sequence2);
        
        // Edge case: Empty sequences
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();
        comparator = new SequencesComparator<>(sequence1, sequence2);
        Assert.assertEquals(sequence1, comparator.sequence1);
        Assert.assertEquals(sequence2, comparator.sequence2);
    }

    @Test
    public void testGetScript() {
        // Typical use case: Increasing sequence
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        Assert.assertEquals(1, script.size());
        Assert.assertEquals(new KeepCommand<>(1), script.getCommand(0));
        
        // Typical use case: Decreasing sequence
        sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        Assert.assertEquals(1, script.size());
        Assert.assertEquals(new KeepCommand<>(1), script.getCommand(0));
        
        // Typical use case: Different values
        sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        Assert.assertEquals(3, script.size());
        Assert.assertEquals(new DeleteCommand<>(1), script.getCommand(0));
        Assert.assertEquals(new DeleteCommand<>(2), script.getCommand(1));
        Assert.assertEquals(new DeleteCommand<>(3), script.getCommand(2));
        
        // Edge case: Empty sequences
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>();
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        Assert.assertEquals(0, script.size());
        
        // Edge case: One empty sequence
        sequence1 = new ArrayList<>();
        sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        comparator = new SequencesComparator<>(sequence1, sequence2);
        script = comparator.getScript();
        Assert.assertEquals(3, script.size());
        Assert.assertEquals(new InsertCommand<>(1), script.getCommand(0));
        Assert.assertEquals(new InsertCommand<>(2), script.getCommand(1));
        Assert.assertEquals(new InsertCommand<>(3), script.getCommand(2));
    }
}