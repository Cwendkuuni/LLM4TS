package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.AbstractRanking;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import java.util.Arrays;

public class MannWhitneyUTest {

    @Test
    public void testCreateInstanceWithDefaultStrategy() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaNStrategy.FIXED, mannWhitneyUTest.naturalRanking.getNaNStrategy());
        assertEquals(TiesStrategy.AVERAGE, mannWhitneyUTest.naturalRanking.getTiesStrategy());
    }

    @Test
    public void testCreateInstanceWithCustomStrategies() {
        NaNStrategy nanStrategy = NaNStrategy.MATCHING;
        TiesStrategy tiesStrategy = TiesStrategy.DENSE_RANK;
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.naturalRanking.getNaNStrategy());
        assertEquals(tiesStrategy, mannWhitneyUTest.naturalRanking.getTiesStrategy());
    }

    @Test
    public void testEnsureDataConformance_ZeroLengthArray() {
        double[] x = new double[0];
        double[] y = new double[0];
        assertThrows(NoDataException.class, () -> MannWhitneyUTest.mannWhitneyU(x, y));
    }

    @Test
    public void testEnsureDataConformance_NullArgument() {
        double[] x = null;
        double[] y = new double[5];
        assertThrows(NullArgumentException.class, () -> MannWhitneyUTest.mannWhitneyU(x, y));

        x = new double[5];
        assertThrows(NullArgumentException.class, () -> MannWhitneyUTest.mannWhitneyU(null, y));
    }

    @Test
    public void testMannWhitneyU_EqualSamples() {
        double[] x = new double[]{1.0, 1.5, 1.2};
        double[] y = new double[]{1.0, 1.5, 1.2};
        assertEquals(2.0, MannWhitneyUTest.mannWhitneyU(x, y), 0.0);
    }

    @Test
    public void testMannWhitneyU_DifferentSamples() {
        double[] x = new double[]{1.0, 2.5, 3.2};
        double[] y = new double[]{4.0, 4.5, 5.2};
        assertEquals(6.0, MannWhitneyUTest.mannWhitneyU(x, y), 0.0);
    }

    @Test
    public void testMannWhitneyUTest_EqualSamples() {
        double[] x = new double[]{1.0, 1.5, 1.2};
        double[] y = new double[]{1.0, 1.5, 1.2};
        assertEquals(0.5, MannWhitneyUTest.mannWhitneyUTest(x, y), 0.05);
    }

    @Test
    public void testMannWhitneyUTest_DifferentSamples() {
        double[] x = new double[]{1.0, 2.5, 3.2};
        double[] y = new double[]{4.0, 4.5, 5.2};
        assertEquals(0.05, MannWhitneyUTest.mannWhitneyUTest(x, y), 0.05);
    }

    @Test
    public void testCalculateAsymptoticPValue_SmallSampleSize()
            throws ConvergenceException, MaxCountExceededException {
        double[] x = new double[]{1.0, 2.5, 3.2};
        double[] y = new double[]{4.0, 4.5, 5.2};
        double Umin = x.length * y.length - MannWhitneyUTest.mannWhitneyU(x, y);
        double pValue = MannWhitneyUTest.calculateAsymptoticPValue(Umin, x.length, y.length);
        assertEquals(0.05, pValue, 0.01);
    }

    @Test
    public void testCalculateAsymptoticPValue_LargeSampleSize()
            throws ConvergenceException, MaxCountExceededException {
        double[] x = new double[10000];
        double[] y = new double[10000];
        Arrays.fill(x, 1.0);
        Arrays.fill(y, 2.0);
        double Umin = x.length * y.length - MannWhitneyUTest.mannWhitneyU(x, y);
        double pValue = MannWhitneyUTest.calculateAsymptoticPValue(Umin, x.length, y.length);
        // The actual pValue may vary due to the randomness of the NormalDistribution
        assertEquals(0.0, pValue, 0.01);
    }

}