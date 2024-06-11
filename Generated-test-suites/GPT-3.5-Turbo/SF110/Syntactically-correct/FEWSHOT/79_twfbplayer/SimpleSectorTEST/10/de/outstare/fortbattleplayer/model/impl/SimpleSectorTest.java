package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import java.util.*;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector.*;

public class SimpleSectorTest {
    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertEquals(1, sector.getOccupierCount());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, sector.getOccupierCount());
    }
    
    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
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
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(5, bonus.getAttackBonus());
        assertEquals(3, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        Set<Area> areas = sector.getAreas();
        assertEquals(0, areas.size());
        
        Area area = new Area();
        sector._addArea(area);
        
        areas = sector.getAreas();
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer1 = new MockSectorObserver();
        SectorObserver observer2 = new MockSectorObserver();
        
        sector.addObserver(observer1);
        sector.addObserver(observer2);
        
        assertEquals(2, sector.getObserversCount());
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer1 = new MockSectorObserver();
        SectorObserver observer2 = new MockSectorObserver();
        
        sector.addObserver(observer1);
        sector.addObserver(observer2);
        
        sector.removeObserver(observer1);
        
        assertEquals(1, sector.getObserversCount());
    }
    
    // Additional test cases for private methods can be added here
    
    // Helper class for mocking SectorObserver
    private class MockSectorObserver implements SectorObserver {
        @Override
        public void occupierChanged() {
            // mock implementation
        }
    }
}