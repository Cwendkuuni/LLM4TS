
package com.ib.client;

import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertEquals(0, comboLeg.m_conId);
        Assert.assertEquals(0, comboLeg.m_ratio);
        Assert.assertNull(comboLeg.m_action);
        Assert.assertNull(comboLeg.m_exchange);
        Assert.assertEquals(0, comboLeg.m_openClose);
        Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
        Assert.assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructorWithParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "ACTION", "EXCHANGE", 3);
        Assert.assertEquals(1, comboLeg.m_conId);
        Assert.assertEquals(2, comboLeg.m_ratio);
        Assert.assertEquals("ACTION", comboLeg.m_action);
        Assert.assertEquals("EXCHANGE", comboLeg.m_exchange);
        Assert.assertEquals(3, comboLeg.m_openClose);
        Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
        Assert.assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructorWithAllParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "ACTION", "EXCHANGE", 3, 4, "LOCATION");
        Assert.assertEquals(1, comboLeg.m_conId);
        Assert.assertEquals(2, comboLeg.m_ratio);
        Assert.assertEquals("ACTION", comboLeg.m_action);
        Assert.assertEquals("EXCHANGE", comboLeg.m_exchange);
        Assert.assertEquals(3, comboLeg.m_openClose);
        Assert.assertEquals(4, comboLeg.m_shortSaleSlot);
        Assert.assertEquals("LOCATION", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "ACTION", "EXCHANGE", 3, 4, "LOCATION");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "ACTION", "EXCHANGE", 3, 4, "LOCATION");
        Assert.assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "ACTION", "EXCHANGE", 3, 4, "LOCATION");
        Assert.assertTrue(comboLeg.equals(comboLeg));
    }

    @Test
    public void testEqualsNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "ACTION", "EXCHANGE", 3, 4, "LOCATION");
        Assert.assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsDifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "ACTION", "EXCHANGE", 3, 4, "LOCATION");
        Object object = new Object();
        Assert.assertFalse(comboLeg1.equals(object));
    }
}
