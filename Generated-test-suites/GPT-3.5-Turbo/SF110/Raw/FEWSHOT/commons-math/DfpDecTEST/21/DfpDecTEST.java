package org.apache.commons.math3.dfp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testDfpDecConstructor() {
        DfpDec dfpDec = new DfpDec();
        assertEquals(0, dfpDec.getMant().length);
        assertEquals(0, dfpDec.getField().getRadiusDigits());
        assertEquals(0, dfpDec.getRadixDigits());
        assertEquals(0, dfpDec.getDecimalDigits());
        assertEquals(0, dfpDec.getExp());
        assertEquals(0, dfpDec.getSign());
        assertEquals(0, dfpDec.getNans());


        assertNotNull(dfpDec.getInstance());
        assertEquals(0, dfpDec.newInstance().getMant().length);
        assertEquals(0, dfpDec.newInstance().getField().getRadiusDigits());
        assertEquals(0, dfpDec.newInstance().getRadixDigits());
        assertEquals(0, dfpDec.newInstance().getDecimalDigits());
        assertEquals(0, dfpDec.newInstance().getExp());
        assertEquals(0, dfpDec.newInstance().getSign());
        assertEquals(0, dfpDec.newInstance().getNans());

        assertEquals(1, dfpDec.newInstance((byte)1).getSign());
        assertEquals(1, dfpDec.newInstance((byte)1).getField().getRadiusDigits());
        assertEquals(1, dfpDec.newInstance((byte)1).getRadixDigits());
        assertEquals(1, dfpDec.newInstance((byte)1).getDecimalDigits());
        assertEquals(1, dfpDec.newInstance((byte)1).getExp());
        assertEquals(1, dfpDec.newInstance((byte)1).getSign());
        assertEquals(1, dfpDec.newInstance((byte)1).getNans());
        
    }

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfpDec = new DfpDec();
        assertEquals(0, dfpDec.getDecimalDigits());
    }

    @Test
    public void testRound() {
        DfpDec dfpDec = new DfpDec();
        assertEquals(0, dfpDec.round(0));
    }

    @Test
    public void testGetMant() {
        DfpDec dfpDec = new DfpDec();
        assertNotNull(dfpDec.getMant());
    }

    @Test
    public void testGetField() {
        DfpDec dfpDec = new DfpDec();
        assertNotNull(dfpDec.getField());
    }

    @Test
    public void testGetRadixDigits() {
        DfpDec dfpDec = new DfpDec();
        assertEquals(0, dfpDec.getRadixDigits());
    }

    @Test
    public void testGetExp() {
        DfpDec dfpDec = new DfpDec();
        assertEquals(0, dfpDec.getExp());
    }

    @Test
    public void testGetSign() {
        DfpDec dfpDec = new DfpDec();
        assertEquals(0, dfpDec.getSign());
    }

    @Test
    public void testGetNans() {
        DfpDec dfpDec = new DfpDec();
        assertEquals(0, dfpDec.getNans());
    }

    @Test
    public void testNextAfter() {
        DfpDec dfpDec = new DfpDec();
        DfpDec x = new DfpDec();
        assertNotNull(dfpDec.nextAfter(x));
    }
    @Test
    public void testDfpDecString() {        
        DfpDec dfpDec = new DfpDec();
        assertNotNull(dfpDec.newInstance("s"));

        assertNotNull(dfpDec.getField().valueOf(1.0));
        assertNotNull(dfpDec.getField().getPi());
        assertNotNull(dfpDec.getField().getOne());
        assertNotNull(dfpDec.getField().getZero());
        assertNotNull(dfpDec.getField().getNaN());
        assertNotNull(dfpDec.getField().getInf());
        assertNotNull(dfpDec.getField().getE());
        assertNotNull(dfpDec.getField().getNegInf());
        
    }
    
    @Test
    public void testDfpDecDouble() {        
        DfpDec dfpDec = new DfpDec();
        assertNotNull(dfpDec.newInstance(1.0));

        assertNotNull(dfpDec.getField().valueOf(1.0));
        assertNotNull(dfpDec.getField().getPi());
        assertNotNull(dfpDec.getField().getOne());
        assertNotNull(dfpDec.getField().getZero());
        assertNotNull(dfpDec.getField().getNaN());
        assertNotNull(dfpDec.getField().getInf());
        assertNotNull(dfpDec.getField().getE());
        assertNotNull(dfpDec.getField().getNegInf());
        
    }







}