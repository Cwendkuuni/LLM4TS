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
    private Sector sector;
    private Set<Area> areas;
    private Combatant combatant;
    private Weapon weapon;

    @Before
    public void setup() {
        areas = new HashSet<>();
        areas.add(new Area());
        combatant = new Combatant(CharacterClass.MAGE);
        weapon = new Weapon();
        
        sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.MAGE);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testFree_NoOccupier() {
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
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
        SectorBonus bonus = sector.getBonus(CharacterClass.MAGE);
        assertEquals(20, bonus.getDefenseBonus());
        assertEquals(10, bonus.getAttackBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonus() {
        weapon.setSectorBonus(SectorBonus.BonusType.DAMAGE, 5);
        areas.iterator().next().setOccupier(combatant);
        combatant.setWeapon(weapon);
        
        int damageBonus = sector.getDamageBonus();
        assertEquals(5, damageBonus);
    }

    @Test
    public void testGetDamageBonus_NoWeaponBonus() {
        int damageBonus = sector.getDamageBonus();
        assertEquals(0, damageBonus);
    }

    @Test
    public void testGetAttackBonus() {
        weapon.setSectorBonus(SectorBonus.BonusType.ATTACK, 5);
        areas.iterator().next().setOccupier(combatant);
        combatant.setWeapon(weapon);
        
        int attackBonus = sector.getAttackBonus(CharacterClass.MAGE);
        assertEquals(15, attackBonus);
    }

    @Test
    public void testGetAttackBonus_NoWeaponBonus() {
        int attackBonus = sector.getAttackBonus(CharacterClass.MAGE);
        assertEquals(10, attackBonus);
    }

    @Test
    public void testGetDefenseBonus() {
        weapon.setSectorBonus(SectorBonus.BonusType.DEFENSE, 5);
        areas.iterator().next().setOccupier(combatant);
        combatant.setWeapon(weapon);
        
        int defenseBonus = sector.getDefenseBonus(CharacterClass.MAGE);
        assertEquals(25, defenseBonus);
    }

    @Test
    public void testGetDefenseBonus_NoWeaponBonus() {
        int defenseBonus = sector.getDefenseBonus(CharacterClass.MAGE);
        assertEquals(20, defenseBonus);
    }

    @Test
    public void testClassBonus() {
        int classBonus = sector.classBonus(CharacterClass.MAGE);
        assertEquals(5, classBonus);
    }

    @Test
    public void testClassBonus_NoBonus() {
        int classBonus = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(0, classBonus);
    }

    @Test
    public void testGetAreas() {
        Set<Area> resultAreas = sector.getAreas();
        
        assertEquals(areas, resultAreas);
    }

    @Test
    public void test_addArea() {
        Area area = new Area();
        sector._addArea(area);
        
        assertTrue(areas.contains(area));
    }

    @Test
    public void testHashCode() {
        Sector other = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.MAGE);
        assertEquals(other.hashCode(), sector.hashCode());
    }

    @Test
    public void testEquals() {
        Sector other = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.MAGE);
        assertTrue(sector.equals(other));
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(sector.equals(sector));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(sector.equals(new Object()));
    }

    @Test
    public void testEquals_DifferentHeight() {
        Sector other = new SimpleSector(10, true, false, 10, 20, true, 5, CharacterClass.MAGE);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_DifferentDefenderSpawn() {
        Sector other = new SimpleSector(5, false, false, 10, 20, true, 5, CharacterClass.MAGE);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_DifferentAttackerSpawn() {
        Sector other = new SimpleSector(5, true, true, 10, 20, true, 5, CharacterClass.MAGE);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_DifferentAttackBonus() {
        Sector other = new SimpleSector(5, true, false, 5, 20, true, 5, CharacterClass.MAGE);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_DifferentDefenseBonus() {
        Sector other = new SimpleSector(5, true, false, 10, 10, true, 5, CharacterClass.MAGE);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_DifferentFlag() {
        Sector other = new SimpleSector(5, true, false, 10, 20, false, 5, CharacterClass.MAGE);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_DifferentClassBonus() {
        Sector other = new SimpleSector(5, true, false, 10, 20, true, 10, CharacterClass.MAGE);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_DifferentClassType() {
        Sector other = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        assertFalse(sector.equals(other));
    }

    @Test
    public void testEquals_DifferentAreas() {
        Sector other = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.MAGE);
        other._addArea(new Area());
        assertFalse(sector.equals(other));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {}
        };
        sector.addObserver(observer);
        
        assertTrue(sector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {}
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        
        assertFalse(sector.getObservers().contains(observer));
    }
}