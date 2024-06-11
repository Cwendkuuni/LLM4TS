
package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    private Area area;
    private Combatant combatant;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(1, false, true, 5, 10, true, 2, CharacterClass.HEALER);
        area = new Area();
        combatant = new Combatant();
    }
    
    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        
        sector.free();
        
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        
        sector.gainControl(CombatantSide.ATTACKER);
        
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(1, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertFalse(sector.isDefenderSpawn());
        
        SimpleSector sector2 = new SimpleSector(2, true, true, 5, 10, true, 2, CharacterClass.HEALER);
        
        assertTrue(sector2.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertTrue(sector.isAttackerSpawn());
        
        SimpleSector sector2 = new SimpleSector(3, false, true, 5, 10, true, 2, CharacterClass.HEALER);
        
        assertTrue(sector2.isAttackerSpawn());
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(sector.isFlag());
        
        SimpleSector sector2 = new SimpleSector(4, false, true, 5, 10, false, 2, CharacterClass.HEALER);
        
        assertFalse(sector2.isFlag());
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.HEALER);
        
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(12, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetBonusFromWeapons() {
        int bonus = sector._getBonusFromWeapons(SectorBonus.BonusType.DEFENSE);
        
        assertEquals(0, bonus);
        
        sector._addArea(area);
        
        int weaponBonus = 5;
        int weaponSectorBonus = 10;
        combatant.setWeapon(new Weapon());
        combatant.getWeapon().setSectorBonus(weaponSectorBonus, SectorBonus.BonusType.DEFENSE);
        area.setOccupier(combatant);
        
        bonus = sector._getBonusFromWeapons(SectorBonus.BonusType.DEFENSE);
        
        assertEquals(weaponSectorBonus, bonus);
        
        combatant.getWeapon().setSectorBonus(weaponBonus, SectorBonus.BonusType.DEFENSE);
        
        bonus = sector._getBonusFromWeapons(SectorBonus.BonusType.DEFENSE);
        
        assertEquals(weaponSectorBonus, bonus);
        
        combatant.setWeapon(null);
        
        bonus = sector._getBonusFromWeapons(SectorBonus.BonusType.DEFENSE);
        
        assertEquals(0, bonus);
    }
    
    @Test
    public void testGetAttackBonus() {
        int bonus = sector._getAttackBonus(CharacterClass.HEALER);
        
        assertEquals(7, bonus);
        
        sector._addArea(area);
        
        int weaponBonus = 5;
        int weaponSectorBonus = 10;
        combatant.setWeapon(new Weapon());
        combatant.getWeapon().setSectorBonus(weaponSectorBonus, SectorBonus.BonusType.ATTACK);
        area.setOccupier(combatant);
        
        bonus = sector._getAttackBonus(CharacterClass.HEALER);
        
        assertEquals(7 + weaponSectorBonus, bonus);
        
        combatant.getWeapon().setSectorBonus(weaponBonus, SectorBonus.BonusType.ATTACK);
        
        bonus = sector._getAttackBonus(CharacterClass.HEALER);
        
        assertEquals(7 + weaponSectorBonus, bonus);
        
        combatant.setWeapon(null);
        
        bonus = sector._getAttackBonus(CharacterClass.HEALER);
        
        assertEquals(7, bonus);
    }
    
    @Test
    public void testGetDefenseBonus() {
        int bonus = sector._getDefenseBonus(CharacterClass.HEALER);
        
        assertEquals(12, bonus);
        
        sector._addArea(area);
        
        int weaponBonus = 5;
        int weaponSectorBonus = 10;
        combatant.setWeapon(new Weapon());
        combatant.getWeapon().setSectorBonus(weaponSectorBonus, SectorBonus.BonusType.DEFENSE);
        area.setOccupier(combatant);
        
        bonus = sector._getDefenseBonus(CharacterClass.HEALER);
        
        assertEquals(12 + weaponSectorBonus, bonus);
        
        combatant.getWeapon().setSectorBonus(weaponBonus, SectorBonus.BonusType.DEFENSE);
        
        bonus = sector._getDefenseBonus(CharacterClass.HEALER);
        
        assertEquals(12 + weaponSectorBonus, bonus);
        
        combatant.setWeapon(null);
        
        bonus = sector._getDefenseBonus(CharacterClass.HEALER);
        
        assertEquals(12, bonus);
    }
    
    @Test
    public void testClassBonus() {
        int bonus = sector._classBonus(CharacterClass.HEALER);
        
        assertEquals(2, bonus);
        
        bonus = sector._classBonus(CharacterClass.WARRIOR);
        
        assertEquals(0, bonus);
    }
    
    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        
        assertEquals(areas, sector.getAreas());
        
        sector._addArea(area);
        areas.add(area);
        
        assertEquals(areas, sector.getAreas());
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector2 = new SimpleSector(1, false, true, 5, 10, true, 2, CharacterClass.HEALER);
        
        assertEquals(sector2.hashCode(), sector.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector2 = new SimpleSector(1, false, true, 5, 10, true, 2, CharacterClass.HEALER);
        
        assertTrue(sector2.equals(sector));
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver() {
            
            @Override
            public void occupierChanged() {
                
            }
        };
        
        assertFalse(sector.removeObserver(observer));
        
        sector.addObserver(observer);
        
        assertTrue(sector.removeObserver(observer));
    }
}
