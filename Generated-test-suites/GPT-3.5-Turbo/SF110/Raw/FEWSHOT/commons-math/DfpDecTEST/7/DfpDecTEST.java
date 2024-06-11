package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testConstructorDfpField() {
        DfpField field = new DfpField();
        DfpDec instance = new DfpDec(field);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testConstructorDfpFieldByte() {
        DfpField field = new DfpField();
        byte x = 0;
        DfpDec instance = new DfpDec(field, x);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testConstructorDfpFieldInt() {
        DfpField field = new DfpField();
        int x = 0;
        DfpDec instance = new DfpDec(field, x);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testConstructorDfpFieldLong() {
        DfpField field = new DfpField();
        long x = 0L;
        DfpDec instance = new DfpDec(field, x);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testConstructorDfpFieldDouble() {
        DfpField field = new DfpField();
        double x = 0.0;
        DfpDec instance = new DfpDec(field, x);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testConstructorDfp() {
        Dfp d = new Dfp();
        DfpDec instance = new DfpDec(d);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testConstructorDfpFieldString() {
        DfpField field = new DfpField();
        String s = "";
        DfpDec instance = new DfpDec(field, s);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testConstructorDfpFieldByteByte() {
        DfpField field = new DfpField();
        byte sign = 0;
        byte nans = 0;
        DfpDec instance = new DfpDec(field, sign, nans);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testNewInstance() {
        DfpDec instance = new DfpDec(new DfpField());
        Dfp result = instance.newInstance();
        
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceByte() {
        DfpDec instance = new DfpDec(new DfpField());
        byte x = 0;
        Dfp result = instance.newInstance(x);
        
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceInt() {
        DfpDec instance = new DfpDec(new DfpField());
        int x = 0;
        Dfp result = instance.newInstance(x);
        
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceLong() {
        DfpDec instance = new DfpDec(new DfpField());
        long x = 0L;
        Dfp result = instance.newInstance(x);
        
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceDouble() {
        DfpDec instance = new DfpDec(new DfpField());
        double x = 0.0;
        Dfp result = instance.newInstance(x);
        
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceDfp() {
        DfpDec instance = new DfpDec(new DfpField());
        Dfp d = new Dfp();
        Dfp result = instance.newInstance(d);
        
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceString() {
        DfpDec instance = new DfpDec(new DfpField());
        String s = "";
        Dfp result = instance.newInstance(s);
        
        assertNotNull(result);
    }
    
    @Test
    public void testNewInstanceByteByte() {
        DfpDec instance = new DfpDec(new DfpField());
        byte sign = 0;
        byte nans = 0;
        Dfp result = instance.newInstance(sign, nans);
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpDec instance = new DfpDec(new DfpField());
        
        int result = instance.getDecimalDigits();
        
        // Write your assertions here
    }
    
    @Test
    public void testRound() {
        DfpDec instance = new DfpDec(new DfpField());
        int in = 0;
        
        int result = instance.round(in);
        
        // Write your assertions here
    }
    
    @Test
    public void testNextAfter() {
        DfpDec instance = new DfpDec(new DfpField());
        Dfp x = new Dfp();
        
        Dfp result = instance.nextAfter(x);
        
        // Write your assertions here
    }
}