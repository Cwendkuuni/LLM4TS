package de.outstare.fortbattleplayer.model.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        // Test that occupier count decreases by 1
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        
        // Test that occupier is set to null when occupier count reaches 0
        sector.free();
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        // Test that occupier count increases by 1
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        
        // Test that occupier is updated when a new side gains control
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testGetOccupier() {
        // Test that initially occupier is null
        assertNull(sector.getOccupier());
        
        // Test that occupier is updated when gain control called
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        // Test that initially sector is not occupied
        assertFalse(sector.isOccupied());
        
        // Test that isOccupied returns true when gain control called
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        // Test that getHeight returns the correct value
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        // Test that isDefenderSpawn returns the correct value
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        // Test that isAttackerSpawn returns the correct value
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        // Test that isFlag returns the correct value
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        // Test that getBonus returns the correct SectorBonus object
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(bonus);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
    }
    
    @Test
    public void testGetAreas() {
        // Test that getAreas returns the correct set of areas
        assertTrue(sector.getAreas().isEmpty());
        
        // Test that _addArea adds the area to the set
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testHashCode() {
        // Test that hashCode returns different values for different objects
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(11, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertNotEquals(sector1.hashCode(), sector2.hashCode());
        
        // Test that hashCode returns the same value for equal objects
        SimpleSector sector3 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector1.hashCode(), sector3.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Test that equals returns false for different object types
        assertFalse(sector.equals(new Object()));
        
        // Test that equals returns false for different objects
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(11, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector1.equals(sector2));
        
        // Test that equals returns true for equal objects
        SimpleSector sector3 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector1.equals(sector3));
    }
    
    @Test
    public void testAddObserver() {
        // Test that addObserver adds the observer to the set
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        // Test that removeObserver removes the observer from the set
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}

class SectorObserverImpl implements SectorObserver {
    public void occupierChanged() {
        // implementation not needed for testing
    }
    
    public void anotherMethod() {
        // implementation not needed for testing
    }
}