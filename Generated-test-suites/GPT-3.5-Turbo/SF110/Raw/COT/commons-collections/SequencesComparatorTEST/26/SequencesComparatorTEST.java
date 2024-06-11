
import org.apache.commons.collections4.equators.DefaultEquator;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testConstructor1() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("x", "y", "z");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        Assert.assertEquals(sequence1, comparator.sequence1);
        Assert.assertEquals(sequence2, comparator.sequence2);
        Assert.assertEquals(DefaultEquator.defaultEquator(), comparator.equator);
    }

    @Test
    public void testConstructor2() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        Equator<Integer> equator = new Equator<Integer>() {
            @Override
            public boolean equate(Integer o1, Integer o2) {
                return o1 % 2 == o2 % 2;
            }

            @Override
            public int hash(Integer o) {
                return o.hashCode();
            }
        };
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
        Assert.assertEquals(sequence1, comparator.sequence1);
        Assert.assertEquals(sequence2, comparator.sequence2);
        Assert.assertEquals(equator, comparator.equator);
    }

    @Test
    public void testGetScript() {
        List<String> sequence1 = Arrays.asList("a", "b", "c", "d");
        List<String> sequence2 = Arrays.asList("a", "b", "c");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();
        Assert.assertEquals(1, script.getCommands().size());
        Assert.assertEquals(new DeleteCommand<>("d"), script.getCommands().get(0));
    }

    @Test
    public void testBuildSnake1() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("x", "y", "z");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        Assert.assertEquals(0, snake.getStart());
        Assert.assertEquals(0, snake.getEnd());
        Assert.assertEquals(0, snake.getDiag());
    }

    @Test
    public void testBuildSnake2() {
        List<String> sequence1 = Arrays.asList("a", "b", "x", "c");
        List<String> sequence2 = Arrays.asList("a", "y", "z", "c");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.buildSnake(0, 0, sequence1.size(), sequence2.size());
        Assert.assertEquals(0, snake.getStart());
        Assert.assertEquals(2, snake.getEnd());
        Assert.assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake1() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("x", "y", "z");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        Assert.assertEquals(0, snake.getStart());
        Assert.assertEquals(0, snake.getEnd());
        Assert.assertEquals(0, snake.getDiag());
    }

    @Test
    public void testGetMiddleSnake2() {
        List<String> sequence1 = Arrays.asList("a", "b", "x", "c");
        List<String> sequence2 = Arrays.asList("a", "y", "z", "c");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        SequencesComparator.Snake snake = comparator.getMiddleSnake(0, sequence1.size(), 0, sequence2.size());
        Assert.assertEquals(0, snake.getStart());
        Assert.assertEquals(2, snake.getEnd());
        Assert.assertEquals(0, snake.getDiag());
    }

    @Test
    public void testBuildScript1() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("x", "y", "z");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        List<EditCommand<String>> commands = script.getCommands();
        Assert.assertEquals(3, commands.size());
        Assert.assertEquals(new DeleteCommand<>("a"), commands.get(0));
        Assert.assertEquals(new InsertCommand<>("x"), commands.get(1));
        Assert.assertEquals(new InsertCommand<>("y"), commands.get(2));
    }

    @Test
    public void testBuildScript2() {
        List<Integer> sequence1 = Arrays.asList(1, 2);
        List<Integer> sequence2 = Arrays.asList(5, 6, 7);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = new EditScript<>();
        comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
        List<EditCommand<Integer>> commands = script.getCommands();
        Assert.assertEquals(3, commands.size());
        Assert.assertEquals(new InsertCommand<>(5), commands.get(0));
        Assert.assertEquals(new InsertCommand<>(6), commands.get(1));
        Assert.assertEquals(new InsertCommand<>(7), commands.get(2));
    }

    @Test(expected = RuntimeException.class)
    public void testBuildScript3() {
        List<String> sequence1 = Arrays.asList("a", "b", "c");
        List<String> sequence2 = Arrays.asList("x", "y", "z");
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = new EditScript<>();
        comparator.buildScript(1, sequence1.size(), 1, sequence2.size(), script);
    }

    @Test
    public void testSnakeGetStart() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 3);
        Assert.assertEquals(1, snake.getStart());
    }

    @Test
    public void testSnakeGetEnd() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 3);
        Assert.assertEquals(2, snake.getEnd());
    }

    @Test
    public void testSnakeGetDiag() {
        SequencesComparator.Snake snake = new SequencesComparator.Snake(1, 2, 3);
        Assert.assertEquals(3, snake.getDiag());
    }

}
