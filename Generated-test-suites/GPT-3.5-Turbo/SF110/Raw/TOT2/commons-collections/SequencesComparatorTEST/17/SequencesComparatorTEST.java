import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SequencesComparatorTest {

    private SequencesComparator<Integer> sequencesComparator;

    @Before
    public void setup() {
        sequencesComparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 3, 4));
    }

    @Test
    public void testGetScriptNotNull() {
        assertNotNull(sequencesComparator.getScript());
    }

    @Test
    public void testBuildSnake() {
        SequencesComparator.Snake snake = sequencesComparator.buildSnake(0, 0, 1, 1);
        assertEquals(0, snake.getStart());
        assertEquals(1, snake.getEnd());
        assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        SequencesComparator.Snake middleSnake = sequencesComparator.getMiddleSnake(0, 3, 0, 3);
        assertNotNull(middleSnake);
    }

    @Test
    public void testBuildScript() {
        EditScript<Integer> editScript = new EditScript<>();
        sequencesComparator.buildScript(0, 3, 0, 3, editScript);
        assertNotNull(editScript);
    }
}