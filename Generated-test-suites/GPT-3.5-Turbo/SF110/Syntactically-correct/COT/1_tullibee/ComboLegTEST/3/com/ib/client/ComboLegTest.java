
import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructors() {
        ComboLeg comboLeg1 = new ComboLeg();
        Assert.assertEquals(0, comboLeg1.m_conId);
        Assert.assertEquals(0, comboLeg1.m_ratio);
        Assert.assertNull(comboLeg1.m_action);
        Assert.assertNull(comboLeg1.m_exchange);
        Assert.assertEquals(0, comboLeg1.m_openClose);
        Assert.assertEquals(0, comboLeg1.m_shortSaleSlot);
        Assert.assertNull(comboLeg1.m_designatedLocation);

        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 3);
        Assert.assertEquals(1, comboLeg2.m_conId);
        Assert.assertEquals(2, comboLeg2.m_ratio);
        Assert.assertEquals("action", comboLeg2.m_action);
        Assert.assertEquals("exchange", comboLeg2.m_exchange);
        Assert.assertEquals(3, comboLeg2.m_openClose);
        Assert.assertEquals(0, comboLeg2.m_shortSaleSlot);
        Assert.assertNull(comboLeg2.m_designatedLocation);

        ComboLeg comboLeg3 = new ComboLeg(4, 5, "action", "exchange", 6, 7, "location");
        Assert.assertEquals(4, comboLeg3.m_conId);
        Assert.assertEquals(5, comboLeg3.m_ratio);
        Assert.assertEquals("action", comboLeg3.m_action);
        Assert.assertEquals("exchange", comboLeg3.m_exchange);
        Assert.assertEquals(6, comboLeg3.m_openClose);
        Assert.assertEquals(7, comboLeg3.m_shortSaleSlot);
        Assert.assertEquals("location", comboLeg3.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", 3, 4, "location");
        ComboLeg comboLeg3 = new ComboLeg(2, 3, "action", "exchange", 4, 5, "location");
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "different", "exchange", 3, 4, "location");

        Assert.assertEquals(comboLeg1, comboLeg2);
        Assert.assertNotEquals(comboLeg1, comboLeg3);
        Assert.assertNotEquals(comboLeg1, comboLeg4);
    }
}
