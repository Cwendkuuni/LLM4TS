package org.apache.commons.collections4.sequence;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SequencesComparatorTest {

    private SequencesComparator<Integer> sequencesComparator;

    @Before
    public void setup() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 3, 4, 5));
        sequencesComparator = new SequencesComparator<>(sequence1, sequence2);
    }

    @Test
    public void testGetScript_validSequences() {
        EditScript<Integer> script = sequencesComparator.getScript();
        assertNotNull(script);
        assertEquals(2, script.size());
    }

    @Test
    public void testBuildSnake_validInputs() {
        SequencesComparator.Snake snake = sequencesComparator.buildSnake(0, 0, 2, 2);
        assertNotNull(snake);
        assertEquals(0, snake.getStart());
        assertEquals(2, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake_emptySequences() {
        SequencesComparator.Snake snake = sequencesComparator.getMiddleSnake(0, 0, 0, 0);
        assertNull(snake);
    }

    @Test(expected = RuntimeException.class)
    public void testGetMiddleSnake_invalidDiag() {
        sequencesComparator.getMiddleSnake(0, 4, 0, 4);
    }

    @Test
    public void testBuildScript_validSequences() {
        EditScript<Integer> script = new EditScript<>();
        sequencesComparator.buildScript(0, 4, 0, 4, script);
        assertEquals(3, script.size());
    }
}