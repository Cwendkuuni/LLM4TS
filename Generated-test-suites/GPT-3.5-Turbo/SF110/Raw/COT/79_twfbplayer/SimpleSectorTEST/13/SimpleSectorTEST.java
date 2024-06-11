
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    private SimpleSector sector;

    @Before
    public void setup() {
        sector = new SimpleSector(10, true, false, 10, 20, false, 5, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        sector.occupierCount = 1;
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        sector.occupierCount = 0;
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.occupierCount);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
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
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(25, bonus.getAttackBonus());
        assertEquals(25, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonus() {
        int damageBonus = sector.getDamageBonus();
        assertEquals(0, damageBonus);
    }

    @Test
    public void testGetAttackBonus() {
        int attackBonus = sector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals(25, attackBonus);
    }

    @Test
    public void testGetDefenseBonus() {
        int defenseBonus = sector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(25, defenseBonus);
    }

    @Test
    public void testClassBonus() {
        int classBonus = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(5, classBonus);
    }

    @Test
    public void testGetAreas() {
        assertNotNull(sector.getAreas());
    }

    @Test
    public void testAddArea() {
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 10, 20, false, 5, CharacterClass.WARRIOR);
        assertEquals(other.hashCode(), sector.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 10, 20, false, 5, CharacterClass.WARRIOR);
        assertTrue(sector.equals(other));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // empty method
            }
        };
        
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // empty method
            }
        };
        
        sector.observers.add(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }
}