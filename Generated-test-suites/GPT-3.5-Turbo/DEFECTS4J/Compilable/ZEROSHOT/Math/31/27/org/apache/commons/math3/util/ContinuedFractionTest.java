package org.apache.commons.math3.util;
import org.junit.Test;
import org.junit.Assert;

public class ContinuedFractionTest {
    
    @Test
    public void testGetA() {
        ContinuedFraction c = new ContinuedFraction(){
            protected double getA(int n, double x){
                return 2 * n;
            }

            protected double getB(int n, double x){
                return 2 * n + 1;
            }
        };
        double result = c.getA(3, 1.0);
        Assert.assertEquals(6, result, 0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction c = new ContinuedFraction(){
            protected double getA(int n, double x){
                return 2 * n;
            }

            protected double getB(int n, double x){
                return 2 * n + 1;
            }
        };
        double result = c.getB(3, 1.0);
        Assert.assertEquals(7, result, 0);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction c = new ContinuedFraction(){
            protected double getA(int n, double x){
                return 2 * n;
            }

            protected double getB(int n, double x){
                return 2 * n + 1;
            }
        };
        double result = c.evaluate(1.0);
        Assert.assertEquals(15.0, result, 0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction c = new ContinuedFraction(){
            protected double getA(int n, double x){
                return 2 * n;
            }

            protected double getB(int n, double x){
                return 2 * n + 1;
            }
        };
        double result = c.evaluate(1.0, 0.001);
        Assert.assertEquals(15.0, result, 0);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction c = new ContinuedFraction(){
            protected double getA(int n, double x){
                return 2 * n;
            }

            protected double getB(int n, double x){
                return 2 * n + 1;
            }
        };
        double result = c.evaluate(1.0, 0.001, 100);
        Assert.assertEquals(15.0, result, 0);
    }

    @Test
    public void testEvaluateWithMaxIterationsAndEpsilon() {
        ContinuedFraction c = new ContinuedFraction(){
            protected double getA(int n, double x){
                return 2 * n;
            }

            protected double getB(int n, double x){
                return 2 * n + 1;
            }
        };
        double result = c.evaluate(1.0, 0.001, 100);
        Assert.assertEquals(15.0, result, 0);
    }
}