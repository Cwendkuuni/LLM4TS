import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(100, true, false, 10, 5, true, 20, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
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
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
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
        assertEquals(100, sector.getHeight());
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
        assertNotNull(bonus);
        assertEquals(10 + 20, bonus.getAttackBonus());
        assertEquals(5 + 20, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertEquals(0, areas.size());
        Area area = new Area();
        sector._addArea(area);
        areas = sector.getAreas();
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }

    // Edge case for getDamageBonus()
    @Test
    public void testGetDamageBonus() {
        final int expectedBonus = 123;
        Area area = new Area();
        Combatant combatant = new Combatant();
        Weapon weapon = new Weapon();
        weapon.setSectorBonus(SectorBonus.BonusType.DAMAGE, expectedBonus);
        area.setOccupier(combatant);
        combatant.setWeapon(weapon);
        sector._addArea(area);
        int bonus = sector.getDamageBonus();
        assertEquals(expectedBonus, bonus);
    }

    // Error scenario for getAttackBonus()
    @Test(expected = NullPointerException.class)
    public void testGetAttackBonus_NullClass() {
        sector.getAttackBonus(null);
    }

    // Typical use case for getDefenseBonus()
    @Test
    public void testGetDefenseBonus() {
        final int expectedBonus = 50;
        Area area = new Area();
        Combatant combatant = new Combatant();
        Weapon weapon = new Weapon();
        weapon.setSectorBonus(SectorBonus.BonusType.DEFENSE, expectedBonus);
        area.setOccupier(combatant);
        combatant.setWeapon(weapon);
        sector._addArea(area);
        int bonus = sector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(5 + 20 + expectedBonus, bonus);
    }

    // Typical use case for classBonus()
    @Test
    public void testClassBonus() {
        int bonus = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(20, bonus);
    }

    // Error scenario for classBonus()
    @Test
    public void testClassBonus_MismatchClass() {
        int bonus = sector.classBonus(CharacterClass.ARCHER);
        assertEquals(0, bonus);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector2 = new SimpleSector(100, true, false, 10, 5, true, 20, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector2 = new SimpleSector(100, true, false, 10, 5, true, 20, CharacterClass.WARRIOR);
        assertTrue(sector.equals(sector2));
        // Change one parameter
        sector2 = new SimpleSector(99, true, false, 10, 5, true, 20, CharacterClass.WARRIOR);
        assertFalse(sector.equals(sector2));
    }

    // Typical use case for addObserver() and removeObserver()
    @Test
    public void testAddRemoveObserver() {
        SimpleSectorObserver observer = new SimpleSectorObserver();
        sector.addObserver(observer);
        Set<SectorObserver> observers = sector.getObservers();
        assertNotNull(observers);
        assertEquals(1, observers.size());
        assertTrue(observers.contains(observer));

        sector.removeObserver(observer);
        observers = sector.getObservers();
        assertNotNull(observers);
        assertEquals(0, observers.size());
    }

    // Error scenario for removeObserver()
    @Test(expected = NullPointerException.class)
    public void testRemoveObserver_NullObserver() {
        sector.removeObserver(null);
    }
}

class Area {
    private Combatant occupier;

    public Combatant getOccupier() {
        return occupier;
    }

    public void setOccupier(Combatant occupier) {
        this.occupier = occupier;
    }

    public boolean isOccupied() {
        return occupier != null;
    }
}

class Combatant {
    private Weapon weapon;

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}

class Weapon {
    private Map<SectorBonus.BonusType, Integer> sectorBonuses;

    public Weapon() {
        sectorBonuses = new HashMap<>();
    }

    public int getSectorBonus(SectorBonus.BonusType type) {
        return sectorBonuses.getOrDefault(type, 0);
    }

    public void setSectorBonus(SectorBonus.BonusType type, int bonus) {
        sectorBonuses.put(type, bonus);
    }
}

class SimpleSectorObserver implements SectorObserver {
    @Override
    public void occupierChanged() {
        // Implementation not relevant for testing purposes
    }
}