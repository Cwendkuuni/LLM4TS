package org.apache.commons.collections4.sequence;

import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

    @Test
    public void testGetScript_emptyLists() {
        SequencesComparator<Object> comparator = new SequencesComparator<>(new ArrayList<>(), new ArrayList<>());
        EditScript<Object> script = comparator.getScript();
        assertTrue(script.getCommands().isEmpty());
    }
    
    @Test
    public void testGetScript_equalLists() {
        ArrayList<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);
        ArrayList<Integer> sequence2 = new ArrayList<>();
        sequence2.add(1);
        sequence2.add(2);
        sequence2.add(3);
        
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        
        ArrayList<EditCommand<Integer>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new KeepCommand<>(1));
        expectedCommands.add(new KeepCommand<>(2));
        expectedCommands.add(new KeepCommand<>(3));
        
        assertEquals(expectedCommands, script.getCommands());
    }
    
    @Test
    public void testGetScript_differentLists() {
        ArrayList<String> sequence1 = new ArrayList<>();
        sequence1.add("a");
        sequence1.add("b");
        sequence1.add("c");
        ArrayList<String> sequence2 = new ArrayList<>();
        sequence2.add("x");
        sequence2.add("y");
        sequence2.add("z");
        
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();
        
        ArrayList<EditCommand<String>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new DeleteCommand<>("a"));
        expectedCommands.add(new DeleteCommand<>("b"));
        expectedCommands.add(new DeleteCommand<>("c"));
        expectedCommands.add(new InsertCommand<>("x"));
        expectedCommands.add(new InsertCommand<>("y"));
        expectedCommands.add(new InsertCommand<>("z"));
        
        assertEquals(expectedCommands, script.getCommands());
    }
}