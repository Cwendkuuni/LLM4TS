package test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import src.VassalRebellion;
import src.Fiefdoms;
import src.Map;

public class VassalRebellionTest {
   
    @Test
    public void testPerform_rebelliousVassalsLessThanValue_notRebellious() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        String fiefdomName = "fiefdom1";
        Fiefdoms fiefdomObj = new Fiefdoms(fiefdomName);
        fiefdomObj.setValue(5);
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(3);
        Map.map.put(fiefdomName, fiefdomObj);
        Collection col = new ArrayList();
        col.add(fiefdomName);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        assertTrue(fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_rebelliousVassalsGreaterThanValue_rebellious() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        String fiefdomName = "fiefdom1";
        Fiefdoms fiefdomObj = new Fiefdoms(fiefdomName);
        fiefdomObj.setValue(2);
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        fiefdomObj.setPersonalCastles(2);
        fiefdomObj.setFiefdomCastles(3);
        Map.map.put(fiefdomName, fiefdomObj);
        Collection col = new ArrayList();
        col.add(fiefdomName);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        assertTrue(fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
    }
    
    @Test
    public void testPerform_rebelliousVassalsGreaterThanAvailableCastles_rebellious() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        String fiefdomName = "fiefdom1";
        Fiefdoms fiefdomObj = new Fiefdoms(fiefdomName);
        fiefdomObj.setValue(5);
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        fiefdomObj.setPersonalCastles(1);
        fiefdomObj.setFiefdomCastles(1);
        Map.map.put(fiefdomName, fiefdomObj);
        Collection col = new ArrayList();
        col.add(fiefdomName);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        assertTrue(fiefdomObj.getRebelliousVassals());
    }
    
    @Test
    public void testPerform_noLoyalVassals_rollsEvent() {
        // Arrange
        VassalRebellion vassalRebellion = new VassalRebellion();
        String fiefdomName = "fiefdom1";
        Fiefdoms fiefdomObj = new Fiefdoms(fiefdomName);
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        Map.map.put(fiefdomName, fiefdomObj);
        Collection col = new ArrayList();
        col.add(fiefdomName);
        
        // Act
        vassalRebellion.perform(col);
        
        // Assert
        assertEquals(1, RollEvent.count);
        assertEquals(fiefdomName, RollEvent.name);
    }
}