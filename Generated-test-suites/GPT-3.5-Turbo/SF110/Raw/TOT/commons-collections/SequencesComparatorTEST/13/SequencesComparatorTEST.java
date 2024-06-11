
package org.apache.commons.collections4.sequence;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testGetScript_emptyLists_shouldReturnEmptyScript() {
        List<Integer> sequence1 = Arrays.asList();
        List<Integer> sequence2 = Arrays.asList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> actualScript = comparator.getScript();
        EditScript<Integer> expectedScript = new EditScript<>();
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_sameLists_shouldReturnEmptyScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> actualScript = comparator.getScript();
        EditScript<Integer> expectedScript = new EditScript<>();
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_singleInsertion_shouldReturnInsertCommand() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> actualScript = comparator.getScript();
        EditScript<Integer> expectedScript = new EditScript<>();
        expectedScript.append(new InsertCommand<>(4));
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_singleDeletion_shouldReturnDeleteCommand() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> actualScript = comparator.getScript();
        EditScript<Integer> expectedScript = new EditScript<>();
        expectedScript.append(new DeleteCommand<>(4));
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_singleReplace_shouldReturnDeleteAndInsertCommand() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 5, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> actualScript = comparator.getScript();
        EditScript<Integer> expectedScript = new EditScript<>();
        expectedScript.append(new DeleteCommand<>(2));
        expectedScript.append(new InsertCommand<>(5));
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test
    public void testGetScript_multipleEdits_shouldReturnCorrectScript() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> sequence2 = Arrays.asList(0, 1, 3, 6, 2, 4, 5);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        EditScript<Integer> actualScript = comparator.getScript();
        EditScript<Integer> expectedScript = new EditScript<>();
        expectedScript.append(new InsertCommand<>(0));
        expectedScript.append(new KeepCommand<>(1));
        expectedScript.append(new DeleteCommand<>(2));
        expectedScript.append(new InsertCommand<>(6));
        expectedScript.append(new KeepCommand<>(3));
        
        assertEquals(expectedScript, actualScript);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetScript_nullLists_shouldThrowIllegalArgumentException() {
        List<Integer> sequence1 = null;
        List<Integer> sequence2 = null;
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        
        comparator.getScript();
    }
}

