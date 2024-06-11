package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testEquals_SameObject() {
        ComboLeg leg = new ComboLeg();
        Assert.assertEquals(true, leg.equals(leg));
    }
    
    @Test
    public void testEquals_NullObject() {
        ComboLeg leg = new ComboLeg();
        Assert.assertEquals(false, leg.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        ComboLeg leg = new ComboLeg();
        Assert.assertEquals(false, leg.equals(new Object()));
    }
    
    @Test
    public void testEquals_EqualObjects() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Action", "Exchange", 1);
        ComboLeg leg2 = new ComboLeg(1, 1, "Action", "Exchange", 1);
        Assert.assertEquals(true, leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentConId() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Action", "Exchange", 1);
        ComboLeg leg2 = new ComboLeg(2, 1, "Action", "Exchange", 1);
        Assert.assertEquals(false, leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentRatio() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Action", "Exchange", 1);
        ComboLeg leg2 = new ComboLeg(1, 2, "Action", "Exchange", 1);
        Assert.assertEquals(false, leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentAction() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Action1", "Exchange", 1);
        ComboLeg leg2 = new ComboLeg(1, 1, "Action2", "Exchange", 1);
        Assert.assertEquals(false, leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentExchange() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Action", "Exchange1", 1);
        ComboLeg leg2 = new ComboLeg(1, 1, "Action", "Exchange2", 1);
        Assert.assertEquals(false, leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentOpenClose() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Action", "Exchange", 1);
        ComboLeg leg2 = new ComboLeg(1, 1, "Action", "Exchange", 2);
        Assert.assertEquals(false, leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentShortSaleSlot() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Action", "Exchange", 1, 1, "Location");
        ComboLeg leg2 = new ComboLeg(1, 1, "Action", "Exchange", 1, 2, "Location");
        Assert.assertEquals(false, leg1.equals(leg2));
    }
    
    @Test
    public void testEquals_DifferentDesignatedLocation() {
        ComboLeg leg1 = new ComboLeg(1, 1, "Action", "Exchange", 1, 1, "Location1");
        ComboLeg leg2 = new ComboLeg(1, 1, "Action", "Exchange", 1, 1, "Location2");
        Assert.assertEquals(false, leg1.equals(leg2));
    }
}