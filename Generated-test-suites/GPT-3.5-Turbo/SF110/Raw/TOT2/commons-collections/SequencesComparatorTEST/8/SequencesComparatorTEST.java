package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;
import org.junit.Test;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> sequencer = new SequencesComparator<>(list1, list2);
        assertNotNull(sequencer);
    }

    @Test
    public void testGetScript() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> sequencer = new SequencesComparator<>(list1, list2);
        EditScript<Integer> script = sequencer.getScript();
        assertNotNull(script);
    }

    // Add more test cases for other methods as needed

}