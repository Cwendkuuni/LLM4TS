
package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        // Test case 1: occupierCount = 0
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        
        // Test case 2: occupierCount > 0
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testOccupierChanged() {
        // not testable since observer interface is not provided
        
    }
    
    @Test
    public void testGainControl() {
        // Test case 1: occupierCount = 0
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        
        // Test case 2: occupierCount > 0
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
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
        SimpleSector.SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(9, bonus.getAttack());
        assertEquals(5, bonus.getDefense());
        assertEquals(0, bonus.getDamage());
    }
    
    @Test
    public void testGetDamageBonus() {
        weapon.setSectorBonus(SimpleSector.SectorBonus.BonusType.DAMAGE, 2);
        // not testable since the set of areas is not accessible
    }
    
    @Test
    public void testGetAttackBonus() {
        weapon.setSectorBonus(SimpleSector.SectorBonus.BonusType.ATTACK, 2);
        int bonus = sector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals(11, bonus);
    }
    
    @Test
    public void testGetDefenseBonus() {
        weapon.setSectorBonus(SimpleSector.SectorBonus.BonusType.DEFENSE, 2);
        int bonus = sector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(5, bonus);
    }
    
    @Test
    public void testClassBonus() {
        int bonus = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(2, bonus);
        
        bonus = sector.classBonus(CharacterClass.MAGE);
        assertEquals(0, bonus);
    }
    
    @Test
    public void testGetAreas() {
        // not testable since the set of areas is not accessible
    }
    
    @Test
    public void test_addArea() {
        // not testable since the set of areas is not accessible
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
        SimpleSector sector3 = new SimpleSector(15, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        assertTrue(sector1.equals(sector2));
        assertTrue(sector2.equals(sector1));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector3.equals(sector2));
    }
    
    @Test
    public void testAddObserver() {
        // not testable since observer interface is not provided
    }
    
    @Test
    public void testRemoveObserver() {
        // not testable since observer interface is not provided
    }
    
}
