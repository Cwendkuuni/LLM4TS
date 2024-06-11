package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.SimpleSector;

public class SimpleSectorTest {
    private SimpleSector sector;
    private Area area;
    private CombatantSide side;
    
    @Before
    public void setup() {
        sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        area = new Area();
        side = CombatantSide.ATTACKER;
    }
    
    @Test
    public void testFree() {
        sector.free();
        assertEquals(0, sector.getOccupantCount());
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(side);
        assertEquals(side, sector.getOccupier());
        assertEquals(1, sector.getOccupantCount());
    }
    
    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
        sector.gainControl(side);
        assertEquals(side, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        sector.gainControl(side);
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
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(10, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testAddObserver() {
        sector.addObserver(new SimpleSectorObserver());
        assertEquals(1, sector.getObserversCount());
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SimpleSectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertEquals(0, sector.getObserversCount());
    }
    
    private static class SimpleSectorObserver implements SectorObserver {
        @Override
        public void occupierChanged() {
            // do nothing
        }
    }
}