
package org.apache.commons.math3.dfp;

import org.junit.Assert;
import org.junit.Test;

public class DfpDecTest {
    @Test
    public void testDfpDec_constructor() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDec_byteConstructor() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory, (byte) 1);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDec_intConstructor() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory, 1);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDec_longConstructor() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory, 1L);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDec_doubleConstructor() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory, 1.0);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDec_DfpConstructor() {
        DfpField factory = new DfpField();
        Dfp d = new Dfp(factory);
        DfpDec dfpDec = new DfpDec(d);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDec_stringConstructor() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory, "1.0");
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testDfpDec_byteSignNansConstructor() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory, (byte) 1, (byte) 1);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance_noArgs() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory).newInstance();
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance_byteArg() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory).newInstance((byte) 1);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance_intArg() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory).newInstance(1);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance_longArg() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory).newInstance(1L);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance_doubleArg() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory).newInstance(1.0);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance_DfpArg() {
        DfpField factory = new DfpField();
        Dfp d = new Dfp(factory);
        DfpDec dfpDec = new DfpDec(factory).newInstance(d);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance_stringArg() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory).newInstance("1.0");
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testNewInstance_byteSignNansArg() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory).newInstance((byte) 1, (byte) 1);
        Assert.assertNotNull(dfpDec);
    }
    
    @Test
    public void testGetDecimalDigits() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory);
        int decimalDigits = dfpDec.getDecimalDigits();
        // Assert the returned value is greater than or equal to zero
        Assert.assertTrue(decimalDigits >= 0);
    }
    
    @Test
    public void testRound_noArg() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory);
        int result = dfpDec.round(0);
        // Assert the returned value is equal to 0
        Assert.assertEquals(0, result);
    }
    
    @Test
    public void testNextAfter() {
        DfpField factory = new DfpField();
        DfpDec dfpDec = new DfpDec(factory);
        DfpDec x = new DfpDec(factory);
        DfpDec result = dfpDec.nextAfter(x);
        Assert.assertNotNull(result);
    }
}
