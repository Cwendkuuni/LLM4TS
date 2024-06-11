package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {
    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.MAGE);
    }
    
    @Test
    public void testFree() {
        // Test when occupierCount is greater than 0
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
        
        // Test when occupierCount is 0
        sector.free();
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertTrue(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.MAGE);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
        
        bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(2, bonus.getAttackBonus());
        assertEquals(0, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        assertTrue(sector.getAreas().isEmpty());
        
        Area area = new Area();
        sector._addArea(area);
        
        assertEquals(1, sector.getAreas().size());
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.MAGE);
        assertEquals(sector.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.MAGE);
        assertTrue(sector.equals(sector2));
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        
        assertTrue(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        
        assertFalse(sector.getObservers().contains(observer));
    }
}