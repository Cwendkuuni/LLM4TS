package de.outstare.fortbattleplayer.model.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        // Test case 1
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());
        
        // Test case 2
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertTrue(sector.isOccupied());
        sector.free();
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, true, 2, CharacterClass.WARRIOR);
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, true, 2, CharacterClass.WARRIOR);
        
        assertFalse(sector.isOccupied());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        
        sector.free();
        assertFalse(sector.isOccupied());
    }
    
    // Add more test methods for other methods in SimpleSector class
    
}