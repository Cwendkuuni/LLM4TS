package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class VassalRebellionTest extends ActionsTest {
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;
    
    @Override
    public void setUp() {
        super.setUp();
        vassalRebellion = new VassalRebellion();
        fiefdomObj = new Fiefdoms();
    }

    @Test
    public void testPerform_withLoyalVassals() {
        Collection<String> col = new ArrayList<String>();
        col.add("Fiefdom1");
        
        vassalRebellion.perform(col);
        
        assertTrue(fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
        assertEquals(fiefdomObj.getValue(), fiefdomObj.getLoyalVassals().size());
        assertEquals(fiefdomObj.getLoyalVassals().size(), fiefdomObj.getRebelliousVassals().size());
        assertEquals(0, fiefdomObj.getRebelliousVassals(False).size());
    }
    
    @Test
    public void testPerform_withoutLoyalVassals() {
        Collection<String> col = new ArrayList<String>();
        col.add("Fiefdom2");
        
        vassalRebellion.perform(col);
        
        RollEvent rollEvent = fiefdomObj.getRollEvent();
        assertNotNull(rollEvent);
        assertEquals("Fiefdom2", rollEvent.getName());
    }
    
    @Test
    public void testPerform_withCastles() {
        Collection<String> col = new ArrayList<String>();
        col.add("Fiefdom3");
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(2);
        
        vassalRebellion.perform(col);
        
        assertTrue(fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
        assertEquals(fiefdomObj.getValue(), fiefdomObj.getLoyalVassals().size());
        assertEquals(fiefdomObj.getLoyalVassals().size(), fiefdomObj.getRebelliousVassals().size());
        assertEquals(0, fiefdomObj.getRebelliousVassals(False).size());
    }
}