package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Set;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        
        // test free method
        sector.free();
        assertEquals(sector.getOccupierCount(), 3);
        
        sector.free();
        assertEquals(sector.getOccupierCount(), 2);
        
        sector.free();
        assertEquals(sector.getOccupierCount(), 1);
        
        sector.free();
        assertEquals(sector.getOccupierCount(), 0);
    }
    
    @Test
    public void testOccupierChanged() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        
        // mock observer
        MockSectorObserver observer = new MockSectorObserver();
        sector.addObserver(observer);
        
        // test occupierChanged method
        sector.occupierChanged();
        
        assertTrue(observer.occupierChangedCalled);
    }
    
    @Test
    public void testGainControl() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        // gain control of attacker side
        sector.gainControl(CombatantSide.ATTACKER);
        
        assertEquals(sector.getOccupier(), CombatantSide.ATTACKER);
        
        // gain control of defender side
        sector.gainControl(CombatantSide.DEFENDER);
        
        assertEquals(sector.getOccupier(), CombatantSide.DEFENDER);
    }
    
    @Test
    public void testGetOccupier() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        // gain control of attacker side
        sector.gainControl(CombatantSide.ATTACKER);
        
        assertEquals(sector.getOccupier(), CombatantSide.ATTACKER);
        
        // gain control of defender side
        sector.gainControl(CombatantSide.DEFENDER);
        
        assertEquals(sector.getOccupier(), CombatantSide.DEFENDER);
        
        // free the sector
        sector.free();
        
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        // gain control of attacker side
        sector.gainControl(CombatantSide.ATTACKER);
        
        assertTrue(sector.isOccupied());
        
        // free the sector
        sector.free();
        
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        assertEquals(sector.getHeight(), 10);
    }
    
    @Test
    public void testIsDefenderSpawn() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        assertFalse(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        SectorBonus bonus = sector.getBonus(CharacterClass.A);
        
        assertNotNull(bonus);
        assertEquals(bonus.getAttackBonus(), 0);
        assertEquals(bonus.getDefenseBonus(), 0);
        assertEquals(bonus.getDamageBonus(), 0);
    }
    
    @Test
    public void testGetDamageBonus() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        int damageBonus = sector.getDamageBonus();
        
        assertEquals(damageBonus, 0);
    }
    
    @Test
    public void testGetBonusFromWeapons() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        int attackBonus = sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK);
        int defenseBonus = sector.getBonusFromWeapons(SectorBonus.BonusType.DEFENSE);
        int damageBonus = sector.getBonusFromWeapons(SectorBonus.BonusType.DAMAGE);
        
        assertEquals(attackBonus, 0);
        assertEquals(defenseBonus, 0);
        assertEquals(damageBonus, 0);
    }
    
    @Test
    public void testGetAttackBonus() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 10, 10, false, 10, CharacterClass.A);
        
        int attackBonus = sector.getAttackBonus(CharacterClass.A);
        
        assertEquals(attackBonus, 10);
    }
    
    @Test
    public void testGetDefenseBonus() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 10, 10, false, 10, CharacterClass.A);
        
        int defenseBonus = sector.getDefenseBonus(CharacterClass.A);
        
        assertEquals(defenseBonus, 10);
    }
    
    @Test
    public void testClassBonus() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 10, CharacterClass.A);
        
        int classBonus = sector.classBonus(CharacterClass.A);
        
        assertEquals(classBonus, 10);
        
        int classBonus2 = sector.classBonus(CharacterClass.B);
        
        assertEquals(classBonus2, 0);
    }
    
    @Test
    public void testGetAreas() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        Set<Area> areas = sector.getAreas();
        
        assertNotNull(areas);
        assertTrue(areas.isEmpty());
    }
    
    @Test
    public void test_addArea() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        Area area = new Area();
        
        sector._addArea(area);
        
        Set<Area> areas = sector.getAreas();
        
        assertNotNull(areas);
        assertFalse(areas.isEmpty());
        assertTrue(areas.contains(area));
    }
    
    @Test
    public void testHashCode() {
        // create initial state
        SimpleSector sector1 = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        SimpleSector sector2 = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        int hashCode1 = sector1.hashCode();
        int hashCode2 = sector2.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals() {
        // create initial state
        SimpleSector sector1 = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        SimpleSector sector2 = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        assertFalse(sector1.equals(null));
        assertFalse(sector1.equals(new Object()));
        assertTrue(sector1.equals(sector2));
    }
    
    @Test
    public void testAddObserver() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        // mock observer
        MockSectorObserver observer = new MockSectorObserver();
        
        sector.addObserver(observer);
        
        assertTrue(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        // create initial state
        SimpleSector sector = new SimpleSector(10, true, false, 0, 0, false, 0, CharacterClass.A);
        
        // mock observer
        MockSectorObserver observer = new MockSectorObserver();
        
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
        
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }

}