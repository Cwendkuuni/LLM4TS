
import org.apache.commons.collections4.equator.DefaultEquator;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testConstructor() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        Assert.assertEquals(sequence1, comparator.sequence1);
        Assert.assertEquals(sequence2, comparator.sequence2);
        Assert.assertEquals(equator, comparator.equator);
        Assert.assertNotNull(comparator.vDown);
        Assert.assertNotNull(comparator.vUp);
    }

    @Test
    public void testGetScript() {
        List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> sequence2 = new ArrayList<>(Arrays.asList(4, 5, 6));
        Equator<Integer> equator = DefaultEquator.defaultEquator();

        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);

        EditScript<Integer> script = comparator.getScript();

        Assert.assertNotNull(script);
    }
}
