package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleSectorTest {
    
    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
        
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
        
        sector.free();
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(8, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        assertTrue(sector.getAreas().isEmpty());
    }
    
    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        assertTrue(sector1.equals(sector2));
    }
    
    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
    
}