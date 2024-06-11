package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {
    
    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        // Test when occupierCount is greater than 0
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        
        // Test when occupierCount is 0
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());
        
        // Test when occupier is already the same
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertEquals(2, sector.getOccupierCount());
    }
    
    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(new SectorBonus(7, 5, 0), sector.getBonus(CharacterClass.WARRIOR));
        assertEquals(new SectorBonus(0, 0, 0), sector.getBonus(CharacterClass.MAGIC));
    }
    
    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        Set<Area> areas = new HashSet<Area>();
        assertEquals(areas, sector.getAreas());
    }
    
    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        Area area = new Area();
        sector._addArea(area);
        Set<Area> areas = new HashSet<Area>();
        areas.add(area);
        assertEquals(areas, sector.getAreas());
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector1.equals(sector2));
        
        SimpleSector sector3 = new SimpleSector(10, false, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector1.equals(sector3));
    }
    
    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        Set<SectorObserver> observers = new HashSet<SectorObserver>();
        observers.add(observer);
        assertEquals(observers, sector.getObservers());
    }
    
    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertTrue(sector.getObservers().isEmpty());
    }
    
    private class SectorObserverImpl implements SectorObserver {
        public void occupierChanged() {
            // do nothing
        }
    }
}