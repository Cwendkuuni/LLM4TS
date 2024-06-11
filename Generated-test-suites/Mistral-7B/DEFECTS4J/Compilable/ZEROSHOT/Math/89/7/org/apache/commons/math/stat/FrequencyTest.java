package org.apache.commons.math.stat;
import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();

        // Test default constructor
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);

        // Test toString method
        String expectedOutput = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 40.0% \t 40.0%\n2 \t 2 \t 40.0% \t 80.0%\n3 \t 1 \t 20.0% \t 100.0%\n";
        String actualOutput = frequency.toString();
        assertEquals(expectedOutput, actualOutput);

        // Test getSumFreq method
        long expectedSumFreq = 5;
        long actualSumFreq = frequency.getSumFreq();
        assertEquals(expectedSumFreq, actualSumFreq);

        // Test getCount method
        long expectedCount1 = 2;
        long expectedCount2 = 2;
        long expectedCount3 = 1;
        long actualCount1 = frequency.getCount(1);
        long actualCount2 = frequency.getCount(2);
        long actualCount3 = frequency.getCount(3);
        assertEquals(expectedCount1, actualCount1);
        assertEquals(expectedCount2, actualCount2);
        assertEquals(expectedCount3, actualCount3);

        // Test getPct method
        double expectedPct1 = 0.4;
        double expectedPct2 = 0.4;
        double expectedPct3 = 0.2;
        double actualPct1 = frequency.getPct(1);
        double actualPct2 = frequency.getPct(2);
        double actualPct3 = frequency.getPct(3);
        assertEquals(expectedPct1, actualPct1, 0.001);
        assertEquals(expectedPct2, actualPct2, 0.001);
        assertEquals(expectedPct3, actualPct3, 0.001);

        // Test getCumFreq method
        long expectedCumFreq1 = 2;
        long expectedCumFreq2 = 4;
        long expectedCumFreq3 = 5;
        long actualCumFreq1 = frequency.getCumFreq(1);
        long actualCumFreq2 = frequency.getCumFreq(2);
        long actualCumFreq3 = frequency.getCumFreq(3);
        assertEquals(expectedCumFreq1, actualCumFreq1);
        assertEquals(expectedCumFreq2, actualCumFreq2);
        assertEquals(expectedCumFreq3, actualCumFreq3);

        // Test getCumPct method
        double expectedCumPct1 = 0.4;
        double expectedCumPct2 = 0.8;
        double expectedCumPct3 = 1.0;
        double actualCumPct1 = frequency.getCumPct(1);
        double actualCumPct2 = frequency.getCumPct(2);
        double actualCumPct3 = frequency.getCumPct(3);
        assertEquals(expectedCumPct1, actualCumPct1, 0.001);
        assertEquals(expectedCumPct2, actualCumPct2, 0.001);
        assertEquals(expectedCumPct3, actualCumPct3, 0.001);

        // Test clear method
        frequency.clear();
        long actualEmptyCount = frequency.getCount(1);
        assertEquals(0, actualEmptyCount);
    }
}