import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValue_Integer() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(1);
        long expectedCount = 1;
        long cumulativeFrequency = frequency.getCumFreq(1);
        long expectedCumulativeFrequency = 1;
        long cumulativePercentage = frequency.getCumPct(1);
        double expectedCumulativePercentage = 1.0;

        // Test functionality
        frequency.addValue(1);
        assert(count == expectedCount);
        assert(cumulativeFrequency == expectedCumulativeFrequency);
        assert(cumulativePercentage == expectedCumulativePercentage);

        // Test edge cases
        frequency.addValue(Integer.MAX_VALUE);
        count = frequency.getCount(Integer.MAX_VALUE);
        expectedCount = 1;
        cumulativeFrequency = frequency.getCumFreq(Integer.MAX_VALUE);
        expectedCumulativeFrequency = frequency.getSumFreq();
        cumulativePercentage = frequency.getCumPct(Integer.MAX_VALUE);
        expectedCumulativePercentage = 1.0;

        assert(count == expectedCount);
        assert(cumulativeFrequency == expectedCumulativeFrequency);
        assert(cumulativePercentage == expectedCumulativePercentage);
    }
}