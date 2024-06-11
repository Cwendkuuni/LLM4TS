package org.apache.commons.collections4.sequence;

import org.apache.commons.collections4.equators.DefaultEquator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class SequencesComparatorTest {

    private SequencesComparator<Integer> sequencesComparator;

    @Before
    public void setUp() {
        List<Integer> sequence1 = new ArrayList<>();
        sequence1.add(1);
        sequence1.add(2);
        sequence1.add(3);

        List<Integer> sequence2 = new ArrayList<>();
        sequence2.add(2);
        sequence2.add(3);
        sequence2.add(4);

        sequencesComparator = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.defaultEquator());
    }

    @Test
    public void testGetScript() {
        EditScript<Integer> script = sequencesComparator.getScript();

        Assert.assertEquals(5, script.getLength());
    }

    @Test
    public void testBuildSnake() {
        SequencesComparator.Snake snake = sequencesComparator.buildSnake(0, 0, 3, 3);

        Assert.assertEquals(0, snake.getStart());
        Assert.assertEquals(3, snake.getEnd());
        Assert.assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake() {
        SequencesComparator.Snake middleSnake = sequencesComparator.getMiddleSnake(0, 3, 0, 3);

        Assert.assertNotNull(middleSnake);
        Assert.assertEquals(1, middleSnake.getStart());
        Assert.assertEquals(1, middleSnake.getEnd());
        Assert.assertEquals(0, middleSnake.getDiag());
    }

    @Test
    public void testBuildScript() {
        EditScript<Integer> script = new EditScript<>();

        sequencesComparator.buildScript(0, 3, 0, 3, script);

        Assert.assertEquals(5, script.getLength());
    }
}