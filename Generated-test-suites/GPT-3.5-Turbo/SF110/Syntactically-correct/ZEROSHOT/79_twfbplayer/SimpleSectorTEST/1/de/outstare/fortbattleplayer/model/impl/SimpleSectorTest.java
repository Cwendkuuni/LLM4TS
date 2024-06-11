
package de.outstare.fortbattleplayer.model.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, false, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, false, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, false, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, false, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, false, 2, CharacterClass.WARRIOR);
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, false, 2, CharacterClass.WARRIOR);
        assertTrue(sector.isDefenderSpawn());
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, false, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, false, 2, CharacterClass.WARRIOR);
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        int expectedAttackBonus = 5 + 2;
        int expectedDefenseBonus = 10 + 2;
        int expectedDamageBonus = 0;
        assertEquals(expectedAttackBonus, bonus.getAttackBonus());
        assertEquals(expectedDefenseBonus, bonus.getDefenseBonus());
        assertEquals(expectedDamageBonus, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 10, false, 2, CharacterClass.WARRIOR);
        assertNotNull(sector.getAreas());
    }
}
