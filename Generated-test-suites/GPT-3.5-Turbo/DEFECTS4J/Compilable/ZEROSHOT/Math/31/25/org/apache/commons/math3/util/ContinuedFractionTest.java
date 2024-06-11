package org.apache.commons.math3.util;
import org.junit.Assert;
import org.junit.Test;

public class ContinuedFractionTest {
    
    @Test
    public void testGetA() {
        ContinuedFractionImpl cf = new ContinuedFractionImpl();
        double result = cf.getA(0, 1);
        Assert.assertEquals(0.5, result, 0.000001);
        
        result = cf.getA(1, 2);
        Assert.assertEquals(0.3, result, 0.000001);
        
        result = cf.getA(2, 3);
        Assert.assertEquals(0.2, result, 0.000001);
    }
    
    @Test
    public void testGetB() {
        ContinuedFractionImpl cf = new ContinuedFractionImpl();
        double result = cf.getB(0, 1);
        Assert.assertEquals(0.5, result, 0.000001);
        
        result = cf.getB(1, 2);
        Assert.assertEquals(0.4, result, 0.000001);
        
        result = cf.getB(2, 3);
        Assert.assertEquals(0.3, result, 0.000001);
    }
    
    @Test
    public void testEvaluate() {
        ContinuedFractionImpl cf = new ContinuedFractionImpl();
        double result = cf.evaluate(2, 0.000001, 100);
        Assert.assertEquals(1.5, result, 0.000001);
        
        result = cf.evaluate(3, 0.000001, 100);
        Assert.assertEquals(0.66666666666, result, 0.000001);
        
        result = cf.evaluate(4, 0.000001, 100);
        Assert.assertEquals(2.2, result, 0.000001);
    }
}

class ContinuedFractionImpl extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        switch(n) {
            case 0:
            return 0.5;
            case 1:
            return 0.3;
            case 2:
            return 0.2;
        }
        return 0;
    }
    
    @Override
    protected double getB(int n, double x) {
        switch(n) {
            case 0:
            return 0.5;
            case 1:
            return 0.4;
            case 2:
            return 0.3;
        }
        return 0;
    }
}