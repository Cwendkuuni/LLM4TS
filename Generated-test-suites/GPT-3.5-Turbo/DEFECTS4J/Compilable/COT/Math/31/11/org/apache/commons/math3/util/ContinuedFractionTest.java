package org.apache.commons.math3.util;
import org.apache.commons.math3.util.ContinuedFraction;
import org.junit.Assert;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Arrange
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        // Act
        double result = cf.getA(1, 2.0);

        // Assert
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetB() {
        // Arrange
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        // Act
        double result = cf.getB(1, 2.0);

        // Assert
        Assert.assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testEvaluate() {
        // Arrange
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        // Act
        double result = cf.evaluate(2.0);

        // Assert
        Assert.assertEquals(2.0, result, 0.0);
    }

    @Test
    public void testEvaluate_withEpsilon() {
        // Arrange
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        // Act
        double result = cf.evaluate(2.0, 0.001);

        // Assert
        Assert.assertEquals(2.0, result, 0.0);
    }

    @Test
    public void testEvaluate_withMaxIterations() {
        // Arrange
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        // Act
        double result = cf.evaluate(2.0, 100);

        // Assert
        Assert.assertEquals(2.0, result, 0.0);
    }

    @Test
    public void testEvaluate_withEpsilonAndMaxIterations() {
        // Arrange
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        // Act
        double result = cf.evaluate(2.0, 0.001, 100);

        // Assert
        Assert.assertEquals(2.0, result, 0.0);
    }

}