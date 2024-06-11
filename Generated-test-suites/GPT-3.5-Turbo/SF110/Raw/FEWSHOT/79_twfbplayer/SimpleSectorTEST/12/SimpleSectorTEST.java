package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;
import java.util.*;

public class SimpleSectorTest {
    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        
        // Test when occupierCount is greater than zero
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(1, sector.getOccupierCount());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        
        // Test when occupierCount is zero
        sector.free();
        assertEquals(0, sector.getOccupierCount());
    }
    
    @Test
    public void testOccupy() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        
        // Test when the occupier is null
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        assertSame(CombatantSide.ATTACKER, sector.getOccupier());
        
        // Test when the occupier is already the same side
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, sector.getOccupierCount());
        assertSame(CombatantSide.ATTACKER, sector.getOccupier());
        
        // Test when the occupier is a different side
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(3, sector.getOccupierCount());
        assertSame(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        
        assertFalse(sector.isOccupied());
        
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        
        sector.free();
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        assertTrue(sector.isDefenderSpawn());
        
        SimpleSector sector2 = new SimpleSector(10, false, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        assertFalse(sector2.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
        
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 2, true, 3, CharacterClass.WARRIOR);
        assertTrue(sector2.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        assertTrue(sector.isFlag());
        
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 2, false, 3, CharacterClass.WARRIOR);
        assertFalse(sector2.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(8, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertEquals(0, areas.size());
    }
    
    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        Area area = new Area();
        sector._addArea(area);
        
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }
    
    @Test
    public void testGetOccupierCount() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        assertEquals(0, sector.getOccupierCount());
        
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, sector.getOccupierCount());
    }
    
    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        assertEquals(3, sector.classBonus(CharacterClass.WARRIOR));
        assertEquals(0, sector.classBonus(CharacterClass.MAGE));
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(20, false, true, 10, 4, false, 2, CharacterClass.WARRIOR);
        SimpleSector sector4 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.MAGE);
        
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), sector4.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(20, false, true, 10, 4, false, 2, CharacterClass.WARRIOR);
        SimpleSector sector4 = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.MAGE);
        
        assertTrue(sector1.equals(sector2));
        assertTrue(sector2.equals(sector1));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
    }
    
    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver() {
            public void occupierChanged() {
                
            }
        };
        
        sector.addObserver(observer);
    }
    
    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 2, true, 3, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver() {
            public void occupierChanged() {
                
            }
        };
        
        sector.addObserver(observer);
        sector.removeObserver(observer);
    }
}