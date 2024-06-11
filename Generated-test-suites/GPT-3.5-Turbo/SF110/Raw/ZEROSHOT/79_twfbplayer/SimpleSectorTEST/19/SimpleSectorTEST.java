
package de.outstare.fortbattleplayer.model.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {
    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        // Test 1: When occupierCount is greater than 0
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
        
        // Test 2: When occupierCount is 0
        sector.free();
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testGainControl() {
        // Test 1: When occupier is null
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        
        // Test 2: When occupier is not null and side changes
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        
        // Test 3: When occupier is not null and side does not change
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testGetOccupier() {
        // Test 1: When occupier is null
        assertNull(sector.getOccupier());
        
        // Test 2: When occupier is not null
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        // Test 1: When occupier is null
        assertFalse(sector.isOccupied());
        
        // Test 2: When occupier is not null
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(5, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        // Test 1: When charClass is WARRIOR
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(15, bonus.getAttackBonus());
        assertEquals(25, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
        
        // Test 2: When charClass is not WARRIOR
        bonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(5, bonus.getAttackBonus());
        assertEquals(15, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        assertTrue(sector.getAreas().isEmpty());
        
        Area area = new SimpleArea();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }
}
