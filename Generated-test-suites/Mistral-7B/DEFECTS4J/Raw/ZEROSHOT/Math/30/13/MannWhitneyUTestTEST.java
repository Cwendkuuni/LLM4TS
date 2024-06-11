package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test for MannWhitneyUTest constructor.
     */
    @Test
    public void testMannWhitneyUTestConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaturalRanking.DEFAULT_STRATEGY, mannWhitneyUTest.getNaturalRanking().getNaNStrategy());
        assertEquals(NaturalRanking.AVERAGE_TIES_STRATEGY, mannWhitneyUTest.getNaturalRanking().getTiesStrategy());
    }

    /**
     * Test for MannWhitneyUTest constructor with custom strategies.
     */
    @Test
    public void testMannWhitneyUTestConstructorWithCustomStrategies() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public int getRank(double value, double[] dataset, int length) {
                int rank = 0;
                for (int i = 0; i < length; i++) {
                    if (Double.isNaN(dataset[i])) {
                        rank++;
                        continue;
                    }
                    if (value < dataset[i]) {
                        rank = i + 1;
                        break;
                    }
                }
                return rank;
            }
        };
        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int getRank(double value, double[] dataset, int length, int rank) {
                int rankTie = rank;
                for (int i = 0; i < length; i++) {
                    if (value == dataset[i]) {
                        rankTie += tiesStrategyCount(dataset, i, length);
                    }
                }
                return rankTie;
            }

            private int tiesStrategyCount(double[] dataset, int index, int length) {
                int count = 1;
                for (int i = index + 1; i < length; i++) {
                    if (dataset[index] == dataset[i]) {
                        count++;
                    } else {
                        break;
                    }
                }
                return count;
            }
        };
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.getNaturalRanking().getNaNStrategy());
        assertEquals(tiesStrategy, mannWhitneyUTest.getNaturalRanking().getTiesStrategy());
    }

    /**
     * Test for mannWhitneyU method.
     */
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        double[] x = new double[] { 1.0, 2.0, 3.0 };
        double[] y = new double[] { 1.5, 2.5, 3.5 };
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(7.5, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);
    }

    /**
     * Test for mannWhitneyU method with custom strategies.
     */
    @Test
    public void testMannWhitneyUWithCustomStrategies() throws NoDataException, NullArgumentException {
        double[] x = new double[] { 1.0, 1.0, 2.0 };
        double[] y = new double[] { 1.5, 2.5, 2.5 };
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                new NaNStrategy() {
                    @Override
                    public int getRank(double value, double[] dataset, int length) {
                        int rank = 0;
                        for (int i = 0; i < length; i++) {
                            if (Double.isNaN(dataset[i])) {
                                rank++;
                                continue;
                            }
                            if (value < dataset[i]) {
                                rank = i + 1;
                                break;
                            }
                        }
                        return rank;
                    }
                },
                new TiesStrategy() {
                    @Override
                    public int getRank(double value, double[] dataset, int length, int rank) {
                        int rankTie = rank;
                        for (int i = 0; i < length; i++) {
                            if (value == dataset[i]) {
                                rankTie += tiesStrategyCount(dataset, i, length);
                            }
                        }
                        return rankTie;
                    }

                    private int tiesStrategyCount(double[] dataset, int index, int length) {
                        int count = 1;
                        for (int i = index + 1; i < length; i++) {
                            if (dataset[index] == dataset[i]) {
                                count++;
                            } else {
                                break;
                            }
                        }
                        return count;
                    }
                }
        );
        assertEquals(3.5, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);
    }

    /**
     * Test for mannWhitneyUTest method with small samples.
     */
    @Test
    public void testMannWhitneyUTestWithSmallSamples() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = new double[] { 1.0, 2.0 };
        double[] y = new double[] { 1.5, 2.5, 3.5 };
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(0.005, mannWhitneyUTest.mannWhitneyUTest(x, y), 0.001);
    }

    /**
     * Test for mannWhitneyUTest method with large samples.
     */
    @Test
    public void testMannWhitneyUTestWithLargeSamples() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = new double[1000];
        double[] y = new double[1000];
        for (int i = 0; i < x.length; i++) {
            x[i] = Math.random();
            y[i] = Math.random();
        }
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(0.5, mannWhitneyUTest.mannWhitneyUTest(x, y), 0.01);
    }
}